package com.huiseong.blockphone.models;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.huiseong.blockphone.DB.DBHelper;
import com.huiseong.blockphone.R;


public class ReservationActivity extends AppCompatActivity {
    LinearLayout setDay;
    LinearLayout showDay;
    Button mon, tue, wed, thu, fri, sat, sun;
    DBHelper myHelper;
    SQLiteDatabase sqlDB;
    TextView ok, cancel;
    TimePicker timePicker1, timePicker2;

    int monOn=1, tueOn=1, wedOn=1, thuOn=1, friOn=1, satOn=1, sunOn =1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        setup();
        myHelper = new DBHelper(this);

              ok.setOnClickListener(new View.OnClickListener() {//모든 설정을 여기서 끝내줘야 한다.

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) { //값을 한번에 넣어야 한다.
                sqlDB = myHelper.getWritableDatabase(); // myhelper객체가 선언되었기 때문에 클래스 멤버들을 모두 이용가능하다.

             //  sqlDB.execSQL("INSERT INTO test VALUES (26)");
                //오전 오후 값을 인식하는 값이 존재하지 않는다. // 따라서 12를 빼줘야할 것 같다.
                if(timePicker1.getHour()==timePicker2.getHour()&&timePicker1.getMinute()==timePicker2.getMinute()){
                    Toast.makeText(getApplicationContext(), "시작시간과 종료시간이 동일합니다", Toast.LENGTH_LONG).show();
                }else {
                    //id 값을 무조건 넣어야 한다.
                    sqlDB.execSQL("INSERT or replace INTO alarmList (fHour, fMin, tHour, tMin, mon, tue, wed, thu, fri, sat, sun, onOff)" +
                            "VALUES (" + timePicker1.getHour() + "," + timePicker1.getMinute() + "," + timePicker2.getHour() + ","
                            + timePicker2.getMinute() + "," + monOn + "," + tueOn + "," + wedOn + "," + thuOn + "," + friOn + "," + satOn + "," + sunOn + ", 1);");
                    sqlDB.close();
                    Intent intent = new Intent(ReservationActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "알람이 설정되었습니다.", Toast.LENGTH_LONG).show();
                }
            }


        });
    }//onCreate

    private void setup() {
        setDay = (LinearLayout) findViewById(R.id.setDay);
        showDay = (LinearLayout) findViewById(R.id.showDay);

        timePicker1=(TimePicker)findViewById(R.id.timePicker1);
        timePicker2=(TimePicker)findViewById(R.id.timePicker2);

        mon = (Button) findViewById(R.id.mon);
        tue = (Button) findViewById(R.id.tue);
        wed = (Button) findViewById(R.id.wed);
        thu = (Button) findViewById(R.id.thu);
        fri = (Button) findViewById(R.id.fri);
        sat = (Button) findViewById(R.id.sat);
        sun = (Button) findViewById(R.id.sun);

        ok = (TextView) findViewById(R.id.ok);
        cancel = (TextView) findViewById(R.id.cancel);

        mon.setOnClickListener(myListener);
        tue.setOnClickListener(myListener);
        wed.setOnClickListener(myListener);
        thu.setOnClickListener(myListener);
        fri.setOnClickListener(myListener);
        sat.setOnClickListener(myListener);
        sun.setOnClickListener(myListener);
        setDay.setOnClickListener(myListener);
        cancel.setOnClickListener(myListener);
        }

    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.setDay:
                    showDay.setVisibility(showDay.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    break;
                case R.id.mon :
                    if(monOn==0){
                        monOn=1;
                        mon.setBackgroundColor(Color.rgb(0, 0, 255));
                        break;
                    }
                    mon.setBackgroundColor(Color.rgb(255, 255, 255));
                    monOn= 0;
                    break;
                case R.id.tue :
                    if(tueOn==0){
                        tueOn=1;
                        tue.setBackgroundColor(Color.rgb(0, 0, 255));
                        break;
                    }
                    tue.setBackgroundColor(Color.rgb(255, 255, 255));
                    tueOn= 0;
                    break;
                case R.id.wed :
                    if(wedOn==0){
                        wedOn=1;
                        wed.setBackgroundColor(Color.rgb(0, 0, 255));
                        break;
                    }
                    wed.setBackgroundColor(Color.rgb(255, 255, 255));
                    wedOn= 0;
                    break;
                case R.id.thu :
                    if(thuOn==0){
                        thuOn=1;
                        thu.setBackgroundColor(Color.rgb(0, 0, 255));
                        break;
                    }
                    thu.setBackgroundColor(Color.rgb(255, 255, 255));
                    thuOn= 0;
                    break;
                case R.id.fri :
                    if(friOn==0){
                        friOn=1;
                        fri.setBackgroundColor(Color.rgb(0, 0, 255));
                        break;
                    }
                    fri.setBackgroundColor(Color.rgb(255, 255, 255));
                    friOn= 0;
                    break;
                case R.id.sat :
                    if(satOn==0){
                        satOn=1;
                        sat.setBackgroundColor(Color.rgb(0, 0, 255));
                        break;
                    }
                    sat.setBackgroundColor(Color.rgb(255, 255, 255));
                    satOn= 0;
                    break;
                case R.id.sun :
                    if(sunOn==0){
                        sunOn=1;
                        sun.setBackgroundColor(Color.rgb(0, 0, 255));
                        break;
                    }
                    sun.setBackgroundColor(Color.rgb(255, 255, 255));
                    sunOn= 0;
                    break;
                case R.id.cancel:
                    finish();
                    break;
            }
        }
    };



    }

