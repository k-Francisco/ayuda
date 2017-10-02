package com.charapp.charapp.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.charapp.ayuda.R;

public class EventDetailsActivity extends AppCompatActivity {

    Bundle bundle;
    TextView headerInfo, tvName, tvDesc;
    String mName, mDate, mStart, mEnd, mAddress, mDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        headerInfo = (TextView) findViewById(R.id.mTxtHeaderInfo);
        tvName = (TextView) findViewById(R.id.mTxtCardTitle1);
        tvDesc = (TextView)findViewById(R.id.mTxtDescription1);

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
}
