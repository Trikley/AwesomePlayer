package de.teamawesome.awesomeplayer.model;

public class Song {

    //MEDIASTORE.AUDIO.MEDIA._ID
    private String id;
    //MEDIASTORE.AUDIO.MEDIA.DATA
    private String path;
    //MEDIASTORE.AUDIO.MEDIA.DISPLAY_NAME
    private String displayName;
    //MEDIASTORE.AUDIO.MEDIA.TITLE
    private String title;
    //MEDIASTORE.AUDIO.MEDIA.DURATION
    private String duration;

    //MEDIASTORE.AUDIO.MEDIA.ALBUM_ID
    private String albumID;
    //MEDIASTORE.AUDIO.MEDIA.ALBUM
    private String albumName;
    //MEDIASTORE.AUDIO.MEDIA.TRACK
    private String trackNumber;

    //MEDIASTORE.AUDIO.MEDIA.ARTIST_ID
    private String artistID;
    //MEDIASTORE.AUDIO.MEDIA.ARTIST
    private String artist;

    public Song(String id, String path, String displayName,
                String title, String duration, String albumID,
                String albumName, String trackName, String artistID,
                String artist) {
        super();
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public String getAlbumID() {
        return albumID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getTrackNumber() {
        return trackNumber;
    }

    public String getArtistID() {
        return artistID;
    }

    public String getArtist() {
        return artist;
    }
}
