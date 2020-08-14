package com.ljz.rxjava.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ljz.rxjava.R;
import com.ljz.rxjava.utils.ToastUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Context mContext;
    private Button simpleBtn;
    private Button mapBtn;
    private Button schedulerBtn;
    private Button retrofitBtn;
    private Button actionBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        simpleBtn = findViewById(R.id.simple_btn);
        simpleBtn.setOnClickListener(this);
        mapBtn = findViewById(R.id.map_btn);
        mapBtn.setOnClickListener(this);
        schedulerBtn = findViewById(R.id.scheduler_btn);
        schedulerBtn.setOnClickListener(this);
        retrofitBtn = findViewById(R.id.retrofit_btn);
        retrofitBtn.setOnClickListener(this);
        actionBtn = findViewById(R.id.test_action);
        actionBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent();
        switch (id) {
            case R.id.simple_btn:
                intent.setClassName("com.ljz.rxjava","com.ljz.rxjava.activity.TestRxSimpleActivity");
                break;
            case R.id.map_btn:
                intent.setComponent(new ComponentName("com.ljz.rxjava","com.ljz.rxjava.activity.TestRxMapActivity"));
                break;
            case R.id.scheduler_btn:
                intent.setComponent(new ComponentName("com.ljz.rxjava","com.ljz.rxjava.activity.SchedulerActivity"));
                break;
            case R.id.retrofit_btn:
                intent.setComponent(new ComponentName("com.ljz.rxjava","com.ljz.rxjava.activity.RetrofitActivity"));
                break;
//            case R.id.test_action:
//                break;
            default:
                ToastUtil.toast(mContext, "没有有效的跳转页面");
                return;
        }
        startActivity(intent);
    }
}
