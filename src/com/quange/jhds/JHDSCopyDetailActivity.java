package com.quange.jhds;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class JHDSCopyDetailActivity  extends DrawActivity{

	private RelativeLayout topView;
	private Button tryBtn;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String detail = "";
        Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
		
			detail = bundle.getString("detail");
		
			
		}
		
		buildCopyView(detail);
	}
	
	public void buildCopyView(String detail)
	{
	
	
		topView = (RelativeLayout) this.findViewById(R.id.topView);
		topView.setVisibility(View.VISIBLE);
		
		ImageView imge = new ImageView(this);
		RelativeLayout.LayoutParams imgelp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		topView.addView(imge, imgelp);
		imge.setOnClickListener(new OnClickListener() {   

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
		AppCommon.getInstance().imageLoader.displayImage(detail, imge, AppCommon.getInstance().options);
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