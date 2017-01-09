package com.quange.views;

import com.quange.jhds.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class JHDSShareLoginHeader extends LinearLayout {
	private Context mContext;

	private View mContentView;
	
	public JHDSShareLoginHeader(Context context) {
		super(context);
		initView(context);
	}

	public JHDSShareLoginHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public void hide() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}

	private void initView(Context context) {
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.view_share_top, null);
		addView(moreView);
		moreView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		mContentView = moreView.findViewById(R.id.rl_header_content);
		
	}

	
}
