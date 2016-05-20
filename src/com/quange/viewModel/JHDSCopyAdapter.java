package com.quange.viewModel;

import java.util.ArrayList;
import java.util.List;

import com.quange.model.JHDSCopyModel;

import com.quange.jhds.AppCommon;
import com.quange.jhds.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;


public class JHDSCopyAdapter extends BaseAdapter{
	private Activity mAct;
	private float density = 0;
	
	private List<JHDSCopyModel> mlList = new ArrayList<JHDSCopyModel>();
	public JHDSCopyAdapter(Activity act, List<JHDSCopyModel>lList) {
		this.mAct = act;
		this.mlList = lList;
	}
	@Override
	public int getCount() {
		return mlList.size();
	}

	@Override
	public JHDSCopyModel getItem(int position) {
		return mlList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi") @Override
	public View getView(int position, View cv, ViewGroup parent) {
		JHDSCopyModel ls = getItem(position);
		HoldView hv = null;
		if (null == cv) {
			hv = new HoldView();
			cv = View.inflate(mAct, R.layout.gridview_item_copy, null);
			GradientDrawable backColor = new GradientDrawable();
			backColor.setColor(0xffffffff);
			backColor.setCornerRadius( 5 *AppCommon.getInstance().metrics.density);
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
	        	cv.setBackground(backColor);
	        else
	        	cv.setBackgroundDrawable(backColor);
			hv.contentIv = (ImageView) cv.findViewById(R.id.contentIv);
		
			
			cv.setTag(hv);
		} else {
			hv = (HoldView) cv.getTag();
		}		
		hv.contentIv.setScaleType(ScaleType.FIT_CENTER);
		AppCommon.getInstance().imageLoader.displayImage(ls.url, hv.contentIv, AppCommon.getInstance().options);
		
		return cv;
	}


	private class HoldView {
	
	
		private ImageView contentIv;
	
		
	}
}
