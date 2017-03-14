package com.quange.viewModel;

import java.util.ArrayList;
import java.util.List;
import com.quange.jhds.R;
import com.quange.model.JHDSMessageModel;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JHDSMessageAdapter extends BaseAdapter {

	private Activity mAct;

	private List<JHDSMessageModel> mlList = new ArrayList<JHDSMessageModel>();

	public JHDSMessageAdapter(Activity act, List<JHDSMessageModel> lList) {
		this.mAct = act;
		this.mlList = lList;
	}

	@Override
	public int getCount() {
		return mlList.size();
	}

	@Override
	public JHDSMessageModel getItem(int position) {
		return mlList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View cv, ViewGroup parent) {
		JHDSMessageModel ls = getItem(position);
		HoldView hv = null;
		if (null == cv) {
			hv = new HoldView();
			cv = View.inflate(mAct, R.layout.list_item_message, null);

			hv.contentTv = (TextView) cv.findViewById(R.id.tv_title);

			cv.setTag(hv);
		} else {
			hv = (HoldView) cv.getTag();
		}

		hv.contentTv.setText(ls.title);

		return cv;
	}

	private class HoldView {

		private TextView contentTv; // name
	}
}
