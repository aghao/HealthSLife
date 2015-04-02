package com.healthlife.util;

import com.healthlife.entity.*;

public class Analyser {

	public Analyser() {
		// TODO 自动生成的构造函数存根
	}
	
	public Sports Analyse(int type,float [][][] sportsData){
		
		Sports sports = null;
		
		denoise(sportsData);
		
		switch(type)
		{
			case 0 : analyseJog(sports,sportsData); break;
			
			case 1 : analysePushUp(sports,sportsData); break;
			
			case 2 : analyseSitUp(sports,sportsData); break;
			
			case 3 : analyseWalk(sports,sportsData); break;
			
			default : System.out.println("unknown sportsType");
			
		}
		
		return sports;
	}
	
	private void denoise(float [][][] sportsData){
		
	}
	
	private void analyseJog(Sports sports,float [][][] sportsData){
		sports = new Jog();
		

	}
	
	private void analyseWalk(Sports sports,float [][][] sportsData){
		sports = new Walk();
		
		
	}
	
	private void analysePushUp(Sports sports,float [][][] sportsData){
		
		sports = new PushUp();
		
		
		
	}
	
	private void analyseSitUp(Sports sports,float [][][] sportsData){
		
		sports = new SitUp();
		
		
		
	}

}
