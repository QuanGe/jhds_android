package com.quange.jhds;

import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;

public class JHDSLastDrawActivity extends DrawActivity {
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        brushView.loadSaveDataAndDraw();
        
        MobclickAgent.onEvent(getApplicationContext(), "mine_last");
	}
}
