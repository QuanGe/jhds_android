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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.quange.model.*;
import com.quange.viewModel.*;
import com.quange.views.JHDSErrorMessage;
import com.umeng.analytics.MobclickAgent;

public class JHDSShopActivity extends Activity implements OnItemClickListener {
	
	@ViewInject(R.id.errorMessage)
	private JHDSErrorMessage errorMessage;
	 @ViewInject(R.id.shop_list)
	 private PullToRefreshListView shopList;
	 private List<JHDSShopModel> data = new ArrayList<JHDSShopModel>();
	 private int mCurPage = 1;
	 private JHDSShopAdapter lAdapter;
	 @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);
		ViewUtils.inject(this); // 注入view和事件
		
		MobclickAgent.onEvent(getApplicationContext(), "mine_shop");
		
		lAdapter = new JHDSShopAdapter(this,data);
		shopList.setAdapter(lAdapter);
		
		shopList.setMode(Mode.BOTH);

		shopList.setOnRefreshListener(orfListener2());
	
		shopList.setOnItemClickListener(this);
		shopList.setOnScrollListener(new OnScrollListener() {
			boolean isLastRow = false;

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// 滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次。
				// firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
				// visibleItemCount：当前能看见的列表项个数（小半个也算）
				// totalItemCount：列表项共数

				// 判断是否滚到最后一行
				if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
					isLastRow = true;
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 正在滚动时回调，回调2-3次，手指没抛则回调2次。scrollState = 2的这次不回调
				// 回调顺序如下
				// 第1次：scrollState = SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动
				// 第2次：scrollState = SCROLL_STATE_FLING(2)
				// 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
				// 第3次：scrollState = SCROLL_STATE_IDLE(0) 停止滚动
				// 当屏幕停止滚动时为0；当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1；
				// 由于用户的操作，屏幕产生惯性滑动时为2

				// 当滚到最后一行且停止滚动时，执行加载
				if (isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
					
					refresh(false);
					isLastRow = false;
				}
			}
		});
		
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
		 JHDSAPIManager.getInstance(this).fetchShopPageNum( 0, new Listener<String>(){
				@Override
				public void onResponse(String response) {
					int num = Integer.parseInt(response);
					mCurPage = isRefresh ? 1 : ++mCurPage ;
					if(num>=mCurPage)
					JHDSAPIManager.getInstance(null).fetchShopList(num-mCurPage, 0, new Listener<List<JHDSShopModel>>(){
						@Override
						public void onResponse(List<JHDSShopModel> response) {
							if(isRefresh)
								data.clear();
							data.addAll(response);
							lAdapter.notifyDataSetChanged();
							
						}
						
						
					} ,  new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							System.out.println(error);
							--mCurPage;
						}
					});
					
					
					
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
		 
		 JHDSShopModel sm = data.get(position-1);
		 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sm.clickUrl));
		 if (AppCommon.getInstance().isAppInstalled(this, "com.taobao.taobao")) {
		     intent.setClassName("com.taobao.taobao", "com.taobao.tao.shop.router.ShopUrlRouterActivity");
		 }
		 startActivity(intent);
		}
	 public void firstLoadData()
		{
			if(data.size()==0)
			{
				shopList.post(new Runnable() {
				      @Override public void run() {
				    	  shopList.setRefreshing(true);
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
