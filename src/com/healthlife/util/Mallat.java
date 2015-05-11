package com.healthlife.util;

public class Mallat {
	public static double analyzeArray[] = new double [200];
	public static double h[] = {.332670552950, .806891509311, .459877502118, -.135011020010,   
        -.085441273882, .035226291882};  
	public static double g[] = {.035226291882, .085441273882, -.135011020010, -.459877502118,  
        .806891509311, -.332670552950};
	//ѭ�����
	//inputΪ�����źţ�outputΪ����źţ�nΪinput����
	public static double[] Analyze(double input[],  int n)

	{
		
		//С��3��ֽ�
		//flag[i]��ʾ��i��ֽ�������������е���ʼλ��
		int flag =n;
		double [][]v1 = new double [4][n];
		for(int i=0;i<n;i++)
		{
			v1[0][i]=input[i];
		}
		for(int i=1;i<4;i++)
		{
			flag=flag/2;
			for(int j=0;j<flag;j++)
			{
				//�����ʼ��
				v1[i][j] = 0;
				analyzeArray[flag+j]=0;
				for(int k=0;k<6;k++)
				{
					int temp = k+j*2;
					if(temp>=flag*2)
					{
						temp = temp-flag*2;
					}
//					if(temp<0)
//					{
//						v1[i][j] += h[k]*v1[i-1][flag*2+temp];
//						analyzeArray[flag+j] += g[k]*v1[i-1][flag*2+temp];
//					}
//					else
					{
						v1[i][j] += h[k]*v1[i-1][temp];
						analyzeArray[flag+j] += g[k]*v1[i-1][temp];
					}
				}
			}
		}
		//���ֽ������ݴ���analyzeArray�У�ǰn/8Ϊv3��֮������Ϊw3,w2,w1
		for(int i=0;i<flag;i++)
		{
			analyzeArray[i] = v1[3][i];
			
		}
		
		double []output = new double [n];
		double [][]v = new double [4][n];
		flag = n/8;
//		for(int i=0;i<n;i++)
//		{
//			Log.v("wwwwwwwwwwwvvvvvv",""+analyzeArray[i]);
//		}
		for(int i=0;i<flag;i++)
		{
			v[0][i]=analyzeArray[i];
		}
		for(int i=1;i<4;i++)
		{
			for(int j=0;j<flag;j++)
			{
				v[i][2*j]=0;
				v[i][2*j+1]=0;
				for(int k=0;k<3;k++)
				{
					int temp = j-k;
					if(temp<0)
					{
						temp = temp +flag;
					}
					v[i][2*j] = v[i][2*j]+h[2*k]*v[i-1][temp]+g[2*k]*analyzeArray[flag+temp] ;
					v[i][2*j+1] = v[i][2*j+1]+h[2*k+1]*v[i-1][temp]+g[2*k+1]*analyzeArray[flag+temp];
					
				}
//				Log.v("1vvvvvvvvvvvvvvvv",""+v[i][2*j]);
//				Log.v("1vvvvvvvvvvvvvvvv",""+v[i][2*j+1]);
			}
			flag = flag*2;
			
		}
		output = v[3];
		return output;
	}
}

