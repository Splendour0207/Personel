package personel.photo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.example.personel.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

public class PhotoloadActivity extends Activity {
	private GridView grv_content;
	private ImageButton btn_add;
	private ImageButton btn_send;
	private ArrayList<String> datas = new ArrayList<String>();
	private ArrayList<Image> upLoads = new ArrayList<Image>();
	public static Context context;
	BaseAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 隐藏状态栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 设置布局
		setContentView(R.layout.photo_load);
		// 初始化组件
		initCompengts();
		// 注册监听
		registerListener();
	}

	private void initCompengts() {
		grv_content = (GridView) findViewById(R.id.grv_content);
		btn_add = (ImageButton) findViewById(R.id.btn_add);
		btn_send = (ImageButton) findViewById(R.id.btn_send);
		adapter = new Adapter_main(this, datas);
		grv_content.setAdapter(adapter);
	}

	/*
	 * 按钮事件
	 */
	public static Context getContext() {
		return context;
	}

	private void registerListener() {
		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (String e : datas) {
					File f = new File(e);
					String[] str = e.split("/");
					String name = null;
					name = str[str.length - 1];
					try {
						FileCopy.copyFile(f, new File(
								"/sdcard/SirenMizhi/data/" + name + ".jpg"));
					} catch (IOException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
					// Log.v("data", name);
					f.delete();
				}
				adapter.notifyDataSetChanged();
				datas.removeAll(datas);
				Toast.makeText(PhotoloadActivity.this, "上传成功", 1).show();
			}
		});
		btn_send.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				// TODO 自动生成的方法存根
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					btn_send.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.upload_d));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					btn_send.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.upload_u));
				}
				return false;
			}
		});
		btn_add.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				// TODO 自动生成的方法存根
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					btn_add.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.add_d));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					btn_add.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.add_u));
				}
				return false;
			}
		});
		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, 1);
			}
		});

		grv_content.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				datas.remove(position);
				adapter.notifyDataSetChanged();

				return false;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// 从图片库
		if (requestCode == 1 && resultCode == RESULT_OK) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			int i = 0;
			for (String e : datas) {
				if (picturePath.equals(e))
					i++;
			}
			if (i == 0)
				datas.add(picturePath);
			adapter.notifyDataSetChanged();
		}
	}
}
