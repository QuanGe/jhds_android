package com.quange.jhds;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import com.umeng.analytics.MobclickAgent;

import uk.co.senab.photoview.PhotoView;

import android.app.Activity;
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



public class PhotosActivity extends Activity {
	private ViewPager photoViewPager;
	private TextView indexTV;
	private Button shareBtn;
	private String curUrl;
	private String[] allUrls ;
	
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
	            
				
	            // Now just add PhotoView to ViewPager and return it
	            ((ViewPager) container).addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	            Bitmap bitmap = getLoacalBitmap(allUrls[position]); //从本地取图片(在cdcard中获取)  //
	            photoView.setImageBitmap(bitmap);
	            return photoView;
			}


	}
	
	/**
	    * 加载本地图片
	    * @param url
	    * @return
	    */
	    public static Bitmap getLoacalBitmap(String url) {
	         try {
	              FileInputStream fis = new FileInputStream(url);
	              return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片        

	           } catch (FileNotFoundException e) {
	              e.printStackTrace();
	              return null;
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
			allUrls = all.split("\\*");
			
		}
		MobclickAgent.onEvent(this, "girls_detail");
		
		shareBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			
			}
		});
	
		photoViewPager.setAdapter(new CurriAdapter() );
		
		for(int i = 0;i<allUrls.length;i++)
		{
			if(allUrls[i].equals(curUrl))
			{
				photoViewPager.setCurrentItem(i);
				indexTV.setText((i+1)+"/"+allUrls.length);
				break;
			}
		}
		photoViewPager.addOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
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
	
}
