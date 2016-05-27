package com.quange.viewModel;

import java.util.ArrayList;
import java.util.List;

import com.quange.jhds.AppCommon;
import com.quange.jhds.R;
import com.quange.model.JHDSLearnModel;
import com.quange.model.JHDSShopModel;


import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

public class JHDSShopAdapter extends BaseAdapter {

	private Activity mAct;
	private float density = 0;
	private int screenWidth = 0;
	private List<JHDSShopModel> mlList = new ArrayList<JHDSShopModel>();
	public JHDSShopAdapter(Activity act, List<JHDSShopModel>lList) {
		this.mAct = act;
		this.mlList = lList;
	}
	@Override
	public int getCount() {
		return mlList.size();
	}

	@Override
	public JHDSShopModel getItem(int position) {
		return mlList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View cv, ViewGroup parent) {
		JHDSShopModel ls = getItem(position);
		HoldView hv = null;
		if (null == cv) {
			hv = new HoldView();
			cv = View.inflate(mAct, R.layout.list_item_shop, null);

			hv.contentTv = (TextView) cv.findViewById(R.id.tv_title);
			hv.contentIv = (ImageView) cv.findViewById(R.id.contentImageView);
			hv.ortherTv = (TextView) cv.findViewById(R.id.tv_orther);
			
			cv.setTag(hv);
		} else {
			hv = (HoldView) cv.getTag();
		}
		
		hv.contentTv.setText(ls.content);
		hv.ortherTv.setText(ls.orther);
		{
			int width = (int) (AppCommon.getInstance().screenWidth-24*density)/3;
			
			RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(width,width*2/3 );
			llp.leftMargin = (int) (12*density);
			llp.bottomMargin = (int) (2*density);
			llp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT); 
			llp.addRule(RelativeLayout.ALIGN_PARENT_TOP); 
			hv.contentIv.setScaleType(ScaleType.CENTER_CROP);
			hv.contentIv.setLayoutParams(llp);
		}
		
		
		
		AppCommon.getInstance().imageLoader.displayImage(ls.imgUrl, hv.contentIv, AppCommon.getInstance().options);
			
		return cv;
	}
	
	private int imageHeight(JHDSLearnModel imagesize)
	{
		DisplayMetrics metrics = new DisplayMetrics();
		mAct.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		density = metrics.density;
		screenWidth = metrics.widthPixels;
		String [] m = imagesize.size.split(",");
		int height = (int) ((metrics.widthPixels - 24*density) *(Float.valueOf(m[1]) /Float.valueOf(m[0]) ));
		return height;
		
	}

	private class HoldView {
	
		private TextView contentTv; // name
		private ImageView contentIv;
		private TextView ortherTv; // name
	}
}
