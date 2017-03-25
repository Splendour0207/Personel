package peesonel.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
/**
 * 类名称：HandleDB 
 * 类描述：数据库的相关操作
 * 创建时间：2016-04-21 上午10:30:00 
 */
public class HandleDB {
	private DBHelper dbHelper;
	
	public HandleDB(Context context){
		dbHelper = new DBHelper(context);
	}
	
	public boolean find(String packageName){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		boolean result = false;  
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select packagename from applock where packagename = ? ", 
					new String[] {packageName});
			if(cursor.moveToNext()){
				result = true; 
			}
			cursor.close();
			db.close();
		}
		return result;
	}
	
	public void add(String packageName){
		if(find(packageName)){
			return;
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.execSQL("insert into applock (packagename) values (?)", 
					new Object[] {packageName});
			db.close();
		}
	}
	
	public void delete(String packageName){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.isOpen()){
			db.execSQL("delete from applock where packagename = ? ", 
					new Object[] {packageName});
			db.close();
		}
	}
	
	public List<String> getAllPackageName(){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		List<String> packageNames = new ArrayList<String>();
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select packagename from applock", null);
			while(cursor.moveToNext()){
				String packageName = cursor.getString(0);
				packageNames.add(packageName);
			}
			cursor.close();
			db.close();
		}
		return packageNames;
	}
}
