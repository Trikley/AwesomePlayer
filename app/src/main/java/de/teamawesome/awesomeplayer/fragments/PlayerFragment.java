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

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the current title

    }

    @Override
    public void onResume() {
        super.onResume();
        if(playerBindMgr == null) {
            createPlayerBindManager();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(playerBindMgr != null) {
            disposeOfPlayerBindManager();
        }
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

        resetAlbumCover(view);
        EditText editText = (EditText) view.findViewById(R.id.Songtitle);
        editText.setText("", TextView.BufferType.EDITABLE);

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
        playerBindMgr.dispose();
        playerBindMgr = null;
    }

    private void resetAlbumCover(View view) {
        if(view!=null) {
            ImageView myImage = (ImageView) view.findViewById(R.id.albumArt);
            myImage.setImageDrawable(getActivity().getResources().getDrawable( R.drawable.default_album_cover));
        }
    }

    @Override
    public void playerBindManagerFinishedBinding(PlayerBindManager pbm) {
        if(progressBar!=null) {
            progressBar.setMax(pbm.returnCurrentSongDuration());
        }
    }

    @Override
    public void newSongStartsPlaying(final Song song) {
        new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                boolean success = false;
                if(song!=null) {
                    View view = getView();
                    if(view!=null) {
                        EditText editText = (EditText) view.findViewById(R.id.Songtitle);
                        editText.setText(song.getTitle(), TextView.BufferType.EDITABLE);
                        editText.setGravity(Gravity.CENTER);

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

                                    EditText editText1 = (EditText) view.findViewById(R.id.noAlbumCover);
                                    editText1.setText("");

                                }
                            } else {

                                //sets No Cover found Message
                                EditText editText1 = (EditText) view.findViewById(R.id.noAlbumCover);
                                editText1.setGravity(Gravity.CENTER);
                                editText1.setText(Html.fromHtml("<small><i>" + "No Album Cover Found" + "</i></small>"));
                                //editText1.setText("No Album Cover Found");



                            }

                        }
                        if(!success) {
                            resetAlbumCover(view);
                        }
                    }
                }

            }
        });
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
        new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                View view = getView();
                if(view != null) {
                    resetAlbumCover(view);
                    EditText editText = (EditText) view.findViewById(R.id.Songtitle);
                    editText.setText("", TextView.BufferType.EDITABLE);
                    progressBar = (ProgressBar) view.findViewById(R.id.progressBarSong);
                    progressBar.setProgress(0);
                }
            }
        });
    }

    @Override
    public void volumeChanged(int newVolume) {
        //TODO something useful!
    }
}