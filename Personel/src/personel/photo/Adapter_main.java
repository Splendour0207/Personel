package personel.photo;

import java.util.ArrayList;

import com.example.personel.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class Adapter_main extends BaseAdapter{
	private Context context;
	private ArrayList<String> datas;

	public Adapter_main(Context context, ArrayList<String> datas) {
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = null;
		Viewholder holder = null;
		
		if (convertView == null) {
			inflater = LayoutInflater.from(context);
			
			convertView = inflater.inflate(R.layout.adapter_main, null);
			
			holder = new Viewholder();
			
			holder.img_upload = (ImageView) convertView.findViewById(R.id.img_icon);
			
			convertView.setTag(holder);
			
		}else {
			holder = (Viewholder) convertView.getTag();
		}
		
		Drawable drawable = BitmapDrawable.createFromPath(datas.get(position).toString());
		holder.img_upload.setBackgroundDrawable(drawable);
		
		return convertView;
	}

	class Viewholder{
		private ImageView img_upload;
	}
}
