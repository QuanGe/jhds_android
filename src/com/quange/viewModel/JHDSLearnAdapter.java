package com.quange.viewModel;

import java.util.ArrayList;
import java.util.List;

import com.quange.jhds.AppCommon;
import com.quange.jhds.R;
import com.quange.model.JHDSLearnModel;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

public class JHDSLearnAdapter extends BaseAdapter {

	private Activity mAct;
	private float density = 0;
	private int screenWidth = 0;
	private List<JHDSLearnModel> mlList = new ArrayList<JHDSLearnModel>();
	public JHDSLearnAdapter(Activity act, List<JHDSLearnModel>lList) {
		this.mAct = act;
		this.mlList = lList;
	}
	@Override
	public int getCount() {
		return mlList.size();
	}

	@Override
	public JHDSLearnModel getItem(int position) {
		return mlList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View cv, ViewGroup parent) {
		JHDSLearnModel ls = getItem(position);
		HoldView hv = null;
		if (null == cv) {
			hv = new HoldView();
			cv = View.inflate(mAct, R.layout.list_item_learn, null);

			hv.contentTv = (TextView) cv.findViewById(R.id.tv_title);
			hv.contentIv = (ImageView) cv.findViewById(R.id.contentImageView);
			hv.ortherTv = (TextView) cv.findViewById(R.id.tv_orther);
			
			cv.setTag(hv);
		} else {
			hv = (HoldView) cv.getTag();
		}
		
		hv.contentTv.setText(ls.info);
		hv.ortherTv.setText("步骤:"+ls.detail.length);
		{
			int height = 0;
			height = imageHeight(ls);
			LinearLayout.LayoutParams llp = new LayoutParams((int) (screenWidth-24*density),height );
			llp.leftMargin = (int) (12*density);
			llp.bottomMargin = (int) (2*density);
			hv.contentIv.setScaleType(ScaleType.FIT_XY);
			hv.contentIv.setLayoutParams(llp);
		}
		
		String url = "http://quangelab.com/images/jhds/learn/"+ls.url+".jpg";
		System.out.println(url);
		AppCommon.getInstance().imageLoader.displayImage(url, hv.contentIv, AppCommon.getInstance().options);
			
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
		private TextView ortherTv;
		
	}
}