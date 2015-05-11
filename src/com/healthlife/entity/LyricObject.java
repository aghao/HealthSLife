package com.healthlife.entity;

public class LyricObject {
	  public int begintime; // 开始时间  
	  public int endtime; // 结束时间  
	  public int timeline; // 单句歌词用时  
	  public String lrc; // 单句歌词 
	  
	  
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
