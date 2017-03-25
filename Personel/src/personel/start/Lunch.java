package personel.start;

import java.io.OutputStream;

import personel.app.AppActivity;
import personel.app.Build_gestures;
import personel.app.Gestures_key;
import personel.app.Gestures_keyforStart;
import personel.main.MainActivity;

import com.example.personel.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

public class Lunch extends Activity {
	private SharedPreferences preferences;
	private Editor editor;
	public static int canIn=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		final View view = View.inflate(this, R.layout.start,null);
		setContentView(view);

		// 渐变展示启动屏,这里通过动画来设置了开启应用程序的界面
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(1000);
		view.startAnimation(aa);
		// 给动画添加监听方法
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				redirectTo();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});
	}

	/**
	 * 跳转到主角面的方法
	 */
	private void redirectTo() {
		preferences = getSharedPreferences("phone", Context.MODE_PRIVATE);  
		//判断是不是首次登录，   
		if (preferences.getBoolean("firststart", true)) {  
		editor = preferences.edit();  
		//将登录标志位设置为false，下次登录时不在显示首次登录界面   
		editor.putBoolean("firststart", false);  
		editor.commit();  
		Intent intent = new Intent(this, Loading.class);
		startActivity(intent);
		
		finish();
		}
		else
		{
			Intent intentk=new Intent(this,Gestures_keyforStart.class);
			startActivity(intentk);
			finish();
		}
	}
}
