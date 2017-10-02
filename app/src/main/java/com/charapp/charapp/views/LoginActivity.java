package com.charapp.charapp.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.charapp.ayuda.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by dobit on 9/27/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login;
    private EditText email;
    private EditText password;
    private TextView register;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        setContentView(R.layout.login_layout);

        email = (EditText) findViewById(R.id.etLoginEmail);
        email.setText("");
        password = (EditText) findViewById(R.id.etLoginPassword);
        password.setText("");
        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(this);
        register = (TextView) findViewById(R.id.tvRegister);
        register.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
    }


    private void loginUser() {
        final String userEmail = email.getText().toString();
        final String userPass = password.getText().toString();

        progressDialog.setMessage("Logging in");
        progressDialog.show();
        progressDialog.setCancelable(false);
        try {
            mAuth.signInWithEmailAndPassword(userEmail, userPass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                progressDialog.hide();
                                Intent intent = new Intent(LoginActivity.this, ViewMyActivityActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                progressDialog.hide();
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }catch (IllegalArgumentException e){
            Toast.makeText(this, "Please input email and password", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToRegisterActivity(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                loginUser();
                break;
            case R.id.tvRegister:
                goToRegisterActivity();
                break;
        }
    }
}
