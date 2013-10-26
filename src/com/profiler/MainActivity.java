package com.profiler;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	final static String TAG = "MainActivity";
	Button createProfileButton;

	private FileInputStream is;
	private BufferedInputStream bis;
	private WallpaperManager wallpaperManager;
	private Drawable wallpaperDrawable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		createProfileButton = (Button) findViewById(R.id.createProfile);

		createProfileButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						CreateProfileActivity.class);
				startActivity(intent);
			}
		});
	}

}
