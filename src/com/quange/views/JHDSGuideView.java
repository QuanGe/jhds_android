package com.quange.views;

import com.quange.jhds.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class JHDSGuideView extends RelativeLayout {

	public ImageView  contentImageView;
	public TextView tv_info;
	public JHDSGuideView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.view_guide, this,true);
		contentImageView = (ImageView)findViewById(R.id.contentImageView);
		tv_info = (TextView)findViewById(R.id.tv_info);
	}
	public JHDSGuideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.view_guide, this,true);
		contentImageView = (ImageView)findViewById(R.id.contentImageView);
		tv_info = (TextView)findViewById(R.id.tv_info);
		
	}

}
