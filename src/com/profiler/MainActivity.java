package com.profiler;



import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.profiler.db.ProfileDbAdapter;
import com.profiler.models.ProfileModel;

public class MainActivity extends Activity {

	final static String TAG = "MainActivity";
	Button buttonAdd;

	private FileInputStream is;
	private BufferedInputStream bis;
	private WallpaperManager wallpaperManager;
	private Drawable wallpaperDrawable;
	protected IntentFilter ifilter;
	private NfcAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		adapter = NfcAdapter.getDefaultAdapter(this);

		ifilter = new IntentFilter();
		ifilter.addAction("android.nfc.action.NDEF_DISCOVERED");
		ifilter.addCategory("android.intent.category.LAUNCHER");

		// Intent nfcReceiver = new Intent(MainActivity.this,
		// NfcReceiver.class);

		/*
		 * IntentFilter filter = new IntentFilter();
		 * filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
		 * filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
		 * filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
		 * 
		 * NfcReceiver receiver = new NfcReceiver(); registerReceiver(receiver,
		 * filter);
		 */

	}

	@Override
	protected void onResume() {
		registerReceiver(receiver, ifilter);

		super.onResume();
		buttonAdd = (Button) findViewById(R.id.buttonAdd);

		buttonAdd.setOnClickListener(new OnClickListener() {

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

	public void onChangeClicked(View v) {
		File sdcard = Environment.getExternalStorageDirectory();
		try {
			is = new FileInputStream(new File(sdcard, "wall.jpg"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bis = new BufferedInputStream(is);
		Bitmap bitmap = BitmapFactory.decodeStream(bis);
		Bitmap useThisBitmap = Bitmap.createBitmap(bitmap);
		// bitmap.recycle();
		// if(imagePath!=null){
		System.out.println("Hi I am try to open Bit map");
		wallpaperManager = WallpaperManager.getInstance(this);
		wallpaperDrawable = wallpaperManager.getDrawable();
		try {
			wallpaperManager.setBitmap(useThisBitmap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// }
	}


	public void onCreateProfileClicked(View v) {
		Log.i(TAG, "onCreateProfileClicked");
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v("", "************* Received  ");
			if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
				Parcelable[] messages = intent
						.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
				NdefMessage[] ndefmessages;
				if (messages != null) {
					ndefmessages = new NdefMessage[messages.length];

					for (int i = 0; i < messages.length; i++) {
						ndefmessages[i] = (NdefMessage) messages[i];
					}

				}

			}

		}
	};
}
