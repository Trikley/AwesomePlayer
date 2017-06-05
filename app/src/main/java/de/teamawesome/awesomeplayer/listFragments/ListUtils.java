package de.teamawesome.awesomeplayer.listFragments;

import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by sven on 6/5/17.
 */

public class ListUtils {

    public static final String URI = "uri";
    public static final String FROM = "from";
    public static final String PROJECTION = "projection";
    public static final String SELECTION_STRING = "selectionString";
    public static final String SELECTION_ARGS = "selectionArgs";

    public static final Uri MEDIA_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    public static final String[] MEDIA_FROM = {MediaStore.Audio.Media.TITLE};
    public static final String MEDIA_FROM_ALBUM_SELECTION_STRING = MediaStore.Audio.Media.ALBUM_ID + "=?";
    public static final String[] MEDIA_FROM_PLAYLIST_FROM = new String[]{MediaStore.Audio.Playlists.Members.TITLE};

    public static final Uri ALBUMS_URI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    public static final String[] ALBUMS_FROM = {MediaStore.Audio.Albums.ALBUM};

    public static final Uri PLAYLISTS_URI = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
    public static final String[] PLAYLISTS_FROM = {MediaStore.Audio.Playlists.NAME};


}
