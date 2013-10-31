package com.example.mapdemo;

import java.util.HashMap;  
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
// import android.view.View;
// import android.widget.Button;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

public class RequestAndParser extends Activity {
	// activityにvolley通信用のqueueを作成
	private RequestQueue requestQueue;
	public void onCreate(Bundle savedInstaceState) {
		// TODO 自動生成されたコンストラクター・スタブ
		super.onCreate(savedInstaceState);
		// setContentView(R.layout.main);
		requestQueue = Volley.newRequestQueue(getApplicationContext());
		Log.d("onCreate", "start_test");
		test();
		Log.d("onCreate", "finish_test");
	    // Button btn = (Button)findViewById(R.id.test_button);
	    /*View.OnClickListener clicked = new View.OnClickListener() {

	    	public void onClick(View v) {
	    		// TODO 自動生成されたメソッド・スタブ
	    		test();
	    		Log.d("Button","onClick");
	    	}
	    };*/
	    // btn.setOnClickListener(clicked);
	}


	public void test(){
		int method = Method.POST;
		String url = "/read_user";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("user_id", "19");
		
		/*
		 public GsonRequest(int method, String url, Class<T> clazz,
		            Map<String, String> params, Listener<T> listener,
		            ErrorListener errorListener) {
		            */
		GsonRequest<JsonObject> req = new GsonRequest<JsonObject>(method,
				url, JsonObject.class, params,
				new Listener<JsonObject>() {
			@Override
			public void onResponse(JsonObject user) {
				// success
				String result = user.toString(); //user.get("result").toString();
				
				Log.v("success:", result);
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// error
				Log.v("error:", error.toString());
			}
		});
		requestQueue.add(req);
	}
}


