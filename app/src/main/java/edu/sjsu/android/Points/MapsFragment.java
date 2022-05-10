package edu.sjsu.android.Points;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private GoogleMap mMap;
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



    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mMap = googleMap;

            if(position != null) {
                mMap.setMapType(mapType);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
            }

            mMap.setOnMapClickListener(point -> {
                LayoutInflater inflater = getActivity().getLayoutInflater();

                final View createMarkerDialogView = inflater.inflate(R.layout.dialog_create_marker, null);
                final TextView titleField = (EditText) createMarkerDialogView.findViewById(R.id.title);
                final TextView descriptionField = (EditText) createMarkerDialogView.findViewById(R.id.description);

                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle("Create New Point")
                        .setView(createMarkerDialogView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String title = titleField.getText().toString();
                                String description = descriptionField.getText().toString();
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(point);
                                markerOptions.title(title);
                                markerOptions.snippet(description);
                                mMap.addMarker(markerOptions).showInfoWindow();
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(LocationsDB.LAT, point.latitude);
                                contentValues.put(LocationsDB.LNG, point.longitude);
                                contentValues.put(LocationsDB.ZOOM, mMap.getCameraPosition().zoom);
                                new MyTask(getActivity()).execute(contentValues);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            });

            mMap.setOnMapLongClickListener(point -> {
                mMap.clear();
                new MyTask(getActivity()).execute();
                Toast.makeText(getActivity(), "All markers are removed", Toast.LENGTH_SHORT).show();
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        getActivity().findViewById(R.id.location).setOnClickListener(this::getLocation);

        LoaderManager.getInstance(this).restartLoader(0, null, this);

        preferences = getActivity().getPreferences(MODE_PRIVATE);
        mapType = preferences.getInt(TYPE, GoogleMap.MAP_TYPE_NORMAL);
        double lat = preferences.getFloat(LAT, -200f);
        double lng = preferences.getFloat(LNG, -200f);
        if (lat != -200f && lng != -200f) {
            position = new LatLng(lat, lng);
        }
        zoom = preferences.getFloat(ZOOM, 0f);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(TYPE, mMap.getMapType());
        editor.putFloat(LAT, (float) mMap.getCameraPosition().target.latitude);
        editor.putFloat(LNG, (float) mMap.getCameraPosition().target.longitude);
        editor.putFloat(ZOOM, mMap.getCameraPosition().zoom);
        editor.apply();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getActivity(), CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        double lat;
        double lng;
        float zoom;
        if (cursor.moveToFirst()) {
            do {
                lat = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationsDB.LAT));
                lng = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationsDB.LNG));
                zoom = cursor.getFloat(cursor.getColumnIndexOrThrow(LocationsDB.ZOOM));
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

    }

    public void getLocation(View view) {
        // Remember to check if the location is enabled
        // and ask for permissions
        // Can implement GPSTracker class to do all these.
        GPSTracker tracker = new GPSTracker(getActivity());
        tracker.getLocation();
    }
}