package com.syzible.dublinnotifier.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ed on 30/01/2017.
 */

public class Result {
    private ArrayList<Stop> stops = new ArrayList<>();
    private String stopId, timestamp;
    private String routeFilter;
    private String terminusFilter;

    public Result(String loadingString) {
        this.routeFilter = loadingString;
    }

    public Result(ArrayList<Stop> stops) {
        this.stops = stops;
    }

    public Result(JSONObject result) {
        this(result, null, null);
    }

    public Result(JSONObject result, String route) {
        this(result, route, null);
    }

    public Result(JSONObject webResult, String route, String terminus) {
        try {
            this.stopId = webResult.getString("stopid");
            this.timestamp = webResult.getString("timestamp");
            this.routeFilter = route;
            this.terminusFilter = terminus;

            JSONArray results = webResult.getJSONArray("results");

            if (results.length() > 0)
                for (int i = 0; i < results.length(); i++) {
                    Stop stop = new Stop(results.getJSONObject(i));

                    if (shouldFilterRoute()) {
                        if (shouldFilterTerminus()) {
                            if (stop.getRoute().equals(route) && stop.getDestination().equals(terminus)) {
                                stops.add(stop);
                            }
                        } else {
                            if (stop.getRoute().equals(route)) {
                                stops.add(stop);
                            }
                        }
                    } else {
                        stops.add(stop);
                    }
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Stop> getStops() {
        return stops;
    }

    public String getStopId() {
        return stopId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean shouldFilterRoute() {
        return routeFilter != null;
    }

    public String getRouteFilter() {
        return routeFilter;
    }

    public boolean shouldFilterTerminus() {
        return terminusFilter != null;
    }

    public String getTerminusFilter() {
        return terminusFilter;
    }
}
