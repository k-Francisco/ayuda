package com.charapp.charapp;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.charapp.ayuda.R;
import com.charapp.charapp.views.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    Animation animation;
    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        logo = (ImageView) findViewById(R.id.splashLogo);
        startAnimation();
    }

    private void startAnimation() {
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        logo.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Thread splashThread = new Thread(){
                    @Override
                    public void run() {
                        try{
                            int waited = 0;
                            while (waited < 2000){
                                sleep(100);
                                waited+=100;
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                                        Pair<View, String> p1 = Pair.create((View) logo, "logo");
                                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat
                                                .makeSceneTransitionAnimation(SplashActivity.this, p1);
                                        startActivity(intent, optionsCompat.toBundle());
                                    }
                                    else{
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                splashThread.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
