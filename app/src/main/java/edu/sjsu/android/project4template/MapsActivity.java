package edu.sjsu.android.project4template;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {

    private GoogleMap mMap;
    // TODO: set up following class attributes
    // Uri: get from LocationsProvider
    // 2 LatLng for SJSU and CS department (given in exercise 7, or you can find those yourself)
    // (Extra credit) SharedPreferences and KEYs needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        // TODO: attach listeners to the buttons
        // TODO: retrieve and draw already saved locations in map
        // Hint: a method in LoaderManager
        // TODO: extra credit - restore the map setting
        //  (camara position & map type) using SharedPreferences
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // TODO: extra credit - restore the map setting
        mMap.setOnMapClickListener(point -> {
            // TODO: insert the LatLng point to the database on click
            // (You may consider put the code in an private helper method)
            // 1) Add a maker on the point to the map
            // 2) Store the latitude, longitude and zoom level of the point to SQLite database using ContentValues
            // 3) Call execute(ContentValues) on a MyTask (defined below) object to add a point
        });

        mMap.setOnMapLongClickListener(point -> {
            // TODO: delete all locations from the database on long click
            // (You may consider put the code in an private helper method)
            // 1) Clear all markers from the Google Map
            // 2) Call execute() on a MyTask (defined below) object to clear the database
            // 3) Toast a message "All makers are removed"
        });
    }

    // Below is the class that extend AsyncTask, to insert/delete data in background
    // Note that AsyncTask is deprecated from API 30, but you can still use it.
    // You can use java.util.concurrent instead, if you are familiar with threads and concurrency.
    private class MyTask extends AsyncTask<ContentValues, Void, Void> {
        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            // TODO: insert one row or delete all data base on input
            // Hint: if the first contentValues is not null, insert it
            // Otherwise, delete all.
            // Both operations should be done through content provider
            return null;
        }
    }
    // ----- End of AsyncTask classes -----


    // Below are for the CursorLoader, that is, the methods of
    // LoaderManager.LoaderCallbacks<Cursor> interface
    /**
     * Instantiate and return a new Loader for the database.
     *
     * @param id   the ID whose loader is to be created
     * @param args any arguments supplied by the caller
     * @return a new Loader instance that is ready to start loading
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        // TODO: load the cursor that's pointing to the database
        // Hint: return a CursorLoader object with this context
        // and the URI (set other parameters to null)
        return null;
    }

    /**
     * Draw the markers after data is loaded, that is, the loader returned
     * in the onCreateLoader has finished its load.
     *
     * @param loader the Loader that has finished
     * @param cursor a cursor to read the data generated by the Loader
     */
    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        // TODO: display markers based on the database using cursor
        // First, get data row by row using the cursor
        // For each row, get the latitude, longitude and zoom level
        // Draw a marker based on the LatLng object on the map
        // After getting all data, move the "camera" to focus on the last clicked location
        // Also the zoom level should be the same as the last clicked location
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // No need to implement
    }
    // ----- End of LoaderManager.LoaderCallbacks<Cursor> methods -----

    // Below are the methods respond to clicks on buttons
    // Remember to attach them to the buttons in onCreate
    // getLocation and switchView are the same as the ones in exercise 6.
    public void uninstall(View view) {
        // TODO: uninstall the app
    }

    public void getLocation(View view) {
        // TODO: get the current location
        // Remember to check if the location is enabled
        // and ask for permissions
        // Can implement GPSTracker class to do all these.
    }

    public void switchView(View view) {
        // TODO: switch between different views based on the button being clicked
    }
}