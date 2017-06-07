package de.teamawesome.awesomeplayer.fragments.listFragments;

import static de.teamawesome.awesomeplayer.fragments.listFragments.ListUtils.*;

import android.app.Activity;
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

import de.teamawesome.awesomeplayer.fragments.FragmentListener;

/**
 * Simple {@link ListFragment} which holds the contents of a cursor.
 */
public class CursorListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * This {@link SimpleCursorAdapter} is used to display the cursor's search results.
     */
    private SimpleCursorAdapter simpleCursorAdapter;

    /**
     * The attached {@link FragmentListener}. Should be {@link de.teamawesome.awesomeplayer.MainMenuActivity}.
     */
    protected FragmentListener fragmentListener;

    /**
     * These attributes are representing the cursors search query.
     * cursorLoaderUri:     Represents the data-set's uri.
     * projection:          The columns received from 'cursorLoaderUri'. 'null' value gets all columns.
     * selectionString:     An SQL like String specifying a where condition's searched columns.
     * selectionArguments:  An SQL like String specifying a where condition's filtered data.
     * sortOrder:           Specifies the results sorting order.
     */
    private Uri cursorLoaderUri = null;
    private String[] projection = null;
    private String selectionString = null;
    private String[] selectionArguments = null;
    private String sortOrder = null;
    /**
     * These two attributes are used to display the different results.
     * from:    The result's columns used to display it. Subset of 'projection'.
     * to:      The contents from the columns specified in 'from' are inserted into these ids.
     *          The ids correspond to the layout used for the 'simpleCursorAdapter'.
     */
    private String[] from = null;
    private int[] to = {android.R.id.text1};

    /**
     * Atributes Which would normally be given to a constructor can be set via this method.
     * @param args Should contain following key value pairs:
     *
     *             "uri" : cursorLoaderUri [{@link Uri}.toString()]
     *             "projection" : projection [{@link String}[]]
     *             "selectionString" : selectionString [{@link String}]
     *             "selectionArguments" : selectionArguments [{@link String}[]]
     *             "sortOrder" : sortOrder [{@link String}[]]
     *             "from" : from [{@link String}]
     *
     *             The Keys can should be declared/accessed by using {@link ListFragment}.URI etc.
     */
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        cursorLoaderUri = Uri.parse(args.getString(URI));
        from= args.getStringArray(FROM);
        projection = args.getStringArray(PROJECTION);
        selectionString= args.getString(SELECTION_STRING);
        selectionArguments = args.getStringArray(SELECTION_ARGS);
        sortOrder = args.getString(SORTORDER);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // simple_list_item_1 is a ListItem layout provided from android.
        simpleCursorAdapter = new SimpleCursorAdapter(getActivity(),android.R.layout.simple_list_item_1,null,from,to, 0);
        getLoaderManager().initLoader(0, null, this);
        setListAdapter(simpleCursorAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), cursorLoaderUri,projection,selectionString,selectionArguments,sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        simpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        simpleCursorAdapter.swapCursor(null);
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
            fragmentListener = (FragmentListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentListener = null;
    }
}
