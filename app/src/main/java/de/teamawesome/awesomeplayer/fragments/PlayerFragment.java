package de.teamawesome.awesomeplayer.fragments;

import android.app.Activity;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import de.teamawesome.awesomeplayer.R;
import de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils;

import static de.teamawesome.awesomeplayer.R.id.Songtitle;

public class PlayerFragment extends Fragment {

    private FragmentListener fListener;
    private String currentTitle = " ";




    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(getArguments().toString());
        String[] paths = (String[]) getArguments().get(ListUtils.MEDIA_DATA_IN_PLAYBACK_ORDER);


        System.out.println("Playlist Size: " + paths.length + "  Entrys :");
        for(String path : paths) {
            System.out.println(path);
        }

        currentTitle = getArguments().getStringArray(ListUtils.MEDIA_DISPLAY_NAMES_IN_ORDER)[0];


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.activity_screen_ii, container, false);
        EditText editText = (EditText) view.findViewById(R.id.Songtitle);
        editText.setText(currentTitle, TextView.BufferType.EDITABLE);





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