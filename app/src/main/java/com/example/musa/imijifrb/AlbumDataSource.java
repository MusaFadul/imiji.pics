package com.example.musa.imijifrb;

import java.util.List;

/**
 * Created by musam on 16/02/2018.
 */

public class AlbumDataSource {

    private String albumTitle;
    private String albumOwner;
    private String albumDescription;
    private String albumTimeStamp;
    private List<String> users;

    public AlbumDataSource(String albumTitle, String albumOwner, String albumDescription, String albumTimeStam,List<String> users) {
        this.albumTitle = albumTitle;
        this.albumOwner = albumOwner;
        this.albumDescription = albumDescription;
        this.albumTimeStamp = albumTimeStamp;
        this.users = users;
    }

    public AlbumDataSource() {
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getAlbumTimeStamp() {
        return albumTimeStamp;
    }

    public void setAlbumTimeStamp(String albumTimeStamp) {
        this.albumTimeStamp = albumTimeStamp;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getAlbumOwner() {
        return albumOwner;
    }

    public void setAlbumOwner(String albumOwner) {
        this.albumOwner = albumOwner;
    }

    public String getAlbumDescription() {
        return albumDescription;
    }

    public void setAlbumDescription(String albumDescription) {
        this.albumDescription = albumDescription;
    }
}
