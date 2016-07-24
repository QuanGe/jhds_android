package com.quange.jhds;

import java.util.Timer;
import java.util.TimerTask;

import com.android.volley.Response.Listener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.quange.viewModel.JHDSAPIManager;
import com.quange.views.CircularProgressButton;
import com.quange.views.JHDSGuideView;
import com.quange.views.JHDSTextGuideView;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class SplashActivity extends Activity implements OnClickListener{
	 @ViewInject(R.id.guidePages)
	 private ViewPager guidePages;
	 @ViewInject(R.id.guidePagesBox)
	 private RelativeLayout guidePagesBox;
	 @ViewInject(R.id.btn_skip_splashimage)
	 private CircularProgressButton skipBtn;
	 @ViewInject(R.id.iv_splash)
	 private ImageView splashImg;
	// 底部小点的图片
	 private ImageView[] points;
	// 记录当前选中位置
	 private int currentIndex;
	 private String[] allInfo = {"给你一块画板\n画出你的天空","海量简画教程\n持续不断更新","临摹优秀作品\n提升自己笔格","手机摇一摇\n重新来画","音量键"};
	 private int iconArray[] = { R.drawable.splash0, R.drawable.splash1,
				R.drawable.splash2, R.drawable.splash3 };
	 
	 private Timer timer;
	 public class guideAdapter extends PagerAdapter {

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
				return allInfo.length;
			}

			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView((View) object);
			}

			public Object instantiateItem(View container, int position) {
				MobclickAgent.onEvent(getApplicationContext(), "splash_guide_look");
				
				if(position!=4)
				{
					JHDSGuideView photoView = new JHDSGuideView(container.getContext());
					photoView.contentImageView.setImageResource(iconArray[position]);
					photoView.tv_info.setText(allInfo[position]);
		            ((ViewPager) container).addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		           
		            return photoView;
				}
				else
				{
					JHDSTextGuideView photoView = new JHDSTextGuideView(container.getContext());
					//photoView.tv_info.setText(allInfo[position]);
		            ((ViewPager) container).addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		           
		            return photoView;
				}
			}


		}
	 @SuppressLint("NewApi") public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_splash);
			ViewUtils.inject(this); // 注入view和事件
			MobclickAgent.onEvent(this, "splash");
			
			JHDSAPIManager.getInstance(this).fetchWeiboTag(new Listener<String>(){
				@Override
				public void onResponse(String response) {
					
				}
			}, null);
			
			//splash
			{
				GradientDrawable skipBack =  new GradientDrawable();
				skipBack.setCornerRadius(40*AppCommon.getInstance().metrics.density/2);
				skipBack.setColor(0x88555555);
	        	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
	        		skipBtn.setBackground(skipBack);
	        	else
	        		skipBtn.setBackgroundDrawable(skipBack);
	        	
	        	timer = new Timer(); // 实例化Timer定时器对象
	        	if(AppSetManager.getFirstUseApp() !=1)
					timer.schedule(new TimerTask() { // schedule方法(安排,计划)需要接收一个TimerTask对象和一个代表毫秒的int值作为参数
								@Override
								public void run() {
									startMainActivity();
								}
							}, 2600);
				
				String splashUrl = AppSetManager.getSplashImgUrl();
				if(!splashUrl.equals(""))
				{
					String localSplashUrl = AppCommon.getInstance().getSplashLocalUrl(splashUrl);
					Bitmap b = AppCommon.getInstance().getLoacalBitmap(localSplashUrl,AppCommon.getInstance().screenWidth,AppCommon.getInstance().screenHeight);
					if(b != null)
					{
						splashImg.setImageBitmap(b);
					}
					else
						AppCommon.getInstance().imageLoader.displayImage(splashUrl, splashImg, AppCommon.getInstance().options);
					
					splashImg.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							clickSplash();
							
							
						}
					});
				}
			}
			//guide
			{
				guidePages.setAdapter(new guideAdapter() );
				initPoint();
				if(AppSetManager.getFirstUseApp() ==1)
					guidePagesBox.setVisibility(View.VISIBLE);
				else
					guidePagesBox.setVisibility(View.GONE);
				guidePages.addOnPageChangeListener(new OnPageChangeListener() {
	
					@Override
					public void onPageSelected(int position) {
						// TODO Auto-generated method stub
						setCurDot(position);
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
	 
	 public void clickSplash()
	 {
		 MobclickAgent.onEvent(this, "splash_img_load");
		 switch(AppSetManager.getSplashType())
			{
				case -1:
					break;
				case 0:
					break;
				case 1:
				{
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppSetManager.getSplashDetail()));
					 if (AppCommon.getInstance().isAppInstalled(getApplicationContext(), "com.taobao.taobao")) {
					     intent.setClassName("com.taobao.taobao", "com.taobao.tao.shop.router.ShopUrlRouterActivity");
					 }
					 startActivity(intent);
				}
				
					break;
			}
		 
		 
	 }
	 
	 /**
	     * 初始化底部小点
	     */
	    private void initPoint() {
	        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);

	        points = new ImageView[5];

	        // 循环取得小点图片
	        for (int i = 0; i < 5; i++) {
	            // 得到一个LinearLayout下面的每一个子元素
	            points[i] = (ImageView) linearLayout.getChildAt(i);
	            // 默认都设为灰色
	            points[i].setEnabled(true);
	            // 给每个小点设置监听
	            points[i].setOnClickListener(this);
	            // 设置位置tag，方便取出与当前位置对应
	            points[i].setTag(i);
	        }

	        // 设置当面默认的位置
	        currentIndex = 0;
	        // 设置为白色，即选中状态
	        points[currentIndex].setEnabled(false);
	    }

	    

	    @Override
	    public void onClick(View v) {
	        int position = (Integer) v.getTag();
	        setCurView(position);
	        setCurDot(position);
	    }

	    /**
	     * 设置当前页面的位置
	     */
	    private void setCurView(int position) {
	        if (position < 0 || position >= 5) {
	            return;
	        }
	        guidePages.setCurrentItem(position);
	    }

	    /**
	     * 设置当前的小点的位置
	     */
	    private void setCurDot(int positon) {
	        if (positon < 0 || positon > 5 - 1 || currentIndex == positon) {
	            return;
	        }
	        points[positon].setEnabled(false);
	        points[currentIndex].setEnabled(true);

	        currentIndex = positon;
	    }
	    
	    private void startMainActivity() {
			 final Intent it = new Intent(this, MainActivity.class);
			 startActivity(it);
			 
		 }
	    
	    @OnClick(R.id.btn_skip_splashimage)
	    private void skipToMain(CircularProgressButton btn)
	    {
	    	btn.cancel();
	    	timer.cancel();
	    	startMainActivity();
	    }
}
