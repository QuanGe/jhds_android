package com.quange.views;

import com.quange.jhds.AppCommon;
import com.quange.jhds.MainActivity;
import com.quange.jhds.R;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi") public class JHDSTextGuideView extends RelativeLayout {
	public Button tv_info;
	public JHDSTextGuideView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.view_textguide, this,true);
		buildView();
	}

	public JHDSTextGuideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.view_textguide, this,true);
		buildView();
	}
	
	public void buildView()
	{
		tv_info = (Button)findViewById(R.id.tv_info);
		
		GradientDrawable btnColor =  new GradientDrawable();
		btnColor.setCornerRadius( 5*AppCommon.getInstance().metrics.density);
		btnColor.setColor(0xffDF0526);
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
    		tv_info.setBackground(btnColor);
    	else
    		tv_info.setBackgroundDrawable(btnColor);
		tv_info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				MobclickAgent.onEvent(getContext(), "splash_guide_try");
				Intent intent = new Intent(getContext(), MainActivity.class);
				
				getContext().startActivity(intent);
			}
		});
	}

}
