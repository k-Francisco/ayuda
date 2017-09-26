package com.charapp.charapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.charapp.ayuda.R;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;


public class ViewMyActivityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_activity);


        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(R.drawable.img_coverphoto)
                .headerLayout(R.layout.header)
                .contentLayout(R.layout.activity_view_my_activity);
        setContentView(helper.createView(this));
        helper.initActionBar(this);



    }
}
