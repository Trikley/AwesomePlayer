package de.teamawesome.awesomeplayer.fragments.listFragments;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import static de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils.URI;

/**
 * A {@link CursorListFragment} used to display playlists.
 */

public class PlaylistsListFragment extends CursorListFragment {
    /**
     * Used to trigger the Fragment transition to a {@link MediaListFragment} displaying all songs
     * from the clicked playlist.
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Bundle arguments = ListBundles.MEDIA_FROM_PLAYLIST_BUNDLE.get();
        arguments.putString(URI, MediaStore.Audio.Playlists.Members.getContentUri("external", id).toString());
        fragmentListener.onFragmentInteraction(arguments, this);
    }

    @Override
    protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
