package com.quange.jhds;

import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.quange.views.SelectBrushView;
import com.quange.views.BrushView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


@SuppressLint("NewApi") public class DrawActivity extends Activity implements OnColorChangedListener, SensorEventListener{
	private BrushView brushView;
	private RelativeLayout selectBtn;
	private RelativeLayout selectBrushView;
	private Animation mInAnim, mOutAnim;
	private GradientDrawable brushColor;
	private RelativeLayout.LayoutParams brushFrame;
	private View brushIcon;
	private SensorManager sensorManager = null;  
	private Vibrator vibrator = null;  
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        brushView = (BrushView)findViewById(R.id.brushView);
     
        selectBtn = (RelativeLayout)findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//showDeleteDialog();
				System.out.println(selectBrushView.getAlpha());
				RelativeLayout.LayoutParams l = (LayoutParams) selectBrushView.getLayoutParams();
				if(l.leftMargin == 0)
				{
					selectBrushView.startAnimation(mOutAnim);
					return;
				}
				else
				{
					l.leftMargin = 0;
					selectBrushView.setLayoutParams(l);
					System.out.println(selectBrushView.getAlpha());
					selectBrushView.startAnimation(mInAnim);
				}
			}
		});
        
        selectBrushView = (RelativeLayout)findViewById(R.id.selectBrushView);
        selectBrushView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//showDeleteDialog();
				selectBrushView.startAnimation(mOutAnim);
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
        
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);  
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE); 
        
        mInAnim = AnimationUtils.loadAnimation(this,R.anim.dialog_in);
        mInAnim.setFillAfter(true);
        mOutAnim = AnimationUtils.loadAnimation(this,R.anim.dialog_out);
        mOutAnim.setFillAfter(true);
        mOutAnim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				
				RelativeLayout.LayoutParams l = (LayoutParams) selectBrushView.getLayoutParams();
				l.leftMargin = 10000;
				selectBrushView.setLayoutParams(l);
			}
		});
        
        buildSelectBrushView();
    }
	
	private void buildSelectBrushView()
	{
		GradientDrawable shape =  new GradientDrawable();
        DisplayMetrics dm = new DisplayMetrics();  
		dm = this.getApplicationContext().getResources().getDisplayMetrics(); 
        shape.setCornerRadius( 5 *dm.density);
        shape.setColor(Color.WHITE);
       
        View brushBox = new View(this);
        brushBox.setBackground(shape);
       
        RelativeLayout.LayoutParams brushBoxFrame = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT ,(int) (350*dm.density) );
        brushBoxFrame.leftMargin = (int) (20*dm.density);
        brushBoxFrame.rightMargin = (int) (20*dm.density);
        brushBoxFrame.addRule(RelativeLayout.CENTER_IN_PARENT); 
        selectBrushView.addView(brushBox, brushBoxFrame);
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
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
        	RelativeLayout.LayoutParams l = (LayoutParams) selectBrushView.getLayoutParams();
			if(l.leftMargin == 0)
			{
				selectBrushView.startAnimation(mOutAnim);
				return false;
			}
			
        }
        
        
        return super.onKeyDown(keyCode, event);
 
    }
	 @Override  
	    protected void onPause()  
	    {  
	        super.onPause();  
	        sensorManager.unregisterListener(this);  
	    }  
	  
	    @Override  
	    protected void onResume()  
	    {  
	        super.onResume();  
	        sensorManager.registerListener(this,  
	                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),  
	                SensorManager.SENSOR_DELAY_NORMAL);  
	    }  
	@Override  
    public void onAccuracyChanged(Sensor sensor, int accuracy)  
    {  
        //当传感器精度改变时回调该方法，Do nothing.  
    }  
  
    @Override  
    public void onSensorChanged(SensorEvent event)  
    {  
  
        int sensorType = event.sensor.getType();  
        //values[0]:X轴，values[1]：Y轴，values[2]：Z轴  
        float[] values = event.values;  
        if (sensorType == Sensor.TYPE_ACCELEROMETER)  
        {  
            if ((Math.abs(values[0]) > 17 || Math.abs(values[1]) > 17 || Math  
                    .abs(values[2]) > 17))  
            {  
                Log.d("sensor x ", "============ values[0] = " + values[0]);  
                Log.d("sensor y ", "============ values[1] = " + values[1]);  
                Log.d("sensor z ", "============ values[2] = " + values[2]);  
                brushView.clearAll();
                //摇动手机后，再伴随震动提示~~  
                vibrator.vibrate(500);  
            }  
  
        }  
    }  
}
