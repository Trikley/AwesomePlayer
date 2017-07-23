package de.teamawesome.awesomeplayer.fragments.listFragments;

import android.net.Uri;
import android.provider.MediaStore;

/**
 * Specifies a bunch of constants which could not be stored in a resource file.
 */

public final class ListUtils {

    /**
     * These {@link String}s are used to specify {@link android.os.Bundle} keys for {@link CursorListFragment}'s 'setArguments' method.
     */
    public static final String URI = "uri";
    public static final String FROM = "from";
    public static final String PROJECTION = "projection";
    public static final String SELECTION_STRING = "selectionString";
    public static final String SELECTION_ARGS = "selectionArgs";
    public static final String SORTORDER = "sortOrder";

    /**
     * The following variables are used to fill the argument {@link android.os.Bundle}s specified in {@link ListBundles}
     */
    public static final Uri MEDIA_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    public static final String[] MEDIA_FROM = {MediaStore.Audio.Media.TITLE};
    public static final String MEDIA_FROM_ALBUM_SELECTION_STRING = MediaStore.Audio.Media.ARTIST_ID + "=?";
    public static final String[] MEDIA_FROM_PLAYLIST_FROM = new String[]{MediaStore.Audio.Playlists.Members.TITLE};

    public static final Uri ALBUMS_URI = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
    public static final String[] ALBUMS_FROM = {MediaStore.Audio.Artists.ARTIST};

    public static final Uri PLAYLISTS_URI = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
    public static final String[] PLAYLISTS_FROM = {MediaStore.Audio.Playlists.NAME};

    /**
     * The following variables are used for passing the selection of songs from {@link MediaListFragment} to anywhere, really.
     * They define keys in bundles, which hold the actual information.
     */
    public static final String MEDIA_DATA_IN_PLAYBACK_ORDER = "mdipo";
    public static final String MEDIA_DISPLAY_NAMES_IN_ORDER = "mdnio";
    public static final String MEDIA_ALBUM_ID_IN_ORDER = "maiio";
}
