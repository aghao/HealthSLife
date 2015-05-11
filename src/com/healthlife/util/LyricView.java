package com.healthlife.util;

import java.io.BufferedReader;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.util.Iterator;  
import java.util.TreeMap;  
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  

import com.healthlife.entity.LyricObject;
  
import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Paint;  
import android.util.AttributeSet;  
import android.util.DisplayMetrics;
import android.util.Log;  
import android.view.MotionEvent;  
import android.view.View;  
  
public class LyricView extends View{  
      
    private static TreeMap<Integer, LyricObject> lrc_map;  
    private float mX;       //��ĻX����е㣬��ֵ�̶������ָ����X�м���ʾ  
    private float offsetY;      //�����Y���ϵ�ƫ��������ֵ����ݸ�ʵĹ�����С  
    private static boolean blLrc=false;  
    private float touchY;   //���������Viewʱ������Ϊ��ǰ�����Y������  
    private float touchX;  
    private boolean blScrollView=false;  
    private int lrcIndex=0; //������TreeMap���±�  
    private int lrcIndex_s=-1;//������TreeMap���±�  (��ʹ����������ʾ�����ǵڶ��е��±�)
    private  int SIZEWORD=0;//��ʾ������ֵĴ�Сֵ  
    private static int WORDNUM=15;//ÿ����ʾ�����������
    private  int INTERVAL=45;//���ÿ�еļ��  
    private int WidgetWidth;//�ؼ����(px)
    private int WidgetHeight;//�ؼ��߶�(px)
    
    private static boolean readlock=false;
    
    Paint paint=new Paint();//���ʣ����ڻ����Ǹ����ĸ��  
    Paint paintHL=new Paint();  //���ʣ����ڻ������ĸ�ʣ�����ǰ���������  
    
    
    
    public LyricView(Context context){  
        super(context);  
        init();  
    }  
      
    public LyricView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        init();  
    }  
      
    /* (non-Javadoc) 
     * @see android.view.View#onDraw(android.graphics.Canvas) 
     */  
    @Override  
    protected void onDraw(Canvas canvas) {  
    
        if(blLrc){  
            //paintHL.setTextSize(SIZEWORD);  
        	//add
        	paintHL.setTextSize(SIZEWORD+5);  
            paint.setTextSize(SIZEWORD);  
            LyricObject temp=lrc_map.get(lrcIndex);  
            canvas.drawText(temp.lrc, mX, offsetY+(SIZEWORD+INTERVAL)*lrcIndex, paintHL);  
            /*
             * ������ʾ
             */
            if(lrcIndex_s>=0)
            {
            	
            	 temp=lrc_map.get(lrcIndex_s);  
                 canvas.drawText(temp.lrc, mX, offsetY+(SIZEWORD+INTERVAL)*lrcIndex_s, paintHL); 
                 
                 // ����ǰ���֮ǰ�ĸ��  
                 for(int i=lrcIndex-2;i>=0;i--){ 
                     temp=lrc_map.get(i);  
                     if(offsetY+(SIZEWORD+INTERVAL)*i<0){  
                         break;  
                     }  
                     
               
                     
                     canvas.drawText(temp.lrc, mX, offsetY+(SIZEWORD+INTERVAL)*i, paint);  
                 }  
            }
            else
            {
            	 // ����ǰ���֮ǰ�ĸ��  
                for(int i=lrcIndex-1;i>=0;i--){ 
                    temp=lrc_map.get(i);  
                    if(offsetY+(SIZEWORD+INTERVAL)*i<0){  
                        break;  
                    }  
                    
              
                    
                    canvas.drawText(temp.lrc, mX, offsetY+(SIZEWORD+INTERVAL)*i, paint);  
                }  
            	
            }
            /*
             * ������ʾ
             */
            
           
            
        
            // ����ǰ���֮��ĸ��  
            for(int i=lrcIndex+1;i<lrc_map.size();i++){  
                temp=lrc_map.get(i);  
                if(offsetY+(SIZEWORD+INTERVAL)*i>WidgetHeight){  
                    break;  
                }  
             
           
                canvas.drawText(temp.lrc, mX, offsetY+(SIZEWORD+INTERVAL)*i, paint);  
            }  
        }  
        else{  
            paint.setTextSize(25);  
            canvas.drawText("�Ҳ������", mX, 310, paint);  
        }  
        super.onDraw(canvas);  
    }  
  
    /* (non-Javadoc) 
     * @see android.view.View#onTouchEvent(android.view.MotionEvent) 
     */  
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        // TODO Auto-generated method stub  
        System.out.println("bllll==="+blScrollView);  
        float tt=event.getY();  
        if(!blLrc){  
            //return super.onTouchEvent(event);  
  
            return super.onTouchEvent(event);  
        }  
        switch(event.getAction()){  
        case MotionEvent.ACTION_DOWN:  
        //    touchX=event.getX();  
            break;  
        case MotionEvent.ACTION_MOVE:  
            touchY=tt-touchY;             
            offsetY=offsetY+touchY;  
            break;  
        case MotionEvent.ACTION_UP:  
        //    blScrollView=false;  
            break;        
        }  
        touchY=tt;  
        return true;  
    }  
  
    public void init(){  
        lrc_map = new TreeMap<Integer, LyricObject>();  
        offsetY=320;      
          
        paint=new Paint();  
        paint.setTextAlign(Paint.Align.CENTER);  
        paint.setColor(Color.GREEN);  
        paint.setAntiAlias(true);  
        paint.setDither(true);  
        paint.setAlpha(180);  
          
          
        paintHL=new Paint();  
        paintHL.setTextAlign(Paint.Align.CENTER);  
          
        paintHL.setColor(Color.RED);  
        paintHL.setAntiAlias(true);  
        paintHL.setAlpha(255);  
        
       
      
        
    }  
     
    
    
    /** 
     * ���ݸ����������Ǿ���ȷ���������Ĵ�С 
     */  
    public void SetTextSize(){  
       
    	 if(!blLrc){  
             return;  
         }  
         int max=lrc_map.get(0).lrc.length();  
         for(int i=1;i<lrc_map.size();i++){  
             LyricObject lrcStrLength=lrc_map.get(i);  
             if(max<lrcStrLength.lrc.length()){  
                 max=lrcStrLength.lrc.length();  
             }  
         }  
         
       
        //SIZEWORD=WidgetWidth/max-5; 
         SIZEWORD=WidgetWidth*4/5/WORDNUM-5; 
    }  
      
    
   
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        mX = w * 0.5f;  
        super.onSizeChanged(w, h, oldw, oldh);  

        WidgetWidth=this.getWidth();
        WidgetHeight=this.getHeight();
        
        /*
         * ���ݸ����������Ǿ���ȷ���������Ĵ�С 
         */
        SetTextSize();
       
    }  
      
    /** 
     *  ��ʹ������ٶ� 
     *  
     * @return ���ظ�ʹ������ٶ� 
     */  
    public Float SpeedLrc(){  
        float speed=0;  
        if(offsetY+(SIZEWORD+INTERVAL)*lrcIndex>320){  
            speed=((offsetY+(SIZEWORD+INTERVAL)*lrcIndex-320)/20);  
  
        } else if(offsetY+(SIZEWORD+INTERVAL)*lrcIndex < 120){  
           //Log.i("speed", "speed is too fast!!!");  
            speed = 0;  
        }  
//      if(speed<0.2){  
//          speed=0.2f;  
//      }  
        return speed;  
    }  
      
    /** 
     * ����ǰ�ĸ����Ĳ���ʱ�䣬�Ӹ����������һ�� 
     * @param time ��ǰ�����Ĳ���ʱ�� 
     * @return ���ص�ǰ��ʵ�����ֵ 
     */  
    public int SelectIndex(int time){  
        if(readlock)
        	return 0;
    	
    	if(!blLrc){  
            return 0;  
        }  
        int index=0;  
 	  
        for(int i=0;i<lrc_map.size();i++){  
            LyricObject temp=lrc_map.get(i);  
           
            if(temp.begintime<time){  
                ++index;  
            }  
        }  
        lrcIndex=index-1;  
        if(lrcIndex<0){  
            lrcIndex=0;  
        }
        
        /*
         * ������ʾ
         */
       lrcIndex_s=-1;
       if(lrcIndex>0)
       {
    	   LyricObject temp1=lrc_map.get(lrcIndex);
    	   LyricObject temp2=lrc_map.get(lrcIndex-1);   	   
    	   if(temp1.begintime==temp2.begintime)
    		   lrcIndex_s=lrcIndex-1;
    	  
       }
       /*
        * ������ʾ
        */
        
        return lrcIndex;  
      
    }  
      
    /** 
     * ��ȡ����ļ� 
     * @param file ��ʵ�·�� 
     *  
     */  
    public static void read(String file) {  
    	readlock=true;
    	
    	TreeMap<Integer, LyricObject> lrc_read =new TreeMap<Integer, LyricObject>();  
        String data = "";  
        try {  
          File saveFile=new File(file);  
         // System.out.println("�Ƿ��и���ļ�"+saveFile.isFile());  
          if(!saveFile.isFile()){  
              blLrc=false;  
              return;  
          }  
          blLrc=true;  
            
          //System.out.println("bllrc==="+blLrc);  
          FileInputStream stream = new FileInputStream(saveFile);//  context.openFileInput(file);  
            
            
          BufferedReader br = new BufferedReader(new InputStreamReader(stream,"utf-8"));     
          int i = 0;  
          Pattern pattern = Pattern.compile("\\d{2}");  
          while ((data = br.readLine()) != null) {     
             // System.out.println("++++++++++++>>"+data);  
                data = data.replace("[","");//��ǰ����滻�ɺ����  
                data = data.replace("]","@");  
                String splitdata[] =data.split("@");//�ָ�  
                if(data.endsWith("@")){  
                    for(int k=0;k<splitdata.length;k++){  
                        String str=splitdata[k];  
                          
                        str = str.replace(":",".");  
                        str = str.replace(".","@");  
                        String timedata[] =str.split("@");  
                        Matcher matcher = pattern.matcher(timedata[0]);  
                        if(timedata.length==3 && matcher.matches()){  
                            int m = Integer.parseInt(timedata[0]);  //��  
                            int s = Integer.parseInt(timedata[1]);  //��  
                            int ms = Integer.parseInt(timedata[2]); //����  
                            int currTime = (m*60+s)*1000+ms*10;  
                            LyricObject item1= new LyricObject();  
                            item1.begintime = currTime;  
                            item1.lrc       = "";  
                            lrc_read.put(currTime,item1);  
                        }  
                    }  
                      
                }  
                else{  
                    String lrcContenet = splitdata[splitdata.length-1];   
              
                    for (int j=0;j<splitdata.length-1;j++)  
                    {  
                        String tmpstr = splitdata[j];  
                          
                        tmpstr = tmpstr.replace(":",".");  
                        tmpstr = tmpstr.replace(".","@");  
                        String timedata[] =tmpstr.split("@");  
                        Matcher matcher = pattern.matcher(timedata[0]);  
                        if(timedata.length==3 && matcher.matches()){  
                            int m = Integer.parseInt(timedata[0]);  //��  
                            int s = Integer.parseInt(timedata[1]);  //��  
                            int ms = Integer.parseInt(timedata[2]); //����  
                            int currTime = (m*60+s)*1000+ms*10;  
                            LyricObject item1= new LyricObject();  
                            item1.begintime = currTime;  
                            item1.lrc       = lrcContenet;  
                            lrc_read.put(currTime,item1);// ��currTime����ǩ  item1������ ����TreeMap��  
                            i++;  
                        }  
                    }  
                }  
                  
          }   
         stream.close();  
        }  
        catch (FileNotFoundException e) {  
        }  
        catch (IOException e) {  
        }  
          
        /* 
         * ����hashmap ����ÿ��������Ҫ��ʱ�� 
        */  
        lrc_map.clear();  
        data ="";  
        Iterator<Integer> iterator = lrc_read.keySet().iterator();  
        LyricObject oldval  = null;  
        int i =0;  
        while(iterator.hasNext()) {  
            Object ob =iterator.next();  
              
            LyricObject val = (LyricObject)lrc_read.get(ob);  
              
            if (oldval==null)  
                oldval = val;  
            else  
            {  
                LyricObject item1= new LyricObject();
                
                item1  = oldval;  
                item1.timeline = val.begintime-oldval.begintime;
               
                //if(item1.getLrc().length()>WORDNUM)
                if(CalStrLength(item1.getLrc())>2*WORDNUM)          
                {
                	LyricObject item2= new LyricObject(oldval); 
                	item2.timeline = val.begintime-oldval.begintime;
                	item2.timeline = val.begintime-oldval.begintime;
                	
                	int StrIndex=getStrIndex(item1.getLrc(),WORDNUM*2);
                	item2.setLrc(item1.getLrc().substring(0, StrIndex));
                	item1.setLrc(item1.getLrc().substring(StrIndex));
                	
                	//item2.setLrc(item1.getLrc().substring(0, WORDNUM));
                	//item1.setLrc(item1.getLrc().substring(WORDNUM));
                	lrc_map.put(new Integer(i), item2);  
                    i++;  
                }
              
                
               
                lrc_map.put(new Integer(i), item1);  
                i++;  
                oldval = val;  
            }  
            if (!iterator.hasNext()) {  
                lrc_map.put(new Integer(i), val);  
            }  
              
        }  
        readlock=false;
      
        	
  
    }     
      
    
    
    /**
     * �����ַ�������,������2����Ӣ���ַ���1��
     * @param str
     * @return
     */
    public static int CalStrLength(String str)
    {
    	
    	int m = 0;  
        char arr[] = str.toCharArray();  
        for(int i=0;i<arr.length;i++)  
        {  
            char c = arr[i];  
            if((c >= 0x0391 && c <= 0xFFE5))  //�����ַ�  
            {  
                m = m + 2;  
            }  
            else if((c>=0x0000 && c<=0x00FF)) //Ӣ���ַ�  
            {  
                m = m + 1;  
            }  
        }  
        return m;  
    }  
    	
    
    /**
     * ��ȡָ���ַ���str�ضϵ��λ��
     * @param str ָ���ַ���
     * @param Length ָ���ضϳ���
     * @return �ضϵ�λ��
     */
    public static int getStrIndex(String str,int Length)  
    {  
        int m = 0;  
        int index=0;
        
        char arr[] = str.toCharArray();  
        for(index=0;index<arr.length;index++)  
        {  
            char c = arr[index];  
            if((c >= 0x0391 && c <= 0xFFE5))  //�����ַ�  
            {  
                m = m + 2;  
            }  
            else if((c>=0x0000 && c<=0x00FF)) //Ӣ���ַ�  
            {  
                m = m + 1;  
            }  
            
            if(m>Length)	
            	break;
            
        }  
       
        
        return index;  
    }  
    
    
    
    
    
    
    
    /** 
     * @return the blLrc 
     */  
    public static boolean isBlLrc() {  
        return blLrc;  
    }  
  
    /** 
     * @return the offsetY 
     */  
    public float getOffsetY() {  
        return offsetY;  
    }  
  
    /** 
     * @param offsetY the offsetY to set 
     */  
    public void setOffsetY(float offsetY) {  
        this.offsetY = offsetY;  
    }  
  
    /** 
     * @return ���ظ�����ֵĴ�С 
     */  
    public int getSIZEWORD() {  
        return SIZEWORD;  
    }  
  
    /** 
     * ���ø�����ֵĴ�С 
     * @param sIZEWORD the sIZEWORD to set 
     */  
    public void setSIZEWORD(int sIZEWORD) {  
        SIZEWORD = sIZEWORD;  
    }  
}  