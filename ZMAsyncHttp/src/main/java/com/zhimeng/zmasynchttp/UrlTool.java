package com.zhimeng.zmasynchttp;

/**
 * Created by zhimengsun on 3/16/16.
 */
public class UrlTool {

    private String urlId;

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public UrlTool(String _urlId){
        setUrlId(_urlId);
    }
    public boolean requestFrom(String httpMethodName, String url){
        return getUrlId().equals(httpMethodName + url);
    }
}
