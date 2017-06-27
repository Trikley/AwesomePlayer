package de.teamawesome.awesomeplayer.fragments;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import de.teamawesome.awesomeplayer.R;

/**
 * This {@link android.app.Fragment} represents the Gesture interface from Screen VI.
 */
public class GestureCanvasFragment extends Fragment {
    private FragmentListener fragmentListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gesture_canvas, container, false);
//        view.setOnTouchListener(new TouchProcessor());
        TouchProcessor touchProcessor = new TouchProcessor();
        view.findViewById(R.id.GestureView).setOnTouchListener(touchProcessor);
        ((GestureOverlayView)view.findViewById(R.id.GestureView)).addOnGesturePerformedListener(touchProcessor);
        return view;
    }

    /**
     * onAttach and onDetach are used to handle the assignment of 'fragmentListener'
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FragmentListener) {
            fragmentListener = (FragmentListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
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
    private class TouchProcessor extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener, GestureOverlayView.OnGesturePerformedListener{

        // The GestureDetector needed to handle the gesture recognition;
        private GestureDetector gestureDetector = new GestureDetector(getActivity(),this);

        private GestureLibrary gestureLib;
        TouchProcessor(){
            gestureLib = GestureLibraries.fromRawResource(getActivity(), R.raw.gesture);
            gestureLib.load();
        }
        /**
         * Catches all events EXCEPT long presses!
         * These are handled via {@link de.teamawesome.awesomeplayer.MainMenuActivity.TouchProcessor}.
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return getActivity().onTouchEvent(event) || gestureDetector.onTouchEvent(event) || true;
        }


        @Override
        public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
            ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
            for (Prediction prediction : predictions) {
                if (prediction.score > 2.0) {
                    Log.d("GestureCanvas", "Gesture: " + prediction.name + " [" + prediction.score + "]");
                }

            }
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.d("GestureCanvas", "ST");
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("GestureCanvas", "Fling");
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

}
