package com.profiler;

import java.util.List;

import com.profiler.models.ProfileModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textViewName = null;
		if (convertView == null)
			convertView = mInflater.inflate(R.layout.profile_listview_item,null);

		textViewName = (TextView) convertView
				.findViewById(R.id.textViewName);

		textViewName.setText(mList.get(position).getName());

		return convertView;
	}

}