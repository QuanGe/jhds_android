package com.quange.views;

import java.util.ArrayList;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quange.jhds.AccessTokenKeeper;
import com.quange.jhds.R;
import com.quange.jhds.SinaConstants;
import com.quange.viewModel.JHDSShareRepostAdapter;
import com.quange.views.NestedListView.MyPullUpListViewCallBack;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.RepostsList;


public class JHDSShareRepostView {
	private View mView;
	private Activity mAct;
	private NestedListView lList;
	private JHDSShareRepostAdapter lAdapter;
	private ArrayList<Status> mLSList = new ArrayList<Status>();
	private String mWeiboID;
	private int mCurPage = 1;
	private StatusesAPI weiboStatusesAPI;
	private RelativeLayout messageBox;
	
	private Button bottomBtn;
	private TextView tv_message;
	public JHDSShareRepostView(Activity act,String weiboId) {
		super();
		this.mAct = act;
		mView = View.inflate(mAct, R.layout.view_share_repost, null);
		initView();
		mWeiboID = weiboId;
		weiboStatusesAPI = new StatusesAPI(act,SinaConstants.APP_KEY,AccessTokenKeeper.readAccessToken(act));
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
		lList = (NestedListView) mView.findViewById(R.id.learn_list);
		messageBox = (RelativeLayout) mView.findViewById(R.id.messageBox);
		
		bottomBtn = (Button) mView.findViewById(R.id.bottomBtn);
		tv_message = (TextView) mView.findViewById(R.id.tv_message);
		bottomBtn.setVisibility(View.INVISIBLE);
		tv_message.setText("小简正在卖力加载中...");
		lAdapter = new JHDSShareRepostAdapter(mAct, mLSList);
		
		lList.initBottomView();
		lList.setAdapter(lAdapter);
		lList.setMyPullUpListViewCallBack(new MyPullUpListViewCallBack() {
 
            public void scrollBottomState() {
                // TODO Auto-generated method stub
            	refresh(false);
            }
        });
	
		
	}
	
	public void firstLoadData()
	{
		if(mLSList.size()==0)
		{
			lList.post(new Runnable() {
			      @Override public void run() {
			    	  refresh(true);
			      }
			 });
		}
	}

	public void refresh(final boolean isRefresh) {
		mCurPage = isRefresh ? 1 : ++mCurPage ;
		weiboStatusesAPI.repostTimeline(Long.parseLong(mWeiboID),(long)(0),(long)(0),50,mCurPage,0,mListener);
	}

	 /**
		 * 微博 OpenAPI 回调接口。
		 */
		private RequestListener mListener = new RequestListener() {
			
			@Override
			public void onWeiboException(WeiboException arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(String arg0) {
				// TODO Auto-generated method stub
				RepostsList repostsList = RepostsList.parse(arg0);
				if (repostsList != null) {
					if(repostsList.statusList != null)
					{
						if(repostsList.statusList.size() == 0)
						{
							tv_message.setText("快来转发精彩内容吧");
						}
						else
						{
							messageBox.setVisibility(View.GONE);
						
							if(mCurPage==1)
								mLSList.clear();
							mLSList.addAll(repostsList.statusList);
							
							lAdapter.notifyDataSetChanged();
						}
					}
					else
					{
						tv_message.setText("快来转发精彩内容吧");
					}
				}else{
					
				}
			}
		}; 

}
