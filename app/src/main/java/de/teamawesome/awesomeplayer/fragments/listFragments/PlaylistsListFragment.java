package de.teamawesome.awesomeplayer.fragments.listFragments;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import static de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils.SELECTION_ARGS;
import static de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils.URI;

/**
 * A {@link CursorListFragment} used to display playlists.
 */

public class PlaylistsListFragment extends CursorListFragment {

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Use onSingleTap(...) instead.
    }

    @Override
    protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    /**
     * Used to trigger the Fragment transition to a {@link MediaListFragment} displaying all songs
     * from the clicked playlist.
     */
    @Override
    protected boolean onSingleTap(MotionEvent e) {
        Bundle arguments = ListBundles.MEDIA_FROM_PLAYLIST_BUNDLE.get();
        // The tapped item's position in the list ( 0 based ).
        int listPosition= getListView().pointToPosition((int) e.getX(), (int) e.getY());
        // The tapped item's id which can be used to querry for data from the MediaStore.
        long itemID = getListView().getItemIdAtPosition( listPosition );
        arguments.putString(URI, MediaStore.Audio.Playlists.Members.getContentUri("external", itemID).toString());

        fragmentListener.onFragmentInteraction(arguments, this);

        // Returns false to enable correct animation.
        return false;
    }
}
