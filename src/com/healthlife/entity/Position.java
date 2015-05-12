package com.healthlife.entity;

import java.io.Serializable;


public class Position implements Serializable{
	
	private long time;
	private double latitude;
	private double longitude;
	private long sportId;
	private long positionId;
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public long getPositionId() {
		return positionId;
	}
	public void setPositionId(long positionId) {
		this.positionId = positionId;
	}
	public long getSportId() {
		return sportId;
	}
	public void setSportId(long sportId) {
		this.sportId = sportId;
	}

}
