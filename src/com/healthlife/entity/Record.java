package com.healthlife.entity;

public class Record {
	
	private long recordId;
	private float AVGSpeed;
	private int numPushUp;
	private int numSitUp;
	private float validRateSitUp;
	private float validRatePushUp;
	private float distance;
	private float AVGPace;
	private float perfectRatePushUp;
	private float perfectRateSitUp;
	private float gradePushUp;
	private float gradeSitUp;
	private float TotalDistance;
	private int totalNumPushUp;
	private int totalNumSitUp;
	private int steps;
	private long userId;
	
	public long getRecordId() {
		return recordId;
	}
	public float getAVGSpeed() {
		return AVGSpeed;
	}
	public void setAVGSpeed(float aVGSpeed) {
		AVGSpeed = aVGSpeed;
	}
	public int getNumPushUp() {
		return numPushUp;
	}
	public void setNumPushUp(int numPushUp) {
		this.numPushUp = numPushUp;
	}
	public int getNumSitUp() {
		return numSitUp;
	}
	public void setNumSitUp(int numSitUp) {
		this.numSitUp = numSitUp;
	}
	public float getValidRateSitUp() {
		return validRateSitUp;
	}
	public void setValidRateSitUp(float validRateSitUp) {
		this.validRateSitUp = validRateSitUp;
	}
	public float getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
	public float getAVGPace() {
		return AVGPace;
	}
	public void setAVGPace(float AVGPace) {
		this.AVGPace = AVGPace;
	}
	public float getPerfectRatePushUp() {
		return perfectRatePushUp;
	}
	public void setPerfectRatePushUp(float perfectRatePushUp) {
		this.perfectRatePushUp = perfectRatePushUp;
	}
	public float getPerfectRateSitUp() {
		return perfectRateSitUp;
	}
	public void setPerfectRateSitUp(float perfectRateSitUp) {
		this.perfectRateSitUp = perfectRateSitUp;
	}
	public float getGradePushUp() {
		return gradePushUp;
	}
	public void setGradePushUp(float gradePushUp) {
		this.gradePushUp = gradePushUp;
	}
	public float getGradeSitUp() {
		return gradeSitUp;
	}
	public void setGradeSitUp(float gradeSitUp) {
		this.gradeSitUp = gradeSitUp;
	}
	public float getTotalDistance() {
		return TotalDistance;
	}
	public void setTotalDistance(float totalDistance) {
		TotalDistance = totalDistance;
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
	public int getSteps() {
		return steps;
	}
	public void setSteps(int steps) {
		this.steps = steps;
	}
	public long getUserId() {
		return userId;
	}

	public float getValidRatePushUp() {
		return validRatePushUp;
	}
	public void setValidRatePushUp(float validRatePushUp) {
		this.validRatePushUp = validRatePushUp;
	}
	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
}
