package com.profiler;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.profiler.db.ProfileDbAdapter;
import com.profiler.models.ProfileModel;

public class CreateProfileActivity extends Activity {

	final int OPEN_GALLERY = 100, OPEN_AUDIO = 200;
	Button openGallery, pickAudio;
	SeekBar volumeBar;
	int volumeLevel;
	ProfileModel profileModel;
	final static String TAG = "CreateProfileActivity";
	EditText txtProfileName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_profile_activity);
		txtProfileName = (EditText) findViewById(R.id.txtProfileName);
		openGallery = (Button) findViewById(R.id.openGallery);
		pickAudio = (Button) findViewById(R.id.pickAudio);

		volumeBar = (SeekBar) findViewById(R.id.volumeBar);
		openGallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

				startActivityForResult(i, OPEN_GALLERY);
			}
		});

		pickAudio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setType("audio/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, OPEN_AUDIO);
			}
		});

		volumeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				volumeLevel = progress;
				profileModel.setVolume(volumeLevel);
				/*
				 * Toast.makeText(CreateProfileActivity.this, "Volume Level " +
				 * volumeLevel, Toast.LENGTH_SHORT) .show();
				 */
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}
		});

		profileModel = new ProfileModel();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == OPEN_GALLERY && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();

			ImageView imageView = (ImageView) findViewById(R.id.thumbImg);
			imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			profileModel.setWallpaper(picturePath);

		} else if (requestCode == OPEN_AUDIO && resultCode == RESULT_OK
				&& data != null) {
			Uri uri = data.getData();
			// Log.v("", "Path " + uri.toString());
			// Log.v("", "Path " + new File(uri.toString()).getAbsolutePath());
			// Log.v("", "Path " + getRealPathFromURI(uri));
			profileModel.setRingtone(uri.toString());
		}

	}

	private String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Audio.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null,
				null, null);
		cursor.moveToFirst();
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public void onSaveBtnClicked(View v) {
		Log.i(TAG, "onSaveBtnClicked");

		ProfileDbAdapter mProfileDbAdapter = new ProfileDbAdapter(this);
		List<ProfileModel> mList = mProfileDbAdapter.getProfileList();
		profileModel.setProfile_id(mList.size() + 1);
		profileModel.setName(txtProfileName.getText().toString());
		// addProfileInDb(profileModel);
		// profileModel = new ProfileModel(mList.size() + 1, 2, "ring3",
		// "wal1","car");

		addProfileInDb(profileModel);
		finish();
	}

	private void addProfileInDb(ProfileModel mProfileModel) {
		ProfileDbAdapter mProfileDbAdapter = new ProfileDbAdapter(this);
		mProfileDbAdapter.create(mProfileModel);
	}
}
