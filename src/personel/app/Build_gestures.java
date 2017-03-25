package personel.app;

import java.io.File;

import com.example.personel.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Build_gestures extends Activity
{
	//public static GestureLibrary store = GestureLibraries.fromFile("/sdcard/mygestures");
	public static GestureLibrary store = GestureLibraries.fromFile("/sdcard/SirenMizhi/mygestures");
	public static boolean HasGes=false;
	private EditText editText;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.build_gestures);
		GestureOverlayView overlay = (GestureOverlayView) findViewById(R.id.gestures_overlay);//实例化覆盖view,用于画手势
		overlay.addOnGestureListener(new GesturesProcessor());//设置手势（覆盖的View）监听
		editText = (EditText) findViewById(R.id.gesture_name);//使上面的编辑栏不会影响画手势。
		editText.setVisibility(View.GONE);//?
	}

	private class GesturesProcessor implements
			GestureOverlayView.OnGestureListener
	{

		public void onGestureStarted(GestureOverlayView overlay,
				MotionEvent event)
		{
			
		}

		public void onGesture(GestureOverlayView overlay, MotionEvent event)
		{
		}

		public void onGestureEnded(final GestureOverlayView overlay,
				MotionEvent event)
		{
			final Gesture gesture = overlay.getGesture();//手势完后，获取所画的手势
			View gestureView = getLayoutInflater().inflate(R.layout.gesture,
					null);
			//设置提示语  您输入的手势如图，保存否？
			final TextView textView = (TextView) gestureView
					.findViewById(R.id.textview);
			//设置图片，保存位图（手势）
			ImageView imageView = (ImageView) gestureView
					.findViewById(R.id.imageview);

			//将手势转化为图片保存
			Bitmap bitmap = gesture.toBitmap(128, 128, 8, 0xFFFFFF00);//宽、高、内间距、0xFFFFFF00黄色。设置一个位图作为这个图像查看的内容
			imageView.setImageBitmap(bitmap);//将位图设置为图片的内容，展示出来
			textView.setText("您输入的手势如图，保存吗？");
			//textView.setVisibility(View.GONE);

			new AlertDialog.Builder(Build_gestures.this).setView(gestureView)
					.setPositiveButton("保存", new OnClickListener()
					{

						@Override
						public void onClick(DialogInterface dialog, int which)
						{

//							store.addGesture(textView.getText().toString(),
//									gesture);
							store.removeEntry("myGes");//移除名为myGes的手势
							store.addGesture("myGes",gesture);//键值对的形式,添加一个名为myGes的手势，
							store.save();//保存
							HasGes=true;
							Toast.makeText(Build_gestures.this, "手势密码设置成功，请重启程序！", 1).show();
							finish();
							
						}
					}).setNegativeButton("取消", null).show();
		}

		public void onGestureCancelled(GestureOverlayView overlay,
				MotionEvent event)
		{
		}
	}
}
