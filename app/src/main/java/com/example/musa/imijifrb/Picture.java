package com.example.musa.imijifrb;

import android.graphics.Bitmap;

/**
 * Created by musam on 18/02/2018.
 */

public class Picture {

    private Bitmap photo;
    private String title;
    private String url;

    public Picture(Bitmap photo, String title, String url) {
        this.photo = photo;
        this.title = title;
        this.url = url;
    }

    public Picture(){

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
