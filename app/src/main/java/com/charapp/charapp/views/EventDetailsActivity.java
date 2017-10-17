package com.charapp.charapp.views;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.charapp.ayuda.R;
import com.charapp.charapp.Utilities.UtilitiesApplication;
import com.charapp.charapp.models.Event;
import com.charapp.charapp.models.Volunteer;
import com.charapp.charapp.service.NotificationService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Bundle bundle;
    TextView headerInfo, tvName, tvDesc;
    String mName, mDate, mStart, mEnd, mAddress, mDesc, userIdentity, key, eventKey, foundationName;
    Button btnJoin;
    DatabaseReference eventRef, mRef, volunteerRef;
    Query query;
    int position;
    int volunteerPosition;
    List<String> fKeys = new ArrayList<>();
    List<String> eventKeys = new ArrayList<>();
    List<String> volunteerKeys = new ArrayList<>();
    boolean checker = false;
    List<Event> events = new ArrayList<>();
    List<Volunteer> volunteers;
    List<Volunteer> volunteerRecords = new ArrayList<>();
    private Calendar calendar = Calendar.getInstance();
    private DateFormat dateFormat;
    private Uri uri;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private String[] permissionsRequired = new String[]{Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR};

    private boolean hasJoined = false;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String volunteerEmail;
    private boolean notifyFoundation = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        checkCalendarPermissions();
        userIdentity = ((UtilitiesApplication)getApplication()).getSharedpreferences().getString("identity", "");

        headerInfo = (TextView) findViewById(R.id.mTxtHeaderInfo);
        tvName = (TextView) findViewById(R.id.mTxtCardTitle1);
        tvDesc = (TextView)findViewById(R.id.mTxtDescription1);
        btnJoin = (Button) findViewById(R.id.joinEventBtn);
        btnJoin.setOnClickListener(this);

        bundle = getIntent().getExtras();
        foundationName = bundle.getString("FNAME");
        mName = bundle.getString("NAME");
        mDate = bundle.getString("DATE");
        mStart = bundle.getString("START");
        mEnd = bundle.getString("END");
        mAddress = bundle.getString("ADDRESS");
        mDesc = bundle.getString("DESC");
        position = bundle.getInt("position");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();


        headerInfo.setText(foundationName+"|"+mDate);
        tvName.setText(mName);
        tvDesc.setText(mDesc);



        if(userIdentity.equals("volunteer")){
            btnJoin.setVisibility(View.VISIBLE);
            volunteerEmail = user.getEmail();
        }


        eventRef = FirebaseDatabase.getInstance().getReference("activities");

        mRef = FirebaseDatabase.getInstance().getReference("activities");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    mRef.child(ds.getKey()).addChildEventListener(childEventListener);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        volunteerRef = FirebaseDatabase.getInstance().getReference("volunteer");
        volunteerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    Volunteer volunteer = ds.getValue(Volunteer.class);
                    volunteerRecords.add(volunteer);
                    volunteerKeys.add(ds.getKey());
                }

                for (int i = 0; i < volunteerRecords.size(); i++) {
                    if(volunteerRecords.get(i).getVolunteerEmail().equals(volunteerEmail)){
                        if(volunteerRecords.get(i).getEventsJoined() != null){

                            for (int j = 0; j < volunteerRecords.get(i).getEventsJoined().size(); j++) {
                                if(volunteerRecords.get(i).getEventsJoined().get(j).getActivityName().equals(mName)){
                                    hasJoined = true;
                                }
                            }

                        }
                    }
                }

                if(hasJoined){
                    btnJoin.setEnabled(false);
                    btnJoin.setText("You have already joined this event");
                }else{

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    private void putToCalendar(String eventName, String timeStart, String timeEnd, String date, String location, String desc) {
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

    private void checkCalendarPermissions() {
        if (ActivityCompat.checkSelfPermission(EventDetailsActivity.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(EventDetailsActivity.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(EventDetailsActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(EventDetailsActivity.this, permissionsRequired[1])) {

                //Show why we need multiple permissions
                AlertDialog.Builder builder = new AlertDialog.Builder(EventDetailsActivity.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Calendar permissions");
                builder.setCancelable(false);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        ActivityCompat.requestPermissions(EventDetailsActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(EventDetailsActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

            permissionStatus = getSharedPreferences("permissions", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        }
    }


    @Override
    public void onClick(View view) {

        putToCalendar(mName,mStart,mEnd,mDate,mAddress,mDesc);

        String volunteerEmail = ((UtilitiesApplication)getApplication()).getSharedpreferences().getString("email", "");
        String volunteerName = ((UtilitiesApplication)getApplication()).getSharedpreferences().getString("name", "");
        Volunteer volunteer = new Volunteer(volunteerName,volunteerEmail);

        if(events.get(position).getVolunteers() != null) {
            volunteers = events.get(position).getVolunteers();
            volunteers.add(volunteer);
        }else{
            volunteers = new ArrayList<>();
            volunteers.add(volunteer);
        }

        Event event = new Event(events.get(position).getActivityName(),
                events.get(position).getDate(),events.get(position).getTimeStart(),
                events.get(position).getTimeEnd(),events.get(position).getAddress(),
                events.get(position).getDescription(), volunteers, events.get(position).getFoundationName());

        Event eventJoined = new Event(events.get(position).getActivityName(),
                events.get(position).getDate(),events.get(position).getTimeStart(),
                events.get(position).getTimeEnd(),events.get(position).getAddress(),
                events.get(position).getDescription(),events.get(position).getFoundationName());

        //TODO update volunteer's joined activities
        for (int i = 0; i < volunteerRecords.size(); i++) {
            if(volunteerRecords.get(i).getVolunteerEmail().equals(volunteerEmail)){
                volunteerPosition = i;
                if(volunteerRecords.get(i).getEventsJoined() != null){
                    volunteerRecords.get(i).getEventsJoined().add(eventJoined);
                }else{
                    volunteerRecords.get(i).setEventsJoined(new ArrayList<Event>());
                    volunteerRecords.get(i).getEventsJoined().add(eventJoined);
                }
            }
        }

        volunteerRef = volunteerRef.child(volunteerKeys.get(volunteerPosition));
        volunteerRef.setValue(volunteerRecords.get(volunteerPosition));

        eventRef = eventRef.child(foundationName).child(eventKeys.get(position));
        eventRef.setValue(event);

        Toast.makeText(this, "Successfully joined an event!", Toast.LENGTH_SHORT).show();


        Intent notifyIntent = new Intent(this, NotificationService.class);
        notifyFoundation = true;
        notifyIntent.putExtra("email", user.getEmail());
        notifyIntent.putExtra("eventName", mName);
        notifyIntent.putExtra("fromVolunteer", notifyFoundation);
        startService(notifyIntent);


        finish();

    }

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Event event = dataSnapshot.getValue(Event.class);
            events.add(event);
            eventKeys.add(dataSnapshot.getKey());
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
