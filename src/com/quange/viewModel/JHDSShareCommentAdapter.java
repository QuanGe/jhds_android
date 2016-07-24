package com.quange.viewModel;

import java.util.ArrayList;
import java.util.List;

import com.quange.jhds.AppCommon;
import com.quange.jhds.DateWeiboUtils;

import com.quange.jhds.R;

import com.quange.views.EmojiTextView;
import com.quange.views.RoundImageView;
import com.sina.weibo.sdk.openapi.models.Comment;


import android.app.Activity;

import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class JHDSShareCommentAdapter extends BaseAdapter {
	private Activity mAct;
	
	private List<Comment> mlList = new ArrayList<Comment>();
	public JHDSShareCommentAdapter(Activity act, List<Comment>lList) {
		this.mAct = act;
		this.mlList = lList;
	}
	@Override
	public int getCount() {
		return mlList.size();
	}

	@Override
	public Comment getItem(int position) {
		return mlList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View cv, ViewGroup parent) {
		Comment ls = getItem(position);
		HoldView hv = null;
		if (null == cv) {
			hv = new HoldView();
			cv = View.inflate(mAct, R.layout.list_item_share_comment, null);

			hv.userNickName = (TextView) cv.findViewById(R.id.commentUserNickName);
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
			
			cv.setTag(hv);
		} else {
			hv = (HoldView) cv.getTag();
		}
		if(hv != null)
		{
			hv.userNickName.setText(ls.user.screen_name);
			hv.tv_content.setEmojiText(ls.text);
			AppCommon.getInstance().imageLoader.displayImage(ls.user.avatar_large, hv.userIcon, AppCommon.getInstance().userIconOptions);
			hv.createTime.setText(DateWeiboUtils.parseTime(ls.created_at) );
			hv.shareImgBox3.setVisibility(View.GONE);
			hv.shareImgBox2.setVisibility(View.GONE);
			hv.shareImgBox1.setVisibility(View.GONE);
		}
		
		return cv;
	}
	
	private class HoldView {
		
		private TextView userNickName; 
		private RoundImageView userIcon;
		private TextView createTime; 
		private EmojiTextView tv_content; 
		private LinearLayout shareImgBox1; 
		private LinearLayout shareImgBox2; 
		private LinearLayout shareImgBox3; 
		private ImageView shareImg[] = new ImageView[9];
		
		
	}
	
	
}