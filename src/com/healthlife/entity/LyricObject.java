package com.healthlife.entity;

public class LyricObject {
	  public int begintime; // ��ʼʱ��  
	  public int endtime; // ����ʱ��  
	  public int timeline; // ��������ʱ  
	  public String lrc; // ������ 
	  
	  
	public LyricObject() {
		// TODO Auto-generated constructor stub
		
	}

	public LyricObject(LyricObject lyric) {
		// TODO Auto-generated constructor stub
		setBegintime(lyric.begintime);
		setEndtime(lyric.endtime);
		setTimeline(lyric.timeline);
		setLrc(lyric.lrc);
	}
	public int getBegintime() {
		return begintime;
	}


	public void setBegintime(int begintime) {
		this.begintime = begintime;
	}


	public int getEndtime() {
		return endtime;
	}


	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}


	public int getTimeline() {
		return timeline;
	}


	public void setTimeline(int timeline) {
		this.timeline = timeline;
	}


	public String getLrc() {
		return lrc;
	}


	public void setLrc(String lrc) {
		this.lrc = lrc;
	}

}
