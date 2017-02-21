package com.syzible.dublinnotifier.tools;

/**
 * Created by ed on 30/01/2017.
 */

public class Constants {
    public static final String ENDPOINT = "https://data.dublinked.ie/cgi-bin/rtpi/realtimebusinformation";
    public static final String MAPS_LOCATIONS_TOTAL = "https://www.dublinbus.ie/Templates/Public/RoutePlannerService/RTPIMapHandler.ashx?ne=53.608436,-5.789642&sw=53.031758,-6.726227&zoom=10&czoom=16&_=1487621599468";

    /**
     * What the fuck is czoom as a parameter?
     *
     * @param latNE
     * @param lngNE
     * @param latSW
     * @param lngSW
     * @param zoom
     * @return
     */
    public static String getMapsLocations(float latNE, float lngNE, float latSW, float lngSW, float zoom) {
        return "https://www.dublinbus.ie/Templates/Public/RoutePlannerService/RTPIMapHandler.ashx" +
                "?ne=" + latNE + "," + lngNE + "&sw=" + latSW + "," + lngSW + "&zoom=" + zoom +
                "&czoom=16&_=" + System.currentTimeMillis();
    }
}
