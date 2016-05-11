package com.quange.jhds;



import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {
	private Button drawbtn;
	protected static boolean isQuit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawbtn = (Button)findViewById(R.id.drawBtn);
        drawbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startMainActivity();
			}
		});
    }
    private void startMainActivity() {
		 final Intent it = new Intent(this, DrawActivity.class);
		 startActivity(it);
		 
	 }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			boolean flag = false;
			if (isQuit) {
				// Intent home = new Intent(Intent.ACTION_MAIN);
				// home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				// home.addCategory(Intent.CATEGORY_HOME);
				// startActivity(home);
				// Process.killProcess(Process.myPid());
				finish();
			} else {
				Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
				isQuit = true;
				Timer timer = new Timer(); // 实例化Timer定时器对象
				timer.schedule(new TimerTask() { // schedule方法(安排,计划)需要接收一个TimerTask对象和一个代表毫秒的int值作为参数
							@Override
							public void run() {
								isQuit = false;
							}
						}, 3000);
			}
			return flag;
		}
		return super.onKeyDown(keyCode, event);

	}
}
