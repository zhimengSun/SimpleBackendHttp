package com.zhimeng.zmasynchttp;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.util.HashMap;
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
    private Handler backgroundHandler;

    public void startSendHttp(T c, String u) {
        startSendHttp(c, u, HTTPMethod.GET);
    }

    public void startSendHttp(T c, String u, String h) {
        startSendHttp(c, u, h, new HashMap<String, String>());
    }

    public void startSendHttp(T c, String u, String h, Map<String, String> ts) {
        startSendHttp(c, u, h, ts, new HashMap<String, String>());
    }

    public void startSendHttp(T c, String u, String h, Map<String, String> ts, Map<String, String> fs) {
        handleFinalRes(c,u,h,ts,fs);
    }

    public void handleFinalRes(final T c, String u, String h, Map<String, String> ts, Map<String, String> fs) {
        if (backgroundHandler == null)
            backgroundHandler = new Handler(BackgroundHttpUtils.getInstance().getBackThread().getLooper());
        sendRequestObservable(c,u,h,ts,fs)
                .subscribeOn(HandlerScheduler.from(backgroundHandler))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.v("ZMAsyncHttp", "onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("ZMAsyncHttp", "onError()", e);
                    }

                    @Override
                    public void onNext(String str) {
                        String[] backArr = str.split("\\|");
                        String urlId = "";
                        String json = "{}";
                        Log.v("ZMAsyncHttp", str);
                        if (backArr.length >= 2) {
                            urlId = backArr[0];
                            json = backArr[1];
                        }
                        ((ZMRequestCallback) c).handleJsonString(urlId, json);
                    }
                });
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

    public Observable<String> sendRequestObservable(final T c,
                                                    final String u,
                                                    final String h,
                                                    final Map<String, String> ts,
                                                    final Map<String, String> fs) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                String res = "";
                try {
                    res = sendHttp(c,u,h,ts,fs);
                } catch (Exception e) {
                    throw OnErrorThrowable.from(e);
                }
                return Observable.just(res);
            }
        });
    }

    private String sendHttp(T c, String u, String h, Map<String, String> ts, Map<String, String> fs) {
        String res;
        if (h.equals(HTTPMethod.GET))
            res = HttpRequest.get(u);
        else
            res = HttpRequest.sendMultiRequestBody(u, h, ts, fs);
        return h + u + "|" + res;
    }

    public static class BackgroundThread extends HandlerThread {
        public BackgroundThread() {
            super("ZMAsyncHttp-BackgroundThread", THREAD_PRIORITY_BACKGROUND);
        }
    }

}
