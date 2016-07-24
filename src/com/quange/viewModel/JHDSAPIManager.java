package com.quange.viewModel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.quange.jhds.AppCommon;
import com.quange.jhds.AppSetManager;
import com.quange.model.JHDSCopyModel;
import com.quange.model.JHDSLearnModel;
import com.quange.model.JHDSMessageModel;
import com.quange.model.JHDSShopModel;
import com.quange.model.JHDSShareModel;
import com.umeng.analytics.MobclickAgent;

public class JHDSAPIManager {
	private static  JHDSAPIManager sharedInstance;
	private RequestQueue requestQueue;
	private static Context theContext;
	private  JHDSAPIManager(final Context context) {
    	theContext = context;
    	requestQueue = getRequestQueue();
	 }
	
	public Context getContextVar()
	{
		return theContext;
	}
	
	 public static synchronized JHDSAPIManager getInstance(Context context) {
	        if (sharedInstance == null) {
	        	sharedInstance = new JHDSAPIManager(context);
	        }
	        return sharedInstance;
	    }
	public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
        	requestQueue = Volley.newRequestQueue(theContext.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
    
    
    public void fetchSplashData(final Listener<String> listener, ErrorListener errorListener)
    {
    	StringRequest request = new StringRequest("http://quangelab.com/images/jhds/splash.txt", new Listener<String>() {
			public void onResponse(String body) {
				
				try {
					JSONObject jsObj = new JSONObject(body);
					
					String r = jsObj.getString("img");
					if(r.equals(AppSetManager.getSplashImgUrl()))
					{
						String localSplashUrl = AppCommon.getInstance().getSplashLocalUrl(r);
						Bitmap b = AppCommon.getInstance().getLoacalBitmap(localSplashUrl,AppCommon.getInstance().screenWidth,AppCommon.getInstance().screenHeight);
						if(b != null)
						{
							return;
						}
						else
						{
							saveSplashImg(r);
						}
						
					}
					else
					{
						String detail = jsObj.getString("detail");
						int type = jsObj.getInt("type");
						AppSetManager.saveSplashImgUrl(r);
						AppSetManager.saveSplashDetail(detail);
						AppSetManager.saveSplashType(type);
						saveSplashImg(r);
						
					}
					listener.onResponse(body);
					
				} catch (Exception e) {
					System.out.println("caocaocaocaocao");
				}
				
				
				
			}
		},errorListener);
		addToRequestQueue(request);
    }
    
    public void saveSplashImg(String url)
    {
    	final String savePath = AppCommon.getInstance().getSplashLocalUrl(url);
     
		ImageRequest ir = new ImageRequest(url,new Listener<Bitmap>() {
			public void onResponse(Bitmap body) {
				MobclickAgent.onEvent(theContext, "splash_img_load");
				 BufferedOutputStream os = null;  
			       
			        try {  
			            File file = new File(savePath);  
			            // String _filePath_file.replace(File.separatorChar +  
			            // file.getName(), "");  
			            int end = savePath.lastIndexOf(File.separator);  
			            String _filePath = savePath.substring(0, end);  
			            File filePath = new File(_filePath);  
			            if (!filePath.exists()) {  
			                filePath.mkdirs();  
			            }  
			            file.createNewFile();  
			            os = new BufferedOutputStream(new FileOutputStream(file));  
			            body.compress(Bitmap.CompressFormat.PNG, 100, os);  
			        }catch (IOException e) {  
	                	System.out.println(e.getMessage());
	                	
	                }finally {  
			            if (os != null) {  
			                try {  
			                	os.flush();
			                    os.close();  
			                    
			                    theContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri
			                            .parse("file://" + savePath)));
			                    
			                } catch (IOException e) {  
			                	System.out.println(e.getMessage());
			                	
			                }  
			            }  
			        }  
			}
		} , 0, 0,ScaleType.CENTER_INSIDE, Config.RGB_565, null);
		addToRequestQueue(ir);
		
    }
    
    public void fetchCopyPageNum(int type,final Listener<String> listener, ErrorListener errorListener)
    {
    	StringRequest request = new StringRequest("http://quangelab.com/images/jhds/copy_"+type+".txt", new Listener<String>() {
			public void onResponse(String body) {
				listener.onResponse(body);
			}
		},errorListener);
		addToRequestQueue(request);
    	
    }
    
    public void fetchCopyList(int pageNum,int type,final Listener<List<JHDSCopyModel>> listener, ErrorListener errorListener)
    {
    	
    	StringRequest request = new StringRequest("http://quangelab.com/images/jhds/copy_"+type+"_"+pageNum+".txt", new Listener<String>() {
			public void onResponse(String body) {
				
				List<JHDSCopyModel> result = new Gson().fromJson(body,
						new TypeToken<List<JHDSCopyModel>>() {
						}.getType());
					
				listener.onResponse(result);
				
			}
		},errorListener);
		addToRequestQueue(request);
    	
    }
    
    public void fetchLearnPageNum(int type,final Listener<String> listener, ErrorListener errorListener)
    {
    	
    	StringRequest request = new StringRequest("http://quangelab.com/images/jhds/learn_"+type+".txt", new Listener<String>() {
			public void onResponse(String body) {
				listener.onResponse(body);
			}
		},errorListener);
		addToRequestQueue(request);
    	
    }
    
    public void fetchLearnList(int pageNum,int type,final Listener<List<JHDSLearnModel>> listener, ErrorListener errorListener)
    {
    	
    	StringRequest request = new StringRequest("http://quangelab.com/images/jhds/learn_"+type+"_"+pageNum+".txt", new Listener<String>() {
			public void onResponse(String body) {
				
				List<JHDSLearnModel> result = new Gson().fromJson(body,
						new TypeToken<List<JHDSLearnModel>>() {
						}.getType());
					
				listener.onResponse(result);
				
			}
		},errorListener);
		addToRequestQueue(request);
    	
    }
    
    public void fetchShopPageNum(int type,final Listener<String> listener, ErrorListener errorListener)
    {
    	StringRequest request = new StringRequest("http://quangelab.com/images/jhds/shop_"+type+".txt", new Listener<String>() {
			public void onResponse(String body) {
				listener.onResponse(body);
			}
		},errorListener);
		addToRequestQueue(request);
    	
    }
    
    public void fetchShopList(int pageNum,int type,final Listener<List<JHDSShopModel>> listener, ErrorListener errorListener)
    {
    	StringRequest request = new StringRequest("http://quangelab.com/images/jhds/shop_"+type+"_"+pageNum+".txt", new Listener<String>() {
			public void onResponse(String body) {
				
				List<JHDSShopModel> result = new Gson().fromJson(body,
						new TypeToken<List<JHDSShopModel>>() {
						}.getType());
					
				listener.onResponse(result);
				
			}
		},errorListener);
		addToRequestQueue(request);
    	
    }
    
    public void fetchWeiboPageNum(final Listener<String> listener, ErrorListener errorListener)
    {
    	StringRequest request = new StringRequest("http://quangelab.com/images/jhds/weibo/weibo_num.txt", new Listener<String>() {
			public void onResponse(String body) {
				listener.onResponse(body);
			}
		},errorListener);
		addToRequestQueue(request);
    	
    }
    
    public void fetchWeiboList(int pageNum,final Listener<List<JHDSShareModel>> listener, ErrorListener errorListener)
    {
    	StringRequest request = new StringRequest("http://quangelab.com/images/jhds/weibo/weibo_"+pageNum+".txt", new Listener<String>() {
			public void onResponse(String body) {
				
				List<JHDSShareModel> result = new Gson().fromJson(body,
						new TypeToken<List<JHDSShareModel>>() {
						}.getType());
				listener.onResponse(result);
			}
		},errorListener);
		addToRequestQueue(request);
    	
    }
    
    
    public void fetchMessageTag( final Listener<String> listener, ErrorListener errorListener)
    {
    	StringRequest request = new StringRequest("http://quangelab.com/images/jhds/messageTag.txt", new Listener<String>() {
			public void onResponse(String body) {
				
				
				
				try {
					JSONObject jsObj = new JSONObject(body);
					
					String newAppVersion = jsObj.getString("appVersion");
					String newShopTag = jsObj.getString("shopTag");
					String newMessageTag = jsObj.getString("messageTag");
					String newProtectBabyTag = jsObj.getString("protectBabyTag");
						
					AppSetManager.setNewAppVersion(newAppVersion);
					AppSetManager.setNewShopTag(newShopTag);
					AppSetManager.setNewMessageTag(newMessageTag);
					AppSetManager.setNewProtectBabyTag(newProtectBabyTag);
						
					
					listener.onResponse(body);
					
				} catch (Exception e) {
					System.out.println("caocaocaocaocao");
				}
				
			}
		},errorListener);
		addToRequestQueue(request);
    	
    }
    
    public void fetchWeiboTag( final Listener<String> listener, ErrorListener errorListener)
    {
    	StringRequest request = new StringRequest("http://quangelab.com/images/jhds/weibo/weiboTag.txt", new Listener<String>() {
			public void onResponse(String body) {
				
				
				
				try {
					JSONObject jsObj = new JSONObject(body);
					
					String weiboid = jsObj.getString("idstr");
					String weiboText = jsObj.getString("text");
					String weiboPics = jsObj.getString("pic_urls");
					
					String userIcon = jsObj.getString("userIcon");
					String nickName = jsObj.getString("nickName");
					String userId = jsObj.getString("userId");
					String original_pic = jsObj.getString("original_pic");
					
					AppSetManager.setTopWeiboId(weiboid);
					AppSetManager.setTopWeiboText(weiboText);
					AppSetManager.setTopWeiboPics(weiboPics);
				
					AppSetManager.setTopWeiboUserNickName(nickName);
					AppSetManager.setTopWeiboUserId(userId);
					AppSetManager.setTopWeiboUserIcon(userIcon);
					AppSetManager.setTopWeiboOrgPic(original_pic);
					
					listener.onResponse(body);
					
				} catch (Exception e) {
					System.out.println("caocaocaocaocao");
				}
				
			}
		},errorListener);
		addToRequestQueue(request);
    	
    }
    
    public void fetchMessageList( final Listener<List<JHDSMessageModel>> listener, ErrorListener errorListener)
    {
    	
    	StringRequest request = new StringRequest("http://quangelab.com/images/jhds/message.txt", new Listener<String>() {
			public void onResponse(String body) {
				List<JHDSMessageModel> result = new Gson().fromJson(body,
						new TypeToken<List<JHDSMessageModel>>() {
						}.getType());
					
				listener.onResponse(result);
			}
		},errorListener);
		addToRequestQueue(request);
    	
    }
    
    public void fetchProtectBabyList( final Listener<List<JHDSMessageModel>> listener, ErrorListener errorListener)
    {
    	
    	StringRequest request = new StringRequest("http://quangelab.com/images/jhds/protectBaby.txt", new Listener<String>() {
			public void onResponse(String body) {
				List<JHDSMessageModel> result = new Gson().fromJson(body,
						new TypeToken<List<JHDSMessageModel>>() {
						}.getType());
					
				listener.onResponse(result);
			}
		},errorListener);
		addToRequestQueue(request);
    	
    }
    
    
}
