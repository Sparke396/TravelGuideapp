package com.example.travelguideapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.travelguideapp.model.Guide;
import com.example.travelguideapp.model.TourSite;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

public class SiteAdapter extends BaseAdapter {
    Context context;
    List<TourSite> tourSites;

    public SiteAdapter(Context context, List<TourSite> tourSites) {
        this.context = context;
        this.tourSites = tourSites;
    }

    @Override
    public int getCount() {
        return tourSites.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.site_layout, parent, false);
        TextView sitenameView = convertView.findViewById(R.id.siteNameView);
        TextView siteCityView = convertView.findViewById(R.id.siteCityView);
        TextView aboutSite = convertView.findViewById(R.id.aboutSiteView);
        TextView infoButton = convertView.findViewById(R.id.info);
        TextView directionButton = convertView.findViewById(R.id.direction);
        ImageView siteImage = convertView.findViewById(R.id.siteImageView);

        TourSite site = tourSites.get(position);
        sitenameView.setText(site.getSiteName());
        siteCityView.setText(", " + site.getSiteLocation());
        aboutSite.setText(site.getDescription() + "\nBest visit during : " + site.getRecommendedSeason());
        Glide.with(context).load(site.getPictureUrl()).into(siteImage);

        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", site.getLatitude(),site.getLongitude());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                context.startActivity(intent);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("guides").orderByChild("guideId").equalTo(site.getCreatedBy()).
                        addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Guide guide = snapshot.getValue(Guide.class);
                        showInfo(guide);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

            }
        });
        return convertView;
    }
    public void showInfo(Guide guide) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("Guide Detail");

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.guide_details,null);
        TextView nameView=view.findViewById(R.id.guideName);
        TextView emailView=view.findViewById(R.id.guideEmail);
        TextView mobileView=view.findViewById(R.id.guideMobile);
        TextView aadharView=view.findViewById(R.id.aadhar);
        ImageView imageView=view.findViewById(R.id.guideImage);
        TextView regDate=view.findViewById(R.id.regDate);

        nameView.setText("Name : "+guide.getName());
        emailView.setText("Email : "+guide.getEmail());
        mobileView.setText("Mobile : "+guide.getMobile());
        aadharView.setText("Aadhar : "+guide.getIdentityProof());
        regDate.setText("Registered since : "+guide.getRegistrationDate());
        Button callGuide=view.findViewById(R.id.callGuide);

        Glide.with(context).load(guide.getImageUrl()).into(imageView);
        builder.setView(view);
        callGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+guide.getMobile()));
                context.startActivity(intent);
            }
        });

        builder.create();
        builder.show();
    }
}
