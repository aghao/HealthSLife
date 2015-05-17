package com.healthlife.entity;

public class Food {
	private long foodId;
	private String foodName;
	private float calorie;
	private int type;
	
	public long getFoodId() {
		return foodId;
	}
	public void setFoodId(long foodId) {
		this.foodId = foodId;
	}
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}
	public float getCalorie() {
		return calorie;
	}
	public void setCalorie(float calorie) {
		this.calorie = calorie;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
