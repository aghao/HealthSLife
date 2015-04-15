package com.healthlife.entity;

public class Beats {

	private long beatId;
	private int beats;
	private String date;
	private int type;
	private long userId;
	
	public long getBeatId() {
		return beatId;
	}
	public void setBeatId(long beatId) {
		this.beatId = beatId;
	}
	public int getBeats() {
		return beats;
	}
	public void setBeats(int beats) {
		this.beats = beats;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
}
