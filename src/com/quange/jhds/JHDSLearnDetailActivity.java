package com.quange.jhds;
import java.util.List;

import com.quange.model.JHDSCopyModel;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class JHDSLearnDetailActivity extends DrawActivity {
	private ViewPager photoViewPager;
	private RelativeLayout topView;
	private Button tryBtn;
	private String[] detail ;
	public class CurriAdapter extends PagerAdapter {

		private View mCurrentView;
	    private String[] urls;
	    public CurriAdapter(String[] imageUrls) {
			
			this.urls = imageUrls;
		}
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
				return urls.length;
			}

			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView((View) object);
			}

			public Object instantiateItem(View container, int position) {
				//indexTV.setText(position+"/"+allUrls.length);
				//return ((ViewPager) container).getChildAt(position);
				ImageView photoView = new ImageView(container.getContext());
				
				String url = "http://quangelab.com/images/jhds/learn/"+urls[position]+".jpg";
				AppCommon.getInstance().imageLoader.displayImage(url, photoView, AppCommon.getInstance().options);
				photoView.setOnClickListener(new OnClickListener() {   

					@Override
					public void onClick(View v) {
						if(tryBtn.getVisibility() == View.INVISIBLE)
						{
							resetTopView();
						}
						else
						{
							
						}
						
					}
				});
				
				((ViewPager) container).addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				return photoView;
			}


	}
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
		
			detail = bundle.getString("detail").split(",");
		
			
		}
		
		buildCopyView(detail);
	}
	
	public void buildCopyView(String[] detail)
	{
	
	
		topView = (RelativeLayout) this.findViewById(R.id.topView);
		topView.setVisibility(View.VISIBLE);
		
		photoViewPager = new ViewPager(this);
		/*
		for(int i = 0;i<detail.length;i++)
		{
			ImageView photoView = new ImageView(this);
			String url = "http://quangelab.com/images/jhds/learn/"+detail[i]+".jpg";
			AppCommon.getInstance().imageLoader.displayImage(url, photoView, AppCommon.getInstance().options);
			photoView.setOnClickListener(new OnClickListener() {   

				@Override
				public void onClick(View v) {
					if(tryBtn.getVisibility() == View.INVISIBLE)
					{
						resetTopView();
					}
					else
					{
						
					}
					
				}
			});
			
			vp.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		}
		*/
		RelativeLayout.LayoutParams imgelp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		topView.addView(photoViewPager, imgelp);
		photoViewPager.setAdapter(new CurriAdapter(detail) );
		
		
		tryBtn =new Button(this);
		RelativeLayout.LayoutParams tryBtnlp = new RelativeLayout.LayoutParams((int)(100*AppCommon.getInstance().metrics.density),(int) (35*AppCommon.getInstance().metrics.density));
		tryBtnlp.addRule(RelativeLayout.CENTER_HORIZONTAL); 
		tryBtnlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM); 
		tryBtnlp.bottomMargin = 20;
		tryBtn.setText("试一试");
		tryBtn.setBackgroundResource(R.drawable.btn_cancel);
		tryBtn.setOnClickListener(new OnClickListener() {   

			@Override
			public void onClick(View v) {
				RelativeLayout.LayoutParams lp = (LayoutParams) topView.getLayoutParams();
				lp.width = AppCommon.getInstance().screenWidth/4;
				lp.height = AppCommon.getInstance().screenHeight/4;
				((RelativeLayout)topView.getParent()).updateViewLayout(topView, lp);
				
				tryBtn.setVisibility(View.INVISIBLE);
				selectBtn.setVisibility(View.VISIBLE);
				brushView.setEnable(true);
				
			}
		});
		topView.addView(tryBtn, tryBtnlp);
		
		resetTopView();
		
	}
	public void resetTopView()
	{
		RelativeLayout.LayoutParams lp = (LayoutParams) topView.getLayoutParams();
		lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
		lp.height = RelativeLayout.LayoutParams.MATCH_PARENT;
		selectBtn.setVisibility(View.INVISIBLE);
		brushView.setEnable(false);
		((RelativeLayout)topView.getParent()).updateViewLayout(topView, lp);
		tryBtn.setVisibility(View.VISIBLE);
	}
}
