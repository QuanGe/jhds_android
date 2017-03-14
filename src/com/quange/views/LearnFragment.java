package com.quange.views;

import java.util.ArrayList;

import com.quange.jhds.R;

import android.R.color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LearnFragment  extends Fragment{
	private View fgmView;
	private ViewPager learnViewPaper;
	private ArrayList<LearnListView> subs= new ArrayList<LearnListView>();
	private PagerSlidingTabStrip learnTabs;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (fgmView == null) {
			fgmView = inflater.inflate(R.layout.fragment_learn, container, false);
			learnViewPaper = (ViewPager) fgmView.findViewById(R.id.learnViewPaper);
			subs.add(new LearnListView(getActivity(), 0));
			subs.add(new LearnListView(getActivity(), 1));
			subs.add(new LearnListView(getActivity(), 2));
			subs.add(new LearnListView(getActivity(), 3));
			
			for(LearnListView sub :subs)
			{
				learnViewPaper.addView(sub.getView());
			}
			learnViewPaper.setAdapter(new PagerAdapter() {
				String[] title = { "动物","植物","人物", "其他"};
				public boolean isViewFromObject(View view, Object o) {
					return view == o;
				}

				public int getCount() {
					return learnViewPaper.getChildCount();
				}

				public void destroyItem(View container, int position, Object object) {
				}

				public Object instantiateItem(View container, int position) {
					
					return learnViewPaper.getChildAt(position);
				}
		
				public CharSequence getPageTitle(int position) {
					return title[position];
				}
				
			});
			
			LearnListView sub = subs.get(0);
	    	sub.firstLoadData();
	    	
	    	learnTabs = (PagerSlidingTabStrip)fgmView.findViewById(R.id.tabs);
	    	learnTabs.setOnPageChangeListener(mPagerChangerListener);
	    	learnTabs.setViewPager(learnViewPaper);
	    	//tab 宽度均分
	    	learnTabs.setShouldExpand(true);
	    	learnTabs.setDividerColor(color.white);
	        //设置选中的滑动指示
	    	learnTabs.setIndicatorColor(this.getResources().getColor(R.color.red));
	    	learnTabs.setIndicatorHeight(3);
	        //设置背景颜色
	    	learnTabs.setBackgroundColor(getResources().getColor(R.color.white));
		}
		else
		{
			learnTabs.resetPagerOnPageChangeListener();
		}
		
		return fgmView;
	}
	
	OnPageChangeListener mPagerChangerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        	learnTabs.setTranslationX(0);
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
        	learnViewPaper.requestLayout();
        	LearnListView sub = subs.get(position);
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
