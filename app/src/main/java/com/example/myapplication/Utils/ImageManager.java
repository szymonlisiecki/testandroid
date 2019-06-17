package com.example.myapplication.Utils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/** Klasa przeznaczona do pobierania zdjęc przez dany URL. Zdjęcie można rzutować na bitmape.
 *
 */
public class ImageManager {

    private static final String TAG = "ImageManager";

    /** \brief zwraca tablicę bajtów z mapy bitowej
     * quality jest z przediału <0; 100>
     * @param imgUrl
     * @return pobrane zdjęcie z danego URL, następnie zrzutowane na Bitmape
     */
    public static Bitmap getBitmap(String imgUrl){
        File imageFile = new File(imgUrl);
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try{
            fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        }catch (FileNotFoundException e){
            Log.e(TAG, "getBitmap: FileNotFoundException: " + e.getMessage() );
        }finally {
            try{
                if(fis!=null){
                    fis.close();
                }

            }catch (IOException e){
                Log.e(TAG, "getBitmap: FileNotFoundException: " + e.getMessage() );
            }
        }
        return bitmap;
    }

    /** \brief zwraca tablicę bajtów z bitmapy
     * quality jest z przediału <0; 100>
     * @param bm
     * @param quality
     * @return
     */
    public static byte[] getBytesFromBitmap(Bitmap bm, int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }
}