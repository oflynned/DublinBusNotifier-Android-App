package com.syzible.dublinnotifier.networking;

/**
 * Created by ed on 16/12/2016
 */

abstract class GetRequest<T> extends Request<T> {
    GetRequest(NetworkCallback<T> networkCallback, String url) {
        super(networkCallback, url, "GET");
    }
}