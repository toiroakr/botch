package com.example.mapdemo;

import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

public class RequestAndParser extends Fragment {
	// activity��volley�ʐM�p��queue���쐬
	private RequestQueue requestQueue;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
		View v = inflater.inflate(R.layout.main, container, false);
		requestQueue = Volley.newRequestQueue(getActivity()
				.getApplicationContext());
		Log.d("onCreate", "start_test");
		test();
		Log.d("onCreate", "finish_test");
		Button btn = (Button) v.findViewById(R.id.test_button);
		View.OnClickListener clicked = new View.OnClickListener() {

			public void onClick(View v) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				test();
				Log.d("Button", "onClick");
			}
		};
		btn.setOnClickListener(clicked);
		return v;
	}

	public void test() {
		int method = Method.POST;
		String url = "/read_user";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("user_id", "19");

		/*
		 * public GsonRequest(int method, String url, Class<T> clazz,
		 * Map<String, String> params, Listener<T> listener, ErrorListener
		 * errorListener) {
		 */
		GsonRequest<JsonObject> req = new GsonRequest<JsonObject>(method, url,
				JsonObject.class, params, new Listener<JsonObject>() {
					@Override
					public void onResponse(JsonObject user) {
						// success
						String result = user.toString(); // user.get("result").toString();

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
