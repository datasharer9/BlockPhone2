package com.huiseong.blockphone.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RemainTime extends IntentService {
    int differenceHour, differenceMin;

    public RemainTime() {
        super("RemainTime");
    } // 생성자

    @Override
    protected void onHandleIntent(@Nullable Intent intent) { //이게 기본 메서드이다.
        //지속시간 받아오기
        differenceHour= intent.getIntExtra("differenceHour",1);
        differenceMin= intent.getIntExtra("differenceMin",1);

        for (int i = 0; i < 5; i++) {
            try {
                // 1초 마다 쉬기
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
           //remainTime.setText("Remain Time = " + example);

            //잠금을 다시 시작을 해도 잠금 서비스는 죽어서는 안된다. 그래서 서비스를 써야하는 것이다.
            Log.d("MyIntentService", "인텐트 서비스 동작 중 " + i);
        }
    }

}

