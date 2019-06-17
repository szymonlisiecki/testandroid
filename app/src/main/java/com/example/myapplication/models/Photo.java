package com.example.myapplication.models;


/** Klasa przeznaczona do pobierania i ustawiania danych zdjęć użytkownika
 *
 */
public class Photo {

    private String caption;
    private String date_created;
    private String image_path;
    private String photo_id;
    private String user_id;
    private String tags;
    private float latitude;
    private float longitude;



    public Photo() {

    }

    public Photo(String caption, String date_created, String image_path, String photo_id, String user_id, String tags, float longitude, float latitude) {
        this.caption = caption;
        this.date_created = date_created;
        this.image_path = image_path;
        this.photo_id = photo_id;
        this.user_id = user_id;
        this.tags = tags;
        this.longitude=longitude;
        this.latitude=latitude;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getPhoto_id() {
        return photo_id;
    }

    public void setPhoto_id(String photo_id) {
        this.photo_id = photo_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public float getLatitude(){ return latitude; }
    public void setLatitude(float l) {
        this.latitude = l;
    }

    public float getLongitude(){ return longitude; }
    public void setLongitude(float l) {
        this.longitude = l;
    }


    //zmienic tutaj jeszcze setLongitude i Latitude
    @Override
    public String toString() {
        return "Photo{" +
                "caption='" + caption + '\'' +
                ", date_created='" + date_created + '\'' +
                ", image_path='" + image_path + '\'' +
                ", photo_id='" + photo_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}