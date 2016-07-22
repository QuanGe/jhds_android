package com.quange.views;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quange.jhds.AccessTokenKeeper;
import com.quange.jhds.AppSetManager;
import com.quange.jhds.R;
import com.quange.jhds.SinaConstants;
import com.quange.viewModel.JHDSShareCommentAdapter;

import com.quange.views.NestedListView.MyPullUpListViewCallBack;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;

import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;


public class JHDSShareCommentListView {
	private View mView;
	private Activity mAct;
	private NestedListView lList;
	public JHDSShareCommentAdapter lAdapter;
	public ArrayList<Comment> mLSList = new ArrayList<Comment>();
	private String mWeiboID;
	private int mCurPage = 1;
	private CommentsAPI weiboCommentsAPIAPI;
	public RelativeLayout messageBox;
	
	private Button bottomBtn;
	private TextView tv_message;
	public JHDSShareCommentListView(Activity act,String weiboId) {
		super();
		this.mAct = act;
		mView = View.inflate(mAct, R.layout.view_share_comment, null);
		initView();
		mWeiboID = weiboId;
		weiboCommentsAPIAPI = new CommentsAPI(act,SinaConstants.APP_KEY,AccessTokenKeeper.readAccessToken(act));
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
		lAdapter = new JHDSShareCommentAdapter(mAct, mLSList);
		
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
		if(AppSetManager.getSinaNickName().equals("") )
			tv_message.setText("还没登录，快去登录吧");
		else
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
	}

	public void refresh(final boolean isRefresh) {
		mCurPage = isRefresh ? 1 : ++mCurPage ;
		
		weiboCommentsAPIAPI.show(Long.parseLong(mWeiboID),(long)(0),(long)(0),50,mCurPage,0,mListener);
	}

	 /**
		 * 微博 OpenAPI 回调接口。
		 */
		private RequestListener mListener = new RequestListener() {
			
			@Override
			public void onWeiboException(WeiboException arg0) {
				// TODO Auto-generated method stub
				if(AppSetManager.getSinaNickName().equals("") )
					tv_message.setText("还没登录，快去登录吧");
				else
					tv_message.setText("数据获取失败");
			}
			
			@Override
			public void onComplete(String arg0) {
				// TODO Auto-generated method stub
				CommentList repostsList = CommentList.parse(arg0);
				if (repostsList != null) {
					if(repostsList.commentList != null)
					{
						if(repostsList.commentList.size() == 0)
						{
							tv_message.setText("快来发表你的评论吧");
						}
						else
						{
							messageBox.setVisibility(View.GONE);
					
							if(mCurPage==1)
								mLSList.clear();
							mLSList.addAll(repostsList.commentList);
							lAdapter.notifyDataSetChanged();
						}
					}
					else
					{
						tv_message.setText("快来发表你的评论吧");
					}
				}else{
					
				}
			}
		}; 

}
