
package personel.main;

import java.io.File;

import com.example.personel.R;
import com.example.personel.R.drawable;

import personel.app.AppActivity;
import personel.app.Gestures_keyforChange;
import personel.app.Gestures_keyforStart;
import personel.main.MyListView.MyListViewFling;
import personel.personolphoto.PPhotoActivity;
import personel.photo.PhotoloadActivity;
import personel.secret.LoginActivity;
import personel.start.Loading;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnTouchListener,
		GestureDetector.OnGestureListener, OnItemClickListener, MyListViewFling {
	private boolean hasMeasured = false;// 是否Measured.
	private LinearLayout layout_left;
	private LinearLayout layout_right;
	private ImageView iv_set;
	private MyListView lv_set;

	/** 每次自动展开/收缩的范围 */
	private int MAX_WIDTH = 0;
	/** 每次自动展开/收缩的速度 负数左移 数和速度成正比 */
	private final static int SPEED = 30;

	private final static int sleep_time = 5;

	private GestureDetector mGestureDetector;// 手势
	private boolean isScrolling = false;
	private float mScrollX; // 滑块滑动距离
	private int window_width;// 屏幕的宽度

	private String TAG = "123";

	private View view = null;// 点击的view

	// private String title[] = { "待发送队列", "同步分享设置", "编辑我的资料", "找朋友", "告诉朋友",
	// "节省流量", "推送设置" };

	private String title[] = { "重置密码","秘制相册"};

	/***
	 * 初始化view
	 */
	@SuppressWarnings("deprecation")
	void InitView() {
		layout_left = (LinearLayout) findViewById(R.id.layout_left);
		layout_right = (LinearLayout) findViewById(R.id.layout_right);
		iv_set = (ImageView) findViewById(R.id.iv_set);
		lv_set = (MyListView) findViewById(R.id.lv_set);
		lv_set.setAdapter(new ArrayAdapter<String>(this, R.layout.item,
				R.id.tv_item, title));
		// 点击监听
		lv_set.setOnItemClickListener(this);

		// listview item滑动监听
		lv_set.setMyListViewFling(this);

		layout_right.setOnTouchListener(this);
		layout_left.setOnTouchListener(this);
		iv_set.setOnTouchListener(this);
		mGestureDetector = new GestureDetector(this);
		// 禁用长按监听
		mGestureDetector.setIsLongpressEnabled(false);
		getMAX_WIDTH();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		final ImageButton lock = (ImageButton) findViewById(R.id.imageButton1);
		final ImageButton image = (ImageButton) findViewById(R.id.imageButton2);
		final ImageButton picture = (ImageButton) findViewById(R.id.imageButton3);
		final ImageButton explain = (ImageButton) findViewById(R.id.imageButton4);
//		final ImageButton logo = (ImageButton) findViewById(R.id.imageButton5);
		String folder=new String("/sdcard/SirenMizhi");
		String data=new String("/sdcard/SirenMizhi/data");
		(new File(folder)).mkdirs();
		(new File(data)).mkdirs();
		/*
		 * 按键的点击事件
		 */
		lock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(MainActivity.this,AppActivity.class);
				startActivity(intent);
				
			}
		});
		image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(MainActivity.this,
						LoginActivity.class);
				startActivity(intent);
				
			}
		});
		picture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(MainActivity.this,
						PhotoloadActivity.class);
				startActivity(intent);
				
			}
		});
		explain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(MainActivity.this,
						Loading.class);
				startActivity(intent);
				
			}
		});
		
		/*
		 * 监听touch事件，按键响应效果，图片切换
		 */
		lock.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					lock.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.button2));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					lock.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.button1));
					
				}

				return false;
			}
		});
		image.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					image.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.button4));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					image.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.button3));
					
				}

				return false;
			}
		});
		picture.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					picture.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.button6));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					picture.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.button5));
					
				}
				
				return false;
			}
		});
		explain.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					explain.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.button8));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					explain.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.button7));
					
				}
				
				return false;
			}
		});
//		logo.setOnTouchListener(new OnTouchListener() {
//
//			@SuppressLint("NewApi")
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					logo.setBackgroundDrawable(getResources().getDrawable(
//							R.drawable.photologo1));
//				} else if (event.getAction() == MotionEvent.ACTION_UP) {
//					logo.setBackgroundDrawable(getResources().getDrawable(
//							R.drawable.photologo2));
//				}
//				
//				return false;
//			}
//		});
		InitView();
	}

	/***
	 * listview 正在滑动时执行.
	 */
	void doScrolling(float distanceX) {
		isScrolling = true;
		mScrollX += distanceX;// distanceX:向左为正，右为负

		// Log.v(TAG, "distanceX= " + distanceX
		// + ",mScrollX =" + mScrollX);
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
				.getLayoutParams();
		RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) layout_right
				.getLayoutParams();
		layoutParams.leftMargin -= mScrollX;
		layoutParams_1.leftMargin = -(MAX_WIDTH - layoutParams.leftMargin);

		if (layoutParams.leftMargin >= MAX_WIDTH) {
			isScrolling = false;// 拖过头了不需要再执行AsynMove了
			layoutParams.leftMargin = MAX_WIDTH;
			layoutParams_1.leftMargin = 0;

		} else if (layoutParams.leftMargin <= 0) {
			// 拖过头了不需要再执行AsynMove了
			isScrolling = false;
			layoutParams.leftMargin = 0;
			layoutParams_1.leftMargin = 0;
		}

		layout_left.setLayoutParams(layoutParams);
		layout_right.setLayoutParams(layoutParams_1);
	}

	/***
	 * 获取移动距离 移动的距离其实就是layout_left的宽度
	 */
	void getMAX_WIDTH() {
		ViewTreeObserver viewTreeObserver = layout_left.getViewTreeObserver();
		// 获取控件宽度
		viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if (!hasMeasured) {
					window_width = getWindowManager().getDefaultDisplay()
							.getWidth();
					MAX_WIDTH = layout_right.getWidth();
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
							.getLayoutParams();
					// RelativeLayout.LayoutParams layoutParams_1 =
					// (RelativeLayout.LayoutParams) layout_right
					// .getLayoutParams();
					ViewGroup.LayoutParams layoutParams_2 = lv_set
							.getLayoutParams();
					// 注意： 设置layout_left的宽度。防止被在移动的时候控件被挤压
					layoutParams.width = window_width;
					layoutParams.rightMargin = -MAX_WIDTH;//
					layout_left.setLayoutParams(layoutParams);

					// 注意：设置lv_set的宽度防止被在移动的时候控件被挤压
					layoutParams_2.width = MAX_WIDTH;
					lv_set.setLayoutParams(layoutParams_2);

					// Log.v(TAG, "MAX_WIDTH=" + MAX_WIDTH + "width="
					// + window_width);
					hasMeasured = true;
				}
				return true;
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			if (layoutParams.leftMargin > 0) {
				new AsynMove().execute(-SPEED);
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		view = v;// 记录点击的控件

		// 松开的时候要判断，如果不到半屏幕位子则缩回去，
		if (MotionEvent.ACTION_UP == event.getAction() && isScrolling == true) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();

			Log.i("123",
					"Math.abs(layoutParams.rightMargin): "
							+ Math.abs(layoutParams.rightMargin)
							+ "---MAX_WIDTH / 2: " + MAX_WIDTH / 2);
			Log.i("123", "isScrolling: " + isScrolling);
			// 缩回去
			if (layoutParams.leftMargin < MAX_WIDTH / 2) {
				new AsynMove().execute(-SPEED);
			} else {
				new AsynMove().execute(SPEED);
			}
		}

		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {

		int position = lv_set.pointToPosition((int) e.getX(), (int) e.getY());
		if (position != ListView.INVALID_POSITION) {
			View child = lv_set.getChildAt(position
					- lv_set.getFirstVisiblePosition());
			if (child != null)
				child.setPressed(true);
		}

		mScrollX = 0;
		isScrolling = false;
		// 将之改为true，才会传递给onSingleTapUp,不然事件不会向下传递.
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	/***
	 * 点击松开执行
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// 点击的不是layout_left
		if (view != null && view == iv_set) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			// 右移动
			if (layoutParams.leftMargin <= 0) {
				new AsynMove().execute(SPEED);
				lv_set.setSelection(0);// 设置为首位.
			} else {
				// 左移动
				new AsynMove().execute(-SPEED);
			}
		} else if (view != null && view == layout_left) {
			RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			if (layoutParams.leftMargin < 0) {
				// 说明layout_left处于移动最左端状态，这个时候如果点击layout_left应该直接所以原有状态.(更人性化)
				// 右移动
				new AsynMove().execute(SPEED);
			}
		}

		return true;
	}

	/***
	 * 滑动监听 就是一个点移动到另外一个点. distanceX=后面点x-前面点x，如果大于0，说明后面点在前面点的右边及向右滑动
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// 执行滑动.
		doScrolling(distanceX);
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	class AsynMove extends AsyncTask<Integer, Integer, Void> {

		@Override
		protected Void doInBackground(Integer... params) {
			int times = 0;
			if (MAX_WIDTH % Math.abs(params[0]) == 0)// 整除
				times = MAX_WIDTH / Math.abs(params[0]);
			else
				times = MAX_WIDTH / Math.abs(params[0]) + 1;// 有余数

			for (int i = 0; i < times; i++) {
				publishProgress(params[0]);
				try {
					Thread.sleep(sleep_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

		/**
		 * update UI
		 */
		@Override
		protected void onProgressUpdate(Integer... values) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			RelativeLayout.LayoutParams layoutParams_1 = (RelativeLayout.LayoutParams) layout_right
					.getLayoutParams();
			// 右移动
			if (values[0] > 0) {
				layoutParams.leftMargin = Math.min(layoutParams.leftMargin
						+ values[0], MAX_WIDTH);
				layoutParams_1.leftMargin = Math.max(
						-(layoutParams_1.leftMargin + values[0]), 0);
				Log.v(TAG, "layout_left右" + layoutParams.leftMargin
						+ ",layout_right右" + layoutParams_1.leftMargin);
			} else {
				// 左移动
				layoutParams.leftMargin = Math.max(layoutParams.leftMargin
						+ values[0], 0);
				layoutParams_1.leftMargin = Math.max(layoutParams_1.leftMargin
						+ values[0], -MAX_WIDTH);
				Log.v(TAG, "layout_left左" + layoutParams.leftMargin
						+ ",layout_right左" + layoutParams_1.leftMargin);
			}
			layout_left.setLayoutParams(layoutParams);
			layout_right.setLayoutParams(layoutParams_1);

		}

	}

	@Override
	public void doFlingLeft(float distanceX) {
		// 执行滑动.
		doScrolling(distanceX);

	}

	@Override
	public void doFlingRight(float distanceX) {
		// 执行滑动.
		doScrolling(distanceX);

	}

	@Override
	public void doFlingOver(MotionEvent event) {
		// 松开的时候要判断，如果不到半屏幕位子则缩回去，
		if (MotionEvent.ACTION_UP == event.getAction() && isScrolling == true) {
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
					.getLayoutParams();
			// 缩回去
			if (layoutParams.leftMargin < MAX_WIDTH / 2) {
				new AsynMove().execute(-SPEED);
			} else {
				new AsynMove().execute(SPEED);
			}
		}

	}

	@Override
	//
	//
	//
	//
	// 事件处理
	//
	//
	//
	//
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) layout_left
				.getLayoutParams();
		// 只要没有滑动则都属于点击
		if (layoutParams.leftMargin == MAX_WIDTH)
		// Toast.makeText(MainActivity.this, title[position], 1).show();
		{
			if (position == 0){
				Intent intentk=new Intent(this,Gestures_keyforChange.class);
				startActivity(intentk);
				//	Toast.makeText(MainActivity.this, "改密码", 1).show();
			}
			if (position == 1)
			{
				Intent intentk=new Intent(this,PPhotoActivity.class);
			    startActivity(intentk);
			}
		}
	}
//	@Override
//	protected void onPause() {
//		finish();
//		super.onStop();
//	}
}