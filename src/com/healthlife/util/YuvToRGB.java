package com.healthlife.util;

import android.util.Log;


public class YuvToRGB {
	
	    //I420是yuv420格式，是3个plane，排列方式为(Y)(U)(V)  
	    public static double getRed(byte[] src, int width, int height){  
	        int numOfPixel = width * height;  
//	        int positionOfV = numOfPixel;  
	        int positionOfU = numOfPixel/4 + numOfPixel;   
	        int	red = 0;
	        for(int i=0; i<height; i++){  
	            int startY = i*width;  
	            int step = (i/2)*(width/2);  
//	            int startU = positionOfV + step;  
	            int startV = positionOfU + step;  
	            for(int j = 0; j < width; j++){  
	                int Y = startY + j;  
//	                int U = startU + j/2;  
	                int V = startV + j/2;  
	                //RGB tmp = yuvTorgb(src[Y], src[U], src[V]);  
	                int tmp = (int)((src[Y]&0xff) + 1.4075 * ((src[V]&0xff)-128));
	                tmp = (tmp<0?0:tmp>255?255:tmp);
	                red = red + tmp;

	            }  
	        }  
	        red = red/numOfPixel;
	        Log.v("num",""+red);
	        if(red < 105 || red > 150)
	        	return -1;
	        return (double)(red);  

	    }      
}
//	    private static class RGB{  
//	        public int r;//, g, b;  
//	    }  
	      
//	    private static RGB yuvTorgb(byte Y, byte U, byte V){  
//	        RGB rgb = new RGB();  
//	        rgb.r = (int)((Y&0xff) + 1.4075 * ((V&0xff)-128));  
////	        rgb.g = (int)((Y&0xff) - 0.3455 * ((U&0xff)-128) - 0.7169*((V&0xff)-128));  
////	        rgb.b = (int)((Y&0xff) + 1.779 * ((U&0xff)-128));  
//	        rgb.r =(rgb.r<0? 0: rgb.r>255? 255 : rgb.r);  
////	        rgb.g =(rgb.g<0? 0: rgb.g>255? 255 : rgb.g);  
////	        rgb.b =(rgb.b<0? 0: rgb.b>255? 255 : rgb.b);  
//	        return rgb;  
//	    }  
//}
