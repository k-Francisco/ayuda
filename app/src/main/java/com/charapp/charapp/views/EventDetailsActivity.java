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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class EventDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Bundle bundle;
    TextView headerInfo, tvName, tvDesc;
    String mName, mDate, mStart, mEnd, mAddress, mDesc, userIdentity, key;
    Button btnJoin;
    DatabaseReference eventRef;
    Query query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EventBus.getDefault().register(this); //Register the bus first to the activity

        userIdentity = ((UtilitiesApplication)getApplication()).getSharedpreferences().getString("identity", "");

        headerInfo = (TextView) findViewById(R.id.mTxtHeaderInfo);
        tvName = (TextView) findViewById(R.id.mTxtCardTitle1);
        tvDesc = (TextView)findViewById(R.id.mTxtDescription1);
        btnJoin = (Button) findViewById(R.id.joinEventBtn);
        btnJoin.setOnClickListener(this);

        bundle = getIntent().getExtras();
        mName = bundle.getString("NAME");
        mDate = bundle.getString("DATE");
        mStart = bundle.getString("START");
        mEnd = bundle.getString("END");
        mAddress = bundle.getString("ADDRESS");
        mDesc = bundle.getString("DESC");

        headerInfo.setText(mDate);
        tvName.setText(mName);
        tvDesc.setText(mDesc);

        eventRef = FirebaseDatabase.getInstance().getReference("activities");


        if(userIdentity.equals("volunteer")){
            btnJoin.setVisibility(View.VISIBLE);
        }


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

    @Subscribe      //EventBus method
    public void onEvent(String test){//Variable from listeners or async methods should be placed here
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        //TODO update List<Volunteers> in an event
        Toast.makeText(this, mName, Toast.LENGTH_SHORT).show();

    }
}
