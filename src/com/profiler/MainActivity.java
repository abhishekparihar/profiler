package com.profiler;



import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.profiler.db.ProfileDbAdapter;
import com.profiler.models.ProfileModel;

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
		
		getProfileList();
	}

	private void getProfileList() {
		ProfileDbAdapter mProfileDbAdapter = new ProfileDbAdapter(this);
		List<ProfileModel> mList=mProfileDbAdapter.getProfileList();
		
		
	}

	public void onChangeClicked(View v){
		File sdcard = Environment.getExternalStorageDirectory();  
		try {
			is = new FileInputStream(new File(sdcard,"wall.jpg"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bis = new BufferedInputStream(is);
		Bitmap bitmap = BitmapFactory.decodeStream(bis);
		Bitmap useThisBitmap = Bitmap.createBitmap(bitmap);
		//bitmap.recycle();
		//if(imagePath!=null){
		System.out.println("Hi I am try to open Bit map");
		wallpaperManager = WallpaperManager.getInstance(this);
		wallpaperDrawable = wallpaperManager.getDrawable();
		try {
			wallpaperManager.setBitmap(useThisBitmap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
