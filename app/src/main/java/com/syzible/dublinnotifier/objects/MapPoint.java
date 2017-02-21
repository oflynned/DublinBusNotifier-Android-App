package com.syzible.dublinnotifier.objects;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ed on 20/02/2017.
 */

public class MapPoint implements ClusterItem {
    private float lat, lng;
    private String stopNumber, address, link, routes;

    public MapPoint(JSONObject point) {
        try {
            this.lat = Float.parseFloat(point.getString("lat"));
            this.lng = Float.parseFloat(point.getString("lng"));
            this.stopNumber = point.getString("stopnumber");
            this.address = point.getString("address");
            this.link = point.getString("link");
            this.routes = point.getString("routes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

    public String getStopNumber() {
        return stopNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getLink() {
        return link;
    }

    public String getRoutes() {
        return routes;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(lat, lng);
    }
}
