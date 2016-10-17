package com.example.cxjminfodemo;

import android.app.Activity;

import com.example.cxjminfodemo.InfoActivity.InfoMainActivity;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.os.Build;

public class MainActivity extends Activity {

	@Bind(R.id.image_cbdj)
	ImageView image_cbdj;

	// image_sjsc

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(MainActivity.this);
	}

	@OnClick(R.id.image_cbdj)
	public void toLoginActivity() {
		Intent intent = new Intent(this, InfoMainActivity.class);
		startActivity(intent);
	}

}
