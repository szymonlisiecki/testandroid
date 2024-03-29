package com.example.myapplication.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.Photo;
import com.example.myapplication.models.User;
import com.example.myapplication.models.UserAccountSettings;
import com.example.myapplication.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/** Klasa przeznaczona do tworzenia metod którę służą do integracji aplikacji z bazą danych Firebase.
 *
 */
public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private StorageReference mStorageReference;
    private String userID;

    private Context mContext;
    private double mPhotoUploadProgress = 0;

    /** \brief Konstruktor klasy FirebaseMethods, pobiera UID użytkownika aktualnie korzystającego z aplikacji
     * @param context
     */
    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        myRef = mFirebaseDatabase.getReference();
        mContext = context;

        String latitude;
        String longitude;

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    /** \brief Metoda przeznaczona do przekazywania zdjęcia do bazdy danych
     *
     * @param photoType
     * @param caption
     * @param count
     * @param imgUrl
     * @param bm
     */
    public void uploadNewPhoto(String photoType, final String caption,final int count, final String imgUrl, Bitmap bm){
        Log.d(TAG, "uploadNewPhoto: attempting to uplaod new photo.");

        Log.d("ADebugTag", "zajebista opcja 123123123: " + Float.toString(ReadExif(imgUrl)[0]));


        final FilePaths filePaths = new FilePaths();

        //case1) new photo
        if(photoType.equals(mContext.getString(R.string.new_photo))){
            Log.d(TAG, "uploadNewPhoto: uploading NEW photo.");

            final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final StorageReference storageReference = mStorageReference
                    .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/photo" + (count + 1));
            //convert image url to bitmap
            if(bm == null){
                bm = ImageManager.getBitmap(imgUrl);
            }
            byte[] bytes = ImageManager.getBytesFromBitmap(bm, 100);

            UploadTask uploadTask;
            uploadTask = storageReference.putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                    mStorageReference.child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/photo" + (count + 1)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {


                            float tmp = Float.parseFloat(Float.toString(ReadExif(imgUrl)[0]));
                            float tmp2 = Float.parseFloat(Float.toString(ReadExif(imgUrl)[1]));


                            addPhotoToDatabase(caption,task.getResult().toString(), tmp, tmp2);
                        }
                    });
                    Toast.makeText(mContext, "photo upload success", Toast.LENGTH_SHORT).show();


                    //add the new photo to 'photos' node and 'user_photos' node

                    //navigate to the main feed so the user can see their photo

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: Photo upload failed.");
                    Toast.makeText(mContext, "Photo upload failed ", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    if(progress - 15 > mPhotoUploadProgress){
                        Toast.makeText(mContext, "photo upload progress: " + String.format("%.0f", progress) + "%", Toast.LENGTH_SHORT).show();
                        mPhotoUploadProgress = progress;
                    }

                    Log.d(TAG, "onProgress: upload progress: " + progress + "% done");
                }
            });

        }


        //case new profile photo
        else if(photoType.equals("profile_photo")){
            Log.d(TAG, "uploadNewPhoto: uploading new PROFILE photo");
        }

    }
    /** \brief Metoda przeznaczona do ustawienia zdjęcia profilowego użytkownika
     * @param url
     */
    private void setProfilePhoto(String url){
        Log.d(TAG, "setProfilePhoto: setting new profile image: " + url);

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(mContext.getString(R.string.profile_photo))
                .setValue(url);
    }

    /** \brief Metoda przeznaczona do ustawienia zdjęcia profilowego użytkownika
     * @return sformatowana data (rok - miesiąc - dzien - godzina - minuta - sekunda)
     */
    private String getTimestamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
        sdf.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
        return sdf.format(new Date());
    }

    /** \brief Metoda przeznaczona do dodania nowego zdjęcia do bazy danych oraz ustawienia w zdjęciu odpowiadających mu długości i szerokości geograficznej
     * @param caption
     * @param url
     * @param qwe
     * @param qwer
     */
    private void addPhotoToDatabase(String caption, String url, float qwe, float qwer){
        Log.d(TAG, "addPhotoToDatabase: adding photo to database.");



        String tags = StringManipulation.getTags(caption);
        String newPhotoKey = myRef.child(mContext.getString(R.string.dbname_photos)).push().getKey();
        Photo photo = new Photo();
        photo.setCaption(caption);
        photo.setDate_created(getTimestamp());
        photo.setImage_path(url);
        photo.setTags(tags);
        photo.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        photo.setPhoto_id(newPhotoKey);

        photo.setLatitude(qwe);
        photo.setLongitude(qwer);

        //insert into database
        myRef.child(mContext.getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser()
                        .getUid()).child(newPhotoKey).setValue(photo);
        myRef.child(mContext.getString(R.string.dbname_photos)).child(newPhotoKey).setValue(photo);

    }

    /** \brief Metoda przeznaczona do pobrania numeru pod którym jest zdjęcie w bazie danych
     * @param dataSnapshot
     */
    public int getImageCount(DataSnapshot dataSnapshot){
        int count = 0;
        for(DataSnapshot ds: dataSnapshot
                .child(mContext.getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .getChildren()){
            count++;
        }
        return count;
    }

    /** \brief Metoda odpowiada za zaktualizowanie węzła 'user_account_settings' dla bieżącego użytkownika
     * @param displayName
     * @param website
     * @param description
     * @param phoneNumber
     */
    public void updateUserAccountSettings(String displayName, String website, String description, long phoneNumber){

        Log.d(TAG, "updateUserAccountSettings: updating user account settings.");

        if(displayName != null){
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_display_name))
                    .setValue(displayName);
        }


        if(website != null) {
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_website))
                    .setValue(website);
        }

        if(description != null) {
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_description))
                    .setValue(description);
        }

        if(phoneNumber != 0) {
            myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.field_phone_number))
                    .setValue(phoneNumber);
        }
    }

    /** \brief Metoda odpowiada za zaktualizowanie nazwy użytkownika w węzłach 'users' i 'user_account_settings'
     * @param username
     */
    public void updateUsername(String username){
        Log.d(TAG, "updateUsername: upadting username to: " + username);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);
    }

    /** \brief Metoda odpowiada za zaktualizowanie maila węźle 'user's'
     * @param email
     */
    public void updateEmail(String email){
        Log.d(TAG, "updateEmail: upadting email to: " + email);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_email))
                .setValue(email);
    }



    /** \brief Metoda odpowiada za rejestrację nowego maila i hasła w bazie danych
     * @param email
     * @param password
     * @param username
     */
    public void registerNewEmail(final String email, String password, final String username){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();

                        }
                        else if(task.isSuccessful()){
                            //send verificaton email
                           // sendVerificationEmail();

                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                        }

                    }
                });
    }


    /** \brief Metoda odpowiada za dodawanie informacji to węzła 'users'
     * dodaję informacje do węzła 'user_account_settings'
     * @param email
     * @param username
     * @param description
     * @param website
     * @param profile_photo
     */
    public void addNewUser(String email, String username, String description, String website, String profile_photo){

        User user = new User( userID,  1,  email,  StringManipulation.condenseUsername(username) );

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .setValue(user);


        UserAccountSettings settings = new UserAccountSettings(
                description,
                username,
                0,
                0,
                0,
                profile_photo,
                StringManipulation.condenseUsername(username),
                website
        );

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .setValue(settings);

    }


    /** \brief Metoda odpowiada za pobranie ustawień konta dla biężącego użytkownika
     * baza danych: węzeł 'user_account_Settings'
     * @param dataSnapshot
     * @return zwraca obiekt klasy UserSetting z ustawionymi argumentami user i settings
     */
    public UserSettings getUserSettings(DataSnapshot dataSnapshot){
        Log.d(TAG, "getUserAccountSettings: retrieving user account settings from firebase.");


        UserAccountSettings settings  = new UserAccountSettings();
        User user = new User();

        for(DataSnapshot ds: dataSnapshot.getChildren()){

            // user_account_settings node
            if(ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))) {
                Log.d(TAG, "getUserAccountSettings: user account settings node datasnapshot: " + ds);

                try {

                    settings.setDisplay_name(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDisplay_name()
                    );
                    settings.setUsername(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getUsername()
                    );
                    settings.setWebsite(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getWebsite()
                    );
                    settings.setDescription(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDescription()
                    );
                    settings.setProfile_photo(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getProfile_photo()
                    );
                    settings.setPosts(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getPosts()
                    );
                    settings.setFollowing(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowing()
                    );
                    settings.setFollowers(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowers()
                    );

                    Log.d(TAG, "getUserAccountSettings: retrieved user_account_settings information: " + settings.toString());
                } catch (NullPointerException e) {
                    Log.e(TAG, "getUserAccountSettings: NullPointerException: " + e.getMessage());
                }
            }


            // users node
            Log.d(TAG, "getUserSettings: snapshot key: " + ds.getKey());
            if(ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                Log.d(TAG, "getUserAccountSettings: users node datasnapshot: " + ds);

                user.setUsername(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUsername()
                );
                user.setEmail(
                        ds.child(userID)
                                .getValue(User.class)
                                .getEmail()
                );
                user.setPhone_number(
                        ds.child(userID)
                                .getValue(User.class)
                                .getPhone_number()
                );
                user.setUser_id(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUser_id()
                );

                Log.d(TAG, "getUserAccountSettings: retrieved users information: " + user.toString());
            }
        }
        return new UserSettings(user, settings);

    }

    /** \brief Metoda przeznaczona do pobierania ze zdjęcia położenia geograficznego
     * @param file
     * @return położenie geograficzne zdjęcia
     */
    public float[] ReadExif(String file){
        Log.d(TAG, "file readexif " + file);

        float[] latLong = new float[2];
        Log.d(TAG, "Jebac je macie");
        try {

            ExifInterface exifInterface = new ExifInterface(file);

            exifInterface.getLatLong(latLong);
            Log.d(TAG, "Jebac stare baby " + latLong[0]);


        }
        catch (Exception e) {
            Log.d(TAG, "wchodze do catch a szmato " + latLong[0]);
            // TODO Auto-generated catch block
            //e.printStackTrace();
            // Toast.makeText(AndroidExifActivity.this,
            //e.toString(),
            //Toast.LENGTH_LONG).show();

        }
        return latLong;
    }
}

