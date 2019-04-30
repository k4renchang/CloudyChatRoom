package com.example.cloudychatroom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cloudychatroom.Components.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUserActivity extends AppCompatActivity {

    private TextInputEditText emailText, passwordText, nameText;
    private ProgressBar myBar;

    private FirebaseAuth auth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        initialize();
    }

    public void initialize() {
        emailText = findViewById(R.id.email_register);
        passwordText = findViewById(R.id.password_register);
        nameText = findViewById(R.id.name_register);
        myBar = findViewById(R.id.progressBar_register);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void returnToLogin (View view) {
        startActivity(new Intent (RegisterUserActivity.this, MainActivity.class));
    }

    public void RegisterUser (View view) {
        myBar.setVisibility(View.VISIBLE);
        final String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        final String name = nameText.getText().toString();
        boolean pass = false;

        // making sure password contains at least one upper case letter.
        if (password.length() >= 8) {
            for (int i = 0; i < password.length(); i++) {
                if (Character.isUpperCase(password.charAt(i))) {
                    pass = true;
                }
            }

        }

        if (email.equals("") && password.equals("") && name.equals("")) {
            myBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "One or more of the required fields are empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pass) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser fireUser = auth.getCurrentUser();
                                User user = new User();
                                user.setEmail(email);
                                user.setName(name);
                                reference.child(fireUser.getUid()).setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "User registered!", Toast.LENGTH_LONG).show();
                                                    myBar.setVisibility(View.GONE);
                                                    finish();
                                                    startActivity(new Intent(RegisterUserActivity.this, ChatActivity.class));
                                                }
                                            }
                                        });
                            } else {
                                myBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Unable to register user, user may already exist.", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        } else {
            myBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Password must at least contains eight characters and one upper case letter.", Toast.LENGTH_SHORT).show();
        }
    }
}
