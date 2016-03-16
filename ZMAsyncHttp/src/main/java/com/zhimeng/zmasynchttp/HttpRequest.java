package com.zhimeng.zmasynchttp;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequest {

    private static OkHttpClient okHttpClient;
    public static final MediaType MediaTypeJSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");
    private static final MediaType MEDIA_TYPE_ZIP = MediaType.parse("zip");
    public static int REQUEST_TIME_OUT = 30;
    public static int WRITE_TIME_OUT = 30;
    public static int READ_TIME_OUT = 30;
    public static String USER_AGENT = "Android OkHttp With ZMAsyncHttp 0.0.3";

    private static OkHttpClient getInstance(){
        if (okHttpClient == null)
           okHttpClient = new OkHttpClient.Builder()
                   .connectTimeout(REQUEST_TIME_OUT, TimeUnit.SECONDS)
                   .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                   .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                   .build();
        return okHttpClient;
    }

    public static String get(String url) {
        Request request = new Request.Builder().url(url)
                .header("User-Agent", USER_AGENT)
                .build();
        return getResponseJSON(request);
    }

    public static String post(String url, String params) {
        return sendRequestBody(url, HTTPMethod.POST, params);
    }

    public static String put(String url, String params) {
        return sendRequestBody(url, HTTPMethod.PUT, params);
    }

    public static String delete(String url) {
        return sendRequestBody(url, HTTPMethod.DELETE,"");
    }

    private static String sendRequestBody(String url, String methodName, String params){
        RequestBody body = RequestBody.create(MediaTypeJSON, params);
        return sendRequest(url, methodName, body);
    }

    public static String sendMultiRequestBody(String url, String methodName,
                                              Map<String, String> textParams,
                                              Map<String, String> fileParams){
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        requestBodyBuilder.addFormDataPart("ZMRequestMethod", methodName);

        if (textParams != null) {
            for (String key : textParams.keySet()) {
                String value = textParams.get(key);
                if (null != value && "".equals(value)){
                    if (key.contains("[]")) {
                        String[] values = value.split(",");
                        for (int i = 0; i < values.length; i++)
                            requestBodyBuilder.addFormDataPart(key, values[i]);
                    } else
                        requestBodyBuilder.addFormDataPart(key, value);
                }
            }
        }
        if (fileParams != null){
            for (String key:fileParams.keySet()){
                String fileName = fileParams.get(key);
                requestBodyBuilder.addFormDataPart(key, fileName, RequestBody.create(getMineTypeFromFileName(fileName), new File(fileName)));
            }
        }
        RequestBody body = requestBodyBuilder.build();
        return sendRequest(url, methodName, body);
    }

    private static MediaType getMineTypeFromFileName(String fileName){
        if (fileName.endsWith(".png")) {
            return MEDIA_TYPE_PNG;
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return MEDIA_TYPE_JPEG;
        } else {
            return MEDIA_TYPE_ZIP;
        }
    }

    private static String sendRequest(String url, String methodName, RequestBody body){
        Request request;
        Request.Builder builder = new Request.Builder().url(url)
                .header("User-Agent", USER_AGENT);
        if (methodName.equals(HTTPMethod.DELETE)){
            request = builder.delete(body).build();
        } else if (methodName.equals(HTTPMethod.PUT)){
            request = builder.put(body).build();
        } else {
            request = builder.post(body).build();
        }
        return getResponseJSON(request);
    }

    // 所有请求的返回处理函数
    private static String getResponseJSON(Request request){
        String res = "";
        try {
            Response response = getInstance().newCall(request).execute();
            res = response.body().string();
        } catch (IOException e){
            e.printStackTrace();
        }
        return res;
    }


}
