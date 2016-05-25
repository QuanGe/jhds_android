package com.quange.views;

import java.util.ArrayList;
import java.util.List;

import com.quange.jhds.AppSetManager;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class BrushView extends View {
	private float lastx = 0;
	private float lasty = 0;
	private List<Path> pathList = new ArrayList<Path>();
	private List<Paint> brushList = new ArrayList<Paint>();
	//paths //lines //points
	private ArrayList<ArrayList<ArrayList<PointF>>> mDrawing = new ArrayList<ArrayList<ArrayList<PointF>>>();
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
	
	public void backToFront()
	{
		
		if(pathList.size()>0)
		{
			
		 	pathList.get(pathList.size()-1).reset();
			ArrayList<ArrayList<PointF>> lines = mDrawing.get(mDrawing.size()-1);
			
			
			lines.remove(lines.size() -1);
			for(int i = 0;i<lines.size();i++)
			{
				ArrayList<PointF> points = lines.get(i);
				pathList.get(pathList.size()-1).moveTo(points.get(0).x,points.get(0).y);
				for(int j =1;j<points.size();j++)
				{
					pathList.get(pathList.size()-1).lineTo(points.get(j).x,points.get(j).y);
				}
			}
			if(lines.size() ==0)
			{
				pathList.remove(pathList.size()-1);
				brushList.remove(brushList.size()-1);
				mDrawing.remove(mDrawing.size()-1);
			}
			
			
			postInvalidate();
		}
		else
			clearAll();
		
		Toast.makeText(this.getContext(), "已经后退至上一次落笔处", Toast.LENGTH_SHORT).show();
	}
	
	public void setEnable(boolean e)
	{
		this.enable =e;
	}

	public void clearAll(){
		pathList.clear();
		brushList.clear();
	
		mDrawing.clear();
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
		ArrayList<ArrayList<PointF>> lines = new ArrayList<ArrayList<PointF>>();
		mDrawing.add(lines);
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
		ArrayList<ArrayList<PointF>> lines = new ArrayList<ArrayList<PointF>>();
		mDrawing.add(lines);
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
				ArrayList<PointF> line = new ArrayList<PointF>();
				line.add(new PointF(pointX,pointY));
				mDrawing.get(mDrawing.size()-1).add(line);
				pathList.get(pathList.size()-1).moveTo(pointX, pointY);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if(enable)
			{
				lastx = pointX;
				lasty = pointY;
				ArrayList<ArrayList<PointF>> path  =  mDrawing.get(mDrawing.size()-1);
				path.get(path.size()-1).add(new PointF(pointX,pointY));
				pathList.get(pathList.size()-1).lineTo(pointX, pointY);
			}
			break;
		case MotionEvent.ACTION_UP:
			if(lastx==pointX &&lasty == pointY &&enable)
			{
				ArrayList<ArrayList<PointF>> path  =  mDrawing.get(mDrawing.size()-1);
				path.get(path.size()-1).add(new PointF(pointX,pointY));
				path.get(path.size()-1).add(new PointF(pointX+1, pointY));
				path.get(path.size()-1).add(new PointF(pointX+1, pointY+1));
				path.get(path.size()-1).add(new PointF(pointX, pointY+1));
				path.get(path.size()-1).add(new PointF(pointX, pointY));
				path.get(path.size()-1).add(new PointF(pointX+1, pointY));
			
				
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
			return true;
		}
		// Force a view to draw.
		postInvalidate();
		return true;

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