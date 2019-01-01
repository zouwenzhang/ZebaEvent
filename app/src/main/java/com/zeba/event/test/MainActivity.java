package com.zeba.event.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zeba.event.ZebaBroad;
import com.zeba.event.ZebaEvent;
import com.zeba.event.annotation.Receiver;
import com.zeba.event.annotation.Subscriber;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ZebaBroad.init(getApplication());
        ZebaBroad.sendBroadcast("testAction");
        ZebaEvent.sendEvent("testTag");
    }

    @Receiver("testAction")
    private void broadCastReceiver(){
        //do something
    }

    @Subscriber("testTag")
    private void onEvent(){
        //do something
    }
}
