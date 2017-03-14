package com.quange.viewModel;

import java.util.ArrayList;
import java.util.List;

import com.quange.jhds.AccessTokenKeeper;
import com.quange.jhds.AppCommon;
import com.quange.jhds.AppSetManager;
import com.quange.jhds.DateUtils;
import com.quange.jhds.JHDSLearnDetailActivity;
import com.quange.jhds.JHDSShareDetailActivity;
import com.quange.jhds.PhotosActivity;
import com.quange.jhds.R;
import com.quange.model.JHDSShareModel;
import com.quange.viewModel.JHDSSaveImagesAdapter.ImageGetter;
import com.quange.views.EmojiTextView;
import com.quange.views.RoundImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class JHDSShareAdapter extends BaseAdapter {
	private Activity mAct;
	private float density = 0;
	private int screenWidth = 0;
	private List<JHDSShareModel> mlList = new ArrayList<JHDSShareModel>();
	public JHDSShareAdapter(Activity act, List<JHDSShareModel>lList) {
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

	@Override
	public View getView(final int position, View cv, ViewGroup parent) {
		JHDSShareModel ls = getItem(position);
		HoldView hv = null;
		if (null == cv) {
			hv = new HoldView();
			cv = View.inflate(mAct, R.layout.list_item_share, null);

			hv.userNickName = (TextView) cv.findViewById(R.id.userNickName);
			hv.userIcon = (RoundImageView) cv.findViewById(R.id.userIcon);
			hv.createTime = (TextView) cv.findViewById(R.id.createTime);
			hv.tv_content = (EmojiTextView) cv.findViewById(R.id.tv_content);
			hv.shareImgBox1 = (LinearLayout) cv.findViewById(R.id.shareImgBox1);
			hv.shareImgBox2 = (LinearLayout) cv.findViewById(R.id.shareImgBox2);
			hv.shareImgBox3 = (LinearLayout) cv.findViewById(R.id.shareImgBox3);
			hv.shareImg[0] = (ImageView) cv.findViewById(R.id.shareImg0);
			hv.shareImg[1] = (ImageView) cv.findViewById(R.id.shareImg1);
			hv.shareImg[2] = (ImageView) cv.findViewById(R.id.shareImg2);
			hv.shareImg[3] = (ImageView) cv.findViewById(R.id.shareImg3);
			hv.shareImg[4] = (ImageView) cv.findViewById(R.id.shareImg4);
			hv.shareImg[5] = (ImageView) cv.findViewById(R.id.shareImg5);
			hv.shareImg[6] = (ImageView) cv.findViewById(R.id.shareImg6);
			hv.shareImg[7] = (ImageView) cv.findViewById(R.id.shareImg7);
			hv.shareImg[8] = (ImageView) cv.findViewById(R.id.shareImg8);
			hv.bottombar_layout = (LinearLayout) cv.findViewById(R.id.bottombar_layout);
			hv.bottombar_retweet = (LinearLayout) cv.findViewById(R.id.bottombar_retweet);
			hv.bottombar_comment = (LinearLayout) cv.findViewById(R.id.bottombar_comment);
			
			hv.redirect = (TextView) cv.findViewById(R.id.redirect);
			hv.comment = (TextView) cv.findViewById(R.id.comment);
			hv.feedlike = (TextView) cv.findViewById(R.id.feedlike);
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
			JHDSWeiboNumAsyncTask task = new JHDSWeiboNumAsyncTask(mAct,hv.redirect,hv.comment,hv.feedlike,ls.idstr,"",null) ;
			task.execute("");
			hv.bottombar_layout.setTag(task);
		}
		else
			hv.bottombar_layout.setVisibility(View.GONE);
		
		
		AppCommon.getInstance().imageLoader.displayImage(ls.userIcon, hv.userIcon, AppCommon.getInstance().userIconOptions);
		if(!id.equals("") && position == 0)
		{
			hv.createTime.setText("刚刚");
			
		}
		else
			hv.createTime.setText(DateUtils.convertTimeToFormat( Long.parseLong(ls.created_timestamp)) );
		if(ls.pic_ids.length<7)
			hv.shareImgBox3.setVisibility(View.GONE);
		else
			hv.shareImgBox3.setVisibility(View.VISIBLE);
		if(ls.pic_ids.length<4)
			hv.shareImgBox2.setVisibility(View.GONE);
		else
			hv.shareImgBox2.setVisibility(View.VISIBLE);
		if(ls.pic_ids.length ==0)
			hv.shareImgBox1.setVisibility(View.GONE);
		else
			hv.shareImgBox1.setVisibility(View.VISIBLE);
		for(int j = 0;j<9;j++)
		{
			hv.shareImg[j].setVisibility(View.INVISIBLE);
		}
		
		if(ls.original_pic != null)
		{
			String url= "";
			String[] urlsubs = ls.original_pic.split("/");
			for(int j = 0;j<urlsubs.length-2;j++)
			{
				url = url+urlsubs[j]+"/";
			}
			
			
			for(int i = 0;i<ls.pic_ids.length;++i)
			{
				hv.shareImg[i].setVisibility(View.VISIBLE);
				hv.shareImg[i].setTag(i+"");
				String shareImgUrl = url+"thumbnail/"+ls.pic_ids[i]+".jpg";
				AppCommon.getInstance().imageLoader.displayImage(shareImgUrl, hv.shareImg[i], AppCommon.getInstance().options);
				hv.shareImg[i].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
							gotoBigImage(position, Integer.valueOf(((ImageView)v).getTag().toString()).intValue());
						
					}
				});
			}
		}
		else
		{
			
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
	
	/*private int imageHeight(JHDSLearnModel imagesize)
	{
		DisplayMetrics metrics = new DisplayMetrics();
		mAct.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		density = metrics.density;
		screenWidth = metrics.widthPixels;
		String [] m = imagesize.size.split(",");
		int height = (int) ((metrics.widthPixels - 24*density) *(Float.valueOf(m[1]) /Float.valueOf(m[0]) ));
		return height;
		
	}*/

	private class HoldView {
	
		private TextView userNickName; 
		private RoundImageView userIcon;
		private TextView createTime; 
		private EmojiTextView tv_content; 
		private LinearLayout shareImgBox1; 
		private LinearLayout shareImgBox2; 
		private LinearLayout shareImgBox3; 
		private ImageView shareImg[] = new ImageView[9];
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
}
