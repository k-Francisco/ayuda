package com.charapp.charapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.charapp.ayuda.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button register;
    private Button login;
    private EditText email;
    private EditText password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){

        }


        login = (Button) findViewById(R.id.btnTestLogin);
        login.setOnClickListener(this);
        register = (Button) findViewById(R.id.btnTestRegister);
        register.setOnClickListener(this);
        email = (EditText) findViewById(R.id.etTestEmail);
        password = (EditText) findViewById(R.id.etTestPass);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnTestRegister:
                registerUser();
                break;
            case R.id.btnTestLogin:
                loginUser();
        }
    }

    private void loginUser() {
        String userEmail = email.getText().toString();
        String userPass = password.getText().toString();

        mAuth.signInWithEmailAndPassword(userEmail, userPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(TestActivity.this, ViewMyActivityActivity.class);
                            startActivity(intent);
                            finish();

                        }else{
                            Toast.makeText(TestActivity.this, "Incorrent email or pass", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void registerUser() {
        String userEmail = email.getText().toString();
        String userPass = password.getText().toString();

        if (userEmail.equals("")) {
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
        }else if (userPass.equals("")) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setMessage("Registering user");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(userEmail, userPass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(TestActivity.this, "Registered succesfully", Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                            } else {
                                Toast.makeText(TestActivity.this, "Could not register user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
