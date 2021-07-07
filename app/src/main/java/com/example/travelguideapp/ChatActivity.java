package com.example.travelguideapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.travelguideapp.model.ChatMessage;
import com.example.travelguideapp.model.TourSite;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
ListView listView;
EditText editText,nameEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        listView=findViewById(R.id.list_of_messages);
        editText=findViewById(R.id.input);
        nameEt=findViewById(R.id.nameEt);


        refreshPage();
           }

    public void sendMessage(View view) {
        FirebaseDatabase.getInstance()
                .getReference("chat")
                .push()
                .setValue(new ChatMessage(editText.getText().toString(),
                       nameEt.getText().toString())
                );
refreshPage();
        // Clear the input
        editText.setText("");
    }
    void refreshPage(){


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("chat");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ChatMessage> chatMessages=new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    chatMessages.add(ds.getValue(ChatMessage.class));


                }
                ChatAdapter adapter=new ChatAdapter(ChatActivity.this,chatMessages);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addListenerForSingleValueEvent(eventListener);

    }
}