package com.charapp.charapp.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.TextView;

import com.charapp.ayuda.R;


public class VolunteerProfileActivity extends AppCompatActivity {

    String mName, mEmail;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_profile);

        Bundle bundle = getIntent().getExtras();
        mEmail = bundle.getString("EMAIL");
        mName = bundle.getString("NAME");

        userName = (TextView)findViewById(R.id.mTxtUserName);
        userName.setAllCaps(true);
        userName.setText(mName);

    }
}
