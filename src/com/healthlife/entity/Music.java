package com.healthlife.entity;

public class Music {
	private long musicId;
	private String musicName;
	private String musicPath;
	private int pace;
	private int ifActive;
	
	public long getMusicId() {
		return musicId;
	}
	public void setMusicId(long musicId) {
		this.musicId = musicId;
	}
	public String getMusicName() {
		return musicName;
	}
	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}
	public String getMusicPath() {
		return musicPath;
	}
	public void setMusicPath(String musicPath) {
		this.musicPath = musicPath;
	}
	public int getPace() {
		return pace;
	}
	public void setPace(int pace) {
		this.pace = pace;
	}
	public int isIfActive() {
		return ifActive;
	}
	public void setIfActive(int ifActive) {
		this.ifActive = ifActive;
	}
}
