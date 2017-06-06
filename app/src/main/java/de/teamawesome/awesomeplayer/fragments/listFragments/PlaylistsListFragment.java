package de.teamawesome.awesomeplayer.listFragments;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;

import static de.teamawesome.awesomeplayer.listFragments.ListUtils.URI;

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

}
