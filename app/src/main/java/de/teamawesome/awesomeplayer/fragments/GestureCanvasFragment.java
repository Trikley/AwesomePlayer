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
        /** All names defined by the raw data from res/raw/gesture.txt
         *  which was created using the gesture building tool**/
        private final String SWIPE_RIGHT = "SWIPE_RIGHT"; /* Swipe from right to left */
        private final String SWIPE_LEFT = "SWIPE_LEFT"; /* Swipe from left to right */
        private final String CIRCLE_CLOCKWISE = "CIRCLE_CLOCKWISE";
        private final String CIRCLE_COUNTERCLOCKWISE = "CIRCLE_COUNTERCLOCKWISE";
        private final String Z_GESTURE = "Z_GESTURE";

        private GestureLibrary gestureLib;

        TouchProcessor(){
            // Loads the raw file created from the gesture builder.
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
            // Recognize custom gestures
            ArrayList<Prediction> predictions = gestureLib.recognize(gesture);

            // Find prediction with the maximum recognition score
            Prediction maxScorePrediction = predictions.get(0);
            for (Prediction prediction : predictions) if (prediction.score > maxScorePrediction.score) maxScorePrediction = prediction;

            // Switch the according name and do action
            if( maxScorePrediction != null && maxScorePrediction.score > 2.0)
                switch (maxScorePrediction.name){
                    case SWIPE_LEFT:
                        Log.d("GestureCanvas", "Gesture recognized: '" + SWIPE_LEFT + "'");
                        // Previous Song
                        break;
                    case SWIPE_RIGHT:
                        Log.d("GestureCanvas", "Gesture recognized: '" + SWIPE_RIGHT + "'");
                        // Next Song
                        break;
                    case CIRCLE_CLOCKWISE:
                        Log.d("GestureCanvas", "Gesture recognized: '" + CIRCLE_CLOCKWISE + "'");
                        // Forwards
                        break;
                    case CIRCLE_COUNTERCLOCKWISE:
                        Log.d("GestureCanvas", "Gesture recognized: '" + CIRCLE_COUNTERCLOCKWISE + "'");
                        // Backwards
                        break;
                    case Z_GESTURE:
                        Log.d("GestureCanvas", "Gesture recognized: '" + Z_GESTURE + "'");
                        // Toggle Shuffle
                        break;
                    default: Log.e("GestureCanvas", "Gesture name '" + maxScorePrediction.name + "' not specified!");
                }

        }

        // Gestures / Touch events NOT recognized via the gesture library
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.d("GestureCanvas", "SingleTap");
            // Pause playback
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if(velocityY > 0) {
                Log.d("GestureCanvas", "Fling down");
                // Fling down
                // Volume down
            } else {
                Log.d("GestureCanvas", "Fling up");
                // Fling up
                // Volume up
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

}
