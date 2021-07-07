package com.example.travelguideapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.travelguideapp.model.TourSite;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
ListView tourSiteLv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tourSiteLv=findViewById(R.id.tourSiteLv);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("tour_sites");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<TourSite> sites=new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                  sites.add(ds.getValue(TourSite.class));


                }
                SiteAdapter adapter=new SiteAdapter(MainActivity.this,sites);
                tourSiteLv.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addListenerForSingleValueEvent(eventListener);
    }

    public void goToLoginPage(View view) {
        Intent i=new Intent(this,GuideLoginActivity.class);
        startActivity(i);
    }

    public void chatPageIntent(View view) {
        startActivity(new Intent(this,ChatActivity.class));
    }
}

