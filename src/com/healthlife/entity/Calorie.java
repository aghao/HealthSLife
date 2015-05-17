package com.healthlife.entity;

public class Calorie {
	
	private long userId;
	private long calorieId;
	private float calorie;
	private String date;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getCalorieId() {
		return calorieId;
	}
	public void setCalorieId(long calorieId) {
		this.calorieId = calorieId;
	}
	public float getCalorie() {
		return calorie;
	}
	public void setCalorie(float calorie) {
		this.calorie = calorie;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

}
