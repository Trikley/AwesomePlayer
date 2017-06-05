package de.teamawesome.awesomeplayer;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import de.teamawesome.awesomeplayer.listFragments.ListBundles;
import de.teamawesome.awesomeplayer.listFragments.PlaylistsListFragment;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        // Create new fragment and transaction
        Fragment listFragment = new PlaylistsListFragment();
        listFragment.setArguments(ListBundles.PLAYLIST_BUNDLE.get());
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.MainContainer, listFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
