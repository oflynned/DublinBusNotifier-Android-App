package com.syzible.dublinnotifier.networking;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by ed on 18/12/2016
 */

public class SearchHelper {
    public static String buildQuery(String url, HashMap<String, String> params) {
        Set<?> keys = params.keySet();
        Iterator iterator = keys.iterator();
        String paramBuilder = "", builtUrl = "";
        int i = 0;

        while(iterator.hasNext()) {
            String key = iterator.next().toString();
            paramBuilder += i == 0 ? "?" : "&";
            paramBuilder += params.get(key);
            iterator.remove();
            i++;
        }

        builtUrl += url.substring(url.length()).equals("/") ? url : url + "/";
        return builtUrl + paramBuilder;
    }
}
