package com.quange.jhds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.http.util.EncodingUtils;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
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
		initImageLoader();
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
				.cacheInMemory(true).cacheOnDisc(true).displayer(new RoundedBitmapDisplayer(5))
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	
	public void saveLineData(String data)
	{
		
		PrintWriter writer = null;
		try {
			String savePath = getSDPath()+"/jhds/drawData/lines";
			 File file = new File(savePath);  
			 int end = savePath.lastIndexOf(File.separator);  
			 String _filePath = savePath.substring(0, end);  
			 File filePath = new File(_filePath);  
			 if (!filePath.exists()) {  
			     filePath.mkdirs();  
			 }  
	        
	        writer = new PrintWriter(file);
			writer.write(data);
			writer.flush();
			
		} catch (Exception ee) {
			ee.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception eee) {
			}
		}
	}
	
	public String readLineData()
	{
		String savePath = getSDPath()+"/jhds/drawData/lines";
		
        return readFileSdcard(savePath); 
		
		
	}
	
	
	 /** 
     * 获取SDK路径 
     * @return 
     */  
    public String getSDPath(){   
           File sdDir = null;   
           boolean sdCardExist = Environment.getExternalStorageState()     
                               .equals(android.os.Environment.MEDIA_MOUNTED);   //判断sd卡是否存在   
           if   (sdCardExist)     
           {                                 
             sdDir = Environment.getExternalStorageDirectory();//获取跟目录   
          }     
           return sdDir.toString();   
             
    }  
    
    /**
     * 一、私有文件夹下的文件存取（/data/data/包名/files）
     * 
     * @param fileName
     * @param message
     */ 
    public void writeFileData(String fileName, String message) { 
        try { 
            FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE); 
            byte[] bytes = message.getBytes(); 
            fout.write(bytes); 
            fout.close(); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
    } 
 
    /**
     * //读文件在./data/data/包名/files/下面
     * 
     * @param fileName
     * @return
     */ 
    public String readFileData(String fileName) { 
        String res = ""; 
        try { 
            FileInputStream fin = openFileInput(fileName); 
            int length = fin.available(); 
            byte[] buffer = new byte[length]; 
            fin.read(buffer); 
            res = EncodingUtils.getString(buffer, "UTF-8"); 
            fin.close(); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return res; 
    } 
 
    /**
     * 写， 读sdcard目录上的文件，要用FileOutputStream， 不能用openFileOutput
     * 不同点：openFileOutput是在raw里编译过的，FileOutputStream是任何文件都可以
     * @param fileName
     * @param message
     */ 
    // 写在/mnt/sdcard/目录下面的文件 
    public void writeFileSdcard(String fileName, String message) { 
        try { 
            // FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE); 
            FileOutputStream fout = new FileOutputStream(fileName); 
            byte[] bytes = message.getBytes(); 
            fout.write(bytes); 
            fout.close(); 
        } 
        catch (Exception e) { 
            e.printStackTrace(); 
        } 
    } 
    // 读在/mnt/sdcard/目录下面的文件 
    public String readFileSdcard(String fileName) { 
        String res = ""; 
        try { 
            FileInputStream fin = new FileInputStream(fileName); 
            int length = fin.available(); 
            byte[] buffer = new byte[length]; 
            fin.read(buffer); 
            res = EncodingUtils.getString(buffer, "UTF-8"); 
            fin.close(); 
        } 
        catch (Exception e) { 
            //e.printStackTrace(); 
        } 
        return res; 
    } 
 
    /**
     * 二、从resource中的raw文件夹中获取文件并读取数据（资源文件只能读不能写）
     * 
     * @param fileInRaw
     * @return
     */ 
    public String readFromRaw(int fileInRaw) { 
        String res = ""; 
        try { 
            InputStream in = getResources().openRawResource(fileInRaw); 
            int length = in.available(); 
            byte[] buffer = new byte[length]; 
            in.read(buffer); 
            res = EncodingUtils.getString(buffer, "GBK"); 
            // res = new String(buffer,"GBK"); 
            in.close(); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return res; 
    } 
 
    /**
     * 三、从asset中获取文件并读取数据（资源文件只能读不能写）
     * 
     * @param fileName
     * @return
     */ 
    public String readFromAsset(String fileName) { 
        String res = ""; 
        try { 
            InputStream in = getResources().getAssets().open(fileName); 
            int length = in.available(); 
            byte[] buffer = new byte[length]; 
            in.read(buffer); 
            res = EncodingUtils.getString(buffer, "UTF-8"); 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
        return res; 
    } 
    
    /**
     * 检测是否安装淘宝客户端
     */
    public boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    /**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return - true 网络连接 - false 网络连接异常
	 */
	public static boolean isConnect(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected() && info.isAvailable()) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 判断网络是否是Wifi
	 * 
	 * @param context
	 * @return - true 网络连接是Wifi - false 网络连接不是Wifi
	 */
	public static boolean isWifi(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo activeNetInfo = connectivity.getActiveNetworkInfo();
				if (activeNetInfo != null && activeNetInfo.isConnected() && activeNetInfo.isAvailable()) {
					if (activeNetInfo.getState() == NetworkInfo.State.CONNECTED) {
						if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
							return true;
						}
					}
				}
				return false;
			}
		} catch (Exception e) {
		}
		return false;
	}
}
