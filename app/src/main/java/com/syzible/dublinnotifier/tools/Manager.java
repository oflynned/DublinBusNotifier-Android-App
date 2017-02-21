package com.syzible.dublinnotifier.tools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.syzible.dublinnotifier.R;
import com.syzible.dublinnotifier.networking.NetworkCallback;
import com.syzible.dublinnotifier.networking.ReqJSONObject;
import com.syzible.dublinnotifier.ui.FilterDialog;
import com.syzible.dublinnotifier.ui.FilterListener;

import org.json.JSONObject;

/**
 * Created by ed on 18/02/2017.
 */

public class Manager {
    private int stage = 0;
    private String stopId = "1358";
    private static Manager manager = new Manager();

    private Manager() {
    }

    public static Manager getInstance() {
        return manager;
    }

    public void changeFragment(Fragment fragment, FragmentManager fragmentManager) {
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment.getClass().getName())
                .addToBackStack(null).commit();
        stage++;
    }

    public void removeLastFragment(FragmentManager fragmentManager) {
        fragmentManager.popBackStack();
        stage--;
    }

    public int getStage() {
        return stage;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public void loadData(NetworkCallback<JSONObject> networkCallback) {
        String url = Constants.ENDPOINT + "?stopid=" + stopId + "&format=json";
        new ReqJSONObject(networkCallback, url).execute();
    }

    public void createFilterDialog(FragmentManager fragmentManager, final NetworkCallback<JSONObject> callback) {
        final FilterDialog filterDialog = new FilterDialog();
        filterDialog.setFilterListener(new FilterListener() {
            @Override
            public void onFilter() {
                loadData(callback);
            }
        }).show(fragmentManager, "Filter");
    }
}
