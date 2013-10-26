package com.profiler;



import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.profiler.db.ProfileDbAdapter;
import com.profiler.models.ProfileModel;

public class MainActivity extends Activity {
	
	final static String TAG = "MainActivity";

	private FileInputStream is;
	private BufferedInputStream bis;
	private WallpaperManager wallpaperManager;
	private Drawable wallpaperDrawable;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
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
		//	}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    

	public void onCreateProfileClicked(View v) {
		Log.i(TAG, "onCreateProfileClicked");
	}
    
}
