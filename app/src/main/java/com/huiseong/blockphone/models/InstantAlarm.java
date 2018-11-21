package com.huiseong.blockphone.models;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.huiseong.blockphone.*;

public class InstantAlarm extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instant);

        Button start_button;
        start_button =(Button)findViewById(R.id.start_button);

        TimePicker timePicker1;
        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        timePicker1.setIs24HourView(true);


        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg=new AlertDialog.Builder(InstantAlarm.this);
                dlg.setTitle("주의! 지금부터 잠금이 시작됩니다.");
                dlg.setMessage("00:00까지 잠금이 됩니다.");
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(InstantAlarm.this,LockingActivity.class);
                        startActivity(intent);
                    }
                });
                dlg.setNegativeButton("취소",null);
                dlg.show();

            }
        });



        }
    }

