package com.charapp.charapp.views;

import android.os.Bundle;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class EventDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Bundle bundle;
    TextView headerInfo, tvName, tvDesc;
    String mName, mDate, mStart, mEnd, mAddress, mDesc, userIdentity, key, eventKey, foundationName;
    Button btnJoin;
    DatabaseReference eventRef, mRef, volunteerRef;
    Query query;
    int position;
    List<String> fKeys = new ArrayList<>();
    List<String> eventKeys = new ArrayList<>();
    boolean checker = false;
    List<Event> events = new ArrayList<>();
    List<Volunteer> volunteers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        headerInfo.setText(foundationName+"|"+mDate);
        tvName.setText(mName);
        tvDesc.setText(mDesc);



        if(userIdentity.equals("volunteer")){
            btnJoin.setVisibility(View.VISIBLE);
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
    }


    @Override
    public void onClick(View view) {
        //TODO update List<Volunteers> in an event

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

        eventRef = eventRef.child(foundationName).child(eventKeys.get(position));
        eventRef.setValue(event);

        Toast.makeText(this, "Successfully joined an event!", Toast.LENGTH_SHORT).show();
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
