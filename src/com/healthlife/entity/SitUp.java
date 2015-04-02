package com.healthlife.entity;

public class SitUp extends Sports {

	public SitUp() {
		// TODO 自动生成的构造函数存根
	}
	
	final static int sportsType = SITUP;
	
	private int num;
	private int validNum;
	private float validRate;
	private float perfectRate;
	private int grade;
	
	/*
	 * getters
	 */
	protected int getNum() {
		return num;
	}
	protected float getValidRate() {
		return validRate;
	}
	protected float getPerfectRate() {
		return perfectRate;
	}
	protected int getGrade() {
		return grade;
	}
	
	protected int getValidNum() {
		return validNum;
	}
	
	/*
	 * setters
	 */
	protected void setNum(int num) {
		this.num = num;
	}
	protected void setValidRate(float validRate) {
		this.validRate = validRate;
	}
	protected void setPerfectRate(float perfectRate) {
		this.perfectRate = perfectRate;
	}
	protected void setGrade(int grade) {
		this.grade = grade;
	}

	protected void setValidNum(int validNum) {
		this.validNum = validNum;
	}
}
