package de.teamawesome.awesomeplayer;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import de.teamawesome.awesomeplayer.fragments.FragmentListener;
import de.teamawesome.awesomeplayer.fragments.GestureCanvasFragment;
import de.teamawesome.awesomeplayer.fragments.InitialSelectionFragment;
import de.teamawesome.awesomeplayer.fragments.PlayerFragment;
import de.teamawesome.awesomeplayer.fragments.listFragments.AlbumsListFragment;
import de.teamawesome.awesomeplayer.fragments.listFragments.ListBundles;
import de.teamawesome.awesomeplayer.fragments.listFragments.MediaListFragment;
import de.teamawesome.awesomeplayer.fragments.listFragments.PlaylistsListFragment;

public class MainMenuActivity extends AppCompatActivity implements FragmentListener {
    /**
     * The Tag under which the main fragment can be found when adding / replacing fragments.
     */
    private static final String MAIN_FRAGMENT_TAG = "MAINFRAGMENT";

    /**
     * The Tag under which the overlay fragment can be found when adding / replacing fragments.
     */
    private static final String OVERLAY_FRAGMENT_TAG = "OVERLAYFRAGMENT";

    /**
     * The gesture detector used to detect and handle long press events.
     */
    public GestureDetector longPressDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        longPressDetector = new GestureDetector(this,new TouchProcessor());
        setContentView(R.layout.activity_main_menu);

        if(savedInstanceState != null) return;
        /* The following code will only be executed on the initial creation.
           Afterwards the if statement prevents wrongly attached fragments.*/
        /* For the initial transaction the .replaceMainFragment method is not used because otherwise
        the adding of the fragment would be revertible to a blank screen.*/
        Fragment initialFragment = new InitialSelectionFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.MainContainer, initialFragment,MAIN_FRAGMENT_TAG);
        transaction.commit();

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
        if(caller instanceof MediaListFragment || caller instanceof SwipeButton){
            if(findViewById(R.id.Songtitle)==null) {
                replaceMainFragment(new PlayerFragment(), arguments);
            }
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
    public void displayFragment(Bundle arguments, Class desiredFragment) {
        if(desiredFragment == PlaylistsListFragment.class){
            replaceMainFragment(new PlaylistsListFragment(), arguments);
        }
    }


    /**
     * Replaces the main fragment with given new fragment set up with given arguments.
     */
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
        return longPressDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }


    /**
     * This class is used to handle the Gesture recognition.
     * It should be used to handle all long press events on all fragments and activities.
     * it can be reached via the activity's or the fragmentListener's onTouchEvent method.
     */
    private class TouchProcessor extends GestureDetector.SimpleOnGestureListener {
        /**
         * Catches all long presses and triggers the fragment transition on the activity;
         */
        @Override
        public void onLongPress(MotionEvent e) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // If an overlay fragment is found it is detached, else a new one is added.
            Fragment gestureCanvasOverlay = getFragmentManager().findFragmentByTag(OVERLAY_FRAGMENT_TAG);
            if(gestureCanvasOverlay != null && gestureCanvasOverlay.isAdded()){
                transaction.detach(gestureCanvasOverlay);
            } else {
                Fragment newFragment = new GestureCanvasFragment();
                transaction.add(R.id.MainContainer, newFragment, OVERLAY_FRAGMENT_TAG);
            }

            transaction.addToBackStack(null);
            transaction.commit();
            Log.d("MainMenuActivity","onLongPress");
        }
    }


}
