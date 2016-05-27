package com.quange.jhds;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.quange.views.JHDSErrorMessage;
import com.quange.views.JHDSSavedImagesGridView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
public class JHDSSavedImagesActivity extends Activity {
	
	@ViewInject(R.id.backBtn)
	private ImageView backBtn;
	@ViewInject(R.id.headerTitle)
	private TextView headerTitle;
	@ViewInject(R.id.errorMessage)
	private JHDSErrorMessage errorMessage;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savedimages);
        LinearLayout content = (LinearLayout)findViewById(R.id.content);
        ViewUtils.inject(this); // 注入view和事件
        
        JHDSSavedImagesGridView si = new JHDSSavedImagesGridView(this);
        content.addView(si.getView());
        
	}
	@OnClick(R.id.backBtn)
	public void OnBackClick(View view) {
		
		finish();
	}
	
	public void showError()
	{
		errorMessage.setVisibility(View.VISIBLE);
		errorMessage.updateMessage("您还没有保存过任何作品哦，请返回并去尝试使用画板吧");
	}
	
}
