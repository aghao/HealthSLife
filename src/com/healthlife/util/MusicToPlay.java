package com.healthlife.util;

import java.io.IOException;

import android.media.MediaPlayer;

/**
 * ����䵱ȫ�ֱ���,���浱ǰ�����ļ�·��,���沥��״̬,�Լ������Ƿ����л�.
 * @see musicpath���浱ǰ�������ֵ��ļ�·��.
 * @see musicstate���浱ǰ�������ֵ��ļ�·��.
 * @see ifchange��ʾ�Ƿ����л����ֵĶ���.
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
