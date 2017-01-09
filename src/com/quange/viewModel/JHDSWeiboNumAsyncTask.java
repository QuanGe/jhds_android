package com.quange.viewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.quange.jhds.AccessTokenKeeper;
import com.quange.jhds.SinaConstants;
import com.quange.model.JHDSShareModel;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.WeiboParameters;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

public class JHDSWeiboNumAsyncTask extends AsyncTask<String, Integer, String> {
	private TextView repostNumTv;
	private TextView commentNumTv;
	private TextView attributeNumTv;
	private String  weiboId;
	private Context context;
	private String weiboIds;
	private HashMap<String, JSONObject> weiboNums ;
	
	
	public JHDSWeiboNumAsyncTask(Context ct,TextView rpTv,TextView cmTv,TextView atTv,String wbid,String ids,HashMap<String, JSONObject> nums) {
		
		repostNumTv = rpTv;
		commentNumTv = cmTv;
		attributeNumTv = atTv;
		weiboId = wbid;
		context = ct;
		weiboIds = ids;
		weiboNums = nums;
    }
	@Override
	protected void onPreExecute() {
	   
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	        
	}
         
	@Override
	protected String doInBackground(String... params) {
	       
		JSONObject jo = weiboNums.get(weiboId);
		if(jo != null)
		{
			return "["+jo.toString()+"]";
		}
		else
		{

			Integer retCode = null;
	         
        	HttpClient client = new DefaultHttpClient();
	        HttpGet get = new HttpGet("https://api.weibo.com/2" + "/statuses" + "/count.json?ids=" +weiboIds+"&access_token="+AccessTokenKeeper.readAccessToken(context).getToken() );
        	HttpResponse response;
			try {
				response = client.execute(get);
				retCode = response.getStatusLine().getStatusCode(); 
	        	if(retCode == 200)
	    	        return EntityUtils.toString(response.getEntity());   
	    	    else
	    	        return "[{\"id\"="+weiboId+",\"reposts\"=0,\"comments\"=0,\"attitudes\"=0}]";
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	        	
	        
	        
			return "[{\"id\"="+weiboId+",\"reposts\"=0,\"comments\"=0,\"attitudes\"=0}]";
	        
		}
        
	}

	@Override
	protected void onPostExecute(String result) {
	        // TODO Auto-generated method stub
	        super.onPostExecute(result);
	        try {
				JSONArray wbNums = new JSONArray(result);
				if(wbNums != null)
				{
					for(int i = 0;i<wbNums.length();i++)
					{
						JSONObject wbNum = wbNums.getJSONObject(i);
						weiboNums.put(wbNum.optString("id"), new JSONObject(wbNum.toString()));
						if(wbNum.optString("id").equals(weiboId))
						{
							if(Integer.parseInt(wbNum.optString("reposts")) == 0)
								repostNumTv.setText("转发");
							else
								repostNumTv.setText(wbNum.optString("reposts"));
							
							if(Integer.parseInt(wbNum.optString("comments")) == 0)
								commentNumTv.setText("评论");
							else
								commentNumTv.setText(wbNum.optString("comments"));
							
							if(Integer.parseInt(wbNum.optString("attitudes")) == 0)
								attributeNumTv.setText("赞");
							else
								attributeNumTv.setText(wbNum.optString("attitudes"));
						}
					}
					
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
