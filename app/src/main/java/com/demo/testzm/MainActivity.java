package com.demo.testzm;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.zhimeng.zmasynchttp.BackgroundHttpUtils;
import com.zhimeng.zmasynchttp.HTTPMethod;
import com.zhimeng.zmasynchttp.ZMRequestCallback;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity implements ZMRequestCallback {

    private String url1 = "http://www.sunzhimeng.cn/sync_data";
    private String url2 = "http://www.sunzhimeng.cn/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BackgroundHttpUtils backgroundHttpUtils = BackgroundHttpUtils.getInstance();
        backgroundHttpUtils.startSendHttp(this, url1);

        Map<String,String> params = new HashMap<>();
//        params.put("name", "name");
        backgroundHttpUtils.startSendHttp(this, url2, HTTPMethod.POST, params);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
