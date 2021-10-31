package edu.sjsu.android.project4template;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * The class is a content provider where the underlying database is LocationsDB.
 */
public class LocationsProvider extends ContentProvider {

    // TODO: provide a public static Uri
    // TODO: a private LocationsDB object
    @Override
    public boolean onCreate() {
        // TODO: construct the LocationDB object
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO: call deleteAll() from the LocationDB object
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: use insert(values) from the LocationDB object to insert the value
        // And there other steps you need to do to insert a value
        // Refer to exercise 5 or lesson 18 page 11 if needed
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: call getAllLocations() from the LocationDB object
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // -----------
    // Following methods won't be used in this project
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}