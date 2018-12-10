package com.huiseong.blockphone.models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;

import com.huiseong.blockphone.DB.DBHelper;
import com.huiseong.blockphone.R;
import com.huiseong.blockphone.adaters.MyAdapter;
import com.huiseong.blockphone.item.AlarmListItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MyAdapter.MyRecyclerViewClickListener {

    DBHelper myHelper;//별도로 오버라이드해서 사용해야한다. 그래서 Reservation파일을 가져다 쓰는 것이 좋다.
    SQLiteDatabase sqlDB;
    Switch alarmOnoff; // 여기는 변수선언이다. 객체선언 아니다.
    MyAdapter mAdapter;
    Cursor cursor;

    private AlarmManager mAlarmManager;

    public GestureDetector gestureDetector;
    List<AlarmListItem> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        alarmOnoff = (Switch) findViewById(R.id.alarmOnoff);

        lockAlarmManager();

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ReservationActivity.class);
                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);

        //리니어 레이아웃 매니저 설정
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //new LinearLayoutManager(this) layoutManager

        makeData();//데이터 생성

        //어댑터 설정 // 데이터를 어댑터에 담아준다.
        mAdapter = new MyAdapter(dataList);
        recyclerView.setAdapter(mAdapter);

        //이 기능은 독립적으로 작동해야한다. 따라서 코드 중복을 어느정도 감수할 수 밖에 없다.
        mAdapter.setOnClickListener(this);
    }

    private void lockAlarmManager() {

        myHelper = new DBHelper(this);
        sqlDB = myHelper.getReadableDatabase();
        cursor = sqlDB.rawQuery("SELECT * FROM alarmList;", null);

        mAlarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        while (cursor.moveToNext()) {

            int hour = cursor.getInt(1); //시작시간
            int minute = cursor.getInt(2); // 시작 분 // 추후에 종료시간 데이터 까지 가져와서
            int endHour = cursor.getInt(3);
            int endMin = cursor.getInt(4);
            //요일
            //요일별로 인스턴스가 각각 필요해서 중복이 불가피하다. 월~일
            int day[] = {2, 3, 4, 5, 6, 7, 1};

            for (int i = 0; i < 7; i++) { //왜 pendingIntent가 계속 작동하는 것인가?

                //if(cursor.getInt(i+5)==1){//요일 캐치하기.
                //시작시간, fhour,fmin
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                //calendar.set(Calendar.DAY_OF_WEEK, day[i] ); //sun 1

                Intent intent = new Intent(this, LockingActivity.class);

                intent.putExtra("endHour", endHour);
                intent.putExtra("endMin", endMin);
                intent.putExtra("startHour", hour);
                intent.putExtra("startMin", minute-1);

                PendingIntent operation = PendingIntent.getActivity(this, 0, intent, FLAG_CANCEL_CURRENT);

                mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), operation);
                // }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.right_lock) {
            Intent intent = new Intent(MainActivity.this, InstantAlarm.class);
            startActivity(intent);

        } else if (id == R.id.reservation_lock) {
            Intent intent = new Intent(MainActivity.this, ReservationActivity.class);
            startActivity(intent);

        } else if (id == R.id.setting) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void makeData() {
        myHelper = new DBHelper(this);
        sqlDB = myHelper.getReadableDatabase();
        cursor = sqlDB.rawQuery("SELECT * FROM alarmList ORDER BY fhour , fmin ;", null);

        alarmOnoff = (Switch) findViewById(R.id.alarmOnoff);

        String strTime = "";
        String strDay = "";
        int swiOnoff;
        String fmidday, tmidday; //오전 오후 설정
        int fhour, thour;
        //AlarmListItem item = new AlarmListItem();

        while (cursor.moveToNext()) {
            //List<AlarmListItem> dataList = new ArrayList<>();
            // AlarmListItem item = new AlarmListItem();

            if (cursor.getInt(1) >= 12) { //시작시간
                fmidday = "오후";
                if (cursor.getInt(1) == 12) {
                    fhour = cursor.getInt(1);
                } else {
                    fhour = cursor.getInt(1) - 12; //toString()을 해야하나?
                }
            } else {
                fmidday = "오전";
                if (cursor.getInt(1) == 0) {
                    fhour = 12;
                } else {
                    fhour = cursor.getInt(1); //toString()을 해야하나?
                }
            }
            if (cursor.getInt(3) > 12) { // 종료시간
                tmidday = "오후";
                thour = cursor.getInt(3) - 12;
            } else {
                tmidday = "오전";
                thour = cursor.getInt(3);
            }
            //여기가 if statement not proper.
            if (cursor.getInt(5) == 1) {//월~일요일 까지 db에서 onOff를 받아서 문자열에 출력해줘야한다.
                strDay += "월 ";
            }
            if (cursor.getInt(6) == 1)
                strDay += " 화";
            if (cursor.getInt(7) == 1)
                strDay += " 수";
            if (cursor.getInt(8) == 1)
                strDay += " 목";
            if (cursor.getInt(9) == 1)
                strDay += " 금";
            if (cursor.getInt(10) == 1)
                strDay += " 토";
            if (cursor.getInt(11) == 1)
                strDay += " 일";

            strTime = fmidday + " " + fhour + ":" + cursor.getInt(2) + " ~ " + tmidday + " " + thour + ":" + cursor.getInt(4);//2열은 min 정보

            swiOnoff = cursor.getInt(12);

            int alarmID = cursor.getInt(0);

            dataList.add(new AlarmListItem(strTime, strDay, swiOnoff, alarmID));
            //초기화
            strDay = "";
            //mAdapter.add(item);
        }//여기서 모두 출력한다.

        cursor.close();
        sqlDB.close();
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(MainActivity.this, ReservationActivity.class);
        startActivity(intent);
        //DB 값을 모두 설정한 상태에서 보여줘야한다.
    }

    //내가 클릭한 뷰홀더의 해당하는 id를 알아내서 전달을 해줘야 update가 가능하다.
    @Override
    public void onSwitchClicked(int position, int alarmID) {
        //myHelper = new DBHelper(this);
        sqlDB = myHelper.getWritableDatabase();
        cursor = sqlDB.rawQuery("SELECT * FROM alarmList where " + alarmID + ";", null);
        //+alarmID+
        //지금 cursor안에 아무것도 담기지 않은 것 같다. sqlite3로 체크를 해보자.
        cursor.moveToFirst();

        int swiOnoff2 = cursor.getInt(12);

        //켜져있으면 끄고
        if (swiOnoff2 == 1) {
            sqlDB.execSQL("update alarmList SET onOff=0 WHERE id=" + alarmID + "; ");
        } else {
            sqlDB.execSQL("update alarmList SET onOff=1 WHERE id=" + alarmID + "; ");
        }
        cursor.close();
        sqlDB.close();
    }

    @Override
    public void onMenuClicked(int position) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
        dlg.setTitle("확인");
        dlg.setMessage("삭제하시겠습니까?");
        dlg.setPositiveButton("삭제", null);
        dlg.show();

    }

    //pendingIntent 잠금 서비스 실행


}
