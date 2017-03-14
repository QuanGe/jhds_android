package com.quange.views;

import com.quange.jhds.AppCommon;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class JHDSErrorMessage extends RelativeLayout {
	private TextView messageTV;
	public JHDSErrorMessage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		buildSubView();
	}
	
	private void buildSubView()
	{
		messageTV = new TextView(this.getContext());
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		lp.leftMargin = (int) (30*AppCommon.getInstance().metrics.density);
		lp.rightMargin = (int) (30*AppCommon.getInstance().metrics.density);
		messageTV.setTextColor(0xff555555);
		messageTV.setTextSize(16);
		messageTV.setGravity(Gravity.CENTER);
		
		addView(messageTV, lp);
	}

	public void updateMessage(String ms)
	{
		messageTV.setText(ms);
	}
	public JHDSErrorMessage(Context context, AttributeSet attrs) {
		super(context,attrs);
		buildSubView();
	}
}
