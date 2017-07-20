package de.teamawesome.awesomeplayer;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import de.teamawesome.awesomeplayer.fragments.FragmentListener;

/**
 * This class can be used to implement a swipeable button like the one on top of all screens.
 */
public class SwipeButton extends android.support.v7.widget.AppCompatButton {
    private Activity attachedActivity;

    /**
     * Yes we need all those constructors! See Issue #12.
     */
    public SwipeButton(Context context) {
        super(context);
        initialize((Activity) context);
    }

    public SwipeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize((Activity) context);
    }

    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize((Activity) context);
    }

    public SwipeButton(Activity activity) {
        super(activity);
        initialize(activity);
    }

    public SwipeButton(Activity activity, AttributeSet attrs) {
        super(activity, attrs);
        initialize(activity);
    }

    public SwipeButton(Activity activity, AttributeSet attrs, int defStyleAttr) {
        super(activity, attrs, defStyleAttr);
        initialize(activity);
    }

    /**
     * Used for in all constructors
     */
    private void initialize(Activity activity){
        attachedActivity = activity;
        setOnTouchListener(new TouchProcessor());
    }

    private void onFlingDown(){
        ((FragmentListener) attachedActivity).onFragmentInteraction(null,this);
    }

    /**
     * This class handles the Touch events.
     * It handles all fling events and triggers the activities onTouchMethod to catch long presses.
     */
    private class TouchProcessor extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener{

        // The GestureDetector needed to handle the gesture recognition;
        private GestureDetector gestureDetector = new GestureDetector(attachedActivity, this);

        TouchProcessor(){
            super();
            gestureDetector = new GestureDetector(attachedActivity, this);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // If Fling goes down and is fast enough
            if( velocityY < 1 || attachedActivity instanceof FragmentListener){
                onFlingDown();
            }
            Log.d("SwipeButton","onFling " + velocityX + " " + velocityY);
            return true;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event) || attachedActivity.onTouchEvent(event) || true;
        }
    }


}
