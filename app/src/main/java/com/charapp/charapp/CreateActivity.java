package com.charapp.charapp;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.charapp.ayuda.R;
import com.charapp.charapp.fragments.DatePickerFragment;
import com.charapp.charapp.fragments.TimePickerFragment;
import com.charapp.charapp.models.Event;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


@RequiresApi(api = Build.VERSION_CODES.N)
public class CreateActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mName, mDate, mTimeStart, mTimeEnd, mAddress, mDesc;
    Button mCreate;
    DatabaseReference dbRef;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        getSupportActionBar().setTitle(getString(R.string.title_create_activity));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbRef = FirebaseDatabase.getInstance().getReference("activities");

        findViews();
        mCreate.setOnClickListener(this);

    }

    private void createActivity() {
        if (!validateForm()) {
            return;
        }

        String id = dbRef.push().getKey();

        String name = mName.getText().toString();
        String date = mDate.getText().toString();
        String timeStart = mTimeStart.getText().toString();
        String timeEnd = mTimeEnd.getText().toString();
        String address = mAddress.getText().toString();
        String desc = mDesc.getText().toString();

        Event event = new Event(name, date, timeStart, timeEnd, address, desc);
        dbRef.child(id).setValue(event);

        Toast.makeText(this, "Event successfully added", Toast.LENGTH_SHORT).show();

        finish();

    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment(v);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(v);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private void findViews() {
        mName = (EditText) findViewById(R.id.edtActivityName);
        mDate = (EditText) findViewById(R.id.edtDate);
        mTimeStart = (EditText) findViewById(R.id.edtTimeStart);
        mTimeEnd = (EditText) findViewById(R.id.edtTimeEnd);
        mAddress = (EditText) findViewById(R.id.edtAddress);
        mDesc = (EditText) findViewById(R.id.edtDescription);
        mCreate = (Button) findViewById(R.id.createBtn);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createBtn:
                createActivity();
                break;

        }
    }

    private boolean validateForm() {
        boolean valid = true;
        String name = mName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mName.setError("Required.");
            valid = false;
        } else {
            mName.setError(null);
        }

        String date = mDate.getText().toString();
        if (TextUtils.isEmpty(date)) {
            mDate.setError("Required.");
            valid = false;
        } else {
            mDate.setError(null);
        }

        String time = mTimeStart.getText().toString();
        if (TextUtils.isEmpty(time)) {
            mTimeStart.setError("Required.");
            valid = false;
        } else {
            mTimeStart.setError(null);
        }

        String time2 = mTimeEnd.getText().toString();
        if (TextUtils.isEmpty(time2)) {
            mTimeEnd.setError("Required.");
            valid = false;
        } else {
            mTimeEnd.setError(null);
        }

        String address = mAddress.getText().toString();
        if (TextUtils.isEmpty(address)) {
            mAddress.setError("Required.");
            valid = false;
        } else {
            mAddress.setError(null);
        }

        String desc = mDesc.getText().toString();
        if (TextUtils.isEmpty(desc)) {
            mDesc.setError("Required.");
            valid = false;
        } else {
            mDesc.setError(null);
        }
        return valid;
    }
}
