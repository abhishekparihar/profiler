package com.profiler.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.profiler.models.ProfileModel;

public class ProfileDbAdapter extends DbAdapter {
    public ProfileDbAdapter(Context context) {
        super(context);
        setDbName();
        setDbColumns();
    }

    protected void setDbName() {
        this.dbName = "my_checkins";
    }

    protected void setDbColumns() {
        this.dbColumns = new String[] { "_id", "profile_id", "name" ,"wallpaper","volume","ringtone"};
    }

    ContentValues createContentValues(ProfileModel mProfileModel) {
        ContentValues values = new ContentValues();
        values.put("profile_id", mProfileModel.getProfile_id());
        values.put("name", mProfileModel.getName());
        values.put("wallpaper", mProfileModel.getWallpaper());
        values.put("volume", mProfileModel.getVolume());
        values.put("ringtone", mProfileModel.getRingtone());

        return values;
    }

    public long create(ProfileModel mProfileModel) {
        ContentValues initialValues = createContentValues(mProfileModel);
        return super.create(initialValues);
    }

    public boolean update(long rowId, ProfileModel mProfileModel) {
        ContentValues updateValues = createContentValues(mProfileModel);
        return super.update(rowId, updateValues);
    }
    
    public HashMap<String, String> getResultParamsFromCursor(Cursor c) {
        String[] columnNames = new String[] { "profile_id", "name" ,"wallpaper","volume","ringtone"};
        HashMap<String, String> profileParams = new HashMap<String, String>();
        Integer columnIndex;

        for ( String columnName : columnNames ) {
            columnIndex = c.getColumnIndex(columnName);
            profileParams.put(columnName, c.getString(columnIndex));
        }

        return profileParams;
    }

    public ProfileModel getProfile(Integer profileId) {
        ProfileDbAdapter mProfileDbAdapter = new ProfileDbAdapter(context);
        Cursor c = mProfileDbAdapter.fetchAll("profile_id=" + profileId.toString(), "1");
        
        ProfileModel mProfileModel=null;
        
        if ( c.moveToFirst() ) {

            HashMap<String, String> profileParams = getResultParamsFromCursor(c);

            mProfileModel = new ProfileModel(profileParams);
        }

        c.close();

        return mProfileModel;
    }
    
    public List<ProfileModel> getProfileList() {
    	 ProfileDbAdapter mProfileDbAdapter = new ProfileDbAdapter(context);
        Cursor c = mProfileDbAdapter.fetchAll(null, null);
        
        List<ProfileModel> mProfileList = cursorToResultList(c);
        c.close();

        return mProfileList;
    }
    
    private List<ProfileModel> cursorToResultList(Cursor c) {
        List<ProfileModel> mProfilelList = new ArrayList<ProfileModel>();
        ProfileModel mProfileModel=null;
        if ( c.moveToFirst() ) {
            
            do {
            	 HashMap<String, String> profileParams = getResultParamsFromCursor(c);
                 mProfileModel = new ProfileModel(profileParams);
                 
                 mProfilelList.add(mProfileModel);
            } while ( c.moveToNext() );
        }

        return mProfilelList;
    }
    
    private void updateProfile(ProfileModel mProfileModel) {
    	 ProfileDbAdapter mProfileDbAdapter = new ProfileDbAdapter(context);

		Cursor c = mProfileDbAdapter.fetchAll("profile_id=" + String.valueOf(mProfileModel.getProfile_id()), "1");
		int profileRowIdColumn = c.getColumnIndex("_id");
		if ( c.moveToFirst() ) {
			int rowId = c.getInt(profileRowIdColumn);
			mProfileDbAdapter.update(Long.valueOf(rowId), mProfileModel);
		}
		c.close();

    }
}
