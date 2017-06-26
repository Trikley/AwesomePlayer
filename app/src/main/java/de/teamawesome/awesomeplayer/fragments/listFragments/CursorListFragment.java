package de.teamawesome.awesomeplayer.fragments.listFragments;

import static de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils.*;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SimpleCursorAdapter;

import de.teamawesome.awesomeplayer.MainMenuActivity;
import de.teamawesome.awesomeplayer.fragments.FragmentListener;

/**
 * Simple {@link ListFragment} which holds the contents of a cursor.
 */
public abstract class CursorListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * This {@link SimpleCursorAdapter} is used to display the cursor's search results.
     */
    private SimpleCursorAdapter simpleCursorAdapter;

    /**
     * The attached {@link FragmentListener}. Should be {@link de.teamawesome.awesomeplayer.MainMenuActivity}.
     */
    protected FragmentListener fragmentListener;

    /**
     * The touch listener which is used to handle touch events such as Gestures.
     * The definition of this listener belongs to the child classes.
     */
    View.OnTouchListener onTouchListener = null;

    /**
     * These attributes are representing the cursors search query.
     * cursorLoaderUri:     Represents the data-set's uri.
     * projection:          The columns received from 'cursorLoaderUri'. 'null' value gets all columns.
     * selectionString:     An SQL like String specifying a where condition's searched columns.
     * selectionArguments:  An SQL like String specifying a where condition's filtered data.
     * sortOrder:           Specifies the results sorting order.
     */
    private Uri cursorLoaderUri = null;
    private String[] projection = null;
    private String selectionString = null;
    private String[] selectionArguments = null;
    private String sortOrder = null;
    /**
     * These two attributes are used to display the different results.
     * from:    The result's columns used to display it. Subset of 'projection'.
     * to:      The contents from the columns specified in 'from' are inserted into these ids.
     *          The ids correspond to the layout used for the 'simpleCursorAdapter'.
     */
    private String[] from = null;
    private int[] to = {android.R.id.text1};

    /**
     * Atributes Which would normally be given to a constructor can be set via this method.
     * @param args Should contain following key value pairs:
     *
     *             "uri" : cursorLoaderUri [{@link Uri}.toString()]
     *             "projection" : projection [{@link String}[]]
     *             "selectionString" : selectionString [{@link String}]
     *             "selectionArguments" : selectionArguments [{@link String}[]]
     *             "sortOrder" : sortOrder [{@link String}[]]
     *             "from" : from [{@link String}]
     *
     *             The Keys can should be declared/accessed by using {@link ListFragment}.URI etc.
     */
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        cursorLoaderUri = Uri.parse(args.getString(URI));
        from= args.getStringArray(FROM);
        projection = args.getStringArray(PROJECTION);
        selectionString= args.getString(SELECTION_STRING);
        selectionArguments = args.getStringArray(SELECTION_ARGS);
        sortOrder = args.getString(SORTORDER);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // simple_list_item_1 is a ListItem layout provided from android.
        simpleCursorAdapter = new SimpleCursorAdapter(getActivity(),android.R.layout.simple_list_item_1,null,from,to, 0);
        getLoaderManager().initLoader(0, null, this);
        setListAdapter(simpleCursorAdapter);
    }

    /**
     * Needed to set the {@link android.widget.ListView}'s onTouchListener.
     * This view actually catches the fling events!
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getListView().setOnTouchListener(new TouchProcessor());
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), cursorLoaderUri,projection,selectionString,selectionArguments,sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        simpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        simpleCursorAdapter.swapCursor(null);
    }

    /**
     * onAttach and onDetach are used to handle the assignment of 'fragmentListener'.
     * ignore the deprecated warning, since the target version of android is 4.1 we cannot
     * use the replacement method.
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
     * This Abstract Method is used to handle Fling events in all list-screens.
     * The method's head is a copy of {@link android.view.GestureDetector.OnGestureListener}.onFling(...)
     */
    abstract protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY);

    /**
     * This Abstract Method is used to handle Single Tap / click events in all list-screens.
     * The method's head is a copy of {@link android.view.GestureDetector.OnGestureListener}.onSingleTapUp(...)
     */
    abstract protected boolean onSingleTap(MotionEvent e);

    /**
     * This class represents the gesture handler for all the {@link CursorListFragment}s.
     * It passes all valid fling events down to the {@link CursorListFragment}'s onFling implementation.
     * Also invalid scroll events are blocked here.
     * A Scroll event is invalid if its Angle is smaller than 45 degrees.
     * A Fling event is invalid if its Angle is greater than 45 degrees or if its distance is smaller than 30 pixel.
     */
    protected class TouchProcessor extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener{

        // The GestureDetector needed to handle the gesture recognition;
        private GestureDetector gestureDetector = new GestureDetector(getActivity(),this);

        /**
         * If a Fling's distance (the distance between touch and release) is smaller than this distance it is considered invalid;
         */
        private final int MIN_FLING_DISTANCE = 30;
        /**
         * If a Fling's Angle (the angle between its direction and the horizontal baseline) is greater than the arctan of this ratio it is considered invalid;
         * If a Scroll's Angle (the angle between its direction and the horizontal baseline) is smaller than the arctan of this ratio it is considered invalid;
         */
        private final double MAX_FLING_ANGLE_RATIO = 1; // == tan(flinAngle); currently the maxAngle is 45 degrees

        /**
         * Directs all events to the fragmentListener to centrally handle the long presses
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return getActivity().onTouchEvent(event) || gestureDetector.onTouchEvent(event) ;
        }

        /**
         * Passes all valid fling events down to the child-class' implementation.
         * Also checks if a fling is invalid!
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = Math.abs(e1.getX() - e2.getX());
            float distanceY= Math.abs(e1.getY() - e2.getY());
            double distance = Math.pow(distanceX, 2) + Math.pow(distanceY, 2);
            if( distanceY/distanceX > MAX_FLING_ANGLE_RATIO || distance < MIN_FLING_DISTANCE) return super.onFling(e1, e2, velocityX, velocityY);

            Log.d("CursorListFragment","onFling");
            return CursorListFragment.this.onFling(e1, e2, velocityX, velocityY);
        }

        /**
         * Catches and denies all invalid scrolls.
         * See MAX_FLING_ANGLE_RATIO
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            Log.d("CursorListFragment","onScroll");
            return Math.abs(distanceY/distanceX) < MAX_FLING_ANGLE_RATIO || super.onScroll(e1, e2, distanceX, distanceY);
        }

        /**
         * Receives single tap / click events and directs them to the child's implementation.
         * Should be used instead of OnListItemClick
         */
        public boolean onSingleTapUp(MotionEvent e){
            Log.d("CursorListFragment", "onSingleTapUp" );
            return onSingleTap(e);
        }

    }

}
