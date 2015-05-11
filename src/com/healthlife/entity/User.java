package com.healthlife.entity;

public class User {
	
	private String usersName;
	private String passWord;
	private long userId;
	
	public User()
	{
		
	}
	
	public  User(String username,String password)
	{
		this.usersName=username;
		this.passWord=password;
	}
	
	public String getUsersName() {
		return usersName;
	}
	public void setUsersName(String usersName) {
		this.usersName = usersName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	

}
