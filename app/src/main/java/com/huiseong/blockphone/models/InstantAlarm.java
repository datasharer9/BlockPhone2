package com.huiseong.blockphone.models;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.huiseong.blockphone.*;

import java.util.Calendar;


public class InstantAlarm extends AppCompatActivity {

    private AlarmManager mAlarmManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant);

        Button start_button;
        start_button = (Button) findViewById(R.id.start_button);

        final TimePicker timePicker1;
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        timePicker1.setIs24HourView(true);


        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(InstantAlarm.this);
                dlg.setTitle("주의! 지금부터 잠금이 시작됩니다.");
                dlg.setMessage("00:00까지 잠금이 됩니다.");
                dlg.setNegativeButton("취소", null);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //시작시간 설정 및 객체 만들기
                        final Calendar c = Calendar.getInstance();
                        int hour = c.get(Calendar.HOUR);
                        int minute = c.get(Calendar.MINUTE);
                        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                        //시작시간 객체에 담기
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        //calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

                        // 즉시잠금은 예약 잠금보다 쉽다. 그냥 putExtra로 종료 시간만 넘겨주면 된다.

                        Intent intent = new Intent(getApplicationContext(), LockingActivity.class);
                        intent.putExtra("stopHour", timePicker1.getCurrentHour());
                        intent.putExtra("stopMin", timePicker1.getCurrentMinute()); // 설정 시간과 분만 받자.
                        PendingIntent operation = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        //종료하라는 pendingIntent를 보낼 수는 없는건가? 시작하는 인텐트만 가능한 것인가?
                        //getSerice를 보낸 다음에 서비스를 종료시키는 전략을 쓰면 안되나. 액티비티 종료는 없는 것 같은데 // 아니다 액티비티 finish()을 하면된다.
                        //종료시간에 종료명령을 다시 한번 intent를 보내면 된다. //종료!
                        mAlarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), operation);
                    }

                });

                dlg.show();
            }


        });
    }
}