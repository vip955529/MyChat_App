package com.techvipin130524.mychatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techvipin130524.mychatapp.adapter.MessageAdapter;
import com.techvipin130524.mychatapp.model.ModelClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyChatActivity extends AppCompatActivity {
    ImageView imageViewBack;
    TextView textViewChat;
    EditText editTextMessage;
    FloatingActionButton fab;
    RecyclerView rvChat;

    String userName, otherName;

    FirebaseDatabase database;
    DatabaseReference reference;

    MessageAdapter adapter;
    List<ModelClass> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageViewBack = findViewById(R.id.imageViewBack);
        textViewChat = findViewById(R.id.textViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        fab = findViewById(R.id.fab);
        rvChat = findViewById(R.id.rvChat);

        rvChat.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        userName = getIntent().getStringExtra("userName");
        otherName = getIntent().getStringExtra("otherName");

        textViewChat.setText(otherName);

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyChatActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString();
                if (!message.equals(""))
                {
                    sendMessage(message);
                    editTextMessage.setText("");
                }
            }
        });

        getMessage();

    }

    public void getMessage() {

        reference.child("Messages").child(userName).child(otherName)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        ModelClass modelClass = snapshot.getValue(ModelClass.class);
                        list.add(modelClass);
                        adapter.notifyDataSetChanged();
                        rvChat.scrollToPosition(list.size() -1);

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

        adapter = new MessageAdapter(list, userName);
        rvChat.setAdapter(adapter);

    }

    public void sendMessage(String message) {

        String key = reference.child("Messages").child(userName)
                .child(otherName).push().getKey();
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("message",message);
        messageMap.put("from",userName);
        reference.child("Messages").child(userName)
                .child(otherName).child(key).setValue(messageMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                {
                    reference.child("Messages").child(otherName)
                            .child(userName).child(key).setValue(messageMap);

                }

            }
        });

    }
}