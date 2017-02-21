package com.syzible.dublinnotifier.fragments;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.LocationListener;
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
import com.syzible.dublinnotifier.ui.FilterDialog;
import com.syzible.dublinnotifier.ui.MapsPinMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsFragment extends Fragment implements OnMapReadyCallback, LocationListener, NetworkCallback<JSONObject> {
    private ArrayList<MapPoint> points = new ArrayList<>();
    private ClusterManager<MapPoint> clusterManager;

    private GoogleMap googleMap;
    private Location lastKnownLocation;

    private static final LatLng DUBLIN_CENTRE = new LatLng(53.347306, -6.259137);
    private static final float INITIAL_ZOOM = 10.0f;
    private static final float MY_LOCATION_ZOOM = 18.0f;

    private static final int ONE_MINUTE = 1000 * 60;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }
        };

        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (android.location.LocationListener) locationListener);
        //lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        new ReqJSONObject(this, Constants.MAPS_LOCATIONS_TOTAL).execute();

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Manager.getInstance().removeLastFragment(getFragmentManager());
                Manager.getInstance().setStopId(marker.getTitle());

                Fragment fragment = getFragmentManager().findFragmentByTag(CardsFragment.class.getName());
                Manager.getInstance().createFilterDialog(getFragmentManager(), (NetworkCallback<JSONObject>) fragment);
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

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DUBLIN_CENTRE, MY_LOCATION_ZOOM));
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

    @Override
    public void onLocationChanged(Location location) {

    }

    private boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > ONE_MINUTE;
        boolean isSignificantlyOlder = timeDelta < -ONE_MINUTE;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

}
