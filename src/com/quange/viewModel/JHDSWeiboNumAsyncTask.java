package com.quange.viewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.quange.jhds.AccessTokenKeeper;
import com.quange.jhds.SinaConstants;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.WeiboParameters;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

public class JHDSWeiboNumAsyncTask extends AsyncTask<String, Integer, String> {
	private TextView repostNumTv;
	private TextView commentNumTv;
	private TextView attributeNumTv;
	private String  weiboId;
	private Context context;
	
	public JHDSWeiboNumAsyncTask(Context ct,TextView rpTv,TextView cmTv,TextView atTv,String wbid) {
		
		repostNumTv = rpTv;
		commentNumTv = cmTv;
		attributeNumTv = atTv;
		weiboId = wbid;
		context = ct;
    }
	@Override
	protected void onPreExecute() {
	   
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	        
	}
         
	@Override
	protected String doInBackground(String... params) {
	       
		WeiboParameters args = new WeiboParameters(SinaConstants.APP_KEY);
		args.put("ids",weiboId);
		args.put("access_token", AccessTokenKeeper.readAccessToken(context).getToken());
        return new AsyncWeiboRunner(context).request("https://api.weibo.com/2" + "/statuses" + "/count.json", args, "GET");
        
	}

	@Override
	protected void onPostExecute(String result) {
	        // TODO Auto-generated method stub
	        super.onPostExecute(result);
	        try {
				JSONArray wbNums = new JSONArray(result);
				if(wbNums != null)
				{
					if(wbNums.length() != 0)
					{
						JSONObject wbNum = wbNums.getJSONObject(0);
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
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
