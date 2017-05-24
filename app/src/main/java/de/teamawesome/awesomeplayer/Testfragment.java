package de.teamawesome.awesomeplayer;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

/**
 * Created by sven on 5/23/17.
 */

public class Testfragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    SimpleCursorAdapter sca;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] from = {MediaStore.Audio.Albums.ALBUM};
        int[] to = {android.R.id.text1};

        sca = new SimpleCursorAdapter(getActivity(),android.R.layout.simple_list_item_1,null,from,to, 0);
        getLoaderManager().initLoader(0, null, this);
        setListAdapter(sca);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        sca.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        sca.swapCursor(null);
    }
}
