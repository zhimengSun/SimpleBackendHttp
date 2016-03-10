package com.zhimeng.zmasynchttp;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.schedulers.HandlerScheduler;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func0;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * Created by zhimengsun on 2/19/16.
 */
public class BackgroundHttpUtils<T> {

    private static BackgroundHttpUtils backgroundHttpUtils;
    private String url, httpMethod;
    private Handler backgroundHandler;
    private T ctx;
    Map<String, String> textParams, fileParams;


    public void startSendHttp(T c, String u) {
        startSendHttp(c, u, "GET");
    }

    public void startSendHttp(T c, String u, String h) {
        startSendHttp(c, u, h, null);
    }

    public void startSendHttp(T c, String u, String h, Map<String, String> ts) {
        startSendHttp(c, u, h, ts, null);

    }

    public void startSendHttp(T c, String u, String h, Map<String, String> ts, Map<String, String> fs) {
        this.ctx = c;
        this.url = u;
        this.httpMethod = h;
        this.textParams = ts;
        this.fileParams = fs;
        handleFinalRes();
    }

    public void handleFinalRes() {
        if (backgroundHandler == null)
            backgroundHandler = new Handler(BackgroundHttpUtils.getInstance().getBackThread().getLooper());
        BackgroundHttpUtils.getInstance().sendRequestObservable()
                .subscribeOn(HandlerScheduler.from(backgroundHandler))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.i("ZMAsyncHttp", "onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("ZMAsyncHttp", "onError()", e);
                    }

                    @Override
                    public void onNext(String str) {
                        String[] backArr = str.split("\\|");
                        String urlId = "";
                        String json = "{}";
                        Log.i("JSON", str);
                        if (backArr.length >= 2) {
                            urlId = backArr[0];
                            json = backArr[1];
                        }
                        ((JSONCallback) ctx).handleJsonString(urlId, json);
                        Log.i("ZMAsyncHttp", "onNext(" + str + ")");
                    }
                });
    }

    public Map<String, String> getTextParams() {
        return textParams;
    }

    public static BackgroundHttpUtils getInstance() {
        if (backgroundHttpUtils == null) backgroundHttpUtils = new BackgroundHttpUtils();
        return backgroundHttpUtils;
    }

    public BackgroundThread getBackThread() {
        BackgroundThread backgroundThread = new BackgroundThread();
        backgroundThread.start();
        return backgroundThread;
    }

    public Observable<String> sendRequestObservable() {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                String res = "";
                try {
                    res = sendHttp();
                } catch (Exception e) {
                    throw OnErrorThrowable.from(e);
                }
                return Observable.just(res);
            }
        });
    }

    private String sendHttp() {
        String res;
        if (httpMethod.equals("GET"))
            res = HttpRequest.doGet(url);
        else
            res = HttpRequest.sendMultiRequestBody(url, httpMethod, getTextParams(), fileParams);
        return httpMethod + url + "|" + res;
    }

    public static class BackgroundThread extends HandlerThread {
        public BackgroundThread() {
            super("ZMAsyncHttp-BackgroundThread", THREAD_PRIORITY_BACKGROUND);
        }
    }

}
