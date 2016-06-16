package com.quange.jhds;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.umeng.analytics.MobclickAgent;

public class WebActivity extends Activity{
	private String TAG = "WebActivity";

	@ViewInject(R.id.webview)
	private WebView webview;
	@ViewInject(R.id.headerTitle)
	private TextView tv_title;
	
	@ViewInject(R.id.share)
	private ImageView share;
	
	@ViewInject(R.id.shareBtn)
	private RelativeLayout shareBtn;
	private String url = null;
	private String title = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_layout);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		ViewUtils.inject(this); // 注入view和事件
		getInfo();
		init();
		setView();
	
		shareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				share();
			}
		});
	}
	
	private void share()
	{
		ShareCollectUtils.shareContent(this, title, url, null);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(this);
	}

	@Override
	public void onStop() {
		
		super.onStop();
	}

	@Override
	public void onDestroy() {
	
		super.onDestroy();
	}

	private void init() {
	
		WebSettings webSettings = webview.getSettings();
		setSettings(webSettings);
	}

	private void getInfo() {
		try {
			Bundle bundle = this.getIntent().getExtras();
			
			url = bundle.getString("url", "");
			title = bundle.getString("title", "");
			tv_title.setText(title);
			canShare(bundle.getBoolean("canShare",false)); 
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	protected void setSettings(WebSettings webSettings) {
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setSupportZoom(true); // 设置可以支持缩放
		webSettings.setBuiltInZoomControls(true); // 设置启用内置的缩放控件
		webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR); // 设置默认缩放方式尺寸是far
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setView() {
		tv_title.setText(title);

		webview.loadUrl(url, getHeaders());
		

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webview.loadUrl(url, getHeaders());
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			
			}
		});

		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress > 50) {
					
				}
			}
		});

	}

	@OnClick(R.id.backBtn)
	public void BackOnclick(View view) {
	
		finish();

	}

	@OnClick(R.id.tv_title)
	public void BackTitleOnclick(View view) {
		
		finish();
	}

	

	@Override
	public boolean onKeyDown(int keyCoder, KeyEvent event) {
	
		return super.onKeyDown(keyCoder, event);
	}

	private Map<String, String> getHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("platform", "Android");
		return headers;
	}
	
	public void canShare(boolean can)
	{
		if(!can)
		{
			share.setVisibility(View.GONE);
			shareBtn.setVisibility(View.GONE);
		}
		else
		{
			share.setVisibility(View.VISIBLE);
			shareBtn.setVisibility(View.VISIBLE);
		}
	}
}
