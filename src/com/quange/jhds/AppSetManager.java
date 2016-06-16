package com.quange.jhds;

import android.app.Application;
import android.content.SharedPreferences;

public class AppSetManager {
	private static SharedPreferences setPref = null;

	public static final String AboutRedTipNoti = "com.quange.ABOUT_RED_TIP_NOTI";
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
	
	public static void saveSplashImgUrl(String url)
	{
		setPref.edit().putString("splashImgUrl", url).commit();
	}
	
	public static String getSplashImgUrl()
	{
		return setPref.getString("splashImgUrl", "");
	}
	
	public static void saveSplashType(int type)
	{
		setPref.edit().putInt("splashType", type).commit();
	}
	
	public static int getSplashType() {
		return setPref.getInt("splashType", -1);
	}
	
	public static void saveSplashDetail(String detail)
	{
		setPref.edit().putString("splashDetail", detail).commit();
	}
	
	public static String getSplashDetail()
	{
		return setPref.getString("splashDetail", "");
	}
	
	public static String getNewAppVersion()
	{
		return setPref.getString("newAppVersion", "");
	}
	
	public static void setNewAppVersion(String nav)
	{
		setPref.edit().putString("newAppVersion", nav).commit();
	}
	
	public static String getNewShopTag()
	{
		return setPref.getString("newShopTag", "");
	}
	
	public static void setNewShopTag(String nst)
	{
		setPref.edit().putString("newShopTag", nst).commit();
	}
	
	public static String getNewMessageTag()
	{
		return setPref.getString("newMessageTag", "");
	}
	
	public static void setNewMessageTag(String nmt)
	{
		setPref.edit().putString("newMessageTag", nmt).commit();
	}
	
	public static String getNewProtectBabyTag()
	{
		return setPref.getString("newProtectBabyTag", "");
	}
	
	public static void setNewProtectBabyTag(String npbt)
	{
		setPref.edit().putString("newProtectBabyTag", npbt).commit();
	}
	
	public static String getOldShopTag()
	{
		return setPref.getString("oldShopTag", "");
	}
	
	public static void setOldShopTag(String ost)
	{
		setPref.edit().putString("oldShopTag", ost).commit();
	}
	
	public static String getOldMessageTag()
	{
		return setPref.getString("oldMessageTag", "");
	}
	
	public static void setOldMessageTag(String omt)
	{
		setPref.edit().putString("oldMessageTag", omt).commit();
	}
	
	public static String getOldProtectBabyTag()
	{
		return setPref.getString("oldProtectBabyTag", "");
	}
	
	public static void setOldProtectBabyTag(String omt)
	{
		setPref.edit().putString("oldProtectBabyTag", omt).commit();
	}
}
