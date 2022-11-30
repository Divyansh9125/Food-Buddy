package com.example.foodbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginButton;
        loginButton = findViewById(R.id.loginButton2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProcess();
            }
        });
    }

    public void loginProcess(){
        TextView emailView, passwordView;
        emailView = findViewById(R.id.emilEditView);
        passwordView = findViewById(R.id.passwordEditView);

        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();


        RequestBody formBody = new FormBody.Builder()
                .add("work_email", email)
                .add("password", password)
                .build();

        String hostAddress = getString(R.string.web_server_address);
        final String url = hostAddress+"/foodapp/sharing/user/login/";

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Thread thread = new Thread(() -> {
            try  {
                try{
                    OkHttpClient client = new OkHttpClient();
                    Response response = client.newCall(request).execute();

                    // Do something with the response.
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Utility.setUserToken(jsonObject.getString("token"));

                    runOnUiThread(() -> {
                        Context context = getApplicationContext();
                        CharSequence text = "User logged in!";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    });
                } catch (IOException e) {
                    e.printStackTrace();

                    runOnUiThread(() -> {
                        Context context = getApplicationContext();
                        CharSequence text = "Sorry! User couldn't be logged in!";
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
                    CharSequence text = "Sorry! User couldn't be logged in!";
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                });
            }
        });
        thread.start();

        Intent openMainActivity = new Intent(LoginActivity.this, MainActivity.class);
        openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(openMainActivity, 0);

        finish();
    }
}