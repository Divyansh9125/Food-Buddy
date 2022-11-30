package com.example.foodbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button newUserButton = findViewById(R.id.newUserButton);
        Button shareFoodButton = findViewById(R.id.sharefoodButton);
        Button loginButton = findViewById(R.id.loginButton1);
        Button veg5OrderButton, veg8OrderButton, nonVeg5OrderButton, nonVeg8OrderButton;
        veg5OrderButton = findViewById(R.id.veg5OrderButton);
        veg8OrderButton = findViewById(R.id.veg8OrderButton);
        nonVeg5OrderButton = findViewById(R.id.nonVeg5OrderButton);
        nonVeg8OrderButton = findViewById(R.id.nonVeg8OrderButton);

        final OkHttpClient client = new OkHttpClient();
        String hostAddress = getString(R.string.web_server_address);
        String url, response;

        url = hostAddress+"/foodapp/sharing/user/numFood/veg/5/";
        TextView veg5TextView = findViewById(R.id.textView2);
        try{
            run(url, client, veg5TextView);
        } catch (IOException e){
            e.printStackTrace();
        }

        url = hostAddress+"/foodapp/sharing/user/numFood/veg/8/";
        TextView veg8TextView = findViewById(R.id.textView4);
        try{
            run(url, client, veg8TextView);
        } catch (IOException e){
            e.printStackTrace();
        }

        url = hostAddress+"/foodapp/sharing/user/numFood/non-veg/5/";
        TextView nonVeg5TextView = findViewById(R.id.textView6);
        try{
            run(url, client, nonVeg5TextView);
        } catch (IOException e){
            e.printStackTrace();
        }


        url = hostAddress+"/foodapp/sharing/user/numFood/non-veg/8/";
        TextView nonVeg8TextView = findViewById(R.id.textView8);
        try{
            run(url, client, nonVeg8TextView);
        } catch (IOException e) {
            e.printStackTrace();
        }


        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newUserButtonOnClick();
            }
        });

        shareFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareFoodButtonOnClick();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButtonOnClick();
            }
        });

        veg5OrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openTakeFoodActivity = new Intent(MainActivity.this, TakeFoodActivity.class);
                openTakeFoodActivity.putExtra("veg", 1);
                openTakeFoodActivity.putExtra("8pcs", 0);
                startActivity(openTakeFoodActivity);
            }
        });

        veg8OrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openTakeFoodActivity = new Intent(MainActivity.this, TakeFoodActivity.class);
                openTakeFoodActivity.putExtra("veg", 1);
                openTakeFoodActivity.putExtra("8pcs", 1);
                startActivity(openTakeFoodActivity);
            }
        });

        nonVeg5OrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openTakeFoodActivity = new Intent(MainActivity.this, TakeFoodActivity.class);
                openTakeFoodActivity.putExtra("veg", 0);
                openTakeFoodActivity.putExtra("8pcs", 0);
                startActivity(openTakeFoodActivity);
            }
        });

        nonVeg8OrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openTakeFoodActivity = new Intent(MainActivity.this, TakeFoodActivity.class);
                openTakeFoodActivity.putExtra("veg", 0);
                openTakeFoodActivity.putExtra("8pcs", 1);
                startActivity(openTakeFoodActivity);
            }
        });
    }

    public final void loginButtonOnClick(){
        Intent loginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginActivityIntent);
    }

    public final void newUserButtonOnClick(){
        Intent userRegistrationActivityIntent = new Intent(MainActivity.this, RegisterUserActivity.class);
        startActivity(userRegistrationActivityIntent);
    }

    public final void shareFoodButtonOnClick(){
        Intent shareFoodDetailsActivityIntent = new Intent(MainActivity.this, TakeFoodDetailsActivity.class);
        startActivity(shareFoodDetailsActivityIntent);
    }

    private void run(String url, OkHttpClient client, TextView textView) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    int success = jsonObject.getInt("success");

                    if(success==1) {
                        int num = jsonObject.getInt("num");
                        String newText = "Available: "+num;
                        textView.setText(newText);
                    }
                    else {
                        // TODO: --------------
                    }
                } catch (JSONException e){
                    // TODO: handle exception
                    System.out.println("JSON Exception Occurred!!!!!!!!!!!!!!!");
                    e.printStackTrace();
                }
            }
        });
    }
}