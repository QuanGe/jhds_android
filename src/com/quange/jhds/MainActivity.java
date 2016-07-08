package com.quange.jhds;



import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.android.volley.Response.Listener;
import com.quange.model.JHDSMessageModel;
import com.quange.viewModel.JHDSAPIManager;
import com.quange.views.CopyFragment;
import com.quange.views.LearnFragment;
import com.quange.views.MineFragment;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;


@SuppressLint("NewApi") public class MainActivity extends FragmentActivity {
	private Button drawbtn;
	public static FragmentTabHost mTabHost;
	private Class<?> fragmentArray[] = {CopyFragment.class,LearnFragment.class,MineFragment.class};
	private int titleArray[] = {R.string.copy,R.string.learn,R.string.mine};
	private Resources re;
	private TextView tv[] = { null, null,null };
	private int iconArray[] = {R.drawable.btn_copy_drawable,R.drawable.btn_learn_drawable,R.drawable.btn_mine_drawable};
	protected static boolean isQuit = false;
	private View theView;
	private PushAgent mPushAgent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mPushAgent = PushAgent.getInstance(this);
        //开启推送并设置注册的回调处理
        mPushAgent.enable(new IUmengRegisterCallback() {

        	@Override
        	public void onRegistered(final String registrationId) {
        	      new Handler().post(new Runnable() {
                          @Override
                          public void run() {
                    	       //onRegistered方法的参数registrationId即是device_token
                        	  Log.d("device_token", registrationId);
                          }
                 });
             }
        });
        
        if(AppSetManager.getFirstUseApp()==1)
        	AppSetManager.setFirstUseApp(0);
        JHDSAPIManager.getInstance(this).fetchSplashData(null, null);
        drawbtn = (Button)findViewById(R.id.drawBtn);
        
        GradientDrawable shape =  new GradientDrawable();
        DisplayMetrics dm = new DisplayMetrics();  
		dm = this.getApplicationContext().getResources().getDisplayMetrics(); 
        shape.setCornerRadius( 35 *dm.density);
        shape.setColor(0xff119B39);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        	drawbtn.setBackground(shape);
        else
        	drawbtn.setBackgroundDrawable(shape);
        drawbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startMainActivity();
			}
		});
        
        init();
        setupTabView();
        
        aboutNoti();
        
        JHDSAPIManager.getInstance(this).fetchMessageTag(new Listener<String>(){
			@Override
			public void onResponse(String response) {
				Intent it = new Intent(AppSetManager.AboutRedTipNoti);
				it.putExtra("updateRedTip", "true");
				sendBroadcast(it);
			
				//updateRedTip();
			}
		}, null);
    }
    private void init() {
		re = this.getResources();
		
	}
    
    private BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String someargv = intent.getStringExtra("clearRedTip");
			String updateRedTip = intent.getStringExtra("updateRedTip");
			if(someargv != null)
			{
				if(someargv.equals("true"))
				{
					//
					View redTip = theView.findViewById(R.id.tv_mineRedTip);
					redTip.setVisibility(View.INVISIBLE);
				}
			}
			if(updateRedTip != null)
			{
				if(updateRedTip.equals("true"))
				{
					updateRedTip();
				}
			}
		}

	};
	
    private void aboutNoti()
    {
    	
    	IntentFilter ifilter = new IntentFilter(AppSetManager.AboutRedTipNoti);
    	registerReceiver(br, ifilter);
    }
    
    private void updateRedTip()
    {
    	//
		View redTip = theView.findViewById(R.id.tv_mineRedTip);
		if(AppSetManager.getOldShopTag().equals(""))
			redTip.setVisibility(View.VISIBLE);
		else
		{
			try {
				PackageManager manager = this.getPackageManager();
				PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
				if(((Float.valueOf(info.versionName).floatValue() < Float.valueOf(AppSetManager.getNewAppVersion()).floatValue()) )||
				(!AppSetManager.getOldShopTag().equals(AppSetManager.getNewShopTag()))||
				(!AppSetManager.getOldMessageTag().equals(AppSetManager.getNewMessageTag()))||
				(!AppSetManager.getOldProtectBabyTag().equals(AppSetManager.getNewProtectBabyTag()))
				
						)
				{
					redTip.setVisibility(View.VISIBLE);
				}
				else
				{
					redTip.setVisibility(View.INVISIBLE);
				}
			
			} catch (Exception e) {
				  e.printStackTrace();
				  
				}
		}
    }
    
    private void setupTabView() {
    	mTabHost = (FragmentTabHost) findViewById(R.id.tabhost); 
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		mTabHost.getTabWidget().setDividerDrawable(null);
		int count = fragmentArray.length;
		for (int i = 0; i < count; i++) {
			TabHost.TabSpec tabSpec;
			theView = getTabItemView(i);
			tabSpec = mTabHost.newTabSpec(re.getString(titleArray[i])).setIndicator(theView);
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
		}

		updateRedTip();
		
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				
				if(tabId.equals("我的"))
				{
					drawbtn.setVisibility(View.GONE);
					MobclickAgent.onEvent(getApplicationContext(), "mine");
				}
				else
				{
					if(tabId.equals("教程"))
						MobclickAgent.onEvent(getApplicationContext(), "copy");
					else
						MobclickAgent.onEvent(getApplicationContext(), "learn");
					drawbtn.setVisibility(View.VISIBLE);
				}
				setTvTextColor(tabId);
			}
		});
    }
    
 // 底部导航的文本颜色
	protected void setTvTextColor(String tabId) {
		for (int i = 0; i < tv.length; i++) {
			if (tv[i].getText().toString().equals(tabId)) {
				tv[i].setTextColor(re.getColor(R.color.color_two));
			} else {
				tv[i].setTextColor(re.getColor(R.color.black_deep));
			}
		}
	}
	 private View getTabItemView(int index) {
			LayoutInflater layoutInflater = LayoutInflater.from(this);
			int lyTab = R.layout.tab_item_view;
			View view = layoutInflater.inflate(lyTab, null);

			ImageView imageView = (ImageView) view.findViewById(R.id.iv_icon);
			imageView.setImageResource(iconArray[index]);
			View redTip = view.findViewById(R.id.tv_mineRedTip);
			redTip.setVisibility(View.GONE);
			TextView textView = (TextView) view.findViewById(R.id.tv_icon);
			tv[index] = textView;
			textView.setText(re.getString(titleArray[index]));
			if (index == 0) {
				textView.setTextColor(re.getColor(R.color.color_two));
			} else {
				textView.setTextColor(re.getColor(R.color.black_deep));
			}
			return view;
		}
 		
    private void startMainActivity() {
		 final Intent it = new Intent(this, DrawActivity.class);
		 startActivity(it);
		 
	 }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			boolean flag = false;
			if (isQuit) {
				// Intent home = new Intent(Intent.ACTION_MAIN);
				// home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// home.addCategory(Intent.CATEGORY_HOME);
				// startActivity(home);
				// Process.killProcess(Process.myPid());
				finish();
			} else {
				Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
				isQuit = true;
				Timer timer = new Timer(); // 实例化Timer定时器对象
				timer.schedule(new TimerTask() { // schedule方法(安排,计划)需要接收一个TimerTask对象和一个代表毫秒的int值作为参数
							@Override
							public void run() {
								isQuit = false;
							}
						}, 3000);
			}
			return flag;
		}
		return super.onKeyDown(keyCode, event);

	}
    
    public void onResume() {
    	super.onResume();
    	MobclickAgent.onResume(this);
    	}
    	public void onPause() {
    	super.onPause();
    	MobclickAgent.onPause(this);
    	}
}
