package com.syzible.dublinnotifier.ui;

import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by ed on 20/02/2017.
 */

public class MapsPinMenuAdapter implements GoogleMap.InfoWindowAdapter {

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    // TODO undecided about marker pin bubble -- modify here to set adapter & custom layout
    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
