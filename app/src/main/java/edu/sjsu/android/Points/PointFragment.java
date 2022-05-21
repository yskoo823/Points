package edu.sjsu.android.Points;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PointFragment extends Fragment {

    private Point selected;
    private GoogleMap mMap;

    public PointFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        String key = getString(R.string.key);
        if (bundle != null && bundle.containsKey(key))
            selected = bundle.getParcelable(key);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_point, container, false);
        final TextView title = view.findViewById(R.id.title);
        final TextView description = view.findViewById(R.id.description);

        if (selected != null) {
            title.setText(selected.getTitle());
            description.setText(selected.getDescription());
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.showUpButton();
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(@NonNull GoogleMap googleMap) {
                    mMap = googleMap;
                    LatLng position = new LatLng(selected.getLatitude(), selected.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 7f));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(position);
                    markerOptions.title(selected.getTitle());
                    mMap.addMarker(markerOptions).showInfoWindow();
                    mMap.getUiSettings().setAllGesturesEnabled(false);
                }
            });
        }
    }
}