package com.profiler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.profiler.db.ProfileDbAdapter;
import com.profiler.models.ProfileModel;

public class MainActivity extends Activity {

	final static String TAG = "MainActivity";
	Button buttonAdd;
	private NfcAdapter mNfcAdapter;
	private IntentFilter[] mReadTagFilters;
	private PendingIntent mNfcPendingIntent;
	private Context context;
	private FileInputStream is;
	private BufferedInputStream bis;
	private WallpaperManager wallpaperManager;
	private Drawable wallpaperDrawable;
	protected IntentFilter ifilter;
	private NfcAdapter adapter;
	private ListView listViewProfiles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		listViewProfiles = (ListView) findViewById(R.id.listViewProfiles);
	}

	private void showList(List<ProfileModel> list) {
		ListViewAdapter mAdapter = new ListViewAdapter(this, list);
		listViewProfiles.setAdapter(mAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		buttonAdd = (Button) findViewById(R.id.buttonAdd);
		buttonAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						CreateProfileActivity.class);
				startActivity(intent);
			}
		});
		showList(getProfileList());
		setNFCAdapter();
		onClickReadTag();
	}

	public void changeWallpaper(String path) {
		FileInputStream is;
		BufferedInputStream bis;
		WallpaperManager wallpaperManager;
		Drawable wallpaperDrawable;
		File sdcard = Environment.getExternalStorageDirectory();
		try {
			is = new FileInputStream(new File(path));
			bis = new BufferedInputStream(is);
			Bitmap bitmap = BitmapFactory.decodeStream(bis);
			Bitmap useThisBitmap = Bitmap.createBitmap(bitmap);
			wallpaperManager = WallpaperManager.getInstance(MainActivity.this);
			wallpaperDrawable = wallpaperManager.getDrawable();
			wallpaperManager.setBitmap(useThisBitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setRingTone(String path) throws IOException {
		// File audioFile = new File("/mnt/sdcard/Music/Maahi.mp3");
		File audioFile = new File(path);
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, audioFile.getAbsolutePath());
		values.put(MediaStore.MediaColumns.TITLE, audioFile.getName());
		values.put(MediaStore.MediaColumns.SIZE, audioFile.getTotalSpace());
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
		values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
		values.put(MediaStore.Audio.Media.IS_ALARM, false);
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);

		// Insert it into the database
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(audioFile
				.getAbsolutePath());
		Uri newUri = MainActivity.this.getContentResolver().insert(uri, values);

		RingtoneManager.setActualDefaultRingtoneUri(MainActivity.this,
				RingtoneManager.TYPE_RINGTONE, newUri);
	}

	private String removeExtras(String path) {
		path = path.replace("/file:", "");
		path = path.replace("/content:", "");
		path = path.replace("%20", " ");
		path = path.replace("/storage/emulated/0", "/mnt/sdcard");

		return path;
	}

	private void setRingtoneVolume(int volume) {
		if (volume == 0) {
			AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
			audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		} else if (volume >= 1) {
			volume = volume - 1;

			AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
			int streamMaxVolume = audioManager
					.getStreamMaxVolume(AudioManager.STREAM_RING);
			int streamMinVolume = audioManager
					.getStreamVolume(AudioManager.STREAM_RING);
			audioManager.setStreamVolume(AudioManager.STREAM_RING, volume,
					AudioManager.FLAG_PLAY_SOUND);
		}
	}

	class SetProfileTask extends AsyncTask<ProfileModel, Void, Void> {
		ProgressDialog progressDialog;
		ProfileModel profile;

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(MainActivity.this, "",
					"Activating profile");
		}

		@Override
		protected Void doInBackground(ProfileModel... params) {
			profile = params[0];
			changeWallpaper(profile.getWallpaper());
			try {
				setRingTone(profile.getRingtone());
			} catch (IOException e) {
				e.printStackTrace();
			}
			setRingtoneVolume(profile.getVolume());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
		}

	}

	private List<ProfileModel> getProfileList() {
		ProfileDbAdapter mProfileDbAdapter = new ProfileDbAdapter(this);
		List<ProfileModel> mList = mProfileDbAdapter.getProfileList();
		Log.i(TAG, "test");
		return mList;

	}

	public void onChangeClicked(View v) {
		File sdcard = Environment.getExternalStorageDirectory();
		try {
			is = new FileInputStream(new File(sdcard, "wall.jpg"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bis = new BufferedInputStream(is);
		Bitmap bitmap = BitmapFactory.decodeStream(bis);
		Bitmap useThisBitmap = Bitmap.createBitmap(bitmap);
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

	public void onCreateProfileClicked(View v) {
		Log.i(TAG, "onCreateProfileClicked");
	}

	private void setNFCAdapter() {
		context = getApplicationContext();
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);

		IntentFilter ndefDetected = new IntentFilter(
				NfcAdapter.ACTION_NDEF_DISCOVERED);
		ndefDetected.addDataScheme("http");

		IntentFilter techDetected = new IntentFilter(
				NfcAdapter.ACTION_TECH_DISCOVERED);

		mReadTagFilters = new IntentFilter[] { ndefDetected, techDetected };

	}

	public void onClickReadTag() {
		if (mNfcAdapter != null) {
			if (!mNfcAdapter.isEnabled()) {
				new AlertDialog.Builder(this)
						.setTitle("NFC Dialog")
						.setMessage("Please switch on NFC")
						.setPositiveButton("Settings",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface arg0,
											int arg1) {
										Intent setnfc = new Intent(
												Settings.ACTION_WIRELESS_SETTINGS);
										startActivity(setnfc);
									}
								})
						.setOnCancelListener(
								new DialogInterface.OnCancelListener() {
									public void onCancel(DialogInterface dialog) {
										dialog.dismiss(); // exit application if
															// user cancels
									}
								}).create().show();
			}
			mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent,
					mReadTagFilters, null);
		} else {
			Toast.makeText(context,
					"Sorry, NFC adapter not available on your device.",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			NdefMessage[] messages = null;
			Parcelable[] rawMsgs = intent
					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				messages = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					messages[i] = (NdefMessage) rawMsgs[i];
				}
			}
			if (messages[0] != null) {
				String result = "";
				byte[] payload = messages[0].getRecords()[0].getPayload();
				// this assumes that we get back am SOH followed by host/code
				for (int b = 1; b < payload.length; b++) { // skip SOH
					result += (char) payload[b];
				}
				setCurrentProfile(result);
				// Toast.makeText(getApplicationContext(),
				// "Tag Contains >>>>>>>> \n " + result,
				// Toast.LENGTH_LONG).show();
			}
		}
	}

	private void setCurrentProfile(String result) {
		ProfileDbAdapter mProfileDbAdapter = new ProfileDbAdapter(this);
		ProfileModel mProfileModel = mProfileDbAdapter.getProfile(Integer
				.valueOf(result));
		if (mProfileModel != null)
			new SetProfileTask().execute(mProfileModel);
	}
}
