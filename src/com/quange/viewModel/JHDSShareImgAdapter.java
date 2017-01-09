package com.quange.viewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.quange.jhds.AccessTokenKeeper;
import com.quange.jhds.AppCommon;
import com.quange.jhds.AppSetManager;
import com.quange.jhds.DateUtils;
import com.quange.jhds.JHDSShareDetailActivity;
import com.quange.jhds.PhotosActivity;
import com.quange.jhds.R;
import com.quange.model.JHDSShareModel;
import com.quange.views.CircleImageView;
import com.quange.views.EmojiTextView;
import com.quange.views.RoundImageView;

public class JHDSShareImgAdapter extends BaseAdapter {
	private Activity mAct;
	private float density = 0;
	private int screenWidth = 0;
	private List<JHDSShareModel> mlList = new ArrayList<JHDSShareModel>();
	private HashMap<String, JSONObject> weiboNums = new HashMap<>();
	public JHDSShareImgAdapter(Activity act, List<JHDSShareModel>lList) {
		this.mAct = act;
		this.mlList = lList;
		
	}
	@Override
	public int getCount() {
		return mlList.size();
	}

	@Override
	public JHDSShareModel getItem(int position) {
		return mlList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public String getIds()
	{
		String ss = "";
		for(int i = 0;i<mlList.size();i++)
		{
			JHDSShareModel m = mlList.get(i);
			ss = ss + m.idstr;
			if(i<mlList.size()-1)
				ss = ss + ",";
		}
		return ss;
	}

	@Override
	public View getView(final int position, View cv, ViewGroup parent) {
		JHDSShareModel ls = getItem(position);
		HoldView hv = null;
		if (null == cv) {
			hv = new HoldView();
			cv = View.inflate(mAct, R.layout.list_item_washare, null);

			hv.userNickName = (TextView) cv.findViewById(R.id.tv_user_nickname);
			hv.userIcon = (CircleImageView) cv.findViewById(R.id.rimg_userIcon);
			hv.createTime = (TextView) cv.findViewById(R.id.tv_create_time);
			hv.tv_content = (EmojiTextView) cv.findViewById(R.id.etv_content);
			hv.shareImg = (ImageView) cv.findViewById(R.id.img_washare);
			
			hv.bottombar_layout = (LinearLayout) cv.findViewById(R.id.ll_bottombar);
			hv.bottombar_retweet = (LinearLayout) cv.findViewById(R.id.ll_bottombar_retweet);
			hv.bottombar_comment = (LinearLayout) cv.findViewById(R.id.ll_bottombar_comment);
			
			hv.redirect = (TextView) cv.findViewById(R.id.tv_redirect);
			hv.comment = (TextView) cv.findViewById(R.id.tv_comment);
			hv.feedlike = (TextView) cv.findViewById(R.id.tv_feedlike);
			cv.setTag(hv);
		} else {
			hv = (HoldView) cv.getTag();
		}
		String id = AppSetManager.getTopWeiboId();
		
		hv.userNickName.setText(ls.nickName);
		
		hv.tv_content.setEmojiText(ls.text);
		
		if(AppSetManager.getSinaNickName().equals("") )
		{
			hv.bottombar_layout.setVisibility(View.GONE);
		}
		else if((System.currentTimeMillis() - AccessTokenKeeper.readAccessToken(mAct).getExpiresTime())<0)
		{
			hv.bottombar_layout.setVisibility(View.VISIBLE);
			
			if(hv.bottombar_layout.getTag() != null) {
			    ((JHDSWeiboNumAsyncTask) hv.bottombar_layout.getTag()).cancel(true);
			}
			JHDSWeiboNumAsyncTask task = new JHDSWeiboNumAsyncTask(mAct,hv.redirect,hv.comment,hv.feedlike,ls.idstr,getIds(),weiboNums) ;
			task.execute("");
			hv.bottombar_layout.setTag(task);
		}
		else
			hv.bottombar_layout.setVisibility(View.GONE);
		
		
		AppCommon.getInstance().imageLoader.displayImage(ls.userIcon, hv.userIcon, AppCommon.getInstance().userIconOptions);
		
		hv.createTime.setText(DateUtils.convertTimeToFormat( Long.parseLong(ls.created_timestamp)) );
	
		if(ls.original_pic != null && ls.pic_ids.length>0)
		{
			String url= "";
			String[] urlsubs = ls.original_pic.split("/");
			for(int j = 0;j<urlsubs.length-2;j++)
			{
				url = url+urlsubs[j]+"/";
			}
			
			hv.shareImg.setVisibility(View.VISIBLE);		
			hv.shareImg.setTag(0+"");
			String shareImgUrl = url+"bmiddle/"+ls.pic_ids[0]+".jpg";
			//mImageFetcher.loadImage(shareImgUrl,hv.shareImg);
			AppCommon.getInstance().imageLoader.displayImage(shareImgUrl, hv.shareImg, AppCommon.getInstance().options);
//			hv.shareImg.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					
//						gotoBigImage(position, Integer.valueOf(((ImageView)v).getTag().toString()).intValue());
//					
//				}
//			});
			
		}
		else
		{
			hv.shareImg.setVisibility(View.GONE);
		}
		
		
		hv.bottombar_retweet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Bundle bundle = new Bundle();
					JHDSShareModel sm = getItem(position);
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
					String id = AppSetManager.getTopWeiboId();
					if(!id.equals("")&&position == 0)
						bundle.putString("created_timestamp", "0");
					else
						bundle.putString("created_timestamp", sm.created_timestamp);
					bundle.putString("selectIndex", "0");
					Intent intent = new Intent(mAct, JHDSShareDetailActivity.class);
					intent.putExtras(bundle);
					mAct.startActivity(intent);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		hv.bottombar_comment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Bundle bundle = new Bundle();
					JHDSShareModel sm = getItem(position);
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
					bundle.putString("selectIndex", "1");
					String id = AppSetManager.getTopWeiboId();
					if(!id.equals("") && position == 0)
						bundle.putString("created_timestamp", "0");
					else
						bundle.putString("created_timestamp", sm.created_timestamp);
					Intent intent = new Intent(mAct, JHDSShareDetailActivity.class);
					intent.putExtras(bundle);
					mAct.startActivity(intent);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		
		return cv;
	}
	private class HoldView {
	
		private TextView userNickName; 
		private CircleImageView userIcon;
		private TextView createTime; 
		private EmojiTextView tv_content; 
		private ImageView shareImg;
		private LinearLayout bottombar_layout;
		private LinearLayout bottombar_retweet;
		private LinearLayout bottombar_comment;
		private TextView redirect;
		private TextView comment;
		private TextView feedlike;
		
	}
	
	private void gotoBigImage(int pos,int imgIndex)
	{
		Bundle bundle = new Bundle();
		String url= "";
		String[] urlsubs = mlList.get(pos).original_pic.split("/");
		for(int j = 0;j<urlsubs.length-1;j++)
		{
			url = url+urlsubs[j]+"/";
		}
		
		String allUrl = "";
		for (int i = 0;i<mlList.get(pos).pic_ids.length;i++)
		{
			if(i==mlList.get(pos).pic_ids.length-1)
				allUrl = allUrl+url+mlList.get(pos).pic_ids[i]+".jpg";
			else 
				allUrl = allUrl +url+ mlList.get(pos).pic_ids[i]+".jpg"+"*";
		}
		bundle.putString("allUrl", allUrl);
		String cururl = url+mlList.get(pos).pic_ids[imgIndex]+".jpg";
		bundle.putString("curUrl",cururl );
		Intent intent = new Intent(mAct, PhotosActivity.class);
		intent.putExtras(bundle);
		mAct.startActivity(intent);
	}
	
	private ImageLoadingListener imgLoadingListener = new ImageLoadingListener(){
		@Override
		public  void onLoadingStarted(String imageUri, View view)
		{
			
		}
		@Override
		public  void onLoadingFailed(String imageUri, View view, FailReason failReason)
		{
			
		}
		@Override
		public  void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
		{
			int density = (int) view.getResources().getDisplayMetrics().density;
			ViewGroup.LayoutParams lp = view.getLayoutParams();
			lp.width = loadedImage.getWidth()*density;
			lp.height = loadedImage.getHeight()*density;
			view.setLayoutParams(lp);
		}
		@Override
		public  void onLoadingCancelled(String imageUri, View view)
		{
			
		}
		
	};
}
