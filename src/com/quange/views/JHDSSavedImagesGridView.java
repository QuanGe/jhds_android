package com.quange.views;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import com.quange.jhds.JHDSSavedImagesActivity;
import com.quange.jhds.PhotosActivity;
import com.quange.jhds.R;

import com.quange.viewModel.JHDSSaveImagesAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.GridView;

public class JHDSSavedImagesGridView implements OnItemClickListener { 
	private View mView;
	private JHDSSavedImagesActivity mAct;
	private String appPath;
	private GridView lList;
	private JHDSSaveImagesAdapter lAdapter;
	private List<String> mlList = new ArrayList<String>();
	
	public JHDSSavedImagesGridView(Activity act)
	{
		super();
		this.mAct = (JHDSSavedImagesActivity)act;
		mView = View.inflate(mAct, R.layout.view_saved, null);
		appPath = mAct.getApplicationContext().getFilesDir().getAbsolutePath();
		initView();
	}
	
	
	
	/**
	 * 得到视图
	 * 
	 * @return
	 */
	public View getView() {
		return mView;
	}

	
	// 初始化设置
		private void initView() {
			
			String path = getSDPath()+"/jhds/jianhuadashi";
			File f = new File(path);        
			if (!f.exists()) {  
				mAct.showError();
            }  
			else
			{
				File file[] = f.listFiles();
				for (int i=0; i < file.length; i++)
				{
					mlList.add(0,file[i].getAbsolutePath());
				}
				lList = (GridView) mView.findViewById(R.id.gv_save);
				
				lAdapter = new JHDSSaveImagesAdapter(mAct, mlList);
				
				lList.setAdapter(lAdapter);
				
			
				lList.setOnItemClickListener(this);
			}
			
			
		}
		

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			try {
				Bundle bundle = new Bundle();
				// bundle.putString("title",
				// this.getResources().getString(R.string.action_find_password));
				String allUrl = "";
				for (String url : mlList)
				{
					allUrl = allUrl+url+"*";
				}
				bundle.putString("allUrl", allUrl);
				bundle.putString("curUrl", mlList.get(position));
				Intent intent = new Intent(this.mAct, PhotosActivity.class);
				intent.putExtras(bundle);
				this.mAct.startActivity(intent);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		/** 
	     * 获取SDK路径 
	     * @return 
	     */  
	    public String getSDPath(){   
	           File sdDir = null;   
	           boolean sdCardExist = Environment.getExternalStorageState()     
	                               .equals(android.os.Environment.MEDIA_MOUNTED);   //判断sd卡是否存在   
	           if   (sdCardExist)     
	           {                                 
	             sdDir = Environment.getExternalStorageDirectory();//获取跟目录   
	          }     
	           return sdDir.toString();   
	             
	    }  
}
