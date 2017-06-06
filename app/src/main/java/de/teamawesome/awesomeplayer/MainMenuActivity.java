package de.teamawesome.awesomeplayer;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import de.teamawesome.awesomeplayer.listFragments.AlbumsListFragment;
import de.teamawesome.awesomeplayer.listFragments.ListBundles;
import de.teamawesome.awesomeplayer.listFragments.MediaListFragment;
import de.teamawesome.awesomeplayer.listFragments.PlaylistsListFragment;

public class MainMenuActivity extends AppCompatActivity implements FragmentListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        // Create new fragment and transaction
        Fragment initialFragment = new InitialSelectionFragment();
        replaceFragment(initialFragment, null);
    }

    @Override
    public void onFragmentInteraction(Bundle arguments, Object caller) {
        if(caller instanceof MediaListFragment){

        }
        if(caller instanceof AlbumsListFragment){
            replaceFragment(new MediaListFragment(), arguments);
        }
        if(caller instanceof PlaylistsListFragment){
            replaceFragment(new MediaListFragment(), arguments);
        }
    }

    @Override
    public void onFragmentButtonClick(int id) {
        switch(id){
            case R.id.Button_All:
                replaceFragment(new MediaListFragment(), ListBundles.MEDIA_BUNDLE.get());
                break;
            case R.id.Button_Albums:
                replaceFragment(new AlbumsListFragment(), ListBundles.ALBUM_BUNDLE.get());
                break;
            case R.id.Button_Playlists:
                replaceFragment(new PlaylistsListFragment(), ListBundles.PLAYLIST_BUNDLE.get());
                break;
            default: break;
        }
    }


    protected void replaceFragment(Fragment newFragment, Bundle arguments){
        newFragment.setArguments(arguments);
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.MainContainer, newFragment);
        transaction.addToBackStack(null);
        // Commit transaction
        transaction.commit();
    }
}
