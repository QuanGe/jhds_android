package com.quange.jhds;



import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.quange.views.SelectBrushView;

import com.quange.views.BrushView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class DrawActivity extends Activity implements OnColorChangedListener{
	private BrushView brushView;
	private Button clearBtn;
	private Button selectBtn;
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
        selectBtn = (Button)findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDeleteDialog();
				
			}
		});
        
        
    }
	
	private void showDeleteDialog() {
    	SelectBrushView ccDlog = new SelectBrushView(this, R.style.selectBrush_dialog, 17, true, true);
		ccDlog.show();
	}
	@Override
	public void onColorChanged(int color) {
		// TODO Auto-generated method stub
		brushView.updateBrushColor(color);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Log.i("abc", "动作：   " + event.getAction());
 
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
          
            brushView.updateBrushWidth(false);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
           
//            if (event.getRepeatCount() == 0) {
//              
//            }
        	brushView.updateBrushWidth(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
 
    }
}
