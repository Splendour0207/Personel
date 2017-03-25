package personel.start;

import java.util.List;

import personel.app.Build_gestures;
import personel.main.MainActivity;

import com.example.personel.R;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class Adapter extends PagerAdapter {
	private List<View> data;
	private Activity activity;

	public Adapter(List<View> data, Activity activity) {
		this.data = data;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		((ViewPager) container).addView(data.get(position));
		if(data.size()-1==position){//判断导航页是不是最后一页
			Button submit = (Button) container.findViewById(R.id.guide_start_app);
			submit.setAlpha((float)0.5);
			submit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					redirect();
				}
			});
			
		}
		return data.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	private void redirect(){
		Intent intent = new Intent(activity,Build_gestures.class);
		activity.startActivity(intent);
		activity.finish();
	}
}
