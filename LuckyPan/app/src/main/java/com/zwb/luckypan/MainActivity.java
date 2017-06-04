package com.zwb.luckypan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zwb.luckypan.view.LuckyPan;

public class MainActivity extends AppCompatActivity {
    private LuckyPan luckyPan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        luckyPan = (LuckyPan)findViewById(R.id.luckyPan);
    }

}
