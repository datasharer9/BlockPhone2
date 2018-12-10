package com.huiseong.blockphone.models;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.huiseong.blockphone.R;
import com.huiseong.blockphone.service.RemainTime;

import java.util.Timer;
import java.util.TimerTask;


public class LockingActivity extends AppCompatActivity {

    //Handler mHandler;
    EditText remainTime;
    int example;// 여기서 초기화하는 지 유무는 에러가 아니다.
    private Thread mThread;
    private int mCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locking);

        remainTime = (EditText) findViewById(R.id.remainTime);

        Intent intent = getIntent();
        int endHour = intent.getIntExtra("endHour", 1);
        int endMin = intent.getIntExtra("endMin", 1);
        int startHour = intent.getIntExtra("startHour", 1);
        int startMin = intent.getIntExtra("startMin", 1);

        int instantEndHour = intent.getIntExtra("stopHour", 1);
        int instantEndMin = intent.getIntExtra("stopMin", 1);

        int differenceHour;
        int differenceMin;

        //예약 잠금 알고리즘 // 타이머 클래스를 통해서 흐른 시간을 캐치하는 것이다.  경우의 수 6가지를 분류해야한다.
        if (startHour > endHour) {//시작시간이 큰 경우

            if (startMin > endMin) {
                differenceHour = 23 - startHour + endHour;
                differenceMin = 60 - startMin + endMin;
            } else {//시작 분보다 클 경우
                differenceHour = 24 - startHour + endHour;
                differenceMin = endMin - startMin;
            }
        } else if (startHour < endHour) {//시작시간이 작은 경우
            if (startMin > endMin) {
                differenceHour = startHour - endHour - 1;
                differenceMin = 60 - startMin + endMin;
            } else {//시작 분보다 작을 경우
                differenceHour = startHour - endHour;
                differenceMin = endMin - startMin;
            }

        } else {//시작시간과 종료 시간이 같은 경우
            if (startMin > endMin) {
                differenceHour = 23;
                differenceMin = 60 - startMin + endMin;
            } else {//시작 분보다 작을 경우
                differenceHour = 24;
                differenceMin = endMin - startMin;
            }
        }
        //함수를 써서 넣으려며 runnable객체를 써야한다.

   /*     Handler mHandler = new Handler();

        mHandler.postDelayed(new Runnable() { //* 핸들러의 postDelayed 사용
            //runnable 생성자 만들기
            @Override
            public void run() {
                mCount++;
                remainTime.setText("Remain Time = " + mCount);

            }
       }, 1000);
*/
//        example=18;
//
//         mThread = new Thread("My Thread ") {
//            @Override
//            public void run() {
//                for (int i = 0; i < 100; i++) {
//
//                    if(example==0){
//                        interrupt();
//                        finish();
//                    }
//                    try {
//                        example--;
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        break;
//                    }
//                    //remainTime.setText("Remain Time = " + mCount);
//                    Log.d("My Thread", "잔여시간 " + example);
//                    if(example==0){
//                        mThread.interrupt();
//                        finish();
//                    }
//                                   }
//            }
//        };
//
//        mThread.start();


//        Intent intentRemain = new Intent(this, RemainTime.class);
//        intentRemain.putExtra("differenceHour",differenceHour);
//        intentRemain.putExtra("differenceMin",differenceMin);
//        startService(intentRemain);

//1시간이 3,600,000밀리초

//pause주면서 thread를 돌리면 된다. // handler를 쓰면 될 것이다.

//서비스로 구현
        //  mHandler.sendEmptyMessage(0);

//        mHandler = new Handler() {
//            int example=100;
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                example--;
//                remainTime.setText("Remain Time = " + example);
//                mHandler.sendEmptyMessageDelayed(0, 1000);
//            }
//           };


//
        example=differenceHour*3600+differenceMin*60;
        // 이 부분을 서비스로 구현.

        example=15;
        final TimerTask timerTask=new TimerTask() {

            @Override
            public void run() {
                example--;
               // remainTime.setText("Remain Time = " + example);
                Log.d("My Thread", "남은 시간 " + example);
        if(example==0){
            cancel();
            finish();
        }

            }
        };

        Timer timer=new Timer();

        //if(example==0) { mTimer.cancel()} // 타이머 종료시켜라.}
        timer.schedule(timerTask,0,1000);

    }//onCreate

  /*  @SuppressLint("HandlerLeak")
    Handler mHandler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            example--;
            remainTime.setText("Remain Time = " + example);
        }
    };
*/

//        mHandler = new Handler() { //핸들러 생성자 만들기
//            public void handleMessage(Message msg) {
//            // mHandler.handleMessage();
//                count--;
//                remainTime.setText("Value = " + value);
//                mHandler.sendEmptyMessageDelayed(0, 10);
//            }
//        };




    @Override
    public void onBackPressed () {
        Toast.makeText(this, "back키 사용불가.", Toast.LENGTH_SHORT).show();
        return; // 그냥 끝내기
    }


    //메뉴키 막는 방법
    protected void onPause() {
        super.onPause();
        if(example==0){
            return;
        }

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
        Toast.makeText(this, "메뉴키 사용불가.", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStop() {
        super.onStop();
        //if() { // 여기서 종료시간을 캐치해서 종료 되게 만드는 것이다.//
        if(example==0){
            return;
        }
        startActivity(new Intent(this, LockingActivity.class));
        Toast.makeText(this, "재실행", Toast.LENGTH_SHORT).show();
       // }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // registerRestartAlarm(false);
    }
/*
    public void registerRestartAlarm(boolean isOn){
        Intent intent = new Intent(StateService.this, StateReceiver.class);
        intent.setAction(RestartReceiver.ACTION_RESTART_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        if(isOn){
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, 60000, sender);
            Log.e("확인", "am");
        }else{
            am.cancel(sender);
        }
    }
    */

}

