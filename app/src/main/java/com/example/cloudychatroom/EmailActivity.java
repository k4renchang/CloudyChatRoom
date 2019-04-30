package com.example.cloudychatroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;


public class EmailActivity extends AppCompatActivity {
    private TextInputEditText eTo, eSubject, eMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        eTo = findViewById(R.id.edit_text_to);
        eSubject = findViewById(R.id.edit_text_subject);
        eMessage = findViewById(R.id.edit_text_message);

        Button buttonSendMail = findViewById(R.id.button_send);
        buttonSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
    }
    private void sendMail() {
        String[] receivers = eTo.getText().toString().split(",");
        String subject = eSubject.getText().toString();
        String message = eMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, receivers);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email recipient!"));
    }

    public void returnToChat (View view) {
        startActivity(new Intent (EmailActivity.this, ChatActivity.class));
    }
}
