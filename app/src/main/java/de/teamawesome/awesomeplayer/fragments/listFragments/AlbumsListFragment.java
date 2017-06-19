package de.teamawesome.awesomeplayer.fragments.listFragments;

import static de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils.*;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * A {@link CursorListFragment} used to display albums.
 */

public class AlbumsListFragment extends de.teamawesome.awesomeplayer.fragments.listFragments.CursorListFragment {

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Use onSingleTap(...) instead.
    }

    @Override
    protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    /**
     * Used to trigger the Frgment transition to a {@link MediaListFragment} displaying all songs
     * from the clicked album.
     */
    @Override
    protected boolean onSingleTap(MotionEvent e) {
        Bundle arguments = de.teamawesome.awesomeplayer.fragments.listFragments.ListBundles.MEDIA_FROM_ALBUM_BUNDLE.get();
        // The tapped item's position in the list ( 0 based ).
        int listPosition= getListView().pointToPosition((int) e.getX(), (int) e.getY());
        // The tapped item's id which can be used to querry for data from the MediaStore.
        long itemID = getListView().getItemIdAtPosition( listPosition );
        arguments.putStringArray(SELECTION_ARGS, new String[]{"" + itemID});

        fragmentListener.onFragmentInteraction(arguments, this);
        // Returns false to enable correct animation.
        return false;
    }
}
