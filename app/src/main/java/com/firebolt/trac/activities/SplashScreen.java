package com.firebolt.trac.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.firebolt.trac.R;
import me.wangyuwei.particleview.ParticleView;

public class SplashScreen extends AppCompatActivity {

    ParticleView splashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashView = (ParticleView) findViewById(R.id.splashView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                splashView.startAnim();
            }
        }, 1500);


        splashView.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 2000);
            }
        });
    }

}
