package com.charapp.charapp.views;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.charapp.ayuda.R;
import com.charapp.charapp.Utilities.UtilitiesApplication;
import com.charapp.charapp.fragments.DatePickerFragment;
import com.charapp.charapp.fragments.TimePickerFragment;
import com.charapp.charapp.models.Event;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@RequiresApi(api = Build.VERSION_CODES.N)
public class CreateActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mName, mDate, mTimeStart, mTimeEnd, mAddress, mDesc;
    Button mCreate;
    DatabaseReference dbRef;
    Calendar calendar = Calendar.getInstance();
    DateFormat dateFormat;
    Uri uri;

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

        UtilitiesApplication utilitiesApplication = new UtilitiesApplication();
        utilitiesApplication.addEvent(event,dbRef,id);

        putToCalendar(name, timeStart, timeEnd, date, address, desc);

        Toast.makeText(this, "Event successfully added", Toast.LENGTH_SHORT).show();

        finish();

    }

    private void putToCalendar(String eventName, String timeStart, String timeEnd, String date, String location, String desc){
        int hourStart = 0;
        int hourEnd = 0;
        int minuteStart = 0;
        int minuteEnd = 0;
        int monthNumber;
        int monthDay;
        int year;
        String[] splitDate;
        String[] splitTime;
        String eventStart;
        String eventEnd;
        long startTime;
        long endTime;
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date parseStart = new Date();
        Date parseEnd = new Date();
        ContentResolver contentResolver = this.getContentResolver();
        ContentValues contentValues = new ContentValues();

        splitTime = timeStart.split(":");
        hourStart = Integer.parseInt(splitTime[0]);
        minuteStart = Integer.parseInt(splitTime[1]);

        splitDate = date.split("/");
        monthNumber = Integer.parseInt(splitDate[0]);
        monthDay = Integer.parseInt(splitDate[1]);
        year = Integer.parseInt(splitDate[2]);

        eventStart = (year + "/" + monthNumber + "/" + monthDay + " " + hourStart + ":" + minuteStart + ":" + "00");
        eventEnd = (year + "/" + monthNumber + "/" + monthDay + " " + hourEnd + ":" + minuteStart + ":" + "00");

        try {
            parseStart = dateFormat.parse(eventStart);
            parseEnd = dateFormat.parse(eventEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        startTime = parseStart.getTime();
        endTime = parseEnd.getTime();

        contentValues.put(CalendarContract.Events.CALENDAR_ID, 1);
        contentValues.put(CalendarContract.Events.DTSTART, startTime);
        contentValues.put(CalendarContract.Events.DTEND, endTime);
        contentValues.put(CalendarContract.Events.TITLE, eventName);
        contentValues.put(CalendarContract.Events.DESCRIPTION, desc);
        contentValues.put(CalendarContract.Events.EVENT_LOCATION, location);
        contentValues.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, contentValues);

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
