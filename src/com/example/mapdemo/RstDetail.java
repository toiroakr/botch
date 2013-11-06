package com.example.mapdemo;

import java.util.HashMap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.example.mapdemo.R.id;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class RstDetail extends Activity {
	private RequestQueue requestQueue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rst_detail);
        
        // Intentでrst_idを受け取る        
		requestQueue = Volley.newRequestQueue(this);
		// methodとurlとparamsを設定する
		int method = Method.POST;
		String url = "/read_rst";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("rst_id", "26001581");  // sample用

		// マッピング用のRestaurantDetailを作成
		GsonRequest<RestaurantDetail> req = new GsonRequest<RestaurantDetail>(method, url,
				RestaurantDetail.class, params, new Listener<RestaurantDetail>() {
					@Override
					// 通信成功時のコールバック関数
					public void onResponse(RestaurantDetail rst) {
						// success
						Log.e("success:", rst.toString());
						// TextViewへの埋め込みはこの関数で行う
						setRestaurantDetailToTextView(rst);
						Log.e("success:", "DONE!");
						
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
    
    
	@SuppressLint("SetJavaScriptEnabled")
	private void setRestaurantDetailToTextView(final RestaurantDetail rst) {
		TextView name = (TextView) findViewById(id.rst_detail_name);
		name.setText(rst.getRestaurantName());
		
		// 食べログのurlをWebView内で開くためのもの
		TextView url = (TextView) findViewById(id.rst_detail_data_tabelog);

		// urlをclickableにする
		MovementMethod mMethod = LinkMovementMethod.getInstance();
		url.setMovementMethod(mMethod);

		// tabelogmobileurlをhtmlを使ってリンクテキストに埋め込む
		CharSequence tabelogLink = Html.fromHtml("<a href=\"" + rst.getTabelogMobileUrl() + "\">食べログ</a>");
		url.setText(tabelogLink);
		
		// タップされたときの挙動を変更。デフォルトだとブラウザ起動
		// Intentを使ってWebViewFromTextViewに遷移し、そちらで開く
		url.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(RstDetail.this, WebViewFromTextView.class);
				i.setAction(Intent.ACTION_VIEW);
				i.setData(Uri.parse(rst.getTabelogMobileUrl()));
				startActivity(i);				
			}
		});
		// これだとブラウザが開いちゃう
		// webView.loadUrl(rst.getTabelogMobileUrl());		
		
		// 以下、埋め込み作業が続く
		TextView category = (TextView) findViewById(id.rst_detail_data_category);
		category.setText(rst.getCategory());
		
		TextView dinner = (TextView) findViewById(id.rst_detail_data_dinner);
		dinner.setText(rst.getDinnerPrice());
		
		TextView lunch = (TextView) findViewById(id.rst_detail_data_lunch);
		lunch.setText(rst.getLunchprice());
		
		TextView situation = (TextView) findViewById(id.rst_detail_data_situation);
		situation.setText(rst.getSituation());
		
		TextView address = (TextView) findViewById(id.rst_detail_data_address);
		address.setText(rst.getAddress());
		
		TextView station = (TextView) findViewById(id.rst_detail_data_station);
		station.setText(rst.getStation());
		
		TextView tel = (TextView) findViewById(id.rst_detail_data_tel);
		tel.setText(rst.getTel());
		
		TextView bh = (TextView) findViewById(id.rst_detail_data_businesshour);
		bh.setText(rst.getBusinesshours());
		
		TextView holiday = (TextView) findViewById(id.rst_detail_data_holiday);
		holiday.setText(rst.getHoliday());		
	}
    
}
