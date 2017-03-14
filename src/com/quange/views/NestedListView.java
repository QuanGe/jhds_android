package com.quange.views;
import com.quange.jhds.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.view.View.OnTouchListener;
import android.widget.ListView;

public class NestedListView extends ListView implements OnTouchListener, OnScrollListener 
{
    private int listViewTouchAction;
    private static final int MAXIMUM_LIST_ITEMS_VIEWABLE = 99;
    /** 底部显示正在加载的页面 */
    private View footerView = null;
    /** 存储上下文 */
    private Context context;
    /** 上拉刷新的ListView的回调监听 */
    private MyPullUpListViewCallBack myPullUpListViewCallBack;
    /** 记录第一行Item的数值 */
    private int firstVisibleItem;
   public NestedListView(Context context, AttributeSet attrs) 
    {
        super(context, attrs);
        listViewTouchAction = -1;
        setOnScrollListener(this);
        setOnTouchListener(this);
        this.context = context;
        initListView();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) 
    {
        if (getAdapter() != null && getAdapter().getCount() > MAXIMUM_LIST_ITEMS_VIEWABLE) 
        {
            if (listViewTouchAction == MotionEvent.ACTION_MOVE) 
            {
                scrollBy(0, -1);
            }
        }
        
        this.firstVisibleItem = firstVisibleItem;
        
        if (footerView != null) {
            //判断可视Item是否能在当前页面完全显示
            if (visibleItemCount == totalItemCount) {
                // removeFooterView(footerView);
                footerView.setVisibility(View.GONE);//隐藏底部布局
            } else {
                // addFooterView(footerView);
                footerView.setVisibility(View.VISIBLE);//显示底部布局
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) 
    {
    	//当滑动到底部时
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && firstVisibleItem != 0) {
            myPullUpListViewCallBack.scrollBottomState();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int newHeight = 0;
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) 
        {
            ListAdapter listAdapter = getAdapter();
            if (listAdapter != null && !listAdapter.isEmpty()) 
            {
                int listPosition = 0;
                for (listPosition = 0; listPosition < listAdapter.getCount()
                        && listPosition < MAXIMUM_LIST_ITEMS_VIEWABLE; listPosition++) 
                {
                    View listItem = listAdapter.getView(listPosition, null, this);
                    //now it will not throw a NPE if listItem is a ViewGroup instance
                    if (listItem instanceof ViewGroup) 
                    {
                        listItem.setLayoutParams(new LayoutParams(
                                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    }
                    listItem.measure(widthMeasureSpec, heightMeasureSpec);
                    newHeight += listItem.getMeasuredHeight();
                }
                newHeight += getDividerHeight() * listPosition;
            }
            if ((heightMode == MeasureSpec.AT_MOST) && (newHeight > heightSize)) 
            {
                if (newHeight > heightSize) 
                {
                    newHeight = heightSize;
                }
            }
        } 
        else 
        {
            newHeight = getMeasuredHeight();
        }
        setMeasuredDimension(getMeasuredWidth(), newHeight);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) 
    {
        if (getAdapter() != null && getAdapter().getCount() > MAXIMUM_LIST_ITEMS_VIEWABLE) 
        {
            if (listViewTouchAction == MotionEvent.ACTION_MOVE) 
            {
                scrollBy(0, 1);
            }
        }
        return false;
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
 
        // 为ListView设置滑动监听
        setOnScrollListener(this);
        // 去掉底部分割线
        setFooterDividersEnabled(false);
    }
    
    /**
     * 初始化话底部页面
     */
    public void initBottomView() {
     
        if (footerView == null) {
            footerView =View.inflate(this.context, R.layout.listview_loadbar, null);
        }
        addFooterView(footerView);
    }
    
    public void setMyPullUpListViewCallBack(
            MyPullUpListViewCallBack myPullUpListViewCallBack) {
        this.myPullUpListViewCallBack = myPullUpListViewCallBack;
    }
 
    /**
     * 上拉刷新的ListView的回调监听
     * 
     * @author xiejinxiong
     * 
     */
    public interface MyPullUpListViewCallBack {
 
        void scrollBottomState();
    }
}