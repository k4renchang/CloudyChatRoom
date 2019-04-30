package com.example.cloudychatroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText emailText, passwordText;
    private ProgressBar myBar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, ChatActivity.class));
        } else {
            setContentView(R.layout.activity_main);
            initialize();
        }

    }

    public void initialize() {
        emailText = findViewById(R.id.email_login);
        passwordText = findViewById(R.id.password_login);
        myBar = findViewById(R.id.progressBar_login);
    }

    public void UserLogin (View view) {
        myBar.setVisibility(View.VISIBLE);

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (!email.equals("") && !password.equals("")) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                myBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "You are logged in!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, ChatActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), "The email or password you entered is wrong.", Toast.LENGTH_SHORT).show();
                                myBar.setVisibility(View.GONE);
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "One or more of the required fields are empty.", Toast.LENGTH_SHORT).show();
            myBar.setVisibility(View.GONE);
        }

    }

    public void OpenRegister (View view) {
        startActivity(new Intent(MainActivity.this, RegisterUserActivity.class));
    }
}
