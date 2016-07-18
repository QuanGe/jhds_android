package com.quange.jhds;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.constant.WBConstants;
import com.umeng.analytics.MobclickAgent;

public class WebActivity extends Activity implements IWeiboHandler.Response{
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
	private String shareTitle = null;
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
		ShareCollectUtils.shareContent(this, shareTitle, url, null,0);
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
			shareTitle = bundle.getString("shareTitle", "");
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
	
	/**
     * @see {@link Activity#onNewIntent}
     */	
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        
        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        ShareCollectUtils.mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    /**
     * 接收微客户端博请求的数据。
     * 当微博客户端唤起当前应用并进行分享时，该方法被调用。
     * 
     * @param baseRequest 微博请求数据对象
     * @see {@link IWeiboShareAPI#handleWeiboRequest}
     */
    @Override
    public void onResponse(BaseResponse baseResp) {
        if(baseResp!= null){
            switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                Toast.makeText(this, R.string.weibosdk_demo_toast_share_success, Toast.LENGTH_LONG).show();
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                Toast.makeText(this, R.string.weibosdk_demo_toast_share_canceled, Toast.LENGTH_LONG).show();
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                Toast.makeText(this, 
                        getString(R.string.weibosdk_demo_toast_share_failed) + "Error Message: " + baseResp.errMsg, 
                        Toast.LENGTH_LONG).show();
                break;
            }
        }
    }
}
