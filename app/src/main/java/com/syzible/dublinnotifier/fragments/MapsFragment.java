package com.syzible.dublinnotifier.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.syzible.dublinnotifier.R;
import com.syzible.dublinnotifier.networking.NetworkCallback;
import com.syzible.dublinnotifier.networking.ReqJSONObject;
import com.syzible.dublinnotifier.objects.MapPoint;
import com.syzible.dublinnotifier.tools.Constants;
import com.syzible.dublinnotifier.tools.Manager;
import com.syzible.dublinnotifier.ui.MapsPinMenu;
import com.syzible.dublinnotifier.ui.OnFilterSelection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsFragment extends Fragment implements OnMapReadyCallback, NetworkCallback<JSONObject> {
    private ArrayList<MapPoint> points = new ArrayList<>();
    private ClusterManager<MapPoint> clusterManager;
    private GoogleMap googleMap;

    private static final LatLng DUBLIN_CENTRE = new LatLng(53.347306, -6.259137);
    private static final float INITIAL_ZOOM = 16.0f;

    private OnFilterSelection onFilterSelection;

    public MapsFragment setOnFilterSelection(OnFilterSelection onFilterSelection) {
        this.onFilterSelection = onFilterSelection;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap updatedMap) {
        this.googleMap = updatedMap;

        new ReqJSONObject(this, Constants.MAPS_LOCATIONS_TOTAL).execute();

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Manager.getInstance().removeLastFragment(getFragmentManager());
            }
        });
    }

    @Override
    public void onSuccess(JSONObject object) {
        try {
            JSONArray pointsArray = object.getJSONArray("points");
            for (int i = 0; i < pointsArray.length(); i++)
                points.add(new MapPoint(pointsArray.getJSONObject(i)));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        setUpClusterManager();
        for (MapPoint mapPoint : points)
            clusterManager.addItem(mapPoint);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DUBLIN_CENTRE, INITIAL_ZOOM));
    }

    @Override
    public void onFailure() {

    }

    private void setUpClusterManager() {
        clusterManager = new ClusterManager<>(getContext(), googleMap);
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);
        clusterManager.setRenderer(new MapsPinMenu(getContext(), googleMap, clusterManager));
    }
}
