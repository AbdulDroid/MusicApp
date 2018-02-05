package mcomm.com.musicapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

/**
 * Created by apple on 03/02/2018.
 */
@Parcel
public class Song {
    @SerializedName("id")
    @Expose
    long id;
    @SerializedName("album_id")
    @Expose
    long albumId;
    @SerializedName("title")
    @Expose
    String title;
    @SerializedName("image")
    @Expose
    String imageUrl;
    @SerializedName("artist")
    @Expose
    String artist;
    @SerializedName("album")
    @Expose
    String album;
    @SerializedName("duration")
    @Expose
    String duration;
    @SerializedName("date_added")
    @Expose
    String dateAdded;

    @ParcelConstructor
    public Song(long id, long albumId, String title, String imageUrl, String artist, String album, String duration,
                String dateAdded) {
        this.id = id;
        this.albumId = albumId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.dateAdded = dateAdded;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", duration='" + duration + '\'' +
                ", dateAdded='" + dateAdded + '\'' +
                '}';
    }
}
