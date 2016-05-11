package com.quange.jhds;



import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.quange.views.SelectBrushView;

import com.quange.views.BrushView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


@SuppressLint("NewApi") public class DrawActivity extends Activity implements OnColorChangedListener{
	private BrushView brushView;
	private RelativeLayout selectBtn;
	private GradientDrawable brushColor;
	private RelativeLayout.LayoutParams brushFrame;
	private View brushIcon;
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        brushView = (BrushView)findViewById(R.id.brushView);
     
        selectBtn = (RelativeLayout)findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDeleteDialog();
				
			}
		});
        
        GradientDrawable shape =  new GradientDrawable();
        DisplayMetrics dm = new DisplayMetrics();  
		dm = this.getApplicationContext().getResources().getDisplayMetrics(); 
        shape.setCornerRadius( 25 *dm.density);
        shape.setColor(Color.LTGRAY);
       
        selectBtn.setBackground(shape);
        brushIcon = new View(this);
        {
        	brushColor =  new GradientDrawable();
        	brushColor.setCornerRadius( brushView.getBrushWidth()/2);
        	brushColor.setColor(Color.RED);
        	brushIcon.setBackground(brushColor);
        }
        brushFrame = new RelativeLayout.LayoutParams(brushView.getBrushWidth(), brushView.getBrushWidth());
        brushFrame.addRule(RelativeLayout.CENTER_IN_PARENT); 
        selectBtn.addView(brushIcon, brushFrame);
        
        
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
		DisplayMetrics dm = new DisplayMetrics();  
		dm = this.getApplicationContext().getResources().getDisplayMetrics(); 
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
          
            brushView.updateBrushWidth(false);
            brushFrame.width = (int) (brushView.getBrushWidth()*dm.density);
            brushFrame.height = (int) (brushView.getBrushWidth()*dm.density);
            brushColor.setCornerRadius(brushView.getBrushWidth()*dm.density/2); 
            selectBtn.updateViewLayout(brushIcon, brushFrame);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
           
//            if (event.getRepeatCount() == 0) {
//              
//            }
        	brushView.updateBrushWidth(true);
        	brushFrame.width = (int) (brushView.getBrushWidth()*dm.density);
            brushFrame.height = (int) (brushView.getBrushWidth()*dm.density);
            brushColor.setCornerRadius(brushView.getBrushWidth()*dm.density/2); 
            selectBtn.updateViewLayout(brushIcon, brushFrame);
            return true;
        }
        return super.onKeyDown(keyCode, event);
 
    }
}
