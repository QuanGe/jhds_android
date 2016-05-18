package com.quange.views;

import java.util.ArrayList;
import java.util.List;

import com.quange.jhds.AppSetManager;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import android.view.MotionEvent;
import android.view.View;


public class BrushView extends View {
	private float lastx = 0;
	private float lasty = 0;
	private List<Path> pathList = new ArrayList<Path>();
	private List<Paint> brushList = new ArrayList<Paint>();
	private int brushColor = AppSetManager.getBrushColor();
	private int brushwidth = AppSetManager.getBrushWidth();
	private boolean enable;
	public BrushView(Context context) {
		this(context, null);
		enable = true;
	}
	
	public int getBrushWidth()
	{
		return brushwidth;
	}
	
	public void setEnable(boolean e)
	{
		this.enable =e;
	}

	public void clearAll(){
		pathList.clear();
		brushList.clear();
		updateBrushColor(brushColor);
		// invalidate the view
		postInvalidate();
	}
	
	public void updateBrushWidth(boolean up)
	{
		
		brushwidth = brushwidth +(up?1:-1)*2;
		if(brushwidth<2)
		{
			brushwidth =2;
			return;
		}
		else if(brushwidth>16)
		{
			brushwidth = 16;
			return;
		}
		Path path = new Path();
		pathList.add(path);
		Paint brush = new Paint();
		brush.setAntiAlias(true);
		
		brush.setColor(brushColor);
		brush.setStyle(Paint.Style.STROKE);
		brush.setStrokeJoin(Paint.Join.ROUND);
		DisplayMetrics dm = new DisplayMetrics();  
		dm = getContext().getApplicationContext().getResources().getDisplayMetrics();  
		
		brush.setStrokeWidth(brushwidth*dm.density);
		
		brushList.add(brush);
		
		AppSetManager.saveBrushWidth(brushwidth);
	}
	public void updateBrushColor(int color)
	{
		brushColor = color;
		Path path = new Path();
		pathList.add(path);
		Paint brush = new Paint();
		brush.setAntiAlias(true);
		brush.setColor(brushColor);
		brush.setStyle(Paint.Style.STROKE);
		brush.setStrokeJoin(Paint.Join.ROUND);
		DisplayMetrics dm = new DisplayMetrics();  
		dm = getContext().getApplicationContext().getResources().getDisplayMetrics();  
		
		brush.setStrokeWidth(brushwidth*dm.density);
	
		brushList.add(brush);
		AppSetManager.saveBrushColor(color);
	
	}
	
	public int getBrushColor()
	{
		return brushColor;
	}
	public BrushView(Context context, AttributeSet attrs) {
		super(context, attrs);
		updateBrushColor(brushColor);
		enable = true;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float pointX = event.getX();
		float pointY = event.getY();

		// Checks for the event that occurs
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(enable)
			{
				lastx = pointX;
				lasty = pointY;
				pathList.get(pathList.size()-1).moveTo(pointX, pointY);
			}
			return true;
		case MotionEvent.ACTION_MOVE:
			if(enable)
			{
				lastx = pointX;
				lasty = pointY;
				pathList.get(pathList.size()-1).lineTo(pointX, pointY);
			}
			break;
		case MotionEvent.ACTION_UP:
			if(lastx==pointX &&lasty == pointY &&enable)
			{
				pathList.get(pathList.size()-1).lineTo(pointX+1, pointY);
				pathList.get(pathList.size()-1).lineTo(pointX+1, pointY+1);
				pathList.get(pathList.size()-1).lineTo(pointX, pointY+1);
				pathList.get(pathList.size()-1).lineTo(pointX, pointY);
				pathList.get(pathList.size()-1).lineTo(pointX+1, pointY);
			}
			
			
			break;
		case MotionEvent.ACTION_CANCEL:
			System.out.println("取消");
			break;
			
		default:
			return false;
		}
		// Force a view to draw.
		postInvalidate();
		return false;

	}
	@Override
	protected void onDraw(Canvas canvas) {
		
		canvas.drawColor(Color.WHITE); 
		if(!enable)
			return;
		for(int i = 0;i<pathList.size();i++)
		{
			
			canvas.drawPath(pathList.get(i), brushList.get(i));
		}
	}
	
	
}