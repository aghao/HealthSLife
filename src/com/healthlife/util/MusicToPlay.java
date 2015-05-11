package com.healthlife.util;

import java.io.IOException;

import android.media.MediaPlayer;

/**
 * 此类充当全局变量,保存当前音乐文件路径,保存播放状态,以及音乐是否已切换.
 * @see musicpath保存当前播放音乐的文件路径.
 * @see musicstate保存当前播放音乐的文件路径.
 * @see ifchange显示是否有切换音乐的动作.
 */
public class MusicToPlay {
	
	
	public static String musicpath="none";
	public static boolean if_music_change=false;
	public static boolean if_lyric_change=false;


	public static final MediaPlayer mediaPlayer=new MediaPlayer();
	public static boolean firstplay=true;
	
	
	public static int musicstate=0;	
	public static final int PAUSE=0;
	public static final int PLAYING=1;
	public static final int STOP=2;
	

	
	public MusicToPlay() {
		// TODO Auto-generated constructor stub
	}
	
	
	public static void ResetMusic() {  
		   
	    	mediaPlayer.reset();  
	        try {  
	  
	        	mediaPlayer.setDataSource(musicpath);  
	        	mediaPlayer.prepare();  
	        } catch (IllegalArgumentException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } catch (IllegalStateException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	}  
	
	
	
}
