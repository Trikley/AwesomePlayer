package de.teamawesome.awesomeplayer.fragments.listFragments;


import android.content.Loader;
import android.view.MotionEvent;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import java.util.Arrays;
import java.util.List;

import de.teamawesome.awesomeplayer.model.Song;
import de.teamawesome.awesomeplayer.playerservice.PlayerBindManager;

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

        int listPosition= getListView().pointToPosition((int) e.getX(), (int) e.getY());
        Song[] selectedSongs = Arrays.copyOfRange(currentSongs, listPosition, currentSongs.length);
        PlayerBindManager manager = new PlayerBindManager(getActivity().getApplication());
        manager.stop();
        manager.playAllSongsWhenReady(selectedSongs);
        manager.dispose();
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

