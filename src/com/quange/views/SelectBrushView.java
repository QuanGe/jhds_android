package com.quange.views;

import android.app.Activity;
import android.app.Dialog;

import org.quange.game.DrawActivity;
import org.quange.game.R;

import com.larswerkman.holocolorpicker.ColorPicker;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;

public class SelectBrushView extends Dialog implements OnClickListener {
	protected static final String TAG = "ClearCacheDialog";
	private static final int LEFT_AND_RIGHT_MARGINS = 80;
	private DrawActivity mAct;
	private View mView;
	private Button btnCancel;
	private Button btnConfirm;
	private ColorPicker colorPicker;

	/**
	 * 发表课程评论的弹出对话框
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
	public SelectBrushView(DrawActivity act, int theme, int gravity, boolean outside, boolean cancelable) {
		super(act, theme);
		mAct = act;
		DisplayMetrics outMetrics = new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		int width = outMetrics.widthPixels - LEFT_AND_RIGHT_MARGINS;
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mView = View.inflate(act, R.layout.dialog_select_brush, null);
		setContentView(mView, params);
		Window window = getWindow();
		window.setGravity(gravity);
		setCanceledOnTouchOutside(outside);// 点击外部区域是否可以消失，true表示可以，false表示不可以
		setCancelable(cancelable);// 点击返回键BACK后是否可以消失，true表示可以，false表示不可以
		initView();
	}

	private void initView() {
		colorPicker = (ColorPicker)findViewById(R.id.picker);
		//To get the color
		colorPicker.getColor();

		//To set the old selected color u can do it like this
		colorPicker.setOldCenterColor(colorPicker.getColor());
		// adds listener to the colorpicker which is implemented
		//in the activity
		colorPicker.setOnColorChangedListener(mAct);

		//to turn of showing the old color
		colorPicker.setShowOldCenterColor(false);
	}

	@Override
	public void onClick(View v) {
	
	}

}
