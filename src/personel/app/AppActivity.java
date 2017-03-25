package personel.app;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.example.personel.R;

import peesonel.data.HandleDB;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 类名称：MainActivity 
 * 类描述：系统主页面
 * 创建时间：2016-04-15 下午3:31:00 
 */
public class AppActivity extends Activity implements OnClickListener,OnLongClickListener{

	private static final int GET_ALL_APP_FINISH = 1;
	private static final int GET_USER_APP_FINISH = 2;
	private ListView lv_app_manager;
	private LinearLayout ll_app_manager_progress; 
    private LinearLayout ll_app_logo;
    private ImageView iv_app_lock;
    private TextView tv_app_lock;
    private EditText etPwd1;
    private EditText etPwd2;
    private EditText etOldPwd;
    private EditText etNewPwd1;
    private EditText etNewPwd2;
    private View viewSetPwd;
    private View viewChangePwd;
    private SharedPreferences preferencesPwd;
    private Editor editor;
	private AppInfoProvider provider;
	private AppManagerAdapter adapter;
	/**
	 * 判断是不是还在加载中，如果还在加载中的话，就不能进行应用的切换  
	 */
    private boolean flag = false; 
	private List<AppInfo> list;
	private PopupWindow popupWindow;
	private PopupWindow gesPopupWindow;
	private TextView tv_app_title;
	private HandleDB handleDB;
	private Intent intentService;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler()  
    {  
        @SuppressLint("HandlerLeak")
		public void handleMessage(Message msg)   
        {  
            switch(msg.what)  
            {  
                case GET_ALL_APP_FINISH :   
                    //进度条设置为不可见  
                    ll_app_manager_progress.setVisibility(View.GONE);  
                    adapter = new AppManagerAdapter(list);  
                    lv_app_manager.setAdapter(adapter);
                    flag = true;
                    break;  
                case GET_USER_APP_FINISH:
                	//进度条设置为不可见
                	ll_app_manager_progress.setVisibility(View.GONE);
                    adapter = new AppManagerAdapter(getUserApp());  
                    lv_app_manager.setAdapter(adapter);  
                    flag = true;  
                    break;   
                default :   
                    break;  
            }  
        };  
    };  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//隐去标题栏（应用程序的名字）  
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main_layout);
		 
		initView();//初始化视图
        initUI(false); //判断是否为用户APP 并获取
      
        
        lv_app_manager.setOnItemClickListener(new OnItemClickListener() {
//为ListView 注册一个监听器，用户点击ListView中任何一个子项时都会回调onItemClick()
        	
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			
			dismissPopupWindow();//弹出的包含运行分享等的那个对话框
			//用来存放当前的item的坐标值，第一个是x的坐标，第二个是y的坐标 
			int[] location = new int[2];
			//把当前的item的坐标值放到int数组里面
			view.getLocationInWindow(location);
			
//			点击每个应用程序（ListView子项）时。加载弹框布局
			View popupView = View.inflate(AppActivity.this, R.layout.popup_item, null);
			LinearLayout ll_app_run = (LinearLayout) popupView.findViewById(R.id.ll_app_start);  
            LinearLayout ll_app_share = (LinearLayout) popupView.findViewById(R.id.ll_app_share);  
            LinearLayout ll_app_lock = (LinearLayout) popupView.findViewById(R.id.ll_app_lock);
            LinearLayout ll_app_uninstall = (LinearLayout) popupView.findViewById(R.id.ll_app_uninstall);
            iv_app_lock = (ImageView) popupView.findViewById(R.id.iv_app_lock);
            tv_app_lock = (TextView) popupView.findViewById(R.id.tv_app_lock);
            ll_app_run.setOnClickListener(AppActivity.this);  
            ll_app_share.setOnClickListener(AppActivity.this);
            ll_app_lock.setOnClickListener(AppActivity.this);
            ll_app_lock.setOnLongClickListener(AppActivity.this);
            ll_app_uninstall.setOnClickListener(AppActivity.this);  
            ll_app_logo.setOnClickListener(AppActivity.this);
            
            //设置密码页面控件
            LayoutInflater inflater = getLayoutInflater();
        	viewSetPwd = inflater.inflate(R.layout.set_pwd,null);
        	etPwd1 = (EditText) viewSetPwd.findViewById(R.id.et_pwd1);
        	etPwd2 = (EditText) viewSetPwd.findViewById(R.id.et_pwd2);
        	viewChangePwd = inflater.inflate(R.layout.change_pwd, null);
        	etOldPwd = (EditText) viewChangePwd.findViewById(R.id.et_old_pwd);
        	etNewPwd1 = (EditText) viewChangePwd.findViewById(R.id.et_new_pwd1);
        	etNewPwd2 = (EditText) viewChangePwd.findViewById(R.id.et_new_pwd2);
        	
          //拿到当时点击的条目，并设置到view里面  
            AppInfo info = (AppInfo) lv_app_manager.getItemAtPosition(position);
            String packageName = info.getPackageName();
            if(handleDB.find(packageName)){//可查询到包名
            	iv_app_lock.setImageDrawable(getResources().getDrawable(R.drawable.app_unlock));
            	tv_app_lock.setText("解锁");
            }
            ll_app_run.setTag(info);
            ll_app_share.setTag(info);
            ll_app_lock.setTag(info);
            ll_app_uninstall.setTag(info);//2016.0613.明天继续看
            
            preferencesPwd = getSharedPreferences("passWord",MODE_PRIVATE);
			editor = preferencesPwd.edit();
          //添加动画
            LinearLayout ll_app_popup = (LinearLayout) popupView.findViewById(R.id.ll_app_popup);
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
            scaleAnimation.setDuration(300);
            
          //new 一个PopupWindow出来
           popupWindow = new PopupWindow(popupView, 400, 100);//615,150
           //一定要给PopupWindow设置一个背景图片，不然的话，会有很多未知的问题的  popupWindow：弹框
           //如没办法给它加上动画，还有显示会有问题等，  
           //如果我们没有要设置的图片，那么我们就给它加上了一个透明的背景图片
           Drawable drawable = new ColorDrawable(Color.TRANSPARENT);
           popupWindow.setBackgroundDrawable(drawable);
           
           int x = location[0]+40;
           int y = location[1];
           //把PopupWindow显示出来 
           popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.TOP, x, y);
        //   popupWindow.showAsDropDown(view);
           
         //开启动画
           ll_app_popup.startAnimation(scaleAnimation);
         
		}
	});
     //当listview滚动的时候调用的方法
     lv_app_manager.setOnScrollListener(new OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			dismissPopupWindow(); 
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			dismissPopupWindow(); 
		}
	});
	}
	
	//初始化视图
	public void initView(){
		//开启监听服务
		intentService = new Intent(this,WatchAppService.class);
		startService(intentService);
		 
		handleDB = new HandleDB(this);
		lv_app_manager = (ListView) findViewById(R.id.lv_app_manager);  
		ll_app_manager_progress = (LinearLayout) findViewById(R.id.ll_app_manager_progress);  
		ll_app_manager_progress.setVisibility(View.VISIBLE);
		ll_app_logo = (LinearLayout) findViewById(R.id.ll_app_logo);
		tv_app_title = (TextView) findViewById(R.id.tv_app_title);
		tv_app_title.setOnClickListener(this);
		ll_app_logo.setOnClickListener(AppActivity.this);
	}
	/**
	 *功能：判断PopupWindow是不是存在，存在就把它dismiss掉  
	 */
    private void dismissPopupWindow()  
    {  
        if(popupWindow != null)  
        {  
            popupWindow.dismiss();  
            popupWindow = null;  
        }  
    }
    /**
     *功能：将锁控件新加一个长按事件，用于更改密码
     * @param v
     * @return
     */
    @Override
    public boolean onLongClick(View v) {
    	// TODO Auto-generated method stub
    	switch (v.getId()) {
		case R.id.ll_app_lock:
		//	String stringPwd = preferencesPwd.getString("pwd", "");
			if(Build_gestures.HasGes==false){
				//没有首次设置密码，长按无效
			}else{
				changePwd();
			}
			break;
		default:
			break;
		}
    	return true;
    }  
    @Override
    public void onClick(View v) {
    	// TODO Auto-generated method stub
    	AppInfo item = (AppInfo) v.getTag(); 
    	switch (v.getId()) {
    	case  R.id.tv_app_title:
    	case R.id.ll_app_start:
			try {
				/**
				 * 拿到这个包对应的PackageInfo对象，这里我们指定了两个flag，  
				 * 一个就是之前讲过的，所有的安装过的应用程序都找出来，包括卸载了但没清除数据的  
				 * 一个就是指定它去扫描这个应用的AndroidMainfest文件时候的activity节点，  
				 * 这样我们才能拿到具有启动意义的ActivityInfo，如果不指定，是无法扫描出来的 
				 * 
				 * */
				PackageInfo packageInfo = getPackageManager().getPackageInfo(item.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_ACTIVITIES);
				//扫描出来的所以activity节点的信息
				ActivityInfo[] activityInfos = packageInfo.activities;
				//有些应用是无法启动的，所以我们就要判断一下 
				if(activityInfos != null && activityInfos.length > 0){
					//在扫描出来的应用里面，第一个是具有启动意义的 
					ActivityInfo startActivity = activityInfos[0];
					//设置Intent，启动activity
					try{
						Intent intent = new Intent();
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.setClassName(item.getPackageName(), startActivity.name);
						startActivity(intent);
					}catch(Exception e){
						Toast.makeText(AppActivity.this, "很抱歉，启动失败！", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(AppActivity.this, "这个应用程序无法启动", Toast.LENGTH_SHORT).show(); 
				}
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case R.id.ll_app_share:
			Intent shareintent = new Intent();
			//设置Intent的action
			shareintent.setAction(Intent.ACTION_SEND);
			//设定分享的类型是纯文本的
			shareintent.setType("text/plain");
			//设置分享主题
			shareintent.putExtra(Intent.EXTRA_SUBJECT, "分享");
			//设置分享的文本
			shareintent.putExtra(Intent.EXTRA_TEXT, "有一个很好的应用程序哦！给你推荐一下：" +item.getAppName()+"/n本消息来自系统测试");
			startActivity(shareintent);
			break;
		case R.id.ll_app_lock:
			String itemPackageName = item.getPackageName();
//			String pwd = preferencesPwd.getString("pwd", "");
//			if(tv_app_lock.getText().equals("加锁")){
//				if(TextUtils.isEmpty(pwd)){
//					setPassWord(itemPackageName);
//				}else{
//					handleDB.add(itemPackageName);
//					Toast.makeText(MainActivity.this, "加锁成功", Toast.LENGTH_SHORT).show();
//				}
//			}else if(tv_app_lock.getText().equals("解锁")){
//				unLock(itemPackageName);
//			}
			if (tv_app_lock.getText().equals("加锁")) {
				if(Build_gestures.HasGes==true){
					handleDB.add(itemPackageName);
				}else{
					Intent intent1 = new Intent(AppActivity.this,
							Build_gestures.class);
					handleDB.add(itemPackageName);
					startActivity(intent1);
				}
				Toast.makeText(AppActivity.this, "加锁成功", Toast.LENGTH_SHORT).show();
			}
			else if(tv_app_lock.getText().equals("解锁")){
//				View gesView = View.inflate(MainActivity.this, R.layout.gestures_key, null);
//			//	unLock(itemPackageName);
//				  gesPopupWindow = new PopupWindow(gesView, 300, 300);//615,150
//		           Drawable drawable = new ColorDrawable(Color.TRANSPARENT);
//		           gesPopupWindow.setBackgroundDrawable(drawable);
//		           
//		           int x = 40;
//		           int y = 40;
//		           //把PopupWindow显示出来 
//		           gesPopupWindow.showAtLocation(v, Gravity.LEFT | Gravity.TOP, x, y);
				tempUnLock(itemPackageName);
			}
			
			adapter.notifyDataSetChanged();
			break;
		case R.id.ll_app_uninstall:
			if(item.isSystemApp()){
				 Toast.makeText(AppActivity.this, "不能卸载系统的应用程序", Toast.LENGTH_SHORT).show();  
			}else{
				String strUri = "package:"+item.getPackageName();
				Uri uri = Uri.parse(strUri);//通过uri去访问你要卸载的包名
				Intent delectIntent = new Intent();
				delectIntent.setAction(Intent.ACTION_DELETE);
				delectIntent.setData(uri);
				startActivityForResult(delectIntent, 0);
			}
			break;
		default:
			break;
		}
    	dismissPopupWindow(); 
    }
    public void tempUnLock(final String packageName){
    	handleDB.delete(packageName);
	//	boolean isVisible = true;
	//	dialogView(dialog,isVisible);
		Toast.makeText(AppActivity.this, "解锁成功", Toast.LENGTH_SHORT).show();
    }
    public void unLock(final String packageName){
    	final EditText pwd = new EditText(this);
    	pwd.setHint("请输入密码");
    	pwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
    	new AlertDialog.Builder(this).setTitle("密码验证").setIcon(
    			R.drawable.ic_setting_pwd).setView(
    		    		 pwd).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String stringPwd = preferencesPwd.getString("pwd", "");
						if(TextUtils.isEmpty(pwd.getText().toString())){
							boolean isVisible = false;
							dialogView(dialog,isVisible);
							Toast.makeText(AppActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
						}else if((pwd.getText().toString()).equals(stringPwd)){
							handleDB.delete(packageName);
							boolean isVisible = true;
							dialogView(dialog,isVisible);
							Toast.makeText(AppActivity.this, "解锁成功", Toast.LENGTH_SHORT).show();
						}else{
							boolean isVisible = false;
							dialogView(dialog,isVisible);
							Toast.makeText(AppActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
						}
					}
				})
    		     .setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						boolean isVisible = true;
						dialogView(dialog,isVisible);
					}
				}).show();
    }
    /**
     * 功能：第一次加锁设置密码
     * @param itemPackageName
     */
    public void setPassWord(final String itemPackageName){
    	 new AlertDialog.Builder(this).setTitle("设置安全锁密钥").setIcon(R.drawable.ic_setting_pwd)
    	.setView(viewSetPwd)
    	.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if((etPwd1.getText().toString()).equals(etPwd2.getText().toString())){
					if(TextUtils.isEmpty(etPwd1.getText().toString()) | TextUtils.isEmpty(etPwd2.getText().toString())){
						Toast.makeText(AppActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
						boolean isVisible = false;
						dialogView(dialog,isVisible);
					}else{
						editor.putString("pwd", etPwd1.getText().toString());
						editor.commit();
						handleDB.add(itemPackageName);
						Toast.makeText(AppActivity.this, "密码设置成功", Toast.LENGTH_SHORT).show();
						Toast.makeText(AppActivity.this, "加锁成功", Toast.LENGTH_SHORT).show();
						boolean isVisible = true;
						dialogView(dialog,isVisible);
					}
				}else{
					etPwd1.setText("");
					etPwd2.setText("");
					Toast.makeText(AppActivity.this, "两次密码不相同", Toast.LENGTH_SHORT).show();
					boolean isVisible = false;
					dialogView(dialog,isVisible);
				}
			}
		})
		.setNeutralButton("重置", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				etPwd1.setText("");
				etPwd2.setText("");
				boolean isVisible = false;
				dialogView(dialog,isVisible);
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				boolean isVisible = true;
				dialogView(dialog,isVisible);
			}
		} )
		.create().show();
    	 }
    /**
     * 功能：控制popupwindow对话框消失与显示
     * @param dialog
     * @param isVisible
     */
    public void dialogView(DialogInterface dialog,boolean isVisible){
    	try {
			//实现点击重置对话框不消失
			Field field;
			field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);  
			//设置mShowing值，欺骗android系统   
			field.set(dialog, isVisible);  //需要关闭的时候将这个参数设置为true 他就会自动关闭了
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }
    
    /**
     * 功能：重置密码
     */
    public void changePwd(){
    	new AlertDialog.Builder(this).setTitle("密码设置").setView(viewChangePwd).
		setIcon(R.drawable.ic_setting_pwd).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String stringPwd = preferencesPwd.getString("pwd", "");
						if((etOldPwd.getText().toString()).equals(stringPwd)){
							if((etNewPwd1.getText().toString()).equals(etNewPwd2.getText().toString())){
								editor.putString("pwd", etNewPwd1.getText().toString());
								editor.commit();
								Toast.makeText(AppActivity.this, "密码重置成功", Toast.LENGTH_SHORT).show();
								boolean isVisible = true;
								dialogView(dialog,isVisible);
							}else{
								etNewPwd1.setText("");
								etNewPwd2.setText("");
								boolean isVisible = false;
								dialogView(dialog,isVisible);
								Toast.makeText(AppActivity.this, "两次新密码不相同", Toast.LENGTH_SHORT).show();
							}
						}else{
							boolean isVisible = false;
							dialogView(dialog,isVisible);
							Toast.makeText(AppActivity.this, "原始密码错误", Toast.LENGTH_SHORT).show();
						}
					}
				})
    		     .setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						boolean isVisible = true;
						dialogView(dialog,isVisible);
					}
				}).show();
    }
    
    /**
     * 功能：重写onActivityResult方法来刷新list列表
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	if("用户应用程序".equals(tv_app_title.getText().toString().trim()))  
        {  
            initUI(true);  
            adapter.setAppInfos(getUserApp());  
            //通知ListView数据发生了变化  
            adapter.notifyDataSetChanged();  
        }  
        else  
        {  
            initUI(false);  
        }  
    	
    }
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	//stopService(intentService);
    }
    private void initUI(final boolean isUserApp)  
    {  
    	//判断是否是用户的APP
        flag = false;  
        ll_app_manager_progress.setVisibility(View.VISIBLE);  
          
        //因为搜索手机里面的应用程序有可能是非常耗时的，所以我们开启一个新的线程去进行搜索  
        //当搜索完成之后，就把一个成功的消息发送给Handler，然后handler把搜索到的数据设置进入listview里面  
        new Thread()  
        {  
            public void run()   
            {  
                provider = new AppInfoProvider(AppActivity.this);  
                list = provider.getAllApps();  
                Log.i("info", "-->list"+"="+list.size());//?
                Message msg = new Message();   
                msg.what = GET_USER_APP_FINISH;   
                handler.sendMessage(msg);  
            };  
        }.start();  
    }
    /**
     * 判断并获得userApp
     * @return
     */
    private List<AppInfo> getUserApp()  
    
    {  
        List<AppInfo> userApps = new ArrayList<AppInfo>();  
        for(AppInfo info : list)  
        {  
            if(!info.isSystemApp())  
            {  
                userApps.add(info);  
            }  
        } 
        Log.i("info", "-->userApps"+"="+userApps.size());
        return userApps;  
    }  
	/*-----------------------*/
    /**
     * App管理适配器
     * @author Administrator
     *
     */
	private class AppManagerAdapter extends BaseAdapter{

		private List<AppInfo> appInfos;
		public AppManagerAdapter(List<AppInfo> appInfos) {
			// TODO Auto-generated constructor stub
			this.appInfos = appInfos; 
		}
		//设置adapter的数据
		public void setAppInfos(List<AppInfo> appInfos) {
			// TODO Auto-generated method stub
			 this.appInfos = appInfos;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return appInfos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return appInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			AppInfo info = appInfos.get(position);
			String packageName = info.getPackageName();
			if(convertView == null){
				View view = View.inflate(AppActivity.this, R.layout.app_manager_item, null);
				AppManagerViews views = new AppManagerViews();  
                views.iv_app_icon = (ImageView) view.findViewById(R.id.iv_app_manager_icon);  
                views.tv_app_name = (TextView) view.findViewById(R.id.tv_app_manager_name);
                views.iv_appisLock = (ImageView) view.findViewById(R.id.iv_app_lock);
                views.iv_app_icon.setImageDrawable(info.getIcon());  
                views.tv_app_name.setText(info.getAppName());
              //这里用于判断应用程是否加锁了，根据加锁情况来添加图标
                if(handleDB.find(packageName)){
                	views.iv_appisLock.setImageDrawable(getResources().getDrawable(R.drawable.lock));
                }else{
                	views.iv_appisLock.setImageDrawable(getResources().getDrawable(R.drawable.unlock));
                }
                view.setTag(views);  
                return view;  
			
			}else{  
                AppManagerViews views = (AppManagerViews) convertView.getTag();  
                views.iv_app_icon.setImageDrawable(info.getIcon());  
                views.tv_app_name.setText(info.getAppName());
                if(handleDB.find(packageName)){
                	//handleDB.delete(packageName);
                	views.iv_appisLock.setImageDrawable(getResources().getDrawable(R.drawable.lock));
                }else{
                	views.iv_appisLock.setImageDrawable(getResources().getDrawable(R.drawable.unlock));
                }
                return convertView;  
            }  
		}
		
	}
	
	/**
	 *功能：用来优化listview的类 
	 * @author LI
	 *
	 */
	private class AppManagerViews  
	{  
		ImageView iv_app_icon;  
		TextView tv_app_name;
		ImageView iv_appisLock;
	}
}
