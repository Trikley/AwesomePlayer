package de.teamawesome.awesomeplayer.listFragments;

import static de.teamawesome.awesomeplayer.listFragments.ListUtils.*;

import android.net.Uri;
import android.os.Bundle;

import java.io.Serializable;

/**
 * Specifies a bunch of Bundles which can be accessed via the .get() method.
 * These Bundles can be used to quickly set up the different List views.
 */

public enum ListBundles {
    /**
     * used to display all albums in a {@link AlbumsListFragment}
     */
    ALBUM_BUNDLE ( new Object[][]{
        {URI, ALBUMS_URI},
        {FROM, ALBUMS_FROM},
        {PROJECTION, null},
        {SELECTION_STRING, null},
        {SELECTION_ARGS, null}
    }),

    /**
     * used to display all songs in a {@link MediaListFragment}
     */
    MEDIA_BUNDLE ( new Object[][]{
        {URI, MEDIA_URI},
        {FROM, MEDIA_FROM},
        {PROJECTION, null},
        {SELECTION_STRING, null},
        {SELECTION_ARGS, null}
    }),

    /**
     * Used to display all songs from a specific album(s) in a {@link MediaListFragment}.
     * It is required to set the {@link ListUtils}.SELECTION_ARGS key's value to an
     * array holding the album's id!
     */
    MEDIA_FROM_ALBUM_BUNDLE ( new Object[][]{
            {URI, MEDIA_URI},
            {FROM, MEDIA_FROM},
            {PROJECTION, null},
            {SELECTION_STRING, MEDIA_FROM_ALBUM_SELECTION_STRING},
            {SELECTION_ARGS, null} //Should be set to an array containing the clicked album's id
    }),

    /**
     * Used to display all songs from a specific playlist in a {@link MediaListFragment}.
     * It is required to set the {@link ListUtils}.URI key's value to an string like MediaStore.Audio.Playlists.Members.getContentUri("external", #playlistid#).toString()!
     */
    MEDIA_FROM_PLAYLIST_BUNDLE ( new Object[][]{
            {URI, null}, //Should be set to the correct uri from the clicked playlist's id
            {FROM, MEDIA_FROM},
            {PROJECTION, null},
            {SELECTION_STRING, null},
            {SELECTION_ARGS, null}
    }),

    /**
     * used to display all playlists in a {@link PlaylistsListFragment}
     */
    PLAYLIST_BUNDLE ( new Object[][]{
        {URI, PLAYLISTS_URI},
        {FROM, PLAYLISTS_FROM},
        {PROJECTION, null},
        {SELECTION_STRING, null},
        {SELECTION_ARGS, null}
    });

    private final Bundle associatedBundle = new Bundle();

    ListBundles(final Object[][] keyValuePairs){
        for(Object[] keyValue : keyValuePairs){
            if(keyValue[1] instanceof String)
                { associatedBundle.putString((String) keyValue[0], (String) keyValue[1]); }
            else if (keyValue[1] instanceof String[])
                { associatedBundle.putStringArray((String) keyValue[0], (String[]) keyValue[1]); }
            else if (keyValue[1] instanceof Uri)
                { associatedBundle.putString((String) keyValue[0], (String) keyValue[1].toString()); }
            else if ( keyValue[1] instanceof Serializable)
                { associatedBundle.putSerializable((String) keyValue[0], (Serializable) keyValue[1]); }
        }
    }

    /**
     * @return Returns the {@link Bundle} associated with the enum key.
     */
    public Bundle get(){
        return new Bundle(associatedBundle);
    };

}
