package com.netease.neeventbus;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;

public class ThirdActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);


        Button btn = findViewById(R.id.btn_three);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                test();
            }
        });

    }

//    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
//    public void getEvent(EventBean bean){
//        Log.e("====>  3 ", "thread = " + Thread.currentThread().getName());
//        Log.e("======> 3 ",bean.toString());
//    }
//
//    public void test(){
//        EventBus.getDefault().register(this);
//    }

}
