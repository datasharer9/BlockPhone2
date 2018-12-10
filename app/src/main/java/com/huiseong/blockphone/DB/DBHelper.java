package com.huiseong.blockphone.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "blockphone", null, 1);
    }
    //클래스 생성자.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE alarmList (id INTEGER primary key autoincrement default 1 , fHour INTEGER, fMin INTEGER, " +
                " tHour INTEGER, tMin INTEGER, mon BOOLEAN, tue BOOLEAN, wed BOOLEAN, thu BOOLEAN, fri BOOLEAN, sat BOOLEAN, sun BOOLEAN, onOff BOOLEAN); ");
    }
    @Override //오버라이드 메서드
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newstVersion) {//DB버전이 바뀌게 되면 기존 DB를 내리고 onCreate newdb.
        //이 함수는 호출될 경우에만 실행된다. // 상식적으로 생각하자.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS alarmList"); // 예제에서는 세미콜론을 제외시키고 FM// 으로는 붙이는 것이 맞다.
        onCreate(sqLiteDatabase);
    }
}
