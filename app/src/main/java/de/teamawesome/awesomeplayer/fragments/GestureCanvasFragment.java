package de.teamawesome.awesomeplayer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import de.teamawesome.awesomeplayer.R;

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
        view.setOnTouchListener(new TouchProcessor());
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
    private class TouchProcessor extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener{

        // The GestureDetector needed to handle the gesture recognition;
        private GestureDetector gestureDetector = new GestureDetector(getActivity(),this);

        /**
         * Catches all events EXCEPT long presses!
         * These are handled via {@link de.teamawesome.awesomeplayer.MainMenuActivity.TouchProcessor}.
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return getActivity().onTouchEvent(event) || true;
        }
    }

}
