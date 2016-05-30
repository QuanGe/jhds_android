package com.quange.jhds;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.quange.model.JHDSMessageModel;
import com.quange.viewModel.JHDSAPIManager;
import com.quange.viewModel.JHDSMessageAdapter;
import com.quange.views.JHDSErrorMessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class JHDSMessageActivity extends Activity implements OnItemClickListener {
	@ViewInject(R.id.errorMessage)
	private JHDSErrorMessage errorMessage;
	 @ViewInject(R.id.message_list)
	 private PullToRefreshListView messgaeList;
	 private List<JHDSMessageModel> data = new ArrayList<JHDSMessageModel>();
	 private JHDSMessageAdapter lAdapter;
	 @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message);
		ViewUtils.inject(this); // 注入view和事件
		
		lAdapter = new JHDSMessageAdapter(this,data);
		messgaeList.setAdapter(lAdapter);
		
		messgaeList.setMode(Mode.PULL_FROM_START);

		messgaeList.setOnRefreshListener(orfListener2());
	
		messgaeList.setOnItemClickListener(this);	
		firstLoadData();
		errorMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				firstLoadData();
				errorMessage.setVisibility(View.GONE);
			}
		});
	 }
	 public void refresh(final boolean isRefresh) {
		
		 if(AppCommon.getInstance().isConnect(this))
		 {
			JHDSAPIManager.getInstance(null).fetchMessageList(new Listener<List<JHDSMessageModel>>(){
				@Override
				public void onResponse(List<JHDSMessageModel> response) {
					if(isRefresh)
						data.clear();
					data.addAll(response);
					lAdapter.notifyDataSetChanged();
					
				}
				
				
			} ,  new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					System.out.println(error);
				
				}
			});
					
					
				
			
		 }
		 else
		 {
			 showError("您当前没有网络，请检查网络后点击屏幕重新获取数据");
			 
		 }
			
		}


		private OnRefreshListener2<ListView> orfListener2() {
			return new OnRefreshListener2<ListView>() {
				@Override
				public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
					
					refresh(true);
					stoprefresh(refreshView);
				}

				@Override
				public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
					
					refresh(false);
					stoprefresh(refreshView);
				}
			};
		}

		protected void stoprefresh(final PullToRefreshBase<ListView> refreshView) {
			refreshView.postDelayed(new Runnable() {
				@Override
				public void run() {
					refreshView.onRefreshComplete();
				}
			}, 2000);
		}

	 @Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			JHDSMessageModel sm = data.get(position-1);
			Bundle bundle = new Bundle();
			bundle.putString("url", sm.detail);
			bundle.putString("title", sm.title);
			Intent intent = new Intent(this, WebActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	 public void firstLoadData()
		{
			if(data.size()==0)
			{
				messgaeList.post(new Runnable() {
				      @Override public void run() {
				    	  messgaeList.setRefreshing(true);
				      }
				 });
			}
		}
	 @OnClick(R.id.backBtn)
		public void OnBackClick(View view) {
			
			finish();
		}
		
		public void showError(String ms)
		{
			errorMessage.setVisibility(View.VISIBLE);
			errorMessage.updateMessage(ms);
		}
}
