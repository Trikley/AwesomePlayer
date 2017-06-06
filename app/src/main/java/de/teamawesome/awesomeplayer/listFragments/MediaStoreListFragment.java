package de.teamawesome.awesomeplayer.listFragments;

import static de.teamawesome.awesomeplayer.listFragments.ListUtils.*;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.SimpleCursorAdapter;

import de.teamawesome.awesomeplayer.FragmentListener;
import de.teamawesome.awesomeplayer.R;

/**
 * Created by sven on 5/24/17.
 */

public class MediaStoreListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    SimpleCursorAdapter sca;
    FragmentListener fListener;

    private Uri cursorLoaderUri = ALBUMS_URI;
    private String[] from = ALBUMS_FROM;
    private String[] projection = null;
    private String selectionString = null;
    private String[] selectionArguments = null;
    private int[] to = {android.R.id.text1};

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        cursorLoaderUri = Uri.parse(args.getString(URI));
        from= args.getStringArray(FROM);
        projection = args.getStringArray(PROJECTION);
        selectionString= args.getString(SELECTION_STRING);
        selectionArguments = args.getStringArray(SELECTION_ARGS);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sca = new SimpleCursorAdapter(getActivity(),android.R.layout.simple_list_item_1,null,from,to, 0);
        getLoaderManager().initLoader(0, null, this);
        setListAdapter(sca);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), cursorLoaderUri,null,selectionString,selectionArguments,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        sca.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        sca.swapCursor(null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            fListener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fListener = null;
    }
}
