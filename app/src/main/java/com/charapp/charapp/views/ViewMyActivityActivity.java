package com.charapp.charapp.views;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.charapp.ayuda.R;
import com.charapp.charapp.service.NotificationService;
import com.charapp.charapp.Utilities.UtilitiesApplication;
import com.charapp.charapp.adapter.EventAdapter;
import com.charapp.charapp.fragments.DatePickerFragment;
import com.charapp.charapp.fragments.TimePickerFragment;
import com.charapp.charapp.models.Event;
import com.charapp.charapp.models.Foundation;
import com.charapp.charapp.models.Volunteer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ViewMyActivityActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static String TAG = ViewMyActivityActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private static ArrayList<Event> arrayListItem = new ArrayList<>();
    private static ArrayList<Foundation> alFoundation = new ArrayList<>();
    private static ArrayList<Volunteer> alVolunteer = new ArrayList<>();
    private static ArrayList<String> keysArray = new ArrayList<>();
    private static ArrayList<String> foundations = new ArrayList<>();
    private List<Event> result;
    private LinearLayoutManager llm;
    private EventAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private DatabaseReference mFoundationRef;
    private DatabaseReference mVolunteerRef;
    private static ViewMyActivityActivity viewMyActivity;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    private ChildEventListener cel;
    private ChildEventListener celVolunteer;
    private ArrayList<Event> al = new ArrayList<>();
    private FirebaseDatabase fdb;
    private FirebaseUser user;
    private String userEmail;
    private String userName;
    private String userIdentity = "", fName = "";
    private NavigationView navigationView;
    private TextView tvDisplayName;
    private TextView tvDisplayEmail;
    private Toolbar toolbar;
    private NotificationCompat.Builder notification;
    private static final int notifyId = 1;
    private int position;

    public ViewMyActivityActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        userIdentity = bundle.getString("IDENTITY");
        fName = bundle.getString("NAME");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userEmail = user.getEmail();
        mRef = FirebaseDatabase.getInstance().getReference("activities/");
        mFoundationRef = FirebaseDatabase.getInstance().getReference("foundation");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewMyActivityActivity.this, CreateActivity.class);
                intent.putExtra("email", userEmail);
                intent.putExtra("foundationName", fName);
                startActivity(intent);
            }
        });

        fab.hide();

        if (userIdentity.equals("foundation")) {
            fab.show();
            getSupportActionBar().setTitle(getString(R.string.title_activity_view_my_activity));
            setView();


        } else if (userIdentity.equals("volunteer")) {
            getSupportActionBar().setTitle(getString(R.string.title_activity_view_activities));
            setView();
        }


        Intent notifyIntent = new Intent(this, NotificationService.class);
        notifyIntent.putExtra("identity", userIdentity);
        notifyIntent.putExtra("email", userEmail);
        notifyIntent.putExtra("name", fName);
        startService(notifyIntent);

    }


    private void setView() {

        final UtilitiesApplication utilitiesApplication = new UtilitiesApplication();
        viewMyActivity = this;

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        recyclerView = (RecyclerView) findViewById(R.id.events_list);
        recyclerView.setHasFixedSize(true);
        adapter = new EventAdapter(ViewMyActivityActivity.this, arrayListItem, userIdentity);
        recyclerView.setAdapter(adapter);

        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(llm);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        View header = navigationView.getHeaderView(0);
        tvDisplayName = header.findViewById(R.id.tvName);
        tvDisplayName.setText(fName);
        tvDisplayEmail = header.findViewById(R.id.tvEmail);
        tvDisplayEmail.setText(user.getEmail());

        if (userIdentity.equals("foundation")) {
            mRef = mRef.child(fName);
            mRef.addChildEventListener(childEventListener);

        } else {

//            foundations = retrieve();
//            for (int i = 0; i < foundations.size(); i++) {
//                mRef.child(foundations.get(i)).addChildEventListener(childEventListener);
//            }

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


        new ViewMyActivityActivity.Wait().execute();
    }

//    private ArrayList<String> retrieve() {
//        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                fetchFoundations(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        return foundations;
//    }
//
//    private void fetchFoundations(DataSnapshot dataSnapshot) {
//        foundations.clear();
//        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//            String name = ds.getValue(Foundation.class).getFoundationName();
//            foundations.add(ds.getKey());
//        }
//    }

    private void getAllEvents(DataSnapshot dataSnapshot) {
        Event event = dataSnapshot.getValue(Event.class);
        arrayListItem.add(event);
        adapter = new EventAdapter(ViewMyActivityActivity.this, arrayListItem, userIdentity);
        recyclerView.setAdapter(adapter);
    }

    public void deleteEvent(int position) {
        String clickedKey = keysArray.get(position);
        mRef.child(clickedKey).removeValue();

    }

    public void updateEvent(Event event, int position) {
        String clickedKey = keysArray.get(position);
        mRef.child((clickedKey)).setValue(event);
    }

    public void viewEvent(Bundle bundle, int position) {
        Intent intent = new Intent(this, EventDetailsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.view_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {

//        } else
            if (id == R.id.nav_profile) {
            startActivity(new Intent(ViewMyActivityActivity.this, VolunteerProfileActivity.class));
        } else if (id == R.id.nav_logout) {
            ((UtilitiesApplication) getApplication()).getEditor().clear();
            ((UtilitiesApplication) getApplication()).getEditor().commit();

            Intent intent = new Intent(ViewMyActivityActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static ViewMyActivityActivity getInstance() {
        return viewMyActivity;
    }


    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment(v);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(v);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    private class Wait extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            showProgressDialog();
            recyclerView.setVisibility(View.GONE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ie) {
                Log.d(TAG, ie.toString());
            }
            return (arrayListItem.size() == 0);
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            if (bool)
                updateView();
        }
    }

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            recyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            Event event;
            event = dataSnapshot.getValue(Event.class);
            arrayListItem.add(event);
            keysArray.add(dataSnapshot.getKey());
            updateView();

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            String changedKey = dataSnapshot.getKey();
            int changedIndex = keysArray.indexOf(changedKey);
            Event event = dataSnapshot.getValue(Event.class);
            arrayListItem.set(changedIndex, event);
            updateView();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String deletedKey = dataSnapshot.getKey();
            int removedIndex = keysArray.indexOf(deletedKey);
            keysArray.remove(removedIndex);
            arrayListItem.remove(removedIndex);
            updateView();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "Could not update.", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        arrayListItem.clear();
        mRef.removeEventListener(childEventListener);
    }

    public void notifyUser() {
        notification = ((UtilitiesApplication) getApplication()).getNotification();
        notification.setSmallIcon(R.drawable.logo_gradient);
        notification.setTicker("");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Event");
        notification.setContentText("A new event has been added");

        Intent intent = new Intent("notify_user");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notifyId, notification.build());
    }

    public void updateView() {
        adapter.notifyDataSetChanged();
        recyclerView.invalidate();
        progressBar.setVisibility(View.GONE);
        hideProgressDialog();
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setIndeterminate(true);
        }

        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
