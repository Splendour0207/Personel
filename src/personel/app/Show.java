package personel.app;

import com.example.personel.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Show extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show);
        Intent intent =getIntent();
		String value=intent.getStringExtra("fla1");
		 TextView text3=( TextView)findViewById(R.id.text2);
		 TextView text1=( TextView)findViewById(R.id.text1);
		 TextView text2=( TextView)findViewById(R.id.text3);
		if("加密".equals(value)){
	    text3.setText("加密前：\n");
        text1.setText("加密后：\n"+Gestures_key .encryptResult);
		}
		if("解密".equals(value)){
		text3.setText("解密系统");
		text1.setText("手势解密：\n");
        text2.setText(Gestures_key .output);
		}
  
    }
}