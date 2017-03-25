package personel.app;
import java.util.ArrayList;

import personel.main.MainActivity;
import personel.start.Lunch;

import com.example.personel.R;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Gestures_keyforStart extends Activity {
	public static String key1;
	public static String encryptResult;
	public static String output;
	private GestureLibrary library;
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.gestures_key);
        library = GestureLibraries.fromFile("/sdcard/SirenMizhi/mygestures");
        library.load();//�������ƿ�
        GestureOverlayView gestureView = (GestureOverlayView)this.findViewById(R.id.mygestures);
        gestureView.addOnGesturePerformedListener(new GestureListener());
    }
    
    private final class GestureListener implements OnGesturePerformedListener{
		@Override
		public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
			//从手势库中查询相匹配的内容，多个相似结果中找到一个最高的，可将匹配结果最高的放前面，此处每次存储都将前面的手势抹除了，只存了一个，
			//所以不需要predictions.get(0);
			ArrayList<Prediction> predictions = library.recognize(gesture);
			if(!predictions.isEmpty()){
				for (Prediction prediction : predictions){
				if(prediction.score>2.1){//score总分为10
					Lunch.canIn=1;
					Intent intent = new Intent(Gestures_keyforStart.this, MainActivity.class);
					startActivity(intent);
					Toast.makeText(Gestures_keyforStart.this, "私人秘制欢迎您！", 1).show();
					finish();
						 					
					//}

				}
				else{
					Toast.makeText(Gestures_keyforStart.this, "手势有误，请重试！", 1).show();
				}
				}
			}
		}
    }
    @Override
	protected void onDestroy() {
	//	android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}
    
}