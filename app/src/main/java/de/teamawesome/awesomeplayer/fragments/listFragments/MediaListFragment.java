package de.teamawesome.awesomeplayer.fragments.listFragments;


import android.app.ListFragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import static de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils.MEDIA_ALBUM_ID_IN_ORDER;
import static de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils.MEDIA_DATA_IN_PLAYBACK_ORDER;
import static de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils.MEDIA_DISPLAY_NAMES_IN_ORDER;
import static de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils.SELECTION_ARGS;

/**
 * A {@link CursorListFragment} used to display songs.
 */
public class MediaListFragment extends CursorListFragment {

    @Override
    protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // The flung item's position in the list ( 0 based ).
        int listPosition= getListView().pointToPosition((int) e1.getX(), (int) e1.getY());
        // The flung item's id which can be used to querry for data from the MediaStore.
        long itemID = getListView().getItemIdAtPosition( listPosition );

        //Log.d("FLING","Fling-position -- " + listPosition );
        return false;
    }

    @Override
    protected boolean onSingleTap(MotionEvent e) {
        Bundle arguments = ListBundles.MEDIA_BUNDLE.get();
        Cursor c = (Cursor) getListView().getItemAtPosition(getListView().pointToPosition((int)e.getX(), (int)e.getY()));

        for(String name : c.getColumnNames()) {
            System.out.println(name);
        }


        if(!(c.getCount()>0)) {
            throw new RuntimeException("Something went terribly wrong! The cursor " + c + " is retrieved" +
                    " from an existing ListView and should always have more than 0 rows!");
        }
        ArrayList<String> mediaDataInPlaybackOrder = new ArrayList<>();
        ArrayList<String> mediaDisplayNamesInOrder = new ArrayList<>();
        ArrayList<String> mediaAlbumIdInOrder = new ArrayList<>();
        //c.getString(1) returns the second collumn of the row the curser points at. This should be
        //the file-path to the selected media
        mediaDataInPlaybackOrder.add(c.getString(1));
        mediaDisplayNamesInOrder.add(c.getString(2));
        mediaAlbumIdInOrder.add(c.getString(13));
        while(c.moveToNext()) {
            mediaDataInPlaybackOrder.add(c.getString(1));
            mediaDisplayNamesInOrder.add(c.getString(2));
            mediaAlbumIdInOrder.add(c.getString(13));
        }
        arguments.putStringArray(MEDIA_DATA_IN_PLAYBACK_ORDER, mediaDataInPlaybackOrder.toArray(new String[0]));
        arguments.putStringArray(MEDIA_DISPLAY_NAMES_IN_ORDER, mediaDisplayNamesInOrder.toArray(new String[0]));
        arguments.putStringArray(MEDIA_ALBUM_ID_IN_ORDER, mediaAlbumIdInOrder.toArray(new String[0]));
        fragmentListener.onFragmentInteraction(arguments, this);
        // Returns false to enable correct animation.
        return false;
    }

}
