package com.profiler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.profiler.models.ProfileModel;

public class ListViewAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	Context mContext;
	List<ProfileModel> mList;

	public ListViewAdapter(Context context, List<ProfileModel> list) {
		mContext = context;
		mList = list;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		TextView textViewName = null;
		if (convertView == null)
			convertView = mInflater.inflate(R.layout.profile_listview_item,
					null);

		textViewName = (TextView) convertView.findViewById(R.id.textViewName);

		textViewName.setText(mList.get(position).getName());

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new SetProfileTask().execute(mList.get(position));
			}
		});
		return convertView;
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
			// bitmap.recycle();
			// if(imagePath!=null){
			System.out.println("Hi I am try to open Bit map");
			wallpaperManager = WallpaperManager.getInstance(mContext);
			wallpaperDrawable = wallpaperManager.getDrawable();
			wallpaperManager.setBitmap(useThisBitmap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setRingTone(String path) {
		File k = new File(path); // path
									// is
									// a
									// file
									// to
		// /sdcard/media/ringtone

		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, k.getAbsolutePath());
		values.put(MediaStore.MediaColumns.TITLE, "My Song title");
		values.put(MediaStore.MediaColumns.SIZE, 215454);
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
		values.put(MediaStore.Audio.Media.ARTIST, "Madonna");
		values.put(MediaStore.Audio.Media.DURATION, 230);
		values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
		values.put(MediaStore.Audio.Media.IS_ALARM, false);
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);

		// Insert it into the database
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(k
				.getAbsolutePath());
		Uri newUri = mContext.getContentResolver().insert(uri, values);

		RingtoneManager.setActualDefaultRingtoneUri(mContext,
				RingtoneManager.TYPE_RINGTONE, newUri);
	}

	class SetProfileTask extends AsyncTask<ProfileModel, Void, Void> {
		ProgressDialog progressDialog;
		ProfileModel profile;

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(mContext, "",
					"Activating profile");
		}

		@Override
		protected Void doInBackground(ProfileModel... params) {
			profile = params[0];
			changeWallpaper(profile.getWallpaper());
			setRingTone(profile.getRingtone());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
		}

	}

}
