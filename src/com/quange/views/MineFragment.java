package com.quange.views;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.quange.jhds.DrawActivity;
import com.quange.jhds.JHDSLastDrawActivity;
import com.quange.jhds.JHDSMessageActivity;
import com.quange.jhds.JHDSSavedImagesActivity;
import com.quange.jhds.JHDSShopActivity;
import com.quange.jhds.R;

import android.content.Intent;
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
			
			try {
				appVersionTv.setGravity(Gravity.CENTER);
				PackageManager manager = this.getActivity().getPackageManager();
				PackageInfo info = manager.getPackageInfo(this.getActivity().getPackageName(), 0);
				String version = "当前版本:"+info.versionName;
				appVersionTv.setText(version);
			} catch (Exception e) {
			  e.printStackTrace();
			  appVersionTv.setText("当前版本:0.9");
			}
		}
		
		return fgmView;
	}
	
	@OnClick(R.id.rl_my_buy)
	public void OnShopClick(View view) {
		Intent intent = new Intent(getActivity(), JHDSShopActivity.class);
		
		getActivity().startActivity(intent);
	}
	
	@OnClick(R.id.rl_my_message)
	public void OnMessageClick(View view) {
		Intent intent = new Intent(getActivity(), JHDSMessageActivity.class);
		
		getActivity().startActivity(intent);
	}
	
	@OnClick(R.id.rl_my_set)
	public void OnAddStartClick(View view) {
		Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getActivity().startActivity(it);

	}
}
