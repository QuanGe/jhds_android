package com.quange.jhds;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.quange.viewModel.JHDSAPIManager;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.constant.WBConstants;
import com.umeng.analytics.MobclickAgent;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class PhotosActivity extends Activity implements IWeiboHandler.Response{
	private ViewPager photoViewPager;
	private TextView indexTV;
	private Button shareBtn;
	private String curUrl;
	private String[] allUrls ;
	private int index = 0;
	public class CurriAdapter extends PagerAdapter {

		private View mCurrentView;
	    
	    @Override
	    public void setPrimaryItem(ViewGroup container, int position, Object object) {
	        mCurrentView = (View)object;
	    }
	                                             
	    public View getPrimaryItem() {
	        return mCurrentView;
	    }
			public boolean isViewFromObject(View view, Object o) {
				return view == o;
			}

			public int getCount() {
				return allUrls.length;
			}

			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView((View) object);
			}

			public Object instantiateItem(View container, int position) {
				//indexTV.setText((((ViewPager) container).getCurrentItem()+1)+"/"+allUrls.length);
				PhotoView photoView = new PhotoView(container.getContext());
				photoView.setOnViewTapListener(tl);
				
	            // Now just add PhotoView to ViewPager and return it
	            ((ViewPager) container).addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	            String subStr = allUrls[position].substring(0, 4);
	            if(subStr.equals("http"))
	            {
	            	AppCommon.getInstance().imageLoader.displayImage(allUrls[position], photoView, AppCommon.getInstance().options);
	            }
	            else
	            {
		            Bitmap bitmap = AppCommon.getInstance().getLoacalBitmap(allUrls[position],AppCommon.getInstance().screenWidth,AppCommon.getInstance().screenHeight); //从本地取图片(在cdcard中获取)  //
		            photoView.setImageBitmap(bitmap);
	            }
	            return photoView;
			}


	}
	
	
	    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        photoViewPager = (ViewPager)findViewById(R.id.photosViewPager);
        indexTV = (TextView)findViewById(R.id.indexTextView);
        shareBtn = (Button)findViewById(R.id.shareBtn);
       
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			curUrl = bundle.getString("curUrl");
			String all = bundle.getString("allUrl");
			if(all != null)
				allUrls = all.split("\\*");
			else
				allUrls = new String[0];
			
		}
		String subStr = curUrl.substring(0, 4);
        if(subStr.equals("http"))
        {
        	shareBtn.setVisibility(View.INVISIBLE);
        }
		MobclickAgent.onEvent(this, "girls_detail");
		
		shareBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				share();
			}
		});
		
		
	
		photoViewPager.setAdapter(new CurriAdapter() );
		
		for(int i = 0;i<allUrls.length;i++)
		{
			if(allUrls[i].equals(curUrl))
			{
				index = i;
				photoViewPager.setCurrentItem(i);
				indexTV.setText((i+1)+"/"+allUrls.length);
				break;
			}
		}
		photoViewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				index = position;
				indexTV.setText((position+1)+"/"+allUrls.length);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	private void share()
	{
		ShareCollectUtils.shareContent(this, "简画大师", allUrls[index], null,1);
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
    
    private OnViewTapListener tl = new OnViewTapListener()
    {
    	@Override
    	public void onViewTap(View view, float x, float y)
    	{
    		finish();
    	}
    };
	
}
