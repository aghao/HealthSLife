package com.healthlife.entity;

public class Music {
	

	public long musicId;
	public String name;
	public String filepath;
	public int pace;
	public int ifActive;
	
	
	public Music() {
		// TODO Auto-generated constructor stub
	
	}
	
	public Music(String name,String filepath,int pace) {
		// TODO Auto-generated constructor stub
		this.name=name;
		this.filepath=filepath;
		this.pace=pace;
	}
	
	
	public Music(long Id,String name,String path,int pace,int ifactive)
	{
		this.musicId=Id;
		this.filepath=path;
		this.name=name;
		this.pace=pace;
		this.ifActive=ifactive;
	}
	
	public Music(Music music)
	{
		this.musicId=music.getMusicId();
		this.filepath=music.getMusicPath();
		this.name=music.getMusicName();
		this.pace=music.getPace();
		this.ifActive=music.isIfActive();
	}
	
	public long getMusicId() {
		return musicId;
	}

	public void setMusicId(long musicId) {
		this.musicId = musicId;
	}

	public String getMusicName() {
		return name;
	}

	public void setMusicName(String name) {
		this.name = name;
	}

	public String getMusicPath() {
		return filepath;
	}

	public void setMusicPath(String filepath) {
		this.filepath = filepath;
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
