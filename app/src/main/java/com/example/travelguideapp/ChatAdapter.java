package com.example.travelguideapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.travelguideapp.model.ChatMessage;
import com.example.travelguideapp.model.Guide;
import com.example.travelguideapp.model.TourSite;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ChatAdapter extends BaseAdapter {
    Context context;
    List<ChatMessage> chatMessages;

    public ChatAdapter(Context context, List<ChatMessage> tourSites) {
        this.context = context;
        this.chatMessages = tourSites;
    }

    @Override
    public int getCount() {
        return chatMessages.size();
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
    public View getView(int position, View v, ViewGroup parent) {
        v = LayoutInflater.from(context).inflate(R.layout.chat_layout, parent, false);
        TextView messageText = (TextView)v.findViewById(R.id.message_text);
        TextView messageUser = (TextView)v.findViewById(R.id.message_user);
        TextView messageTime = (TextView)v.findViewById(R.id.message_time);

        ChatMessage message=chatMessages.get(position);
        // Set their text
        messageText.setText(message.getMessageText());
        messageUser.setText(message.getMessageUser());

        // Format the date before showing it
        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                message.getMessageTime()));

        return v;
    }
}
