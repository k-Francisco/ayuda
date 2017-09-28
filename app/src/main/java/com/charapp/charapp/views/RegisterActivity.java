package com.charapp.charapp.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.charapp.ayuda.R;
import com.charapp.charapp.models.Foundation;
import com.charapp.charapp.models.Volunteer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button register;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private EditText email;
    private EditText password;
    private EditText fullName;
    private ProgressDialog progressDialog;
    private String userIdentity = "Volunteer";
    private DatabaseReference databaseVolunteer;
    private DatabaseReference databaseFoundation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        radioGroup = (RadioGroup) findViewById(R.id.rg);
        register = (Button) findViewById(R.id.btnRegister);
        register.setOnClickListener(this);
        email = (EditText) findViewById(R.id.etRegisterEmail);
        password = (EditText) findViewById(R.id.etRegisterPassword);
        fullName = (EditText) findViewById(R.id.etFullName);
        databaseVolunteer = FirebaseDatabase.getInstance().getReference("volunteer");
        databaseFoundation = FirebaseDatabase.getInstance().getReference("foundation");

    }

    private void registerUser() {
        final String userEmail = email.getText().toString();
        final String userPass = password.getText().toString();
        final String userFullName = fullName.getText().toString();
        final String volunteerId = databaseVolunteer.push().getKey();
        final String foundationId = databaseFoundation.push().getKey();

        if(userFullName.equals("")){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }else if (userEmail.equals("")) {
            Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show();
        }else if (userPass.equals("")) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
        }else if(!isEmailValid(userEmail)){
            Toast.makeText(this, "That is not a valid email address", Toast.LENGTH_SHORT).show();
        }else if (userFullName.equals("")) {
            Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show();
        }else {
            progressDialog.setMessage("Registering user");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(userEmail, userPass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                    Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                    progressDialog.hide();

                                    if (userIdentity.equals("Volunteer")) {
                                        Volunteer volunteer = new Volunteer();
                                        volunteer.setVolunteerEmail(userEmail);
                                        volunteer.setVolunteerName(userFullName);
                                        databaseVolunteer.child(volunteerId).setValue(volunteer);
                                    } else if (userIdentity.equals("Foundation")) {
                                        Foundation foundation = new Foundation();
                                        foundation.setRepresentativeEmail(userEmail);
                                        foundation.setRepresentativeName(userFullName);
                                        databaseFoundation.child(foundationId).setValue(foundation);
                                    }

                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();

                            } else {
                                Toast.makeText(RegisterActivity.this, "Could not register user. " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.hide();
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegister:
                registerUser();
        }
    }

    public void userIsVolunteerOrFoundation(View view) {
        int radioButtonId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(radioButtonId);
        userIdentity = radioButton.getText().toString();
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
