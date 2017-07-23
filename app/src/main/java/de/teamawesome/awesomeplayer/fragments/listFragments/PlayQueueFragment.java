package de.teamawesome.awesomeplayer.fragments.listFragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import de.teamawesome.awesomeplayer.R;
import de.teamawesome.awesomeplayer.fragments.FragmentListener;
import de.teamawesome.awesomeplayer.model.Song;
import de.teamawesome.awesomeplayer.playerservice.IPlaybackListener;
import de.teamawesome.awesomeplayer.playerservice.PlayerBindManager;

public class PlayQueueFragment extends Fragment implements IPlaybackListener {

    private FragmentListener fListener;

    private boolean shown;

    private boolean showingPlayqueue;

    private PlayerBindManager playerBindMgr;

    public PlayQueueFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_queue, container, false);

        //mapping functions to Buttons

        final Button playqueueButton = (Button)view.findViewById(R.id.playqueuebutton);
        final Button backstackButton = (Button)view.findViewById(R.id.backstackbutton);
        showingPlayqueue = true;
        playqueueButton.setEnabled(false);
        backstackButton.setEnabled(true);

        //playqueuebutton Button
        playqueueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!showingPlayqueue) {
                    displayPlayqueue();
                }
            }
        });

        //backstack Button
        backstackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showingPlayqueue) {
                    displayBackstack();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            fListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(playerBindMgr == null) {
            createPlayerBindManager();
        }
        shown = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(playerBindMgr != null) {
            disposeOfPlayerBindManager();
        }
        shown = false;
    }

    private void createPlayerBindManager() {
        playerBindMgr = new PlayerBindManager(this.getActivity().getApplication());
        playerBindMgr.addPlaybackListener(this);
    }

    private void disposeOfPlayerBindManager() {
        playerBindMgr.removePlaybackListener(this);
        playerBindMgr.dispose();
        playerBindMgr = null;
    }

    private void displayPlayqueue() {
        if(shown && playerBindMgr!=null && playerBindMgr.isBound()) {
            View view = getView();
            ListView listView = (ListView) view.findViewById(R.id.songlist);
            if(listView!=null) {
                List<Song> playQueueList = playerBindMgr.getPlayQueueAsListCopy();
                playQueueList.add(0, new Song() {
                    @Override
                    public String toString() {
                        return "";
                    }
                });
                listView.setAdapter(new ArrayAdapter<Song>(this.getActivity(),
                        android.R.layout.simple_list_item_1,
                        playQueueList.toArray(new Song[0])));
                final Button playqueueButton = (Button)view.findViewById(R.id.playqueuebutton);
                final Button backstackButton = (Button)view.findViewById(R.id.backstackbutton);
                showingPlayqueue = true;
                playqueueButton.setEnabled(false);
                backstackButton.setEnabled(true);
            }
        }
    }

    private void displayBackstack() {
        if(shown && playerBindMgr!=null && playerBindMgr.isBound()) {
            View view = getView();
            ListView listView = (ListView) view.findViewById(R.id.songlist);
            if(listView!=null) {
                List<Song> backStackList = playerBindMgr.getBackStackAsListCopy();
                backStackList.add(0, new Song() {
                    @Override
                    public String toString() {
                        return "";
                    }
                });
                listView.setAdapter(new ArrayAdapter<Song>(this.getActivity(),
                        android.R.layout.simple_list_item_1,
                        backStackList.toArray(new Song[0])));
                final Button playqueueButton = (Button)view.findViewById(R.id.playqueuebutton);
                final Button backstackButton = (Button)view.findViewById(R.id.backstackbutton);
                showingPlayqueue = false;
                playqueueButton.setEnabled(true);
                backstackButton.setEnabled(false);
            }
        }
    }

    private void updateDisplay() {
        new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(showingPlayqueue) {
                    displayPlayqueue();
                }else {
                    displayBackstack();
                }
            }
        });
    }

    @Override
    public void playerBindManagerFinishedBinding(PlayerBindManager pbm) {
      updateDisplay();
    }

    @Override
    public void newSongStartsPlaying(Song song) {
        updateDisplay();
    }

    @Override
    public void playbackPaused() {

    }

    @Override
    public void playbackResumed() {

    }

    @Override
    public void playbackStopped() {
        updateDisplay();
    }

    @Override
    public void volumeChanged(float newVolume) {

    }

}
