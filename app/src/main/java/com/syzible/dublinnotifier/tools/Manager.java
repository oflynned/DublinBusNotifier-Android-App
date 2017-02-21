package com.syzible.dublinnotifier.tools;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.syzible.dublinnotifier.R;

/**
 * Created by ed on 18/02/2017.
 */

public class Manager {
    private int stage = 0;
    private static Manager manager = new Manager();

    private Manager() {
    }

    public static Manager getInstance() {
        return manager;
    }

    public void changeFragment(Fragment fragment, FragmentManager fragmentManager) {
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, String.valueOf(fragment.getClass()))
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
}
