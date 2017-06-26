package de.teamawesome.awesomeplayer.fragments;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import de.teamawesome.awesomeplayer.PlayerService;
import de.teamawesome.awesomeplayer.R;
import de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils;

import static de.teamawesome.awesomeplayer.R.id.Songtitle;
import static de.teamawesome.awesomeplayer.R.id.play;
import static de.teamawesome.awesomeplayer.R.id.albumArt;

public class PlayerFragment extends Fragment implements ServiceConnection{

    private FragmentListener fListener;
    private String currentTitle = " ";



    private String currentTitle;
    private PlayerService.PlayerBind playerBind = null;
    private boolean playerServiceBound = false;

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
        // get the current title
        currentTitle = getArguments().getStringArray(ListUtils.MEDIA_DISPLAY_NAMES_IN_ORDER)[0];


    }

    @Override
    public void onResume() {
        super.onResume();
        if(!playerServiceBound) {
            Intent playerServiceIntent = new Intent(getActivity().getApplication(), PlayerService.class);
            System.out.println("Service started: " + getActivity().getApplication().startService(playerServiceIntent));
            System.out.println("Service bound? " + getActivity().getApplication().bindService(playerServiceIntent , this, 0));
            playerServiceBound = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(playerServiceBound) {
            getActivity().getApplication().unbindService(this);
            playerBind = null;
            playerServiceBound = false;
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
        EditText editText = (EditText) view.findViewById(R.id.Songtitle);
        editText.setText(currentTitle, TextView.BufferType.EDITABLE);


/**
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

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        playerBind = (PlayerService.PlayerBind) service;
        playerServiceBound = true;
        playerBind.clearPlayQueue();
        playerBind.stop();
        String[] pathToSongs = getArguments().getStringArray(ListUtils.MEDIA_DATA_IN_PLAYBACK_ORDER);
        playerBind.playSongNow(pathToSongs[0]);
        for(int i=1; i<pathToSongs.length; i++) {
            playerBind.playSongWhenReady(pathToSongs[i]);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        playerBind = null;
        playerServiceBound = false;
    }
}