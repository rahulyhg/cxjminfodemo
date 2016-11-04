package com.example.cxjminfodemo;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import butterknife.ButterKnife;
import android.os.Build;

public class MainActivity extends Activity {

	// image_sjsc

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(MainActivity.this);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// Í¸Ã÷×´Ì¬À¸
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// Í¸Ã÷µ¼º½À¸
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}

		handler.sendEmptyMessageDelayed(0, 5000);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			toLoginActivity();
			super.handleMessage(msg);
		}
	};

	public void toLoginActivity() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	/*
	 * InputStream inputStream =
	 * getResources().openRawResource(R.raw.countrycode); Map<String, String>
	 * oldMap = new TextToMap().TextToMap(inputStream);
	 * 
	 * InputStream inputStream1 = getResources().openRawResource(R.raw.nation);
	 * Map<String, String> oldMap1 = new TextToMap().TextToMap(inputStream1);
	 */
}

// @OnClick(R.id.editText1)
// public void toLoginActivity() {
// Intent intent = new Intent(this, LoginActivity.class);
// startActivity(intent);
// finish();
// }
