package de.teamawesome.awesomeplayer.fragments.listFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import static de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils.SELECTION_ARGS;

/**
 * A {@link CursorListFragment} used to display songs.
 */

public class MediaListFragment extends CursorListFragment {
    /**
     * Used to trigger the Frgment transition to a {@link MediaListFragment} displaying all songs
     * from the clicked album.
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Bundle arguments = ListBundles.MEDIA_BUNDLE.get();
        arguments.putStringArray(SELECTION_ARGS, new String[]{"" + id});
        fragmentListener.onFragmentInteraction(arguments, this);
    }
}
