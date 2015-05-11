package com.healthlife.entity;

import java.io.Serializable;

/*
 * a abstract class for vrious sports
 */


@SuppressWarnings("serial")
public class Sports implements Serializable{
	
	private long sportsID;
	private String Date;
	private int type;
	private long userId;
	private int num;
	private float validNum;
	private float perfectNum;
	private float goodNum;
	private float maxSpeed;
	private float AVGSpeed;
	private float distance;
	private String duration;
	public long getSportsID() {
		return sportsID;
	}
	public void setSportsID(long sportsID) {
		this.sportsID = sportsID;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
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
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public float getValidNum() {
		return validNum;
	}
	public void setValidNum(float validNum) {
		this.validNum = validNum;
	}
	public float getPerfectNum() {
		return perfectNum;
	}
	public void setPerfectNum(float perfectNum) {
		this.perfectNum = perfectNum;
	}
	public float getGoodNum() {
		return goodNum;
	}
	public void setGoodNum(float goodNum) {
		this.goodNum = goodNum;
	}
	public float getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public float getAVGSpeed() {
		return AVGSpeed;
	}
	public void setAVGSpeed(float aVGSpeed) {
		AVGSpeed = aVGSpeed;
	}
	public float getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
}

