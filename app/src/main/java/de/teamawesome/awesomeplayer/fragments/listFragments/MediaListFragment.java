package de.teamawesome.awesomeplayer.fragments.listFragments;

import android.app.ListFragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * A {@link CursorListFragment} used to display songs.
 */
public class MediaListFragment extends CursorListFragment {

    @Override
    protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // The flung item's position in the list ( 0 based ).
        int listPosition= getListView().pointToPosition((int) e1.getX(), (int) e1.getY());
        // The flung item's id which can be used to querry for data from the MediaStore.
        long itemID = getListView().getItemIdAtPosition( listPosition );

        //Log.d("FLING","Fling-position -- " + listPosition );
        return false;
    }
}
