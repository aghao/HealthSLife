package com.healthlife.entity;

import java.io.Serializable;

public class Position implements Serializable{
	private long time;
	private double latitude;
	private double longitude;
	private long sportID;
	private long positionID;
	
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
	public long getSportID() {
		return sportID;
	}
	public void setSportID(long sportID) {
		this.sportID = sportID;
	}
	public long getPositionID() {
		return positionID;
	}
	public void setPositionID(long positionID) {
		this.positionID = positionID;
	}
}
