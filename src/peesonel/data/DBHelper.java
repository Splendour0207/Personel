package peesonel.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * 类名称：DBHelper 
 * 类描述：数据库的创建和更新
 * 创建时间：2016-04-21 上午10:30:00 
 */
public class DBHelper extends SQLiteOpenHelper {

	private String CREATE_BLACKNAME_TABLE_SQL = "create table blacknumber "
			+ "(_id integer primary key autoincrement, "
			+ "number varchar(20))";
	private String CREATE_APPLOCK_TABLE_SQL = "create table applock "
			+ "(_id integer primary key autoincrement, "
			+ "packagename varchar(30))";
	public DBHelper(Context context) {
		super(context, "security.db", null, 2);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_BLACKNAME_TABLE_SQL);//
		db.execSQL(CREATE_APPLOCK_TABLE_SQL);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_APPLOCK_TABLE_SQL);
	}

}
