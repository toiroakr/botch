package kom.botch.rasaki;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;


public class SplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		/*
		LinearLayout splashView = (LinearLayout) findViewById(R.layout.splash);
		ImageView splashImage = new ImageView(this);
		splashImage.setImageResource(R.id.splash_image);
		splashView.addView(splashImage);
		 */
		Handler hdl = new Handler();
		hdl.postDelayed(new splashHandler(), 750);
	}

	class splashHandler implements Runnable {
		@Override
		public void run() {
			Intent i = new Intent(getApplication(),
					MainActivity.class);
			SplashActivity.this.startActivity(i);
			// SplashActivity.this
			// .overridePendingTransition(R.drawable.splashing_fade_in,
			// R.drawable.splashing_fade_out);
			SplashActivity.this.finish();
		}
	}
}
