package edu.sjsu.android.Points;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class ListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final Uri CONTENT_URI = LocationsProvider.CONTENT_URI;
    ArrayList<Point> data;
    ListAdapter adapter;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = new ArrayList<>();
        initDataSet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setAdapter(new ListAdapter(getActivity(), getActivity().getContentResolver().query(CONTENT_URI, null, null, null)));
        return view;
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
        String title;
        String description;
        if (cursor.moveToFirst()) {
            do {
                lat = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationsDB.LAT));
                lng = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationsDB.LNG));
                zoom = cursor.getFloat(cursor.getColumnIndexOrThrow(LocationsDB.ZOOM));
                title = cursor.getString(cursor.getColumnIndexOrThrow(LocationsDB.TITLE));
                description = cursor.getString(cursor.getColumnIndexOrThrow(LocationsDB.DESCRIPTION));
                data.add(new Point(lat, lng, zoom, title, description));
            }
            while(cursor.moveToNext());
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public void initDataSet() {
        LoaderManager.getInstance(this).restartLoader(0, null, this);
    }
}