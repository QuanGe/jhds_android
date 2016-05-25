package com.quange.jhds;

import com.quange.views.JHDSSavedImagesGridView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class JHDSSavedImagesActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savedimages);
        LinearLayout content = (LinearLayout)findViewById(R.id.content);
        JHDSSavedImagesGridView si = new JHDSSavedImagesGridView(this);
        content.addView(si.getView());
        
	}
}
