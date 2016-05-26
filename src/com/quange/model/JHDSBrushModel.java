package com.quange.model;

import java.util.ArrayList;

import android.graphics.PointF;

public class JHDSBrushModel {
	public int brushColor;
	public int brushwidth;
	public static class JHDSBrushLineModel {
		public ArrayList<PointF> points = new ArrayList<PointF>();		
	}
	public ArrayList<JHDSBrushLineModel> lines = new ArrayList<JHDSBrushLineModel>();
	
	
}
