package kom.botch.rasaki;

import java.util.HashMap;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.JsonObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Setting extends Activity {
	private RequestQueue requestQueue;
	private static final Object TAG_REQUEST_QUEUE = new Object();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		requestQueue = Volley.newRequestQueue(this);
		UserSettings preference = new UserSettings(this, "botch_user_setting");
		final String loaded_user_name = preference.ReadKeyValue("user_name");
		TextView userNameTV = (TextView) findViewById(R.id.setting_username);
		userNameTV.setText(loaded_user_name);
		TextView mapLisence = (TextView) findViewById(R.id.setting_lisence);
		mapLisence.setText(GooglePlayServicesUtil.getOpenSourceSoftwareLicenseInfo(getApplicationContext()));
		Button changeButton = (Button) findViewById(R.id.settingChange);
		changeButton.setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						changeUserName(loaded_user_name);
					}
				});
	}
	
	private void changeUserName(String original_user_name) {
        final UserSettings preference = new UserSettings(this, "botch_user_setting");
        final EditText editView = new EditText(this);
        editView.setText(original_user_name);
        final String user_id = preference.ReadKeyValue("user_id");
		new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("名前を入力してください")
				// setViewにてビューを設定します。
				.setView(editView)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 入力した文字をトースト出力する
								String user_name = editView.getText()
										.toString();
								if (user_name.length() < 1) {
									Toast.makeText(getApplicationContext(), "名前を入力してください！", Toast.LENGTH_SHORT).show();
									return;
								}
								sendUserName(user_id, user_name);
								preference.WriteKeyValue("user_name", user_name);
								TextView userNameTV = (TextView) findViewById(R.id.setting_username);
								userNameTV.setText(user_name);
								Toast.makeText(getApplicationContext(),
										"名前を " + user_name + "に変更しました",
										Toast.LENGTH_SHORT).show();
							}
						})
				.setNegativeButton("キャンセル",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).show();
	}
	private void sendUserName(String user_id, String user_name) {
		// checkUserSettingで呼び出す。user_nameでcreate_userしてuser_idを与える
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("new_name", user_name);
		params.put("new_home", "");
		params.put("user_id", user_id);
		// Log.v("user_id", user_id);
		String url = "/update_user";
		int method = Method.PUT;
		GsonRequest<JsonObject> req = new GsonRequest<JsonObject>(method, url,
				JsonObject.class, params, new Listener<JsonObject>() {
					@Override
					// 通信成功時のコールバック関数
					public void onResponse(JsonObject response) {
						// success
						// String _user_id = response.get("update").toString();
						// Log.v("success sendUserName:", _user_id);
						// preference.WriteKeyValue("user_id", _user_id);
						// user_id = _user_id;
					}
				}, new ErrorListener() {
					@Override
					// 通信失敗時のコールバック関数
					public void onErrorResponse(VolleyError error) {
						// error
						// Log.v("error:", error.toString() + "：通信に失敗しました");
						Toast.makeText(getApplicationContext(), "通信に失敗しました",
								Toast.LENGTH_SHORT).show();
					}
				});
		// requestQueueに上で定義したreqをaddすることで、非同期通信が行われる
		// Queueなので、入れた順番に通信される
		// 通信が終われば、それぞれのreqで定義したコールバック関数が呼ばれる
		req.setTag(TAG_REQUEST_QUEUE);
		requestQueue.add(req);
	}
}
