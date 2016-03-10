# SimpleBackendHttp
It is an easy way to handle simple json (response string also) from http request which based on RxAndroid and OkHttp.

#Usage

#### For example 
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
