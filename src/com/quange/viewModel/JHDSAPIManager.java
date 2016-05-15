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
    
    public void fetchCopyList(int pageNum,int type,final Listener<List<JHDSCopyModel>> listener, ErrorListener errorListener)
    {
    	StringRequest request = new StringRequest("http://www.dbmeinv.com/dbgroup/show.htm?pager_offset="+pageNum+"&cid="+type, new Listener<String>() {
			public void onResponse(String body) {
				ArrayList<JHDSCopyModel> result = new ArrayList<JHDSCopyModel>();
				
				
			    // if(href.contains("http://www.dbmeinv.com/dbgroup/") && target.equals("_topic_detail"))
				Document doc = Jsoup.parse(body);                    	
				Elements eles=doc.getElementsByTag("a");
		         for(Element e :eles)
		         {
		               System.out.println(e.text());
		               System.out.println(e.attr("href"));
		               if(e.attr("href") != null &&e.attr("target")!=null)
		               {
		            	   if(e.attr("href").contains("http://www.dbmeinv.com/dbgroup/") && e.attr("target").equals("_topic_detail"))
		            	   {
		            		   Elements images = e.getElementsByTag("img");
		            		   for(Element image :images)
			      		         {
		            			   JHDSCopyModel girl = new JHDSCopyModel();
		            			   girl.imageUrlStr = image.attr("src");
		            			   result.add(girl);
			      		         }
		            	   }
		               }
		         }
					
				listener.onResponse(result);
				
			}
		},errorListener);
		addToRequestQueue(request);
    	
    }
}
