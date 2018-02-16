package com.example.musa.imijifrb;

/**
 * Created by musam on 16/02/2018.
 */

public class AlbumDataSource {

    private String albumTitle;
    private String albumOwner;
    private String albumDescription;

    public AlbumDataSource(String albumTitle, String albumOwner, String albumDescription) {
        this.albumTitle = albumTitle;
        this.albumOwner = albumOwner;
        this.albumDescription = albumDescription;
    }

    public AlbumDataSource() {
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
