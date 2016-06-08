package com.quange.jhds;

import android.app.Application;
import android.content.SharedPreferences;

public class AppSetManager {
	private static SharedPreferences setPref = null;

	public static void initialize(Application app) {
		if (setPref == null) {
			setPref = app.getSharedPreferences("appSetPref", Application.MODE_PRIVATE);
		}
	}
	
	public static int getFirstUseApp() {
		return setPref.getInt("firstUseApp", 1);
	}
	
	public static void setFirstUseApp(int firstUseApp)
	{
		setPref.edit().putInt("firstUseApp", firstUseApp).commit();
	}
	
	
	public static int getBrushWidth() {
		return setPref.getInt("brushWidth", 8);
	}
	
	public static void saveBrushWidth(int brushWidth) {
		setPref.edit().putInt("brushWidth", brushWidth).commit();
	}
	
	public static int getBrushColor() {
		return setPref.getInt("brushColor", 0xffEC0B5F);
	}
	
	public static void saveBrushColor(int brushColor) {
		setPref.edit().putInt("brushColor", brushColor).commit();
	}
}
