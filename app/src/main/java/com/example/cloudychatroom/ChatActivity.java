package com.example.cloudychatroom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.cloudychatroom.Components.Message;
import com.example.cloudychatroom.Components.User;
import com.example.cloudychatroom.Adapters.Adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth auth;
    private DatabaseReference reference;
    private FirebaseDatabase firebaseDatabase;
    private Adapter adapter;
    private User user;
    private List<Message> messages;

    private RecyclerView recyclerView;
    private EditText editText;
    private ImageButton imageButton;
    private ImageButton emailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initialize();
    }

    public void initialize() {
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = new User();
        recyclerView = findViewById(R.id.chatRecyclerView);
        editText = findViewById(R.id.message);
        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(this);
        emailButton = findViewById(R.id.emailButton);
        messages = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            Toast.makeText(getApplicationContext(), "The message body is empty.", Toast.LENGTH_SHORT).show();
        } else {
            Message message = new Message(editText.getText().toString(), user.getName());
            editText.setText("");
            reference.push().setValue(message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            auth.signOut();
            finish();
            Intent intent = new Intent(ChatActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser current = auth.getCurrentUser();
        user.setUserID(current.getUid());
        user.setEmail(current.getEmail());

        firebaseDatabase.getReference("Users").child(current.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                user.setUserID(current.getUid());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference = firebaseDatabase.getReference("messages");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                messages.add(message);
                recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                recyclerView.setAdapter(new Adapter(ChatActivity.this, messages, reference));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                List<Message> newMessages = new ArrayList<>();
                for (Message myM : messages) {
                    if (!myM.getKey().equals(message.getKey())) {
                        newMessages.add(myM);
                    } else {
                        newMessages.add(message);
                    }
                }
                messages = newMessages;
                recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                adapter = new Adapter(ChatActivity.this, messages, reference);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                List<Message> newMessages = new ArrayList<>();
                for (Message myM : messages) {
                    if (!myM.getKey().equals(message.getKey())) {
                        newMessages.add(myM);
                    }
                }
                messages = newMessages;
                recyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
                adapter = new Adapter(ChatActivity.this, messages, reference);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void OpenEmail (View view) {
        startActivity(new Intent(ChatActivity.this, EmailActivity.class));
    }

}
