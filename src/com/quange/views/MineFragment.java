package com.quange.views;

import com.quange.jhds.DrawActivity;
import com.quange.jhds.JHDSLastDrawActivity;
import com.quange.jhds.JHDSSavedImagesActivity;
import com.quange.jhds.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class MineFragment extends Fragment {
	private View fgmView;
	private RelativeLayout savedImageBtn;
	private RelativeLayout lastBtn;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (fgmView == null) {
			fgmView = inflater.inflate(R.layout.fragment_mine, container, false);
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
		}
		
		return fgmView;
	}
}
