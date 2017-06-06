package de.teamawesome.awesomeplayer.listFragments;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ListView;

import static de.teamawesome.awesomeplayer.listFragments.ListUtils.URI;

/**
 * Created by sven on 6/5/17.
 */

public class PlaylistsListFragment extends MediaStoreListFragment {
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Bundle arguments = ListBundles.MEDIA_FROM_PLAYLIST_BUNDLE.get();
        arguments.putString(URI, MediaStore.Audio.Playlists.Members.getContentUri("external", id).toString());
        fListener.onFragmentInteraction(arguments, this);
    }

}
