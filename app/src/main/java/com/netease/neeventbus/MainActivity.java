package com.netease.neeventbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.netease.neeventbus.neeventbus.EventBus;
import com.netease.neeventbus.neeventbus.Subscribe;
import com.netease.neeventbus.neeventbus.ThreadMode;
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button btnOne = findViewById(R.id.btn_one);
        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        EventBus.getDefault().ungister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(EventBean bean){
        Log.e("====>", "thread = " + Thread.currentThread().getName());
        Log.e("======> ",bean.toString());
    }


}
