package de.teamawesome.awesomeplayer;

import android.os.Bundle;

/**
 * Created by sven on 6/6/17.
 */

public interface FragmentListener {
    public void onFragmentInteraction(Bundle arguments, Object caller);
    public void onFragmentButtonClick(int id);
}
