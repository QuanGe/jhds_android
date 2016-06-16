package com.quange.views;

import com.android.volley.Response.Listener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.quange.jhds.AppSetManager;
import com.quange.jhds.DrawActivity;
import com.quange.jhds.JHDSBabyAcibity;
import com.quange.jhds.JHDSLastDrawActivity;
import com.quange.jhds.JHDSMessageActivity;
import com.quange.jhds.JHDSSavedImagesActivity;
import com.quange.jhds.JHDSShopActivity;
import com.quange.jhds.R;
import com.quange.viewModel.JHDSAPIManager;
import com.umeng.analytics.MobclickAgent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MineFragment extends Fragment {
	private View fgmView;
	private RelativeLayout savedImageBtn;
	private RelativeLayout lastBtn;
	
	@ViewInject(R.id.rl_my_buy)
	private RelativeLayout shopBtn;
	@ViewInject(R.id.rl_my_message)
	private RelativeLayout messageBtn;
	@ViewInject(R.id.tv_appVersion)
	private TextView appVersionTv;
	@ViewInject(R.id.tv_appVersionRedTip)
	private View tv_appVersionRedTip;
	
	@ViewInject(R.id.iv_my_buyRedTip)
	private View iv_my_buyRedTip;
	
	@ViewInject(R.id.iv_my_messageRedTip)
	private View iv_my_messageRedTip;
	
	@ViewInject(R.id.iv_my_protect_babyRedTip)
	private View iv_my_protect_babyRedTip;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (fgmView == null) {
			fgmView = inflater.inflate(R.layout.fragment_mine, container, false);
			
			ViewUtils.inject(this,fgmView); // 注入view和事件
			
			savedImageBtn = (RelativeLayout)fgmView.findViewById(R.id.rl_my_save);
			savedImageBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), JHDSSavedImagesActivity.class);
				
					getActivity().startActivity(intent);
					
				}
			});
			
			lastBtn = (RelativeLayout)fgmView.findViewById(R.id.rl_my_last);
			lastBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), JHDSLastDrawActivity.class);
				
					getActivity().startActivity(intent);
					
				}
			});
			
//			 JHDSAPIManager.getInstance(this.getActivity()).fetchMessageTag(new Listener<String>(){
//					@Override
//					public void onResponse(String response) {
//						sendUpdateReTip();
//					
//					}
//				}, null);
			
			
		}
		
		
		updateRedTip();
		
		return fgmView;
	}
	
    
	@OnClick(R.id.rl_my_buy)
	public void OnShopClick(View view) {
		Intent intent = new Intent(getActivity(), JHDSShopActivity.class);
		
		getActivity().startActivity(intent);
		
		AppSetManager.setOldShopTag(AppSetManager.getNewShopTag());
		if(iv_my_buyRedTip.getVisibility() == View.VISIBLE)
		{
			iv_my_buyRedTip.setVisibility(View.INVISIBLE);
			sendClearRedTip();
		}
	}
	
	@OnClick(R.id.rl_protect_baby)
	public void OnProtectBabyClick(View view) {
		Intent intent = new Intent(getActivity(), JHDSBabyAcibity.class);
		
		getActivity().startActivity(intent);
		AppSetManager.setOldProtectBabyTag(AppSetManager.getNewProtectBabyTag());
		if(iv_my_protect_babyRedTip.getVisibility() == View.VISIBLE)
		{
			iv_my_protect_babyRedTip.setVisibility(View.INVISIBLE);
			sendClearRedTip();
		}
	}
	
	@OnClick(R.id.rl_my_message)
	public void OnMessageClick(View view) {
		Intent intent = new Intent(getActivity(), JHDSMessageActivity.class);
		getActivity().startActivity(intent);
		AppSetManager.setOldMessageTag(AppSetManager.getNewMessageTag());
		if(iv_my_messageRedTip.getVisibility() == View.VISIBLE)
		{
			iv_my_messageRedTip.setVisibility(View.INVISIBLE);
			sendClearRedTip();
		}
	}
	
	@OnClick(R.id.rl_my_set)
	public void OnAddStartClick(View view) {
		
		addStart();
	}
	
	public void addStart()
	{
		Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getActivity().startActivity(it);
		
		MobclickAgent.onEvent(getContext(), "mine_add_start");
		
	}
	
	public void sendClearRedTip()
	{
		if((iv_my_buyRedTip.getVisibility() == View.INVISIBLE)&&
				(iv_my_messageRedTip.getVisibility() == View.INVISIBLE)&&
				(iv_my_protect_babyRedTip.getVisibility() == View.INVISIBLE)&&
				(tv_appVersionRedTip.getVisibility() == View.INVISIBLE)
				)
		{
			sendUpdateReTip();
		}
	}
	
	private void sendUpdateReTip()
	{
		Intent it = new Intent(AppSetManager.AboutRedTipNoti);
		it.putExtra("clearRedTip", "true");
		
		
		this.getActivity().sendBroadcast(it);
	}
	
	
	private void updateRedTip()
	{
		tv_appVersionRedTip.setVisibility(View.INVISIBLE);
		try {
			appVersionTv.setGravity(Gravity.CENTER);
			PackageManager manager = this.getActivity().getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getActivity().getPackageName(), 0);
			String version = "当前版本:"+info.versionName;
			if(!AppSetManager.getNewAppVersion().equals(""))
			{
				
				if(Float.valueOf(info.versionName).floatValue() != Float.valueOf(AppSetManager.getNewAppVersion()).floatValue())
				{
					tv_appVersionRedTip.setVisibility(View.VISIBLE);
					version = version+"  点击更新到最新版本:"+AppSetManager.getNewAppVersion();
					appVersionTv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							addStart();
						}
					});
				}
				else
				{
					tv_appVersionRedTip.setVisibility(View.INVISIBLE);
				}
					
			}
			
			appVersionTv.setText(version);
		} catch (Exception e) {
		  e.printStackTrace();
		  appVersionTv.setText("当前版本:0.9");
		}
		
		if(AppSetManager.getOldShopTag().equals(""))
		{
			iv_my_buyRedTip.setVisibility(View.VISIBLE);
		}
		else
		{
			if(AppSetManager.getOldShopTag().equals(AppSetManager.getNewShopTag()))
			{
				iv_my_buyRedTip.setVisibility(View.INVISIBLE);
			}
			else
			{
				iv_my_buyRedTip.setVisibility(View.VISIBLE);
			}
		}
		
		if(AppSetManager.getOldMessageTag().equals(""))
		{
			iv_my_messageRedTip.setVisibility(View.VISIBLE);
		}
		else
		{
			if(AppSetManager.getOldMessageTag().equals(AppSetManager.getNewMessageTag()))
			{
				iv_my_messageRedTip.setVisibility(View.INVISIBLE);
			}
			else
			{
				iv_my_messageRedTip.setVisibility(View.VISIBLE);
			}
		}
		
		if(AppSetManager.getOldProtectBabyTag().equals(""))
		{
			iv_my_protect_babyRedTip.setVisibility(View.VISIBLE);
		}
		else
		{
			if(AppSetManager.getOldProtectBabyTag().equals(AppSetManager.getNewProtectBabyTag()))
			{
				iv_my_protect_babyRedTip.setVisibility(View.INVISIBLE);
			}
			else
			{
				iv_my_protect_babyRedTip.setVisibility(View.VISIBLE);
			}
		}
		
	}
	
}
