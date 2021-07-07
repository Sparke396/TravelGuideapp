package com.example.travelguideapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.travelguideapp.model.Guide;

public class GuideHomeActivity extends AppCompatActivity {
Guide  guide;
TextView guideInfoTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_home);
        guide=getIntent().getParcelableExtra("guide");
        guideInfoTv=findViewById(R.id.guideInfoTv);
        guideInfoTv.setText(guide.getName());
    }

    public void showInfo(View v) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Guide Detail");

        LayoutInflater inflater=LayoutInflater.from(this);
        View view=inflater.inflate(R.layout.guide_details,null);
        TextView nameView=view.findViewById(R.id.guideName);
        TextView emailView=view.findViewById(R.id.guideEmail);
        TextView mobileView=view.findViewById(R.id.guideMobile);
        TextView aadharView=view.findViewById(R.id.aadhar);
        ImageView imageView=view.findViewById(R.id.guideImage);
        TextView regDate=view.findViewById(R.id.regDate);
        Button callGuide=view.findViewById(R.id.callGuide);
        callGuide.setVisibility(View.GONE);

        nameView.setText("Name : "+guide.getName());
        emailView.setText("Email : "+guide.getEmail());
        mobileView.setText("Mobile : "+guide.getMobile());
        aadharView.setText("Aadhar : "+guide.getIdentityProof());
        regDate.setText("Registered since : "+guide.getRegistrationDate());

        Glide.with(this).load(guide.getImageUrl()).into(imageView);
        builder.setView(view);

        builder.create();
        builder.show();
    }

    public void newPlaceIntent(View view) {
        Intent i=new Intent(this,NewTourActivity.class);
        i.putExtra("guideId",guide.getGuideId());
        startActivity(i);
    }
}