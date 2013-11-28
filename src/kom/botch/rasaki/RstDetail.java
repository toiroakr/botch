package kom.botch.rasaki;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.example.mapdemo.R;
import com.example.mapdemo.R.id;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class RstDetail extends Activity {
	private RequestQueue requestQueue;
	private String rst_id;
	private HashMap<String, String> params = new HashMap<String, String>();
	private int method;
	private String url;	
	// getterとsetterは一番下
	private static final Object TAG_REQUEST_QUEUE = new Object();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rst_detail);
        
        // Intentでrst_idを受け取る        
        this.rst_id = getIntent().getExtras().getString("rst_id");
		requestQueue = Volley.newRequestQueue(this);
		// methodとurlとparamsを設定する		
		// this.params.put("rst_id", this.getRst_id());  // sample用
		this.setRst_id(rst_id);
		this.setUrl("/read_rst");
		this.setMethod(Method.POST);
		this.setParams();
		this.readRst();
		this.readComments();
		//this.requestQueue.start();
    }
    
    private void readRst() {
		// マッピング用のRestaurantDetailを作成
		GsonRequest<RestaurantDetail> req = new GsonRequest<RestaurantDetail>(method, "/read_rst",
				RestaurantDetail.class, params, new Listener<RestaurantDetail>() {
					@Override
					// 通信成功時のコールバック関数
					public void onResponse(RestaurantDetail rst) {
						// success
						Log.v("success:", rst.toString());
						// TextViewへの埋め込みはこの関数で行う
						setRestaurantDetailToTextView(rst);
						Log.v("success:", "DONE!");
					    // Toast.makeText(getApplicationContext(), rst.toString(), Toast.LENGTH_LONG).show();						
					}
				}, new ErrorListener() {
					@Override
					// 通信失敗時のコールバック関数
					public void onErrorResponse(VolleyError error) {
						// error
						Log.v("error:", error.toString() + "：再読み込みしてください");
					    Toast.makeText(getApplicationContext(), "onErrorResponse", Toast.LENGTH_LONG).show();
					}
				});
		// requestQueueに上で定義したreqをaddすることで、非同期通信が行われる
		// Queueなので、入れた順番に通信される
		// 通信が終われば、それぞれのreqで定義したコールバック関数が呼ばれる
		req.setTag(TAG_REQUEST_QUEUE);
		requestQueue.add(req);    	
    }
    private void readComments() {
		// マッピング用のRestaurantDetailを作成
		GsonRequest<JsonObject> req = new GsonRequest<JsonObject>(method, "/read_comments",
				JsonObject.class, params, new Listener<JsonObject>() {
					@Override
					// 通信成功時のコールバック関数
					public void onResponse(JsonObject comments) {
						// success						
						JsonArray results = (JsonArray) comments.get("result");
						String comment;
						float difficulty;
						String user_name;
						JsonObject json_comments;
						List<String> names = new ArrayList<String>();
						// ScrollView comments = (ScrollView) findViewById(id.comments);  
						
						LinearLayout comment_component = (LinearLayout) findViewById(id.comment_component);

						for (int i = 0, length = results.size(); i < length; i++) {
							json_comments = results.get(i).getAsJsonObject();							
							comment = json_comments.get("comment").toString().replaceAll("\"", "");;							
							user_name = json_comments.get("user_name").toString().replaceAll("\"", "");
							difficulty = (float) Double.parseDouble(json_comments.get("difficulty").toString());
							if (names.indexOf(user_name) >= 0) {
								continue;
							}
							Log.v("success !!!:", json_comments.toString());														
							// names.add(user_name);			            
							View view = getLayoutInflater().inflate(R.layout.user_comment, null);		
							comment_component.addView(view);	
							
							TextView name = (TextView) view.findViewById(R.id.user_name);
							RatingBar dif = (RatingBar) view.findViewById(R.id.user_difficulty);
							TextView com = (TextView) view.findViewById(R.id.user_comment);
							
							name.setText(user_name);
							dif.setRating(difficulty);
							com.setText(comment);

							// restaurants.put(rst_id, restaurant);
						}
						// Log.v("success:", comments.toString());						
						Log.v("success:", "DONE!");
					    // Toast.makeText(getApplicationContext(), comments.toString(), Toast.LENGTH_LONG).show();						
					}
				}, new ErrorListener() {
					@Override
					// 通信失敗時のコールバック関数
					public void onErrorResponse(VolleyError error) {
						// error
						Log.v("error:", error.toString() + "：再読み込みしてください");
					    Toast.makeText(getApplicationContext(), "onErrorResponse", Toast.LENGTH_LONG).show();
					}
				});
		// requestQueueに上で定義したreqをaddすることで、非同期通信が行われる
		// Queueなので、入れた順番に通信される
		// 通信が終われば、それぞれのreqで定義したコールバック関数が呼ばれる
		req.setTag(TAG_REQUEST_QUEUE);
		requestQueue.add(req);    	
    }
    @Override
    public void onStart(){
     super.onStart();
     // Log.v("length:", requestQueue.toString());
     //HTTPリクエストを行う
     // 2回通信されてしまう
     // this.startRequest();
    }
     
    @Override
    public void onStop(){
     super.onStop();
     requestQueue.cancelAll(TAG_REQUEST_QUEUE);
    }

	@SuppressLint("SetJavaScriptEnabled")
	private void setRestaurantDetailToTextView(final RestaurantDetail rst) {
		TextView name = (TextView) findViewById(id.rst_detail_name);
		name.setText(rst.getRestaurantName());
		
		// 食べログのurlをWebView内で開くためのもの
		// TextView url = (TextView) findViewById(id.rst_detail_data_tabelog);
		Button url = (Button) findViewById(id.rst_detail_data_tabelog);

		// urlをclickableにする
		MovementMethod mMethod = LinkMovementMethod.getInstance();
		url.setMovementMethod(mMethod);

		// tabelogmobileurlをhtmlを使ってリンクテキストに埋め込む
		CharSequence tabelogLink = Html.fromHtml("<a href=\"" + rst.getTabelogMobileUrl() + "\">食べログを見る</a>");
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
		Linkify.addLinks(tel, Linkify.PHONE_NUMBERS);
		
		TextView bh = (TextView) findViewById(id.rst_detail_data_businesshour);
		bh.setText(rst.getBusinesshours());
		
		TextView holiday = (TextView) findViewById(id.rst_detail_data_holiday);
		holiday.setText(rst.getHoliday());		
		
		RatingBar difficulty = (RatingBar) findViewById(id.detail_difficulty);
		difficulty.setRating((float) rst.getDifficulty());
	}
	// setParamsに注意
	public String getRst_id() {
		return rst_id;
	}
	public void setRst_id(String rst_id) {
		this.rst_id = rst_id;
	}
	public HashMap<String, String> getParams() {
		return params;
	}
	public void setParams() {
		this.params.put("rst_id", this.rst_id);
	}
	public int getMethod() {
		return method;
	}
	public void setMethod(int method) {
		this.method = method;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
    
}
