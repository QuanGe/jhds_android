package com.quange.jhds;


import android.app.Application;
import android.util.DisplayMetrics;

public class AppCommon extends Application {
	public static AppCommon appCommon;
	public static int statusHeight;
	public static DisplayMetrics metrics;
	public static AppSetManager appset;
	public static int screenHeight;
	public static int screenWidth;
	
	// 单例模式中获取唯一的MyApplication实例
    public static AppCommon getInstance(){
        if (null == appCommon){
        	appCommon = new AppCommon();
        }
        return appCommon;
    }
    
	@Override
	public void onCreate() {
		super.onCreate();
		appCommon = this;
		AppSetManager.initialize(this); 
	}
}
