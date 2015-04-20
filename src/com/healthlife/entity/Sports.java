package com.healthlife.entity;

/*
 * a abstract class for vrious sports
 */


public class Sports {
	
	private long sportsID;
	private String Date;
	private int type;
	private long userId;
	private int num;
	private float ValidRate;
	private float perfectRate;
	private float grade;
	private float MaxSpeed;
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
		this.Date = date;
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
	public float getValidRate() {
		return ValidRate;
	}
	public void setValidRate(float validRate) {
		this.ValidRate = validRate;
	}
	public float getPerfectRate() {
		return perfectRate;
	}
	public void setPerfectRate(float perfectRate) {
		this.perfectRate = perfectRate;
	}
	public float getGrade() {
		return grade;
	}
	public void setGrade(float grade) {
		this.grade = grade;
	}
	public float getMaxSpeed() {
		return MaxSpeed;
	}
	public void setMaxSpeed(float maxSpeed) {
		this.MaxSpeed = maxSpeed;
	}
	public float getAVGSpeed() {
		return AVGSpeed;
	}
	public void setAVGSpeed(float AVGSpeed) {
		this.AVGSpeed = AVGSpeed;
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

