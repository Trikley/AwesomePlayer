package de.teamawesome.awesomeplayer.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

import de.teamawesome.awesomeplayer.playerservice.PlayerBindManager;
import de.teamawesome.awesomeplayer.R;
import de.teamawesome.awesomeplayer.model.Song;
import de.teamawesome.awesomeplayer.playerservice.IPlaybackListener;

public class PlayerFragment extends Fragment implements IPlaybackListener{

    private FragmentListener fListener;

    private PlayerBindManager playerBindMgr;

    private ProgressBar progressBar;

    private boolean shown;
    private Song currentlyDisplayedSong;
    private long timestampLastCheckOnDisplay;

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentlyDisplayedSong = null;
        timestampLastCheckOnDisplay = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(playerBindMgr == null) {
            createPlayerBindManager();
        }
        shown = true;
        periodicCheckOfDisplayedInformation();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(playerBindMgr != null) {
            disposeOfPlayerBindManager();
        }
        shown = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // set the current Title
        View view = inflater.inflate(R.layout.activity_screen_ii, container, false);

        TextView songtitleText = (TextView) view.findViewById(R.id.Songtitle);
        songtitleText.setText("", TextView.BufferType.NORMAL);
        TextView noAlbumCover = (TextView) view.findViewById(R.id.noAlbumCover);
        noAlbumCover.setText("", TextView.BufferType.NORMAL);
        resetAlbumCover(view);

        //mapping functions to Buttons
        //Pause Button
        Button pauseButton = (Button)view.findViewById(R.id.pause);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerBindMgr != null) {
                    playerBindMgr.pause();
                }
            }
        });

        //Stop Button
        Button stopButton = (Button)view.findViewById(R.id.stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerBindMgr != null) {
                    playerBindMgr.stop();
                }
            }
        });

        //Play Button
        Button playButton = (Button)view.findViewById(R.id.play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerBindMgr != null) {
                    playerBindMgr.resume();
                }
            }
        });

        //Previous Button
        Button previousButton = (Button)view.findViewById(R.id.previous);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerBindMgr != null) {
                    playerBindMgr.previous();
                }
            }
        });

        //Next Button
        Button nextButton = (Button)view.findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerBindMgr != null) {
                    playerBindMgr.next();
                }
            }
        });

        //Shuffle Button
        Button shuffleButton = (Button)view.findViewById(R.id.shuffle);
        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerBindMgr != null && playerBindMgr.isBound()) {
                    if(playerBindMgr.returnRepeatMode()) {
                        playerBindMgr.shufflePlayQueue();
                    }else {
                        playerBindMgr.shufflePlayQueue();
                    }
                }
            }
        });

        //Backwards Button
        Button backwardsButton = (Button)view.findViewById(R.id.backwards);
        backwardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerBindMgr != null && playerBindMgr.isBound()) {
                    playerBindMgr.jump10SecondsBackward();
                }
            }
        });

        //Forwards Button
        Button forwardsButton = (Button)view.findViewById(R.id.forwards);
        forwardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerBindMgr != null && playerBindMgr.isBound()) {
                    playerBindMgr.jump10SecondsForward();
                }
            }
        });

        //Louder Button
        Button louderButton = (Button)view.findViewById(R.id.louder);
        louderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerBindMgr != null && playerBindMgr.isBound()) {
                    playerBindMgr.increaseVolumeBySomethingLikeOneTenth();
                }
            }
        });

        //quieter Button
        Button quiterButton = (Button)view.findViewById(R.id.quieter);
        quiterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerBindMgr != null && playerBindMgr.isBound()) {
                    playerBindMgr.decreaseVolumeBySomethingLikeOneTenth();
                }
            }
        });

        //ProgressBar
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarSong);
        progressBar.setProgress(0);





/**     //Not-sure-if-working-Album-art-load-Prototype
        //get the current album art
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, null, null, null, null);


                getActivity().managedQuery(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID+ "=?",
                new String[] {String.valueOf(getArguments().getStringArray(ListUtils.MEDIA_ALBUM_ID_IN_ORDER)[0])},
                null);

        if (cursor.moveToFirst()) {
            String albumPath = null;
            while(true) {
                //set current album art
                albumPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                if (albumPath == null) {
                    if(cursor.moveToNext()) {
                        continue;
                    }else {
                        break;
                    }
                }
            }
            if(albumPath != null){
                File imgFile = new  File(albumPath);

                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(albumPath);
                    ImageView myImage = (ImageView) view.findViewById(R.id.albumArt);
                    myImage.setImageBitmap(myBitmap);
                }
            }

        }
 **/

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        progressBar = null;
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

    private void createPlayerBindManager() {
        playerBindMgr = new PlayerBindManager(this.getActivity().getApplication());
        playerBindMgr.addPlaybackListener(this);
    }

    private void disposeOfPlayerBindManager() {
        playerBindMgr.removePlaybackListener(this);
        playerBindMgr.dispose();
        playerBindMgr = null;
    }

    private void resetAlbumCover(View view) {
        if(view!=null) {
            ImageView myImage = (ImageView) view.findViewById(R.id.albumArt);
            myImage.setImageDrawable(getActivity().getResources().getDrawable( R.drawable.default_album_cover));
            TextView noAlbumCover =  (TextView) view.findViewById(R.id.noAlbumCover);
            noAlbumCover.setText("No Album Cover available!", TextView.BufferType.NORMAL);
            noAlbumCover.setGravity(Gravity.CENTER);
        }
    }

    private void updateDisplayedInformation(final Song pSong) {
        new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(shown) {
                    Song song = null;
                    if (pSong != null) {
                        song = pSong;
                    } else if (playerBindMgr != null && playerBindMgr.isBound()) {
                        song = playerBindMgr.returnCurrentSong();
                    }
                    if (song != currentlyDisplayedSong) {
                        if (song == null) {
                            resetAlbumCover(getView());
                            resetSongtitle();
                        } else {
                            updateAlbumCover(song);
                            updateSongtitle(song);
                        }
                    }
                    updateProgressBar(song);
                    currentlyDisplayedSong = song;
                    timestampLastCheckOnDisplay = System.currentTimeMillis();
                }
            }
        });
    }

    private void updateProgressBar(Song song) {
        if(progressBar!=null) {
            if (song == null) {
                progressBar.setProgress(0);
            } else {
                if (playerBindMgr != null && playerBindMgr.isBound()) {
                    progressBar.setMax(playerBindMgr.returnCurrentSongDuration());
                    progressBar.setProgress(playerBindMgr.returnCurrentPosition());
                }
            }
        }
    }

    private void updateAlbumCover(Song song) {
        boolean success = false;
        View view = getView();
        if(view!=null) {

            Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                    MediaStore.Audio.Albums._ID+ "=?",
                    new String[] {song.getAlbumID()},
                    null);

            if (cursor!=null && cursor.moveToFirst()) {
                String albumPath = null;
                while(true) {
                    //set current album art
                    albumPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                    if (albumPath == null) {
                        if(cursor.moveToNext()) {
                            continue;
                        }else {
                            break;
                        }
                    }else {
                        break;
                    }
                }
                cursor.close();
                if(albumPath != null){
                    File imgFile = new  File(albumPath);

                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(albumPath);
                        ImageView myImage = (ImageView) view.findViewById(R.id.albumArt);
                        myImage.setImageBitmap(myBitmap);
                        success = true;

                        TextView noAlbumCover = (TextView) view.findViewById(R.id.noAlbumCover);
                        noAlbumCover.setText("");

                    }
                } else {

                    //sets No Cover found Message
                    TextView noAlbumCover = (TextView) view.findViewById(R.id.noAlbumCover);
                    noAlbumCover.setGravity(Gravity.CENTER);
                    noAlbumCover.setText(Html.fromHtml("<small><i>" + "No Album Cover Found" + "</i></small>"));

                }

            }
            if(!success) {
                resetAlbumCover(view);
            }
        }
    }

    private void updateSongtitle(Song song) {
        View view = getView();
        if (view != null) {
            TextView editText = (TextView) view.findViewById(R.id.Songtitle);
            editText.setText(song.getTitle(), TextView.BufferType.NORMAL);
            editText.setGravity(Gravity.CENTER);
        }
    }

    private void resetSongtitle() {
        View view = getView();
        if (view != null) {
            TextView editText = (TextView) view.findViewById(R.id.Songtitle);
            editText.setText("", TextView.BufferType.NORMAL);
            editText.setGravity(Gravity.CENTER);
        }
    }

    private void periodicCheckOfDisplayedInformation() {
        if(shown) {
            new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(System.currentTimeMillis() - timestampLastCheckOnDisplay > 333) {
                        updateDisplayedInformation(null);
                    }
                    periodicCheckOfDisplayedInformation();
                }
            }, 333);
        }
    }

    @Override
    public void playerBindManagerFinishedBinding(PlayerBindManager pbm) {
        updateDisplayedInformation(null);
    }

    @Override
    public void newSongStartsPlaying(final Song song) {
        updateDisplayedInformation(song);
    }

    @Override
    public void playbackPaused() {
        //TODO something useful!
    }

    @Override
    public void playbackResumed() {
        //TODO something useful!
    }

    @Override
    public void playbackStopped() {
        updateDisplayedInformation(null);
    }

    @Override
    public void volumeChanged(float newVolume) {
        //TODO something useful!
    }
}