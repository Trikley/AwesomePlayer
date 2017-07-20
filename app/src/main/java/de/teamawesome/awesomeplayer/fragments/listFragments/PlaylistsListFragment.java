package de.teamawesome.awesomeplayer.fragments.listFragments;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import de.teamawesome.awesomeplayer.R;
import de.teamawesome.awesomeplayer.model.Song;

import static de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils.*;

/**
 * A {@link CursorListFragment} used to display playlists.
 */

public class PlaylistsListFragment extends CursorListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_list, container, false);
        view.findViewById(R.id.Button_Add_Playlist).setOnClickListener(new AddButtonListener());
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Use onSingleTap(...) instead.
    }

    @Override
    protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // The tapped item's position in the list ( 0 based ).
        int listPosition= getListView().pointToPosition((int) e1.getX(), (int) e2.getY());
        // The tapped item's id which can be used to querry for data from the MediaStore.
        long itemID = getListView().getItemIdAtPosition( listPosition );
        // Cursor loads all Titles from flung playlist
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Playlists.Members.getContentUri("external", itemID), null, null, null, null);
        Song[] songsFromPlaylist = Song.extractSongsFromCursor(cursor);
        cursor.close();

        // Returns false to enable correct animation.
        return false;
    }

    /**
     * Used to trigger the Fragment transition to a {@link MediaListFragment} displaying all songs
     * from the clicked playlist.
     */
    @Override
    protected boolean onSingleTap(MotionEvent e) {
//        Bundle arguments = ListBundles.MEDIA_FROM_PLAYLIST_BUNDLE.get();

        // The tapped item's position in the list ( 0 based ).
        int listPosition= getListView().pointToPosition((int) e.getX(), (int) e.getY());
        // The tapped item's id which can be used to querry for data from the MediaStore.
        long itemID = getListView().getItemIdAtPosition( listPosition );

//        arguments.putString(URI, MediaStore.Audio.Playlists.Members.getContentUri("external", itemID).toString());
//        fragmentListener.onFragmentInteraction(arguments, this);

        //adds Song to playlist
        if(this.getArguments().containsKey("KeyCurrentSong")){
            Song song = this.getArguments().getParcelable("KeyCurrentSong");
            ContentValues cV = new ContentValues();
            cV.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, 0);
            cV.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, song.getId());
            getActivity().getContentResolver().insert(MediaStore.Audio.Playlists.Members.getContentUri("external", itemID), cV);
            fragmentListener.onFragmentInteraction(this.getArguments().getBundle("SongsBundle"), this);
        } else {
            //directs back to Songlist
            Bundle arguments = ListBundles.MEDIA_FROM_PLAYLIST_BUNDLE.get();
            arguments.putString(URI, MediaStore.Audio.Playlists.Members.getContentUri("external", itemID).toString());
            fragmentListener.onFragmentInteraction(arguments, this);
        }

        // Returns false to enable correct animation.
        return false;
    }

    private class AddButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // Prompt user for name
            AlertDialog.Builder promptDialog = new AlertDialog.Builder(getActivity());
            promptDialog.setTitle("ADD NEW PLAYLIST");
            promptDialog.setMessage("Enter playlist's name");
            final EditText input = new EditText(getActivity());
            promptDialog.setView(input);

            promptDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String value = input.getText().toString();
                    if(value == null || value.equals("")) return;

                    // Add playlist to media store
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.Audio.Playlists.NAME, value);
                    contentValues.put(MediaStore.Audio.Playlists.DATE_ADDED, System.currentTimeMillis());
                    contentValues.put(MediaStore.Audio.Playlists.DATE_MODIFIED, System.currentTimeMillis());

                    ContentResolver contentResolver = getActivity().getContentResolver();
                    contentResolver.insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, contentValues);
                }
            });

            promptDialog.setNegativeButton("Cancel", null);

            promptDialog.show();
        }
    }
}
