package com.quange.jhds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.quange.views.JHDSGuideView;
import com.quange.views.JHDSShareCommentListView;
import com.quange.views.JHDSShareRepostView;
import com.quange.views.JHDSTextGuideView;
import com.quange.views.WrapContentHeightViewPager;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.RepostsList;
import com.sina.weibo.sdk.openapi.models.Status;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class JHDSShareDetailActivity extends Activity {
	
	public String weibo_idstr;
	public String[] weibo_pic_urls;
	public String weibo_text;
	public String weibo_nickName;
	public String weibo_userId;
	public String weibo_userIconUrl;
	public String weibo_created_timestamp;
	
	@ViewInject(R.id.userIcon)
	private ImageView userIcon;
	@ViewInject(R.id.userNickName)
	private TextView userNickName;
	@ViewInject(R.id.createTime)
	private TextView createTime;
	@ViewInject(R.id.tv_content)
	private TextView tv_content;
	@ViewInject(R.id.shareImgBox1)
	private LinearLayout shareImgBox1;
	@ViewInject(R.id.shareImgBox2)
	private LinearLayout shareImgBox2;
	@ViewInject(R.id.shareImgBox3)
	private LinearLayout shareImgBox3;
	private ImageView shareImg[] = new ImageView[9];
	
	
	@ViewInject(R.id.repostNumBtn)
	private TextView repostNumBtn;
	@ViewInject(R.id.commentNumBtn)
	private TextView commentNumBtn;
	@ViewInject(R.id.goodNumBtn)
	private TextView goodNumBtn;
	
	@ViewInject(R.id.BottomViewPaper)
	private WrapContentHeightViewPager BottomViewPaper;
	private StatusesAPI weiboStatusesAPI;
	private CommentsAPI weiboCommentsAPIAPI;
	private JHDSShareRepostView sr;
	private JHDSShareCommentListView cr;
	public class weiboDetailAdapter extends PagerAdapter {

		private View mCurrentView;
	    
	    @Override
	    public void setPrimaryItem(ViewGroup container, int position, Object object) {
	        mCurrentView = (View)object;
	    }
	                                             
	    public View getPrimaryItem() {
	        return mCurrentView;
	    }
		public boolean isViewFromObject(View view, Object o) {
			return view == o;
		}

		public int getCount() {
			return 2;
		}

		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		public Object instantiateItem(View container, int position) {
			MobclickAgent.onEvent(getApplicationContext(), "splash_guide_look");
			
			if(position==0)
			{
				 sr = new JHDSShareRepostView((Activity)container.getContext(),weibo_idstr);
				sr.firstLoadData();
	            ((ViewPager) container).addView(sr.getView(), LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	           
	            return sr.getView();
			}
			else
			{
				 cr = new JHDSShareCommentListView((Activity)container.getContext(),weibo_idstr);
				cr.firstLoadData();
				((ViewPager) container).addView(cr.getView(), LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
           
				return cr.getView();
			}
		}


	}
	
	protected void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_detail);
        ViewUtils.inject(this); // 注入view和事件
        
        weiboStatusesAPI = new StatusesAPI(this,SinaConstants.APP_KEY,AccessTokenKeeper.readAccessToken(this));
        weiboCommentsAPIAPI = new CommentsAPI(this,SinaConstants.APP_KEY,AccessTokenKeeper.readAccessToken(this));
        
        
       
        
        shareImg[0] = (ImageView)findViewById(R.id.shareImg0);
		shareImg[1] = (ImageView) findViewById(R.id.shareImg1);
		shareImg[2] = (ImageView) findViewById(R.id.shareImg2);
		shareImg[3] = (ImageView) findViewById(R.id.shareImg3);
		shareImg[4] = (ImageView) findViewById(R.id.shareImg4);
		shareImg[5] = (ImageView) findViewById(R.id.shareImg5);
		shareImg[6] = (ImageView) findViewById(R.id.shareImg6);
		shareImg[7] = (ImageView) findViewById(R.id.shareImg7);
		shareImg[8] = (ImageView) findViewById(R.id.shareImg8);
        Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			weibo_idstr = bundle.getString("idstr");
			weibo_text = bundle.getString("text");
			weibo_nickName = bundle.getString("nickName");
			weibo_userId = bundle.getString("userId");
			weibo_userIconUrl = bundle.getString("userIcon");
			weibo_created_timestamp = bundle.getString("created_timestamp");
			
			String all = bundle.getString("allUrl");
			weibo_pic_urls = all.split("\\*");
			
			userNickName.setText(weibo_nickName);
			tv_content.setText(weibo_text);
			AppCommon.getInstance().imageLoader.displayImage(weibo_userIconUrl, userIcon, AppCommon.getInstance().userIconOptions);
		    createTime.setText(DateUtils.convertTimeToFormat( Long.parseLong(weibo_created_timestamp)) );
			if(weibo_pic_urls.length<7)
				shareImgBox3.setVisibility(View.GONE);
			if(weibo_pic_urls.length<4)
				shareImgBox2.setVisibility(View.GONE);
			if(weibo_pic_urls.length ==0)
				shareImgBox1.setVisibility(View.GONE);
			
			
			
			for(int j = 0;j<9;j++)
			{
				shareImg[j].setVisibility(View.INVISIBLE);
			}
			for(int i = 0;i<weibo_pic_urls.length;++i)
			{
				shareImg[i].setVisibility(View.VISIBLE);
				shareImg[i].setTag(i+"");
				
				AppCommon.getInstance().imageLoader.displayImage(weibo_pic_urls[i], shareImg[i], AppCommon.getInstance().options);
				shareImg[i].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
							gotoBigImage(Integer.valueOf(((ImageView)v).getTag().toString()).intValue());
						
					}
				});
			}
			String []ids = weibo_idstr.split(",");
			weiboStatusesAPI.count(ids, mweiboDetailListener);
		}
        
		BottomViewPaper.setAdapter(new weiboDetailAdapter());
		repostNumBtn.setTextColor(0xffffa500);
		repostNumBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f);
		repostNumBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BottomViewPaper.setCurrentItem(0);
				repostNumBtn.setTextColor(0xffffa500);
				repostNumBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f);
				commentNumBtn.setTextColor(0xff333333);
				commentNumBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f);
				
			}
		});
		commentNumBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BottomViewPaper.setCurrentItem(1);
				commentNumBtn.setTextColor(0xffffa500);
				commentNumBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f);
				repostNumBtn.setTextColor(0xff333333);
				repostNumBtn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f);
			}
		});
	}
	
	@OnClick(R.id.bottombar_retweet)
	public void OnRepostClick(View view) {
		weiboStatusesAPI.repost(Long.parseLong(weibo_idstr), "转发微博", 0, mweiboRepostListener);
		//转发
		
	}
	@OnClick(R.id.bottombar_comment)
	public void OnCommentClick(View view) {
		
		
		//评论
	}
	@OnClick(R.id.bottombar_attitude)
	public void OnAttitudeClick(View view) {
		weiboStatusesAPI.attitude(Long.parseLong(weibo_idstr), mweiboAttributeListener);
		//点赞
	}
	
	private void gotoBigImage(int imgIndex)
	{
		Bundle bundle = new Bundle();
		
		String allUrl = "";
		for (int i = 0;i<weibo_pic_urls.length;i++)
		{
			if(i==weibo_pic_urls.length-1)
				allUrl = allUrl+weibo_pic_urls[i];
			else 
				allUrl = allUrl +weibo_pic_urls[i]+"*";
		}
		bundle.putString("allUrl", allUrl);
		String cururl = weibo_pic_urls[imgIndex];
		bundle.putString("curUrl",cururl );
		Intent intent = new Intent(this, PhotosActivity.class);
		intent.putExtras(bundle);
		this.startActivity(intent);
	}
	
	/**
	 * 微博 OpenAPI 回调接口。
	 */
	private RequestListener mweiboDetailListener = new RequestListener() {
		
		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			try {
				JSONArray wbNums = new JSONArray(arg0);
				if(wbNums != null)
				{
					if(wbNums.length() != 0)
					{
						JSONObject wbNum = wbNums.getJSONObject(0);
						repostNumBtn.setText("转发 "+wbNum.optString("reposts"));
						commentNumBtn.setText("评论 "+wbNum.optString("comments"));
						goodNumBtn.setText("赞 "+wbNum.optString("attitudes"));
					}
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			Status weiboStatus = Status.parse(arg0);
//			if (weiboStatus != null) {
//				repostNumBtn.setText("转发 "+weiboStatus.reposts_count);
//				commentNumBtn.setText("评论 "+weiboStatus.comments_count);
//				goodNumBtn.setText("赞 "+weiboStatus.attitudes_count);
//			}else{
//				
//			}
		}
	}; 
	
	/**
	 * 微博 OpenAPI 回调接口。
	 */
	private RequestListener mweiboRepostListener = new RequestListener() {
		
		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			
			errorRepostTip(arg0.getLocalizedMessage());
		}
		
		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			
			
			Status w = Status.parse(arg0);
			if(w != null)
			{
				sr.messageBox.setVisibility(View.GONE);
				successRepostTip("赞成功");
				sr.mLSList.add(w);
				sr.lAdapter.notifyDataSetChanged();
				String text = (String) repostNumBtn.getText();
				String[] subt = text.split(" ");
				if(subt.length == 1)
				{
					repostNumBtn.setText("转发 "+1);
				}
				else
				{
					repostNumBtn.setText("转发 "+(Integer.parseInt(subt[1]) +1));
				}
			}
		
		}
	}; 
	
	/**
	 * 微博 OpenAPI 回调接口。
	 */
	private RequestListener mweiboAttributeListener = new RequestListener() {
		
		@Override
		public void onWeiboException(WeiboException arg0) {
			// TODO Auto-generated method stub
			
			errorRepostTip(arg0.getLocalizedMessage());
		}
		
		@Override
		public void onComplete(String arg0) {
			// TODO Auto-generated method stub
			
			
			Status w = Status.parse(arg0);
			if(w != null)
			{
				sr.messageBox.setVisibility(View.GONE);
				successRepostTip("转发成功");
				sr.mLSList.add(w);
				sr.lAdapter.notifyDataSetChanged();
				String text = (String) repostNumBtn.getText();
				String[] subt = text.split(" ");
				if(subt.length == 1)
				{
					repostNumBtn.setText("转发 "+1);
				}
				else
				{
					repostNumBtn.setText("转发 "+(Integer.parseInt(subt[1]) +1));
				}
			}
		
		}
	}; 
	
	public void successRepostTip(String message)
	{
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	public void errorRepostTip(String error)
	{
		Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
	}
	
}
