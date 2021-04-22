package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    LinearLayout l1;
    Animation uptodown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l1=(LinearLayout) findViewById(R.id.logo);
        uptodown= AnimationUtils.loadAnimation(MainActivity.this,R.anim.uptodown);
        l1.setAnimation(uptodown);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(MainActivity.this,Login.class);
                startActivity(i);
                finish();
            }
        },3000);

    }
}
