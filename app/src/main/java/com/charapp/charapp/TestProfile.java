package com.charapp.charapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.charapp.ayuda.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TestProfile extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView userEmail;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            Intent intent = new Intent(TestProfile.this, TestActivity.class);
            startActivity(intent);
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String email = user.getEmail();



        userEmail = (TextView) findViewById(R.id.txtTestUserEmail);
        userEmail.setText(email);
        logout = (Button) findViewById(R.id.btnTestLogOut);
        logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnTestLogOut:
                firebaseAuth.signOut();
                Intent intent = new Intent(TestProfile.this, TestActivity.class);
                startActivity(intent);
                finish();
        }
    }
}
