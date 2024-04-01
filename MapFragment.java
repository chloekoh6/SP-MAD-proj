package com.sp.android_studio_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private GPSTracker gpsTracker;


    // below are the latitude and longitude
    // of 4 different locations.
    LatLng b1 = new LatLng(1.446735, 103.792589);
    LatLng b2 = new LatLng(1.432924, 103.847409);
    LatLng b3 = new LatLng(1.398602, 103.826810);
    LatLng b4 = new LatLng(1.380411, 103.758832);
    LatLng b5 = new LatLng(1.457635, 103.827840);
    LatLng b6 = new LatLng(1.301812, 103.819257);
    LatLng b7 = new LatLng(1.276756, 103.813764);
    LatLng b8 = new LatLng(1.284666, 103.829578);
    LatLng b9 = new LatLng(1.293980, 103.802844);
    LatLng b10 = new LatLng(1.303883, 103.828019);
    LatLng b11 = new LatLng(1.358863, 103.985038);
    LatLng b12 = new LatLng(1.370876, 103.932853);
    LatLng b13 = new LatLng(1.321794, 103.928046);
    LatLng b14 = new LatLng(1.321451, 103.898864);
    LatLng b15 = new LatLng(1.357146, 103.947959);
    LatLng b16 = new LatLng(1.326256, 103.747115);
    LatLng b17 = new LatLng(1.335866, 103.706260);
    LatLng b18 = new LatLng(1.317454, 103.712561);
    LatLng b19 = new LatLng(1.316791, 103.673474);
    LatLng b20 = new LatLng(1.359351, 103.683774);


    // creating array list for adding all our locations.
    private ArrayList<LatLng> locationArrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        gpsTracker=new GPSTracker(getContext());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // in below line we are initializing our array list.
        locationArrayList = new ArrayList<>();

        // on below line we are adding our
        // locations in our array list.
        locationArrayList.add(b1);
        locationArrayList.add(b2);
        locationArrayList.add(b3);
        locationArrayList.add(b4);
        locationArrayList.add(b5);
        locationArrayList.add(b6);
        locationArrayList.add(b7);
        locationArrayList.add(b8);
        locationArrayList.add(b9);
        locationArrayList.add(b10);
        locationArrayList.add(b11);
        locationArrayList.add(b12);
        locationArrayList.add(b13);
        locationArrayList.add(b14);
        locationArrayList.add(b15);
        locationArrayList.add(b16);
        locationArrayList.add(b17);
        locationArrayList.add(b18);
        locationArrayList.add(b19);
        locationArrayList.add(b20);

        TextView tvAll = view.findViewById(R.id.tv_all);
        TextView tvNorth = view.findViewById(R.id.tv_North);
        TextView tvSouth = view.findViewById(R.id.tv_South);
        TextView tvEast = view.findViewById(R.id.tv_East);
        TextView tvWest = view.findViewById(R.id.tv_West);
        TextView tvLocate = view.findViewById(R.id.tv_locate);
        tvLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap != null) {
                    if (gpsTracker.canGetLocation()){
                        double latitude = gpsTracker.getLatitude();
                        double   longitude = gpsTracker.getLongitude();
                        LatLng currentLocation = new LatLng(latitude, longitude);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
                        mMap.addMarker(new MarkerOptions()
                                .position(currentLocation)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }
                }
            }
        });
        tvAll.setOnClickListener(view1 -> {
            addMark(0, locationArrayList.size());
            tvAll.setSelected(true);
            tvNorth.setSelected(false);
            tvSouth.setSelected(false);
            tvEast.setSelected(false);
            tvWest.setSelected(false);
        });
        tvNorth.setOnClickListener(view1 -> {
            addMark(0, 5);
            tvAll.setSelected(false);
            tvNorth.setSelected(true);
            tvSouth.setSelected(false);
            tvEast.setSelected(false);
            tvWest.setSelected(false);
        });
        tvSouth.setOnClickListener(view1 -> {
            addMark(5, 10);
            tvAll.setSelected(false);
            tvNorth.setSelected(false);
            tvSouth.setSelected(true);
            tvEast.setSelected(false);
            tvWest.setSelected(false);
        });
        tvEast.setOnClickListener(view1 -> {
            addMark(10, 15);
            tvAll.setSelected(false);
            tvNorth.setSelected(false);
            tvSouth.setSelected(false);
            tvEast.setSelected(true);
            tvWest.setSelected(false);
        });
        tvWest.setOnClickListener(view1 -> {
            addMark(15, locationArrayList.size());
            tvAll.setSelected(false);
            tvNorth.setSelected(false);
            tvSouth.setSelected(false);
            tvEast.setSelected(false);
            tvWest.setSelected(true);
        });
        return view;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double   longitude = gpsTracker.getLongitude();
            LatLng currentLocation = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
            mMap.addMarker(new MarkerOptions()
                    .position(currentLocation)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }

        // inside on map ready method
        // we will be displaying all our markers.
        // for adding markers we are running for loop and
        // inside that we are drawing marker on our map.

    }

    private void addMark(int from, int to) {
        mMap.clear();
        for (int i = from; i < to; i++) {

            // below line is use to add marker to each location of our array list.
            mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title("Recycle Bin"));

            // below line is use to zoom our camera on map.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));

            // below line is use to move our camera to the specific location.
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));
        }
    }
}