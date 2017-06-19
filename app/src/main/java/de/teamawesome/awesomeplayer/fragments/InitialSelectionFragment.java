package de.teamawesome.awesomeplayer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    private FragmentListener fragmentListener;

    /**
     * Used to set the Fragment's Button's Listeners.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_initial_selection, container, false);

        View.OnTouchListener customTouchListener = new TouchProcessor();

        // Listener Button ALL
        view.findViewById(R.id.Button_All).setOnTouchListener(customTouchListener);

        // Listener Button ALBUMS
        view.findViewById(R.id.Button_Albums).setOnTouchListener(customTouchListener);

        // Listener Button PLAYLISTS
        view.findViewById(R.id.Button_Playlists).setOnTouchListener(customTouchListener);
        return view;
    }

    /**
     * onAttach and onDetach are used to handle the assignment of 'fragmentListener'
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            fragmentListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }

    private class TouchProcessor extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener{
        private GestureDetector gestureDetector = new GestureDetector(getActivity(), this);
        private int currentViewID;

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d("InitialSelectionF" , "onSingleTapUp");
            fragmentListener.onFragmentButtonClick(currentViewID);
            return super.onSingleTapUp(e) || true;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            currentViewID = v.getId();
            return fragmentListener.onTouchEvent(event) || gestureDetector.onTouchEvent(event);
        }

    }
}
