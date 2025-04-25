package com.example.map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Welcome extends AppCompatActivity {
    SharedPreferences sp;
    TextView showhello;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        sp = this.getSharedPreferences("username", this.MODE_PRIVATE);  //获取sharepreferences
        showhello = this.findViewById(R.id.mainword);           //显示欢迎

        showhello.setText("welcome！" + sp.getString("Loginname", ""));   //获取用户名
        Timer timer=new Timer();
        TimerTask task=new TimerTask() {
            @Override
            public void run() {//跳转主界面的任务代码写在TimerTask的run()方法中
                Intent intent=new Intent(Welcome.this,SecondActivity.class);
                startActivity(intent);
                Welcome.this.finish();
            }
        };
        timer.schedule(task,1500);
    }
}
