package com.quange.views;
import java.util.ArrayList;

import com.quange.jhds.R;
import com.umeng.analytics.MobclickAgent;

import android.R.color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class CopyFragment extends Fragment {
	private View fgmView;
	private ViewPager copyViewPaper;
	private ArrayList<CopyGridView> subs= new ArrayList<CopyGridView>();
	private PagerSlidingTabStrip copyTabs;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (fgmView == null) {
			fgmView = inflater.inflate(R.layout.fragment_copy, container, false);
			copyViewPaper = (ViewPager) fgmView.findViewById(R.id.copyViewPaper);
			subs.add(new CopyGridView(getActivity(), 3));
			subs.add(new CopyGridView(getActivity(), 6));
			subs.add(new CopyGridView(getActivity(), 4));
			subs.add(new CopyGridView(getActivity(), 2));
			
			for(CopyGridView sub :subs)
			{
				copyViewPaper.addView(sub.getView());
			}
			copyViewPaper.setAdapter(new PagerAdapter() {
				String[] title = { "动物","植物","人物", "其他"};
				public boolean isViewFromObject(View view, Object o) {
					return view == o;
				}

				public int getCount() {
					return copyViewPaper.getChildCount();
				}

				public void destroyItem(View container, int position, Object object) {
				}

				public Object instantiateItem(View container, int position) {
					
					return copyViewPaper.getChildAt(position);
				}
		
				public CharSequence getPageTitle(int position) {
					return title[position];
				}
				
			});
			
			CopyGridView sub = subs.get(0);
	    	sub.firstLoadData();
	    	
	    	copyTabs = (PagerSlidingTabStrip)fgmView.findViewById(R.id.tabs);
	    	copyTabs.setOnPageChangeListener(mPagerChangerListener);
	    	copyTabs.setViewPager(copyViewPaper);
	    	//tab 宽度均分
	    	copyTabs.setShouldExpand(true);
	    	copyTabs.setDividerColor(color.white);
	        //设置选中的滑动指示
	    	copyTabs.setIndicatorColor(this.getResources().getColor(R.color.red));
	    	copyTabs.setIndicatorHeight(3);
	        //设置背景颜色
	    	copyTabs.setBackgroundColor(getResources().getColor(R.color.white));
		}
		else
		{
			copyTabs.resetPagerOnPageChangeListener();
		}
		
		return fgmView;
	}
	
	OnPageChangeListener mPagerChangerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        	copyTabs.setTranslationX(0);
        }

        @Override
        public void onPageSelected(int position) {
//            switch (position){
//                case 0:
//                    //MyToast.makeText(MainActivity.this, "the page is " + "messager", Toast.LENGTH_SHORT);
//                    break;
//                case 1:
//                    //MyToast.makeText(MainActivity.this, "the page is " + "news", Toast.LENGTH_SHORT);
//                    break;
//                case 2:
//                    //MyToast.makeText(MainActivity.this, "the page is " + "user", Toast.LENGTH_SHORT);
//                    break;
//            }
        	copyViewPaper.requestLayout();
        	CopyGridView sub = subs.get(position);
        	sub.firstLoadData();
        	
        }

        @Override
        public void onPageScrollStateChanged(int state) {


        }
	};
	
	@Override
    public void onResume() {
		super.onResume();
	}
    @Override
	public void onPause() {
		super.onPause();
		
	}
}
