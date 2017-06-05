package de.teamawesome.awesomeplayer.listFragments;

import static de.teamawesome.awesomeplayer.listFragments.ListUtils.*;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * Created by sven on 6/5/17.
 */

public class AlbumsListFragment extends MediaStoreListFragment {
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Bundle arguments = ListBundles.MEDIA_FROM_ALBUM_BUNDLE.get();
        arguments.putStringArray(SELECTION_ARGS, new String[]{"" + id});

        super.replaceListFragment(new MediaListFragment(), arguments);
    }
}
