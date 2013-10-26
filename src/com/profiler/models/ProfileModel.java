package com.profiler.models;

import java.util.HashMap;

public class ProfileModel {

	private int profile_id;
	private String name;
	private String wallpaper;
	private int volume;
	private String ringtone;

	public ProfileModel(HashMap<String, String> profileParams) {
		this.profile_id = Integer.valueOf(profileParams.get("profile_id"));
		this.volume = Integer.valueOf(profileParams.get("volume"));
		this.ringtone = profileParams.get("ringtone");
		this.wallpaper = profileParams.get("wallpaper");
		this.name = profileParams.get("name");
	}

	public ProfileModel(int profile_id, int volume, String ringtone, String wallpaper,
			String name) {
		this.profile_id = profile_id;
		this.volume = volume;
		this.ringtone = ringtone;
		this.wallpaper = wallpaper;
		this.name = name;	
	}

	public int getProfile_id() {
		return profile_id;
	}

	public void setProfile_id(int profile_id) {
		this.profile_id = profile_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWallpaper() {
		return wallpaper;
	}

	public void setWallpaper(String wallpaper) {
		this.wallpaper = wallpaper;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public String getRingtone() {
		return ringtone;
	}

	public void setRingtone(String ringtone) {
		this.ringtone = ringtone;
	}

}
