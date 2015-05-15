package com.healthlife.util;

import com.healthlife.entity.User;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
//SharedPreferences只能被同一个包下的类访问
public class CurrentUser {
	Context context;
	public CurrentUser(Context context)
	{
		this.context=context;
	}
	
	public void Login(long userid,String username,String password){
		      
		SharedPreferences mySharedPreferences= context.getSharedPreferences("CurrentUser",
				Activity.MODE_PRIVATE); 
		//实例化SharedPreferences.Editor对象（第二步） 
		SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		//用putString的方法保存数据 
		editor.putString("loginstate", "login"); 
		editor.putLong("userid", userid);
		editor.putString("username", username); 
		editor.putString("password", password); 
		//提交当前数据 
		editor.commit(); 


	}
	
	public void Logout(){
		SharedPreferences mySharedPreferences= context.getSharedPreferences("CurrentUser",
				Activity.MODE_PRIVATE); 
		//实例化SharedPreferences.Editor对象（第二步） 
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		//用putString的方法保存数据 
		
		editor.putString("loginstate", "loginout"); 
		editor.putString("username", " "); 
		editor.putString("password", " "); 
		//提交当前数据 
		editor.commit(); 
		

		
	}
	
	public User QueryCurrentUser()
	{
		SharedPreferences mySharedPreferences= context.getSharedPreferences("CurrentUser",
				Activity.MODE_PRIVATE); 
		// 使用getString方法获得value，注意第2个参数是value的默认值 
		String loginstate =mySharedPreferences.getString("loginstate", "logout"); 
		if(loginstate.equals("login"))
		{
			long userid=mySharedPreferences.getLong("userid", 0);
			String username =mySharedPreferences.getString("username", " "); 
			String password =mySharedPreferences.getString("password", " ");
			User user=new User(username,password);
			user.setUserId(userid);
			return user;
		}
		else
			return null;
	}
	
	
	
}
