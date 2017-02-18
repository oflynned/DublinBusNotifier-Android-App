package com.syzible.dublinnotifier.objects;

/**
 * Created by ed on 18/02/2017.
 */

public class Constraint {
    private String route, stopId, terminus;
    private int minTime;

    public Constraint(String route, String stopId, String terminus, int minTime) {
        this.route = route;
        this.stopId = stopId;
        this.terminus = terminus;
        this.minTime = minTime;
    }

    public String getRoute() {
        return route;
    }

    public String getStopId() {
        return stopId;
    }

    public String getTerminus() {
        return terminus;
    }

    public int getMinTime() {
        return minTime;
    }
}
