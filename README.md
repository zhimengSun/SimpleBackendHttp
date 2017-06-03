# ZMAsyncHttp (0.0.61)
It is an easy way to handle simple json (response string also) from http request which based on RxAndroid and OkHttp.
You can use handleJsonString callback to handle the response from backend without considering which status code backed or 
failed, This lib always think the response is ok even we get a 4xx code, Mostly the most thing we are concerned is the response string
instead of HTTP status code, So I just simplify the style of requesting and  handling data in Andriod, 
and I hope it's a great tool to make your activities fragments adapters and many other stuffs in android much cleaner. 


#Usage

##setup

* build.gradle  

```java
dependencies {
    compile 'com.squareup.okhttp3:okhttp:3.0.1'
    compile 'io.reactivex:rxandroid:1.1.0'
    compile 'io.reactivex:rxjava:1.1.0'
    compile 'com.github.zhimengsun:ZMAsyncHttp:0.0.61'
}
``` 

> You can also download the ZMAsyncHttp-0.0.61.jar file directly, copy it into you app libs directory and set it up in your build.gradle


* INTERNET permission in your AndroidManifest.xml

```java
    <uses-permission android:name="android.permission.INTERNET" />
```

## customize

```java
    HttpRequest.REQUEST_TIME_OUT = 'YOUR_VAL' // default 30
    HttpRequest.WRITE_TIME_OUT = 'YOUR_VAL' // default 30
    HttpRequest.READ_TIME_OUT = 'YOUR_VAL' // default 30
    HttpRequest.USER_AGENT = 'YOUR_VAL' // default 'Android OkHttp With ZMAsyncHttp #{version}'
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
