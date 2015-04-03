package com.healthlife.entity;

import java.util.Date;

/*
 * a abstract class for vrious sports
 */


public abstract class Sports {
	
	int sportType;
	Date sportsDate;
	
	public final static int JOG = 0;
	public final static int PUSHUP = 1;
	public final static int SITUP = 2;
	public final static int WALK = 3;
}

