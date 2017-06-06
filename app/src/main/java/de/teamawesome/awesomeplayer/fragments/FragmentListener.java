package de.teamawesome.awesomeplayer.fragments;

import android.os.Bundle;

/**
 * This interface is used to trigger events on an implementing {@link android.app.Activity} which is
 * attached to a {@link android.app.Fragment}.
 * The Events triggered should be transitions to other Fragments/ Activities
 */

public interface FragmentListener {
    /**
     * Used to trigger Interactions
     * @param arguments This Bundle can be hold additional interaction information.
     * @param caller Should always be referencing the calling object.
     */
    public void onFragmentInteraction(Bundle arguments, Object caller);

    /**
     * Should be called when a button with a specified id is clicked.
     * @param id The clicked button's id.
     */
    public void onFragmentButtonClick(int id);
}
