package de.teamawesome.awesomeplayer.listFragments;

import static de.teamawesome.awesomeplayer.listFragments.ListUtils.*;

import android.net.Uri;
import android.os.Bundle;

import java.io.Serializable;

/**
 * Created by sven on 6/5/17.
 */

public enum ListBundles {
    ALBUM_BUNDLE ( new Object[][]{
        {URI, ALBUMS_URI},
        {FROM, ALBUMS_FROM},
        {PROJECTION, null},
        {SELECTION_STRING, null},
        {SELECTION_ARGS, null}
    }),

    MEDIA_BUNDLE ( new Object[][]{
        {URI, MEDIA_URI},
        {FROM, MEDIA_FROM},
        {PROJECTION, null},
        {SELECTION_STRING, null},
        {SELECTION_ARGS, null}
    }),

    MEDIA_FROM_ALBUM_BUNDLE ( new Object[][]{
            {URI, MEDIA_URI},
            {FROM, MEDIA_FROM},
            {PROJECTION, null},
            {SELECTION_STRING, MEDIA_FROM_ALBUM_SELECTION_STRING},
            {SELECTION_ARGS, null} //Should be set to an array containing the clicked album's is
    }),

    MEDIA_FROM_PLAYLIST_BUNDLE ( new Object[][]{
            {URI, null}, //Should be set to the correct uri from the clicked playlist's id
            {FROM, MEDIA_FROM},
            {PROJECTION, null},
            {SELECTION_STRING, null},
            {SELECTION_ARGS, null}
    }),


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

    public Bundle get(){
        return new Bundle(associatedBundle);
    };

}
