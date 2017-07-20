package de.teamawesome.awesomeplayer.model;

import android.database.Cursor;
import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable{

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

    public static final Parcelable.Creator<Song> CREATOR
            = new Parcelable.Creator<Song>() {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    private Song(){};

    public Song(String id, String path, String displayName,
                String title, String duration, String albumID,
                String albumName, String trackNumber, String artistID,
                String artist) {
        super();
        this.id = id;
        this.path = path;
        this.displayName = displayName;
        this.title = title;
        this.duration = duration;
        this.albumID = albumID;
        this.albumName = albumName;
        this.trackNumber = trackNumber;
        this.artistID = artistID;
        this.artist = artist;
    }

    private Song(Parcel in) {
        this();
        String[] values = new String[10];
        in.readStringArray(values);
        id = values[0];
        path = values[1];
        displayName = values[2];
        title = values[3];
        duration = values[4];
        albumID = values[5];
        albumName = values[6];
        trackNumber = values[7];
        artistID = values[8];
        artist = values[9];
    }

    public static Song[] extractSongsFromCursor(Cursor cursor){
        ArrayList<Song> returnedSongs = new ArrayList<Song>();
        // resets the cursor to position 0 (The position in front of the first element!)
        cursor.moveToFirst();
        // moves the cursor to the first element.
        cursor.moveToPrevious();
        while(cursor.moveToNext()){
            Song currentSong = new Song();

            currentSong.id = cursor.getString(cursor.getColumnIndex("_ID"));
            currentSong.path = cursor.getString(cursor.getColumnIndex("_DATA"));
            currentSong.displayName = cursor.getString(cursor.getColumnIndex("_DISPLAY_NAME"));
            currentSong.title = cursor.getString(cursor.getColumnIndex("TITLE"));
            currentSong.duration = cursor.getString(cursor.getColumnIndex("DURATION"));

            currentSong.albumID = cursor.getString(cursor.getColumnIndex("ALBUM_ID"));
            currentSong.albumName = cursor.getString(cursor.getColumnIndex("ALBUM"));
            currentSong.trackNumber = cursor.getString(cursor.getColumnIndex("TRACK"));

            currentSong.artistID = cursor.getString(cursor.getColumnIndex("ARTIST_ID"));
            currentSong.artist = cursor.getString(cursor.getColumnIndex("ARTIST"));

            returnedSongs.add(currentSong);
        }
        return (Song[]) returnedSongs.toArray(new Song[0]);
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

    //I have no idea, what this Method is supposed to do
    @Override
    public int describeContents() {
        try {
            return Integer.parseInt(id);
        }catch (NumberFormatException e) {
            return 0;
        }

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{id, path, displayName,
                title, duration, albumID,
                albumName, trackNumber, artistID,
                artist});
    }
}
