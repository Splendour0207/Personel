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

public class Gestures_keyforChange extends Activity {
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
        library.load();
        GestureOverlayView gestureView = (GestureOverlayView)this.findViewById(R.id.mygestures);
        gestureView.addOnGesturePerformedListener(new GestureListener());
    }
    
    private final class GestureListener implements OnGesturePerformedListener{
		@Override
		public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
			ArrayList<Prediction> predictions = library.recognize(gesture);
			if(!predictions.isEmpty()){
				for (Prediction prediction : predictions){
				//Prediction prediction = predictions.get(1);//��ȡ��ƥ��ļ�¼
				if(prediction.score>2.1){
					Intent intent = new Intent(Gestures_keyforChange.this, Build_gestures.class);
					startActivity(intent);
					finish();
				}
				else{
					Toast.makeText(Gestures_keyforChange.this, "手势输入有误，请重试！", 1).show();
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