package de.teamawesome.awesomeplayer.fragments.listFragments;


import android.app.ListFragment;
import android.content.Loader;
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

import de.teamawesome.awesomeplayer.fragments.FragmentListener;
import de.teamawesome.awesomeplayer.model.Song;

import static de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils.MEDIA_ALBUM_ID_IN_ORDER;
import static de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils.MEDIA_DATA_IN_PLAYBACK_ORDER;
import static de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils.MEDIA_DISPLAY_NAMES_IN_ORDER;
import static de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils.SELECTION_ARGS;

/**
 * A {@link CursorListFragment} used to display songs.
 */
public class MediaListFragment extends CursorListFragment {
    private Song[] currentSongs;
    @Override
    protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // The flung item's position in the list ( 0 based ).
        int listPosition= getListView().pointToPosition((int) e1.getX(), (int) e1.getY());
        // The flung item's id which can be used to querry for data from the MediaStore.
        long itemID = getListView().getItemIdAtPosition( listPosition );

        //Log.d("FLING","Fling-position -- " + listPosition );

        //opens Playlistview and adds the flung song to a chosen playlist
        Bundle arguments = ListBundles.PLAYLIST_BUNDLE.get();
        arguments.putParcelable("KeyCurrentSong", currentSongs[listPosition]);
        arguments.putBundle("SongsBundle", this.getArguments());
        fragmentListener.displayFragment(arguments, PlaylistsListFragment.class);

        return false;
    }

    @Override
    protected boolean onSingleTap(MotionEvent e) {
        Bundle arguments = ListBundles.MEDIA_BUNDLE.get();

        fragmentListener.onFragmentInteraction(arguments, this);
        // Returns false to enable correct animation.
        return false;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        super.onLoadFinished(loader, data);
        currentSongs = Song.extractSongsFromCursor(data);
    }
}

