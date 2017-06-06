package de.teamawesome.awesomeplayer.fragments.listFragments;

import static de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils.*;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * A {@link CursorListFragment} used to display albums.
 */

public class AlbumsListFragment extends de.teamawesome.awesomeplayer.fragments.listFragments.CursorListFragment {
    /**
     * Used to trigger the Frgment transition to a {@link MediaListFragment} displaying all songs
     * from the clicked album.
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Bundle arguments = de.teamawesome.awesomeplayer.fragments.listFragments.ListBundles.MEDIA_FROM_ALBUM_BUNDLE.get();
        arguments.putStringArray(SELECTION_ARGS, new String[]{"" + id});
        fragmentListener.onFragmentInteraction(arguments, this);
    }
}
