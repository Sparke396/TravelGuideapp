package com.example.travelguideapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    TextView logoTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logoTv=findViewById(R.id.logo);
        Animation animation=new TranslateAnimation(0.0f,0.0f,-200.0f,200.0f);
//        Animation animation=new AlphaAnimation(0.0f,1.0f);
        animation.setDuration(4000);
        logoTv.setAnimation(animation);
        animation.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
            }
        },4000);
    }
}