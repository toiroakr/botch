package com.example.mapdemo;

import java.util.HashMap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class RstDetail extends Activity {
	private RequestQueue requestQueue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rst_detail);
		requestQueue = Volley.newRequestQueue(this);
		// methodとurlとparamsを設定する
		int method = Method.POST;
		String url = "/read_rst";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("rst_id", "26001581");  // example
		
		// reqにmethodとurlとparamsを渡す。返り値の型は変えられるが、めんどくさいのでこれで
		// 引数の順番に注意
		/*
		GsonRequest<JsonObject> req = new GsonRequest<JsonObject>(method, url,
				JsonObject.class, params, new Listener<JsonObject>() {
					@Override
					// 通信成功時のコールバック関数
					public void onResponse(JsonObject rst) {
						// success
						// JsonElement result = rst; // user.get("result").toString();
						Log.e("success:", rst.toString());
					}
				}, new ErrorListener() {
					@Override
					// 通信失敗時のコールバック関数
					public void onErrorResponse(VolleyError error) {
						// error
						Log.v("error:", error.toString());
					}
				});*/
		
		// マッピング用のRestaurantDetailを作成
		GsonRequest<RestaurantDetail> req = new GsonRequest<RestaurantDetail>(method, url,
				RestaurantDetail.class, params, new Listener<RestaurantDetail>() {
					@Override
					// 通信成功時のコールバック関数
					public void onResponse(RestaurantDetail rst) {
						// success
						// JsonElement result = rst; // user.get("result").toString();
						Log.e("success:", rst.toString());
					}
				}, new ErrorListener() {
					@Override
					// 通信失敗時のコールバック関数
					public void onErrorResponse(VolleyError error) {
						// error
						Log.v("error:", error.toString());
					}
				});
		// requestQueueに上で定義したreqをaddすることで、非同期通信が行われる
		// Queueなので、入れた順番に通信される
		// 通信が終われば、それぞれのreqで定義したコールバック関数が呼ばれる
		requestQueue.add(req);
    }
	
    
}
