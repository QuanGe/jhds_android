package com.quange.views;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.quange.jhds.AppCommon;
import com.quange.jhds.DrawActivity;
import com.quange.jhds.R;

import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class JHDSBackSelectDialog extends Dialog implements OnClickListener {

	private static final int LEFT_AND_RIGHT_MARGINS = 0;
	private DrawActivity mAct;
	private View mView;
	private Button lastBtn;
	private Button saveBtn;
	private Button shareBtn;
	private Button backBtn;
	private Button cancelBtn;
	/**
	 * 
	 * 
	 * @param act
	 *            所属的Activity对象
	 * @param theme
	 *            Dialog的样式，去掉title，背景透明，Dialog的外部区域背景为灰暗效果
	 * @param gravity
	 *            Dialog的对齐方式, @See Gravity
	 * @param outside
	 *            Dialog，点击外部区域是否可以消失，true表示可以，false表示不可以
	 * @param cancelable
	 *            Dialog,点击返回键BACK后是否可以消失，true表示可以，false表示不可以
	 */
	public JHDSBackSelectDialog(DrawActivity act, int theme, int gravity, boolean outside, boolean cancelable) {
		super(act, theme);
		mAct = act;
		DisplayMetrics outMetrics = new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		int width = outMetrics.widthPixels - LEFT_AND_RIGHT_MARGINS;
		LayoutParams params = new LayoutParams(width, (int) (330*AppCommon.getInstance().metrics.density));
		mView = View.inflate(act, R.layout.dialog_back_select, null);
		setContentView(mView, params);
		Window window = getWindow();
		window.setGravity(gravity);
		setCanceledOnTouchOutside(outside);// 点击外部区域是否可以消失，true表示可以，false表示不可以
		setCancelable(cancelable);// 点击返回键BACK后是否可以消失，true表示可以，false表示不可以
		initView();
		
	}

	private void initView() {
		lastBtn = (Button) mView.findViewById(R.id.lastBtn);
		saveBtn = (Button) mView.findViewById(R.id.saveBtn);
		shareBtn = (Button) mView.findViewById(R.id.shareBtn);
		backBtn = (Button) mView.findViewById(R.id.backBtn);
		cancelBtn = (Button) mView.findViewById(R.id.cancelBtn);
		
		lastBtn.setOnClickListener(this);
		saveBtn.setOnClickListener(this);
		shareBtn.setOnClickListener(this);
		backBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (R.id.lastBtn == v.getId()) {
			mAct.backToLast();
		} else if (R.id.saveBtn == v.getId()) {
			mAct.backToSave();
		} else if (R.id.shareBtn == v.getId()) {
			mAct.backToShare();
		}else if (R.id.backBtn == v.getId()) {
			mAct.finish();
		}else if (R.id.cancelBtn == v.getId()) {
			
		}
		dismiss();
	}

}
