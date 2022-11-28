package com.example.foodbuddy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        final TextView fnameView, lnameView, emailView, contactView;
        fnameView = findViewById(R.id.fnameEditBox);
        lnameView = findViewById(R.id.lnameEditBox);
        emailView = findViewById(R.id.emailEditBox);
        contactView = findViewById(R.id.phoneEditBox);

        Button registerButton = findViewById(R.id.regsiterButton);

        registerButton.setOnClickListener(view -> {
            String fname = fnameView.getText().toString();
            String lname = lnameView.getText().toString();
            String email = emailView.getText().toString();
            String contact = contactView.getText().toString();

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("work_email", email)
                    .add("fname", fname)
                    .add("lname", lname)
                    .add("contact", contact)
                    .build();

            String hostAddress = getString(R.string.web_server_address);
            final String url = hostAddress+"/foodapp/sharing/user/signup/";

            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();

            Thread thread = new Thread(() -> {
                try  {
                    try{
                        Response response = client.newCall(request).execute();
                        assert response.body() != null;

                        // Do something with the response.
                        System.out.println("Response Body: "+response.body());

                        runOnUiThread(() -> {
                            Context context = getApplicationContext();
                            CharSequence text = "User Registered!";
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        });
                    } catch (IOException e) {
                        e.printStackTrace();

                        runOnUiThread(() -> {
                            Context context = getApplicationContext();
                            CharSequence text = "Sorry! User can't be registered!";
                            int duration = Toast.LENGTH_LONG;
                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (AssertionError e){
                    runOnUiThread(() -> {
                        Context context = getApplicationContext();
                        CharSequence text = "Sorry! User can't be registered!";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    });
                }
            });
            thread.start();

            Intent openMainActivity = new Intent(RegisterUserActivity.this, MainActivity.class);
            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivityIfNeeded(openMainActivity, 0);

            finish();
        });
    }
}