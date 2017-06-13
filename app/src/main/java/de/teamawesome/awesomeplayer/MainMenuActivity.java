package de.teamawesome.awesomeplayer;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import de.teamawesome.awesomeplayer.fragments.FragmentListener;
import de.teamawesome.awesomeplayer.fragments.GestureCanvasFragment;
import de.teamawesome.awesomeplayer.fragments.InitialSelectionFragment;
import de.teamawesome.awesomeplayer.fragments.listFragments.AlbumsListFragment;
import de.teamawesome.awesomeplayer.fragments.listFragments.ListBundles;
import de.teamawesome.awesomeplayer.fragments.listFragments.MediaListFragment;
import de.teamawesome.awesomeplayer.fragments.listFragments.PlaylistsListFragment;

public class MainMenuActivity extends AppCompatActivity implements FragmentListener {
    private static final String MAIN_FRAGMENT_TAG = "MAINFRAGMENT";
    private static final String OVERLAY_FRAGMENT_TAG = "OVERLAYFRAGMENT";
    private GestureDetector doubleTapDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doubleTapDetector = new GestureDetector(this,new GestureCanvasOnTouchListener(this));
        setContentView(R.layout.activity_main_menu);
        // Create new fragment and transaction
        Fragment initialFragment = new InitialSelectionFragment();
        replaceMainFragment(initialFragment, null);
        Button b = (Button) findViewById(R.id.TestButton);
        b.setOnTouchListener(new SwipeButtonOnTouchListener(this));

    }

    /**
     * Handles interactions with the currently loaded Fragments.
     * Interactions should only result in layout changes (e.g. replacing a CursorListFragment).
     * Data handling like setting up a the arguments Bundle for the new Fragment or triggering media
     * playback should be done in the Fragment's implementation.
     * @param arguments This Bundle can be hold additional interaction information.
     * @param caller Should always be referencing the calling object.
     */
    @Override
    public void onFragmentInteraction(Bundle arguments, Object caller) {
        if(caller instanceof MediaListFragment){

        }
        if(caller instanceof AlbumsListFragment){
            replaceMainFragment(new MediaListFragment(), arguments);
        }
        if(caller instanceof PlaylistsListFragment){
            replaceMainFragment(new MediaListFragment(), arguments);
        }
    }

    /**
     * Handles interactions with hard-coded Buttons.
     * Interactions should only result in layout changes (e.g. replacing a CursorListFragment).
     * Data handling like setting up a the arguments Bundle for the new Fragment or triggering media
     * playback should be done in the Fragment's implementation.
     * @param id The clicked button's id.
     */
    @Override
    public void onFragmentButtonClick(int id) {
        switch(id){
            case R.id.Button_All:
                replaceMainFragment(new MediaListFragment(), ListBundles.MEDIA_BUNDLE.get());
                break;
            case R.id.Button_Albums:
                replaceMainFragment(new AlbumsListFragment(), ListBundles.ALBUM_BUNDLE.get());
                break;
            case R.id.Button_Playlists:
                replaceMainFragment(new PlaylistsListFragment(), ListBundles.PLAYLIST_BUNDLE.get());
                break;
            default: break;
        }
    }

    @Override
    public void onFragmentDoubleTap(Object caller) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if(caller instanceof GestureCanvasFragment){
            transaction.detach(getFragmentManager().findFragmentByTag(OVERLAY_FRAGMENT_TAG));
        } else {
            Fragment newFragment = new GestureCanvasFragment();
            transaction.add(R.id.MainContainer, newFragment, OVERLAY_FRAGMENT_TAG);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }


    protected void replaceMainFragment(Fragment newFragment, Bundle arguments){
        newFragment.setArguments(arguments);
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.MainContainer, newFragment,MAIN_FRAGMENT_TAG);
        transaction.addToBackStack(null);
        // Commit transaction
        transaction.commit();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return doubleTapDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }


    /**
     * This class is used to handle the Gesture recognition.
     */
    private class GestureCanvasOnTouchListener extends GestureDetector.SimpleOnGestureListener {

        // The CursorListFragment used to handle onFling events
        private MainMenuActivity attachedActivity;
        // The GestureDetector needed to handle the gesture recognition;


        GestureCanvasOnTouchListener(MainMenuActivity _attachedActivity){
            super();
            attachedActivity = _attachedActivity;
        }

        /**
         * Catches all double taps and triggers the fragment transition on the activity;
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            attachedActivity.onFragmentDoubleTap(attachedActivity);
            return true;
        }

    }

    private class SwipeButtonOnTouchListener extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener{

        // The CursorListFragment used to handle onFling events
        private MainMenuActivity attachedActivity;
        // The GestureDetector needed to handle the gesture recognition;
        private GestureDetector gd;

        SwipeButtonOnTouchListener (MainMenuActivity _attachedActivity){
            super();
            attachedActivity = _attachedActivity;
            gd = new GestureDetector(attachedActivity, this);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("FLING","FLING");
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gd.onTouchEvent(event) || true;
        }
    }
}
