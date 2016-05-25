package com.quange.viewModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.quange.jhds.AppCommon;
import com.quange.jhds.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class JHDSSaveImagesAdapter extends BaseAdapter {
	private Activity mAct;
	private List<String> mlList = new ArrayList<String>();
	public JHDSSaveImagesAdapter(Activity act, List<String>lList) {
		this.mAct = act;
		this.mlList = lList;
	}
	@Override
	public int getCount() {
		return mlList.size();
	}

	@Override
	public String getItem(int position) {
		return mlList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi") @Override
	public View getView(int position, View cv, ViewGroup parent) {
		String Url = getItem(position);
		HoldView hv = null;
		if (null == cv) {
			hv = new HoldView();
			cv = View.inflate(mAct, R.layout.gridview_item_copy, null);
			
			hv.contentIv = (ImageView) cv.findViewById(R.id.contentIv);
			
			
			cv.setTag(hv);
		} else {
			hv = (HoldView) cv.getTag();
		}		
		hv.contentIv.setScaleType(ScaleType.CENTER_CROP);
		Bitmap bitmap = getLoacalBitmap(Url); //从本地取图片(在cdcard中获取)  //
		hv.contentIv.setImageBitmap(bitmap); //设置Bitmap
		
		
		return cv;
	}

	 /**
	    * 加载本地图片
	    * @param url
	    * @return
	    */
	    public static Bitmap getLoacalBitmap(String url) {
	         try {
	              FileInputStream fis = new FileInputStream(url);
	              return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片        

	           } catch (FileNotFoundException e) {
	              e.printStackTrace();
	              return null;
	         }
	    }


	private class HoldView {
	
	
		private ImageView contentIv;
	
		
	}

}
