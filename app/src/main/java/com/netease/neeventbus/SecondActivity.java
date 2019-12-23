package com.netease.neeventbus;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.netease.neeventbus.neeventbus.EventBus;

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button btn = findViewById(R.id.btn_two);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // UserInfo Book Order
                // ABean

                new Thread(){

                    @Override
                    public void run() {
                        super.run();
                        EventBus.getDefault().post(new EventBean("1111","22222"));
                        Log.e("====> 2 ", "发送者 thread = " + Thread.currentThread().getName());
                    }
                }.start();


            }
        });

    }

}
