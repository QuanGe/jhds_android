package com.quange.viewModel;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.quange.model.JHDSCopyModel;
import com.quange.model.JHDSLearnModel;
import com.quange.model.JHDSShopModel;

public class JHDSAPIManager {
	private static  JHDSAPIManager sharedInstance;
	private RequestQueue requestQueue;
	private static Context theContext;
	private  JHDSAPIManager(final Context context) {
    	theContext = context;
    	requestQueue = getRequestQueue();
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
    
    
}
