package com.healthlife.util;

import com.healthlife.entity.User;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
//SharedPreferencesֻ�ܱ�ͬһ�����µ������
public class CurrentUser {
	Context context;
	public CurrentUser(Context context)
	{
		this.context=context;
	}
	
	public void Login(String username,String password,String userId){
		      
		SharedPreferences mySharedPreferences= context.getSharedPreferences("CurrentUser",
				Activity.MODE_PRIVATE); 
		//ʵ����SharedPreferences.Editor���󣨵ڶ����� 
		SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		//��putString�ķ����������� 
		editor.putString("loginstate", "login"); 
		editor.putString("username", username); 
		editor.putString("password", password); 
		//�ύ��ǰ���� 
		editor.commit(); 


	}
	
	public void Logout(){
		SharedPreferences mySharedPreferences= context.getSharedPreferences("CurrentUser",
				Activity.MODE_PRIVATE); 
		//ʵ����SharedPreferences.Editor���󣨵ڶ����� 
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		//��putString�ķ����������� 
		
		editor.putString("loginstate", "loginout"); 
		editor.putString("username", " "); 
		editor.putString("password", " "); 
		//�ύ��ǰ���� 
		editor.commit(); 
		

		
	}
	
	public User QueryCurrentUser()
	{
		SharedPreferences mySharedPreferences= context.getSharedPreferences("CurrentUser",
				Activity.MODE_PRIVATE); 
		// ʹ��getString�������value��ע���2��������value��Ĭ��ֵ 
		String loginstate =mySharedPreferences.getString("loginstate", "logout"); 
		if(loginstate.equals("login"))
		{
			String username =mySharedPreferences.getString("username", " "); 
			String password =mySharedPreferences.getString("password", " ");
			User user=new User(username,password);
			return user;
		}
		else
			return null;
	
	}
	
	
	
}
