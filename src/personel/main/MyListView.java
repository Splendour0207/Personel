package personel.main;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.ListView;

public class MyListView extends ListView implements OnGestureListener {

	private GestureDetector gd;
	// 事件状态
	public static final char FLING_CLICK = 0;
	public static final char FLING_LEFT = 1;
	public static final char FLING_RIGHT = 2;
	public static char flingState = FLING_CLICK;

	private float distanceX;// 水平滑动的距离

	private MyListViewFling myListViewFling;

	public static boolean isClick = false;// 是否可以点击

	public void setMyListViewFling(MyListViewFling myListViewFling) {
		this.myListViewFling = myListViewFling;
	}

	public float getDistanceX() {
		return distanceX;
	}

	public char getFlingState() {
		return flingState;
	}

	public MyListView(Context context) {
		super(context);

	}

	@SuppressWarnings("deprecation")
	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		gd = new GestureDetector(this);
	}

	/**
	 * 覆写此方法，以解决ListView滑动被屏蔽问题
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		myListViewFling.doFlingOver(event);// 回调执行完毕.
		this.gd.onTouchEvent(event);

		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		/***
		 * 当移动的时候
		 */
		if (ev.getAction() == MotionEvent.ACTION_DOWN)
			isClick = true;
		if (ev.getAction() == MotionEvent.ACTION_MOVE)
			isClick = false;
		return super.onTouchEvent(ev);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		int position = pointToPosition((int) e.getX(), (int) e.getY());
		if (position != ListView.INVALID_POSITION) {
			getChildAt(position - getFirstVisiblePosition());
		}
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {

		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// 左滑动
		if (distanceX > 0) {
			flingState = FLING_RIGHT;
			Log.v("jj", "左distanceX=" + distanceX);
			myListViewFling.doFlingLeft(distanceX);// 回调
			// 右滑动.
		} else if (distanceX < 0) {
			flingState = FLING_LEFT;
			Log.v("jj", "右distanceX=" + distanceX);
			myListViewFling.doFlingRight(distanceX);// 回调
		}

		return false;
	}

	/***
	 * 上下文菜单
	 */
	@Override
	public void onLongPress(MotionEvent e) {
		// System.out.println("Listview long press");
		// int position = pointToPosition((int) e.getX(), (int) e.getY());
		// if (position != ListView.INVALID_POSITION) {
		// View child = getChildAt(position - getFirstVisiblePosition());
		// if (child != null) {
		// showContextMenuForChild(child);
		// this.requestFocusFromTouch();
		// }
		//
		// }
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		return false;
	}

	/***
	 * 回调接口
	 * 
	 * @author jjhappyforever...
	 * 
	 */
	interface MyListViewFling {
		void doFlingLeft(float distanceX);// 左滑动执行

		void doFlingRight(float distanceX);// 右滑动执行

		void doFlingOver(MotionEvent event);// 拖拽松开时执行

	}

}
