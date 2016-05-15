package com.quange.jhds;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

public class AppCommon extends Application {
	public static AppCommon appCommon;
	public static int statusHeight;
	public static DisplayMetrics metrics;
	public static AppSetManager appset;
	public static int screenHeight;
	public static int screenWidth;
	public static ImageLoader imageLoader = ImageLoader.getInstance();
	public static DisplayImageOptions options;
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
		
		new Thread() {
			public void run() {
				initImageLoader(getApplicationContext());
			}
		}.start();
		 
		metrics = this.getApplicationContext().getResources().getDisplayMetrics(); 
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels; 
	}
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option,
		// you may tune some of them, or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this); method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
//				.writeDebugLogs() // Remove for release app
				.threadPoolSize(5)
//				.memoryCacheExtraOptions(512, 341)
				.build();

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);

	}
	
	void initImageLoader() {
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.n)
				.showImageForEmptyUri(R.drawable.n).showImageOnFail(R.drawable.n)
				.cacheInMemory(true).cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(20))
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}
}
