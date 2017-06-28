package de.teamawesome.awesomeplayer.model;

import android.database.Cursor;

import java.util.ArrayList;

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

    private Song(){};

    public Song(String id, String path, String displayName,
                String title, String duration, String albumID,
                String albumName, String trackName, String artistID,
                String artist) {
        super();
    }

    public static Song[] extractSongsFromCursor(Cursor cursor){
        ArrayList<Song> returnedSongs = new ArrayList<Song>();
        while(cursor.moveToNext()){
            Song currentSong = new Song();

            currentSong.id = cursor.getString(cursor.getColumnIndex("_ID"));
            currentSong.path = cursor.getString(cursor.getColumnIndex("DATA"));
            currentSong.displayName = cursor.getString(cursor.getColumnIndex("DISPLAY_NAME"));
            currentSong.title = cursor.getString(cursor.getColumnIndex("TITLE"));
            currentSong.duration = cursor.getString(cursor.getColumnIndex("DURATION"));

            currentSong.albumID = cursor.getString(cursor.getColumnIndex("ALBUM_ID"));
            currentSong.albumName = cursor.getString(cursor.getColumnIndex("ALBUM"));
            currentSong.trackNumber = cursor.getString(cursor.getColumnIndex("TRACK"));

            currentSong.artistID = cursor.getString(cursor.getColumnIndex("ARTIST_ID"));
            currentSong.artist = cursor.getString(cursor.getColumnIndex("ARTIST"));

            returnedSongs.add(currentSong);
        }
        return (Song[]) returnedSongs.toArray();
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
