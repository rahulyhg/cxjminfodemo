package com.neuqsoft.cxjmcj;

import com.neuqsoft.cxjmcj.R;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import butterknife.ButterKnife;
import android.os.Build;

public class WelcomeActivity extends Activity {

	// image_sjsc

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		handler.sendEmptyMessageDelayed(0, 3000);
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

}
