package ru.tishin.musicapp;

import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;

public class SongInfo {
    private int id;
    private String title;
    private String artist;
    private String filePath;
    private Uri album_art_uri;
    private int duration;

    public SongInfo(){
        id = -1;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
    public void setArtist(String artist){
        this.artist = artist;
    }

    public String getArtist(){
        return artist;
    }

    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    public String getFilePath(){
        return filePath;
    }

    public Uri getAlbumArtUri() { return album_art_uri; }
    public void setAlbumArtUri(Uri album_art_uri) { this.album_art_uri = album_art_uri; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
}
