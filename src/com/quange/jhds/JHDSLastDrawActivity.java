package com.quange.jhds;

import android.os.Bundle;

public class JHDSLastDrawActivity extends DrawActivity {
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        brushView.loadSaveDataAndDraw();
	}
}
