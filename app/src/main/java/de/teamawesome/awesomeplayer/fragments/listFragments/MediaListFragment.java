package de.teamawesome.awesomeplayer.fragments.listFragments;

import android.app.ListFragment;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * A {@link CursorListFragment} used to display songs.
 */

public class MediaListFragment extends CursorListFragment {

    @Override
    protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
