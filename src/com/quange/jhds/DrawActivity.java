package com.quange.jhds;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.quange.views.SelectBrushView;
import com.quange.views.BrushView;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TextView;


@SuppressLint("NewApi") public class DrawActivity extends Activity implements OnColorChangedListener, SensorEventListener{
	protected BrushView brushView;
	protected RelativeLayout selectBtn;
	protected RelativeLayout backBtn;
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
        
        MobclickAgent.onEvent(this, "canvas");
        
        brushView = (BrushView)findViewById(R.id.brushView);
        backBtn = (RelativeLayout)findViewById(R.id.backBtn);
        selectBtn = (RelativeLayout)findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//showDeleteDialog();
				MobclickAgent.onEvent(getApplicationContext(), "canvas_select_brush");
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
        
        backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				back();
			}
		});
        
        selectBrushView = (RelativeLayout)findViewById(R.id.selectBrushView);
        GradientDrawable selectBrushViewColor = new GradientDrawable();
        selectBrushViewColor.setColor(0x88000000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        	selectBrushView.setBackground(selectBrushViewColor);
        else
        	selectBrushView.setBackgroundDrawable(selectBrushViewColor);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        	selectBtn.setBackground(shape);
        else
        	selectBtn.setBackgroundDrawable(shape);
        brushIcon = new View(this);
        {
        	brushColor =  new GradientDrawable();
        	brushColor.setCornerRadius( brushView.getBrushWidth()*dm.density/2);
        	brushColor.setColor(brushView.getBrushColor());
        	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        	brushIcon.setBackground(brushColor);
        	else
        		brushIcon.setBackgroundDrawable(brushColor);
        }
        brushFrame = new RelativeLayout.LayoutParams((int)(brushView.getBrushWidth()*dm.density), (int)(brushView.getBrushWidth()*dm.density));
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
        
        {
        	GradientDrawable backColor =  new GradientDrawable();
        	backColor.setCornerRadius( 50*dm.density/2);
        	backColor.setColor(0x55555555);
        	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        		backBtn.setBackground(backColor);
        	else
        		backBtn.setBackgroundDrawable(backColor);
        	
        }
    }
	
	private void buildSelectBrushView()
	{
		//背景
		GradientDrawable shape =  new GradientDrawable();
        DisplayMetrics dm = new DisplayMetrics();  
		dm = this.getApplicationContext().getResources().getDisplayMetrics(); 
        shape.setCornerRadius( 5 *dm.density);
        shape.setColor(Color.WHITE);
       
        RelativeLayout brushBox = new RelativeLayout(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        	brushBox.setBackground(shape);
        else
        	brushBox.setBackgroundDrawable(shape);
       
        RelativeLayout.LayoutParams brushBoxFrame = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT ,(int) (380*dm.density) );
        brushBoxFrame.leftMargin = (int) (20*dm.density);
        brushBoxFrame.rightMargin = (int) (20*dm.density);
        brushBoxFrame.addRule(RelativeLayout.CENTER_IN_PARENT); 
        selectBrushView.addView(brushBox, brushBoxFrame);
        
        //titile
        TextView title = new TextView(this);
        title.setText("选择笔的颜色");
        title.setTextSize(20);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams titleFrame = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT ,(int) (30*dm.density) );
        titleFrame.topMargin = (int)(5*dm.density);
        brushBox.addView(title, titleFrame);
 
		int width3 = dm.widthPixels;
        int[] colors = {0xffDF0526,0xffEC0B5F,0xff9D25A9,0xff6438A0,0xff4052AE,
        		0xff5A78F4,0xff00AAF0,0xff00BED2,0xff009687,0xff119B39,
        		0xff87C35B,0xffCADC57,0xffFFEB5F,0xffFFBF3E,0xffFF512F,
        		0xff73554B,0xff9E9E00,0xff5F7D8A,0xffeeeeee,0xffcccccc,
        		0xff888888,0xff555555,0xff333333,0xff111111,0xff000000};
        //all brush
        for(int  i = 0;i<colors.length;i++)
        {
	        View brush = new View(this);
	        GradientDrawable brushBackground =  new GradientDrawable();
	        brushBackground.setCornerRadius(25*dm.density);
	        final int theColor =  colors[i];
	        brushBackground.setColor(colors[i]);
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
	        	brush.setBackground(brushBackground);
	        else
	        	brush.setBackgroundDrawable(brushBackground);
	        brush.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					brushView.updateBrushColor(theColor);
					brushColor.setColor(brushView.getBrushColor());
					selectBrushView.startAnimation(mOutAnim);
				}
			});
	        RelativeLayout.LayoutParams brushFrame = new RelativeLayout.LayoutParams((int) (50*dm.density) ,(int) (50*dm.density) );
	        brushFrame.topMargin = (int)(55*dm.density*(i/5)+35*dm.density);
	        int subWidth = (int) (width3-20*dm.density*2);
	        
	        int sub = (int) ((subWidth-50*dm.density*5)/6);
	        brushFrame.leftMargin = sub +(i%5)*(int) (50*dm.density + sub);
	        
	        brushBox.addView(brush, brushFrame);
        }
        
        Button saveBtn = new Button(this);
        saveBtn.setBackgroundResource(R.drawable.btn_cancel);
        saveBtn.setText("保存作品");
        RelativeLayout.LayoutParams saveBtnFrame = new RelativeLayout.LayoutParams((int) (80*dm.density) ,(int) (30*dm.density) );
        saveBtnFrame.leftMargin = (int) (40*dm.density);
        saveBtnFrame.topMargin = (int) (340*dm.density);
        saveBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(getApplicationContext(), "canvas_save");
				Bitmap bitmap = screenShot(brushView);
				
				Calendar c = Calendar.getInstance();
				String localImgUrl = getSDPath()+"/jhds/jianhuadashi/"+c.get(Calendar.YEAR)+c.get(Calendar.MONTH)+c.get(Calendar.DAY_OF_MONTH)+c.get(Calendar.HOUR_OF_DAY)+c.get(Calendar.MINUTE)+".jpg";
				try {
					AppCommon.getInstance().saveBitmapToFile(bitmap, localImgUrl, true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
			}
		});
        brushBox.addView(saveBtn, saveBtnFrame);
        
        Button shareBtn = new Button(this);
        shareBtn.setBackgroundResource(R.drawable.btn_cancel);
        shareBtn.setText("分享作品");
        RelativeLayout.LayoutParams shareBtnFrame = new RelativeLayout.LayoutParams((int) (80*dm.density) ,(int) (30*dm.density) );
        shareBtnFrame.leftMargin = (int) (width3-20*dm.density*2-40*dm.density-80*dm.density);
        shareBtnFrame.topMargin = (int) (340*dm.density);
        brushBox.addView(shareBtn, shareBtnFrame);
        shareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bitmap bitmap = screenShot(brushView);
				Calendar c = Calendar.getInstance();
				String localImgUrl = getSDPath()+"/jhds/jianhuadashi/"+c.get(Calendar.YEAR)+c.get(Calendar.MONTH)+c.get(Calendar.DAY_OF_MONTH)+c.get(Calendar.HOUR_OF_DAY)+c.get(Calendar.MINUTE)+".jpg";
				share(localImgUrl,bitmap);
				
			}
		});
        
	}
	
	private void share(String localImgUrl,Bitmap bitmap) {
		
		final String content = "测试";
		ShareCollectUtils.shareContent(this, content, localImgUrl, bitmap);
	}
	
	public Bitmap screenShot(View view) {
	    Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
	            view.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap);
	    view.draw(canvas);
	    return bitmap;
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
        	MobclickAgent.onEvent(this, "canvas_brush_fine");
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
        	MobclickAgent.onEvent(this, "canvas_brush_bold");
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
			else
			{
				if(brushView.actCanBack)
					finish();
				else
				{
					new AlertDialog.Builder(this).setMessage("请选择是撤销还是返回上一页")
					.setNegativeButton("撤销", new DialogInterface.OnClickListener() {
	
						@Override
						public void onClick(DialogInterface dialog, int which) {
							brushView.backToFront();
							
						}
					}).setPositiveButton("返回", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}
					}).show();
					return false;
				}
			}
			
			
        }
        
        
        return super.onKeyDown(keyCode, event);
 
    }
	
	private void back()
	{
		RelativeLayout.LayoutParams l = (LayoutParams) selectBrushView.getLayoutParams();
		if(l.leftMargin == 0)
		{
			selectBrushView.startAnimation(mOutAnim);
			
		}
		else
		{
			new AlertDialog.Builder(this).setMessage("请选择是撤销还是返回上一页")
			.setNegativeButton("撤销", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					brushView.backToFront();
					
				}
			}).setPositiveButton("返回", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					finish();
				}
			}).show();
		
		}
	}
	 @Override  
	    protected void onPause()  
	    {  
	        super.onPause();  
	        MobclickAgent.onResume(this);
	        sensorManager.unregisterListener(this);  
	    }  
	  
	    @Override  
	    protected void onResume()  
	    {  
	        super.onResume();  
	        MobclickAgent.onPause(this);
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
            if ((Math.abs(values[0]) > 19 || Math.abs(values[1]) > 19 || Math  
                    .abs(values[2]) > 19))  
            {  
                Log.d("sensor x ", "============ values[0] = " + values[0]);  
                Log.d("sensor y ", "============ values[1] = " + values[1]);  
                Log.d("sensor z ", "============ values[2] = " + values[2]);  
                brushView.clearAll();
                //摇动手机后，再伴随震动提示~~  
                vibrator.vibrate(100);  
            }  
  
        }  
    }  
    
    
    /** 
     * 获取SDK路径 
     * @return 
     */  
    public String getSDPath(){   
           File sdDir = null;   
           boolean sdCardExist = Environment.getExternalStorageState()     
                               .equals(android.os.Environment.MEDIA_MOUNTED);   //判断sd卡是否存在   
           if   (sdCardExist)     
           {                                 
             sdDir = Environment.getExternalStorageDirectory();//获取跟目录   
          }     
           return sdDir.toString();   
             
    }  

 
}
