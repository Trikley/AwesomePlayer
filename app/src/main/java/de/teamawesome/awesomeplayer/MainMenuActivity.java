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
        initialFragment.setArguments(ListBundles.PLAYLIST_BUNDLE.get());
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.MainContainer, initialFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Bundle arguments, Object caller) {

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
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.MainContainer, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
