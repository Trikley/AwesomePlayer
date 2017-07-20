package de.teamawesome.awesomeplayer.fragments.listFragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import de.teamawesome.awesomeplayer.model.Song;

import static de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils.*;

/**
 * A {@link CursorListFragment} used to display playlists.
 */

public class PlaylistsListFragment extends CursorListFragment {

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

}
