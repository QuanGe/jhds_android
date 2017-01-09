package com.quange.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.quange.jhds.AccessTokenKeeper;
import com.quange.jhds.AppSetManager;
import com.quange.jhds.JHDSLearnDetailActivity;
import com.quange.jhds.JHDSShareDetailActivity;
import com.quange.jhds.R;
import com.quange.jhds.SinaConstants;
import com.quange.model.JHDSLearnModel;
import com.quange.model.JHDSShareModel;

import com.quange.viewModel.JHDSAPIManager;
import com.quange.viewModel.JHDSShareAdapter;
import com.quange.viewModel.JHDSShareImgAdapter;
import com.quange.views.JHDSShareListView.JHDSShareListViewListener;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.widget.LoginButton;
import com.sina.weibo.sdk.widget.LoginoutButton;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.media.SinaShareContent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class ShareFragment extends Fragment implements OnItemClickListener,JHDSShareListViewListener {
	private View fgmView;
	private JHDSShareImgAdapter lAdapter;
	private JHDSShareListView lList;
	private ArrayList<JHDSShareModel> mLSList = new ArrayList<JHDSShareModel>();
	private LoginButton mSinaLoginBtn;
	private EmojiTextView headerTitle;
	private JHDSShareLoginHeader shareHead ;
	/** 登陆认证对应的listener */
    private AuthListener mLoginListener = new AuthListener();
    /** 用户信息接口 */
	private UsersAPI mUsersAPI;
    
    private AuthInfo mAuthInfo;
	private int mCurPage = 1;
	private Timer timer;
	private Handler handler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (fgmView == null) {
			fgmView = inflater.inflate(R.layout.fragment_share, container, false);
			lList = (JHDSShareListView) fgmView.findViewById(R.id.share_list);
			lAdapter = new JHDSShareImgAdapter(getActivity(), mLSList);
			shareHead = new JHDSShareLoginHeader(getActivity());
			lList.addHeaderView(shareHead);
			mSinaLoginBtn = (LoginButton) shareHead.findViewById(R.id.sinaLoginBtn);
			headerTitle = (EmojiTextView)shareHead.findViewById(R.id.headerTitle);
			// 创建授权认证信息
	        mAuthInfo = new AuthInfo(getActivity(), SinaConstants.APP_KEY, SinaConstants.REDIRECT_URL, SinaConstants.SCOPE);
			mSinaLoginBtn.setWeiboAuthInfo(mAuthInfo, mLoginListener);
			mSinaLoginBtn.setStyle(LoginButton.LOGIN_INCON_STYLE_3);
			lList.setAdapter(lAdapter);
		}
		
		if(AppSetManager.getSinaNickName().equals("") )
		{
			headerTitle.setEmojiText("未登录 [向右]");
		}
		else 
		{
			if((System.currentTimeMillis() - AccessTokenKeeper.readAccessToken(getActivity()).getExpiresTime())<0)
			{
				headerTitle.setEmojiText(AppSetManager.getSinaNickName());
			}
			else
			{
				headerTitle.setEmojiText("已过期请重新登录 [向右]");
			}
		}
		timer = new Timer();
		handler = new Handler(){     
		    
	        public void handleMessage(Message msg) {     
	            switch (msg.what) {         
	            case 1:         
	            	mLSList.remove(0);
					lAdapter.notifyDataSetChanged();    
	                break;         
	            }         
	            super.handleMessage(msg);     
	        }     
	             
	    };    
	    
	    
	    
	    
		
		lList.setPullLoadEnable(true);
		lList.setXListViewListener(this);
		lList.triggerRefresh();
//		lList.post(new Runnable() {
//		      @Override public void run() {
//		    	  lList.setRefreshing(true);
//		      }
//		 });
		return fgmView;
	}
	
	public ArrayList<JHDSLearnModel> removeDuplicate(List<JHDSLearnModel> mLSList2){
		   return new ArrayList<JHDSLearnModel>(new HashSet<JHDSLearnModel>(mLSList2));
		}
	
	public void refresh(final boolean isRefresh) {
		JHDSAPIManager.getInstance(getActivity()).fetchWeiboPageNum( new Listener<String>(){
			@Override
			public void onResponse(String response) {
				int num  =0 ;
				if(response.length()<20)
					num = Integer.parseInt(response);
				mCurPage = isRefresh ? 1 : ++mCurPage ;
				
				if(isRefresh)
		    		MobclickAgent.onEvent(getActivity(), "weibo_refresh");
		    	else
		    		MobclickAgent.onEvent(getActivity(), "weibo_more");
				
				if(num>=mCurPage)
				JHDSAPIManager.getInstance(getActivity()).fetchWeiboList(num-mCurPage, new Listener<List<JHDSShareModel>>(){
					@Override
					public void onResponse(List<JHDSShareModel> response) {
						if(isRefresh)
						{
							mLSList.clear();
						}
						mLSList.addAll(response);
						
						lAdapter.notifyDataSetChanged();
						if(isRefresh)
							lList.stopRefresh();
						else
							lList.stopLoadMore();
					}
					
					
				} ,  new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						System.out.println(error);
						--mCurPage;
					}
				});
				
				
				
			}
			
			
		} ,  new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				System.out.println(error);
			
			}
		});
	}


	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		try {
			Bundle bundle = new Bundle();
			JHDSShareModel sm = mLSList.get(position-1);
			String url= "";
			String[] urlsubs = sm.original_pic.split("/");
			for(int j = 0;j<urlsubs.length-1;j++)
			{
				url = url+urlsubs[j]+"/";
			}
			
			String allUrl = "";
			for (int i = 0;i<sm.pic_ids.length;i++)
			{
				if(i==sm.pic_ids.length-1)
					allUrl = allUrl+url+sm.pic_ids[i]+".jpg";
				else 
					allUrl = allUrl +url+ sm.pic_ids[i]+".jpg"+"*";
			}
			bundle.putString("allUrl", allUrl);
			bundle.putString("idstr", sm.idstr);
			bundle.putString("text", sm.text);
			bundle.putString("nickName", sm.nickName);
			bundle.putString("userId", sm.userId);
			bundle.putString("userIcon", sm.userIcon);
			bundle.putString("selectIndex", "0");
			String weiboid = AppSetManager.getTopWeiboId();
			if(!weiboid.equals("") && position ==1)
				bundle.putString("created_timestamp", "0");
			else
				bundle.putString("created_timestamp", sm.created_timestamp);
			Intent intent = new Intent(getActivity(), JHDSShareDetailActivity.class);
			intent.putExtras(bundle);
			getActivity().startActivity(intent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	/**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     * 
     * @see {@link Activity#onActivityResult}
     */
   
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mSinaLoginBtn != null) {
            ((LoginButton)mSinaLoginBtn).onActivityResult(requestCode, resultCode, data);
        }
    }
    
	/**
     * 登入按钮的监听器，接收授权结果。
     */
    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                        new java.util.Date(accessToken.getExpiresTime()));
                String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
                //headerTitle.setText(String.format(format, accessToken.getToken(), date));
                //headerTitle.setText("登录成功");
                mUsersAPI = new UsersAPI(getActivity(),SinaConstants.APP_KEY,accessToken);
    			mUsersAPI.show(Long.parseLong(accessToken.getUid()), mListener);
                AccessTokenKeeper.writeAccessToken(getActivity(), accessToken);
            }
            else
            {
            	String code = values.getString("code", "");
            	headerTitle.setEmojiText("登录失败"+code);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            headerTitle.setEmojiText("登录失败 [向右]");
        }

        @Override
        public void onCancel() {
        	headerTitle.setEmojiText("取消登录 [向右]");
            Toast.makeText(getActivity(), 
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 登出按钮的监听器，接收登出处理结果。（API 请求结果的监听器）
     */
    private class LogOutRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");

                    if ("true".equalsIgnoreCase(value)) {
                        AccessTokenKeeper.clear(getActivity());
                        //mTokenView.setText(R.string.weibosdk_demo_logout_success);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }     

        @Override
        public void onWeiboException(WeiboException e) {
            //mTokenView.setText(R.string.weibosdk_demo_logout_failed);
        }
    }
    
    public void onRefresh()
    {
    	refresh(true);
    }

	public void onLoadMore()
	{
		refresh(false);
	}
    
    /**
	 * 微博 OpenAPI 回调接口。
	 */
	private RequestListener mListener = new RequestListener() {
		
		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), "用户信息获取失败", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			User user = User.parse(arg0);
			if (user != null) {
				headerTitle.setEmojiText(user.screen_name);
				AppSetManager.setSinaUserIcon(user.avatar_large);
				AppSetManager.setSinaNickName(user.screen_name);
				lAdapter.notifyDataSetChanged();
			}else{
			}
		}
	}; 
	
	
}
