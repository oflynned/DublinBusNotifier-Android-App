package com.syzible.dublinnotifier.ui;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.syzible.dublinnotifier.objects.MapPoint;

/**
 * Created by ed on 20/02/2017.
 */

public class MapsPinMenu extends DefaultClusterRenderer<MapPoint> {

    public MapsPinMenu(Context context, GoogleMap map, ClusterManager<MapPoint> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(MapPoint item, MarkerOptions markerOptions) {
        markerOptions.title(item.getStopNumber());
        markerOptions.snippet(item.getAddress());

        super.onBeforeClusterItemRendered(item, markerOptions);
    }
}
