package kom.botch.rasaki;

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
import com.example.mapdemo.R;
import com.google.gson.JsonObject;

public class RequestAndParser extends Fragment {
	// activityにvolley通信用のqueueを作成
	private RequestQueue requestQueue;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main, container, false);
		// onCreateでrequestQueueを定義する
		requestQueue = Volley.newRequestQueue(getActivity()
				.getApplicationContext());
		// こっからしたはテスト用ボタンの設定
		Button btn = (Button) v.findViewById(R.id.test_button);
		View.OnClickListener clicked = new View.OnClickListener() {

			public void onClick(View v) {
				test();
				Log.d("Button", "onClick");
			}
		};
		btn.setOnClickListener(clicked);
		return v;
	}

	public void test() {
		// methodとurlとparamsを設定する
		int method = Method.POST;
		String url = "/read_user";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("user_id", "19");

		/*
		 * public GsonRequest(int method, String url, Class<T> clazz,
		 * Map<String, String> params, Listener<T> listener, ErrorListener
		 * errorListener) {
		 */
		// reqにmethodとurlとparamsを渡す。返り値の型は変えられるが、めんどくさいのでこれで
		// 引数の順番に注意
		GsonRequest<JsonObject> req = new GsonRequest<JsonObject>(method, url,
				JsonObject.class, params, new Listener<JsonObject>() {
					@Override
					// 通信成功時のコールバック関数
					public void onResponse(JsonObject user) {
						// success
						String result = user.toString(); // user.get("result").toString();

						Log.v("success:", result);
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
