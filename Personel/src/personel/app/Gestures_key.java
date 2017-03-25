package personel.app;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import com.example.personel.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.Toast;

public class Gestures_key extends Activity {
	public static String key1;
	public static String encryptResult;
	public static String output;
	private GestureLibrary library;
	private static final int MAX_PROGRESS =100;
	private ProgressDialog progressDialog;
	private Handler progressHandler;
	private int progress;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.gestures_key);
//        final String path = new File(Environment.getExternalStorageDirectory(),
//        "mygestures").getAbsolutePath();
//        library = GestureLibraries.fromFile(path);
        library = GestureLibraries.fromFile("/sdcard/SirenMizhi/mygestures");
        library.load();//加载手势库
        GestureOverlayView gestureView = (GestureOverlayView)this.findViewById(R.id.mygestures);
        gestureView.addOnGesturePerformedListener(new GestureListener());
    }
    
    private final class GestureListener implements OnGesturePerformedListener{
		@Override
		public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
			//从当前手势库中识别与gesture匹配的全部手势，返回值存入链表中
			ArrayList<Prediction> predictions = library.recognize(gesture);//识别手势
			
			if(!predictions.isEmpty()){
				//Toast.makeText(Gestures_key.this, "大小为"+Integer.toString(predictions.size()), 1).show();

				for (Prediction prediction : predictions){
				//Prediction prediction = predictions.get(1);//获取最匹配的记录
				if(prediction.score>2.1){
					WatchAppService.lastRunningApp = WatchAppService.runningApp;//这里赋值，终于解决了反复弹出验证页面的BUG
		            finish();
					Toast.makeText(Gestures_key.this, "所画手势被识别", 1).show();
					

					
		
//						key1=prediction.name;
//						Intent intent2=new Intent(); 
//						intent2.setClass(Gestures_key.this, Show.class);
//						intent2.putExtra("fla1", "解密");
//						// encryptResult= AES.encryptResult;
//						Gestures_key.this.startActivity(intent2);
//						output="已完成解密,手势名："+key1;
						//showProgressDialog(ProgressDialog.STYLE_SPINNER);//显示进度条
						 					
					//}

				}
				else{
					Toast.makeText(Gestures_key.this, "所画手势差别太大", 1).show();
				}
				}
			}
		}
    }
    private void showProgressDialog(int style)
	{
		progressDialog = new ProgressDialog(this);
		progressDialog.setIcon(R.drawable.icon);
		progressDialog.setTitle("正在处理数据...");
		progressDialog.setMessage("请稍后...");		
		// 设置进度对话框的风格
		progressDialog.setProgressStyle(style);
		// 设置进度对话框的进度最大值
		progressDialog.setMax(MAX_PROGRESS);
		progressDialog.show();
		progressHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				if (progress >= MAX_PROGRESS)
				{
					// 进度达到最大值，关闭对话框
					progress = 0;
					progressDialog.dismiss();
					
				}
				else
				{
					progress=progress+3;
					// 将进度递增3
					progressDialog.incrementProgressBy(1);
					// 随机设置下一次递增进度（调用handleMessage方法）的时间
					progressHandler.sendEmptyMessageDelayed(1,50 + new Random().nextInt(500));

				}
			}
		};

		// 设置进度初始值
		progress = (progress > 0) ? progress : 0;
		progressDialog.setProgress(progress);
		progressHandler.sendEmptyMessage(1);
	}
    
    
	@Override
	protected void onDestroy() {
	//	android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}
    
}