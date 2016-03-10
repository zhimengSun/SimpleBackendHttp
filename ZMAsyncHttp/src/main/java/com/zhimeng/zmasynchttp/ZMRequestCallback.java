package com.zhimeng.zmasynchttp;

/**
 * Created by zhimengsun on 3/10/16.
 */
public interface ZMRequestCallback {

    public int handleJsonString(String urlId, String jsonString);

//    public int handleResponse(int statusCode, String urlId, String responseString);

}
