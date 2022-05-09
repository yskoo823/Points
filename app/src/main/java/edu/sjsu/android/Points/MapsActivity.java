package edu.sjsu.android.Points;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {

    private GoogleMap mMap;
    // TODO: set up following class attributes
    // Uri: get from LocationsProvider
    private final Uri CONTENT_URI = LocationsProvider.CONTENT_URI;
    // 2 LatLng for SJSU and CS department (given in exercise 6, or you can find those yourself)
    // (Extra credit) SharedPreferences and KEYs needed
    SharedPreferences preferences;
    private final LatLng LOCATION_UNIV = new LatLng(37.335371, -121.881050);
    private final LatLng LOCATION_CS = new LatLng(37.333714, -121.881860);

    private final String TYPE = "type";
    private final String LAT = "lat";
    private final String LNG = "lng";
    private final String ZOOM = "zoom";
    private int mapType;
    private LatLng position;
    private float zoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        findViewById(R.id.city).setOnClickListener(this::switchView);
        findViewById(R.id.university).setOnClickListener(this::switchView);
        findViewById(R.id.cs).setOnClickListener(this::switchView);
        findViewById(R.id.location).setOnClickListener(this::getLocation);
        findViewById(R.id.uninstall).setOnClickListener(this::uninstall);

        // TODO: retrieve and draw already saved locations in map
        // Hint: a method in LoaderManager
        LoaderManager.getInstance(this).restartLoader(0, null, this);
        // TODO: extra credit - restore the map setting
        //  (camara position & map type) using SharedPreferences
        preferences = getPreferences(MODE_PRIVATE);
        mapType = preferences.getInt(TYPE, GoogleMap.MAP_TYPE_NORMAL);
        double lat = preferences.getFloat(LAT, -200f);
        double lng = preferences.getFloat(LNG, -200f);
        if (lat != -200f && lng != -200f) {
            position = new LatLng(lat, lng);
        }
        zoom = preferences.getFloat(ZOOM, 0f);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(TYPE, mMap.getMapType());
        editor.putFloat(LAT, (float) mMap.getCameraPosition().target.latitude);
        editor.putFloat(LNG, (float) mMap.getCameraPosition().target.longitude);
        editor.putFloat(ZOOM, mMap.getCameraPosition().zoom);
        editor.apply();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if(position != null) {
            mMap.setMapType(mapType);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
        }

        mMap.setOnMapClickListener(point -> {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(point);
            mMap.addMarker(markerOptions);
            ContentValues contentValues = new ContentValues();
            contentValues.put(LocationsDB.COLUMN_1, point.latitude);
            contentValues.put(LocationsDB.COLUMN_2, point.longitude);
            contentValues.put(LocationsDB.COLUMN_3, mMap.getCameraPosition().zoom);
            new MyTask().execute(contentValues);
        });

        mMap.setOnMapLongClickListener(point -> {
            mMap.clear();
            new MyTask().execute();
            Toast.makeText(this, "All markers are removed", Toast.LENGTH_SHORT).show();
        });
    }

    // Below is the class that extend AsyncTask, to insert/delete data in background
    // Note that AsyncTask is deprecated from API 30, but you can still use it.
    // You can use java.util.concurrent instead, if you are familiar with threads and concurrency.
    private class MyTask extends AsyncTask<ContentValues, Void, Void> {
        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            if (contentValues.length > 0) {
                getContentResolver().insert(CONTENT_URI, contentValues[0]);
            }
            else {
                getContentResolver().delete(CONTENT_URI, null, null);
            }
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
        return new CursorLoader(this, CONTENT_URI, null, null, null, null);
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
        double lat;
        double lng;
        float zoom;
        if (cursor.moveToFirst()) {
            do {
                lat = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationsDB.COLUMN_1));
                lng = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationsDB.COLUMN_2));
                zoom = cursor.getFloat(cursor.getColumnIndexOrThrow(LocationsDB.COLUMN_3));
                LatLng point = new LatLng(lat, lng);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(point);
                mMap.addMarker(markerOptions);
            }
            while(cursor.moveToNext());

            if (position != null) return;

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));
        }
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
        Intent delete = new Intent(Intent.ACTION_DELETE,
                Uri.parse("package:" + getPackageName()));
        startActivity(delete);
    }

    public void getLocation(View view) {
        // Remember to check if the location is enabled
        // and ask for permissions
        // Can implement GPSTracker class to do all these.
        GPSTracker tracker = new GPSTracker(this);
        tracker.getLocation();
    }

    public void switchView(View view) {
        CameraUpdate update = null;
        if (view.getId() == R.id.city) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 10f);
        } else if (view.getId() == R.id.university) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 14f);
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            update = CameraUpdateFactory.newLatLngZoom(LOCATION_CS, 18f);
        }
        mMap.animateCamera(update);
    }
}