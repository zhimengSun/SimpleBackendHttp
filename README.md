# ZMAsyncHttp
It is an easy way to handle simple json (response string also) from http request which based on RxAndroid and OkHttp.

#Usage

##setup

* build.gradle  

```java
	
dependencies {
    compile 'com.squareup.okhttp3:okhttp:3.0.1'
    compile 'io.reactivex:rxandroid:1.1.0'
    compile 'io.reactivex:rxjava:1.1.0'
}
``` 

* INTERNET permission in your AndroidManifest.xml

```java
    <uses-permission android:name="android.permission.INTERNET" />

```

## Enjoy
 
```java
public class MyActivity extends Activity implements ZMRequestCallback {
    
    private String url1 = "http://www.sunzhimeng.cn/sync_data";
    private String url2 = "http://www.sunzhimeng.cn/login";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	
        BackgroundHttpUtils backgroundHttpUtils = BackgroundHttpUtils.getInstance();
        backgroundHttpUtils.startSendHttp(this, url1);
	
        Map<String,String> params = new HashMap<>();
        params.put("name", "name");
        backgroundHttpUtils.startSendHttp(this, url2, HTTPMethod.POST, params);
    }
    
    @Override
    public int handleJsonString(String urlId, String jsonString) {
        if (urlId.equals(HTTPMethod.GET + url1)){
            Log.e("ZMAsyncHttp", urlId + " from get url1 ---> " + jsonString);
        } else if (urlId.equals(HTTPMethod.POST + url2)) {
            Log.e("ZMAsyncHttp", urlId + " from post url2 ---> " + jsonString);
        }
        return 0;
    }
}
```
  
> both Fragment and Adapter are the same way as Activity 