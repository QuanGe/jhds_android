package org.quange.game;

import com.quange.views.BrushView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class DrawActivity extends Activity {
	private BrushView brushView;
	private Button clearBtn;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        brushView = (BrushView)findViewById(R.id.brushView);
        clearBtn = (Button)findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				brushView.clearAll();
			}
		});
    }
}
