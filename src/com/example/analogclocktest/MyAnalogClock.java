package com.example.analogclocktest;

import java.util.Calendar;
import java.util.TimeZone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class MyAnalogClock extends View{

	private Bitmap mBmpDial;
	private Bitmap mBmpHour;
	private Bitmap mBmpMinute;
	private Bitmap mBmpSecond;
	private BitmapDrawable bmdHour;
	private BitmapDrawable bmdMinute;
	private BitmapDrawable bmdSecond;
	private BitmapDrawable bmdDial;
	private Paint mPaint;
	private Handler tickHandler;
	private int mWidth;
	private int mHeight;
	private int mTempWidth=bmdSecond.getIntrinsicWidth();
	private int mTempHeight;
	private int centerX;
	private int centerY;
	private int availableWidth=100;
	private int availableHeight=100;
	private String sTimeZoneString;
	public MyAnalogClock(Context context){
		
		this(context,"GMT+8:00");
		//super(context);
	}
	public MyAnalogClock(Context context,AttributeSet attrs,int defStyle){
		//super(context,attrs,defStyle);
		this(context,"GMT+8:00");
	}
	public MyAnalogClock(Context context,AttributeSet attr) {
		// TODO 自动生成的构造函数存根
		this(context,"GMT+8:00");
	}
	public MyAnalogClock(Context context,String sTimeZone){
		super(context);
		this.sTimeZoneString=sTimeZone;
		mBmpHour=BitmapFactory.decodeResource(getResources(), R.drawable.hour);
		//bmdHour=new BitmapDrawable(mBmpHour);
		bmdHour=new BitmapDrawable(getResources(),mBmpHour);
		mBmpMinute=BitmapFactory.decodeResource(getResources(), R.drawable.minute);
		//bmdMinute=new BitmapDrawable(mBmpMinute);
		bmdMinute=new BitmapDrawable(getResources(),mBmpMinute);
		mBmpSecond=BitmapFactory.decodeResource(getResources(), R.drawable.second);
		//bmdSecond=new BitmapDrawable(mBmpSecond);
		bmdSecond=new BitmapDrawable(getResources(),mBmpSecond);
		mBmpDial=BitmapFactory.decodeResource(getResources(), R.drawable.clockbg);
		//bmdDial=new BitmapDrawable(mBmpDial);
		bmdDial=new BitmapDrawable(getResources(),mBmpDial);
		mWidth=mBmpDial.getWidth();
		mHeight=mBmpDial.getHeight();
		centerX=availableWidth/2;
		centerY=availableHeight/2;
		mPaint=new Paint();
		mPaint.setColor(Color.BLUE);
		run();
	}
	public void run(){
		tickHandler=new Handler();
		tickHandler.post(tickRunnable);
	}
	private Runnable tickRunnable =new Runnable(){
		@Override
		public void run(){
			postInvalidate();
			tickHandler.postDelayed(tickRunnable, 1000);
		}
	};
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		Calendar calendar=Calendar.getInstance(TimeZone.getTimeZone(sTimeZoneString));
		int hour=calendar.get(Calendar.HOUR);
		int minute=calendar.get(Calendar.MINUTE);
		int second=calendar.get(Calendar.SECOND);
		float hourRotate=hour*30.0f+minute/60.0f*30.0f;
		float minuteRotate=minute*6.0f;
		float secondRotate=second*6.0f;
		boolean scaled=false;
		if(availableWidth<mWidth||availableHeight<mHeight){
			scaled=true;
			float scale=Math.min((float)availableWidth/(float)mWidth,(float)availableHeight/(float)mHeight);
			canvas.save();
			canvas.scale(scale, scale,centerX,centerY);
		}
		
		bmdDial.setBounds(centerX-(mWidth/2),centerY-(mHeight/2),
				centerX+(mWidth/2),centerY+(mHeight/2));
		bmdDial.draw(canvas);
		
		mTempWidth=bmdHour.getIntrinsicWidth();
		mTempHeight=bmdHour.getIntrinsicHeight();
		canvas.save();
		canvas.rotate(hourRotate, centerX, centerY);
		
		bmdHour.setBounds(centerX-mTempWidth/2,centerY-mTempHeight/2 ,
				centerX+mTempWidth/2,centerY+mTempWidth/2);
		bmdHour.draw(canvas);
		
		canvas.restore();
		mTempWidth=bmdMinute.getIntrinsicWidth();
		mTempHeight=bmdMinute.getIntrinsicHeight();
		canvas.save();
		canvas.rotate(minuteRotate, centerX, centerY);
		bmdMinute.setBounds(centerX-mTempWidth/2, centerY-mTempHeight/2, 
				centerX+mTempWidth/2, centerY+mTempHeight/2);
		bmdMinute.draw(canvas);
		
		canvas.restore();
		mTempWidth=bmdSecond.getIntrinsicWidth();
		mTempHeight=bmdSecond.getIntrinsicHeight();
		canvas.rotate(secondRotate, centerX, centerY);
		bmdSecond.setBounds(centerX-mTempWidth/2, centerY-mTempHeight/2, 
				centerX+mTempWidth/2, centerY+mTempHeight/2);
		bmdSecond.draw(canvas);
		if(scaled){
			canvas.restore();
		}
	}
}
