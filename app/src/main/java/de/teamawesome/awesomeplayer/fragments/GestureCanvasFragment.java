package de.teamawesome.awesomeplayer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import de.teamawesome.awesomeplayer.R;
import de.teamawesome.awesomeplayer.fragments.listFragments.CursorListFragment;

/**
 * This {@link android.app.Fragment} represents the Gesture interface from Screen VI.
 */
public class GestureCanvasFragment extends Fragment {
    private FragmentListener fragmentListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gesture_canvas, container, false);
        view.setOnTouchListener(new GestureCanvasOnTouchListener(this));
        return view;
    }

    /**
     * onAttach and onDetach are used to handle the assignment of 'fragmentListener'
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            fragmentListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    /**
     * This class is used to handle the Gesture recognition.
     */
    private class GestureCanvasOnTouchListener extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener{

        // The CursorListFragment used to handle onFling events
        private GestureCanvasFragment attachedListFragment;
        // The GestureDetector needed to handle the gesture recognition;
        private GestureDetector gd = new GestureDetector(getActivity(),this);

        GestureCanvasOnTouchListener(GestureCanvasFragment _attachCursorListFragment){
            super();
            attachedListFragment = _attachCursorListFragment;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gd.onTouchEvent(event) || true;
        }

        /**
         * Catches all double taps and triggers the fragment transition on the activity;
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            fragmentListener.onFragmentDoubleTap(attachedListFragment);
            return true;
        }

    }

}
