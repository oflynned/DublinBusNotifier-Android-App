package com.syzible.dublinnotifier.objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ed on 30/01/2017.
 */

public class Stop {
    private int dueTime;
    private String scheduledDepartureTime;
    private String destination, destinationLocalised, origin, originLocalised;
    private String route, direction;

    public Stop(JSONObject result) {
        try {
            this.dueTime = result.getString("duetime").equals("Due") ? 0 : result.getInt("duetime");
            this.scheduledDepartureTime = result.getString("scheduleddeparturedatetime");
            this.destination = result.getString("destination");
            this.destinationLocalised = result.getString("destinationlocalized");
            this.origin = result.getString("origin");
            this.originLocalised = result.getString("originlocalized");
            this.route = result.getString("route");
            this.direction = result.getString("direction");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getDueTime() {
        return dueTime;
    }

    public String getScheduledDepartureTime() {
        return scheduledDepartureTime;
    }

    public String getDestination() {
        return destination;
    }

    public String getDestinationLocalised() {
        return destinationLocalised;
    }

    public String getOrigin() {
        return origin;
    }

    public String getOriginLocalised() {
        return originLocalised;
    }

    public String getRoute() {
        return route;
    }

    public String getDirection() {
        return direction;
    }
}
