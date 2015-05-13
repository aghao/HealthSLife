package com.healthlife.entity;

public class Record {
	
	private long recordId;
	private long userId;
	
	private int AVGSpeed;
	private int distance;
	
	private float calOfPushUp;
	private float calOfSitUp;
	private float calOfJog;
	private float totalCal;
		
	private float durationPushUp;
	private float durationJog; 
	private float durationSitUp;
	private float durationPerDay;
	private float totalDuration;
		
	private float totalDistance;
	private int totalNumPushUp;
	private int totalNumSitUp;
	private int totalSteps;
	public long getRecordId() {
		return recordId;
	}
	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public int getAVGSpeed() {
		return AVGSpeed;
	}
	public void setAVGSpeed(int aVGSpeed) {
		AVGSpeed = aVGSpeed;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public float getCalOfPushUp() {
		return calOfPushUp;
	}
	public void setCalOfPushUp(float calOfPushUp) {
		this.calOfPushUp = calOfPushUp;
	}
	public float getCalOfSitUp() {
		return calOfSitUp;
	}
	public void setCalOfSitUp(float calOfSitUp) {
		this.calOfSitUp = calOfSitUp;
	}
	public float getCalOfJog() {
		return calOfJog;
	}
	public void setCalOfJog(float calOfJog) {
		this.calOfJog = calOfJog;
	}
	public float getTotalCal() {
		return totalCal;
	}
	public void setTotalCal(float totalCal) {
		this.totalCal = totalCal;
	}
	public float getDurationPushUp() {
		return durationPushUp;
	}
	public void setDurationPushUp(float durationPushUp) {
		this.durationPushUp = durationPushUp;
	}
	public float getDurationJog() {
		return durationJog;
	}
	public void setDurationJog(float durationJog) {
		this.durationJog = durationJog;
	}
	public float getDurationSitUp() {
		return durationSitUp;
	}
	public void setDurationSitUp(float durationSitUp) {
		this.durationSitUp = durationSitUp;
	}
	public float getDurationPerDay() {
		return durationPerDay;
	}
	public void setDurationPerDay(float durationPerDay) {
		this.durationPerDay = durationPerDay;
	}
	public float getTotalDuration() {
		return totalDuration;
	}
	public void setTotalDuration(float totalDuration) {
		this.totalDuration = totalDuration;
	}
	public float getTotalDistance() {
		return totalDistance;
	}
	public void setTotalDistance(float totalDistance) {
		this.totalDistance = totalDistance;
	}
	public int getTotalNumPushUp() {
		return totalNumPushUp;
	}
	public void setTotalNumPushUp(int totalNumPushUp) {
		this.totalNumPushUp = totalNumPushUp;
	}
	public int getTotalNumSitUp() {
		return totalNumSitUp;
	}
	public void setTotalNumSitUp(int totalNumSitUp) {
		this.totalNumSitUp = totalNumSitUp;
	}
	public int getTotalSteps() {
		return totalSteps;
	}
	public void setTotalSteps(int totalSteps) {
		this.totalSteps = totalSteps;
	}
	
	
}
