package de.teamawesome.awesomeplayer.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.teamawesome.awesomeplayer.R;

/**
 * A simple Fragment showing Buttons to display All songs, albums or playlists.
 */
public class InitialSelectionFragment extends Fragment {
    /**
     * The attached {@link FragmentListener}. Should be {@link de.teamawesome.awesomeplayer.MainMenuActivity}.
     */
    private FragmentListener fListener;

    /**
     * Used to set the Fragment's Button's Listeners.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_initial_selection, container, false);

        // Listener Button ALL
        view.findViewById(R.id.Button_All).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fListener.onFragmentButtonClick(R.id.Button_All);
            }
        });

        // Listener Button ALBUMS
        view.findViewById(R.id.Button_Albums).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fListener.onFragmentButtonClick(R.id.Button_Albums);
            }
        });

        // Listener Button PLAYLISTS
        view.findViewById(R.id.Button_Playlists).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fListener.onFragmentButtonClick(R.id.Button_Playlists);
            }
        });

        return view;
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
            fListener = (FragmentListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fListener = null;
    }
}
