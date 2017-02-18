package com.syzible.dublinnotifier.tools;

import com.syzible.dublinnotifier.objects.Stop;

/**
 * Created by ed on 18/02/2017.
 */

public class Manager {
    private static Manager manager = new Manager();
    private Manager() {}
    public static Manager getInstance() {
        return manager;
    }

    public void createNotification(Stop stop) {

    }

    public void updateNotification(Stop stop) {

    }

    public void cancelNotification(Stop stop) {

    }
}
