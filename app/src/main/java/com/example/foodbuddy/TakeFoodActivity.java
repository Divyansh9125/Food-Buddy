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

public class TakeFoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_food);

        Intent currentIntent = getIntent();
        Bundle extras = currentIntent.getExtras();
        int veg = 0, pcs_8 = 0;
        if(extras != null){
            veg = extras.getInt("veg");
            pcs_8 = extras.getInt("8pcs");
        }

        final int peice = (pcs_8==1)?8:5;
        final String option = (veg==1)?"veg":"non-veg";

        TextView orderidView = findViewById(R.id.orderIdplaceOrder);
        Button placeOrderButton = findViewById(R.id.button_place_order);

        orderidView.setVisibility(View.INVISIBLE);

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OkHttpClient client = new OkHttpClient();
                String userToken="";
                try {
                    userToken = Utility.getUserToken();

                    String hostAddress = getString(R.string.web_server_address);
                    final String url = hostAddress+"/foodapp/sharing/user/placeorder/"+option+"/"+peice+"/";

                    RequestBody formBody = new FormBody.Builder()
                            .add("token", userToken)
                            .build();

                    Request request = new Request.Builder()
                            .url(url)
                            .post(formBody)
                            .build();

                    System.out.println(url);
                    System.out.println(userToken);

                    Thread thread = new Thread(() -> {
                        try  {
                            try{
                                Response response = client.newCall(request).execute();

                                // Do something with the response.
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                String orderId = jsonObject.getString("order_id");
                                String orderIdText = "Order Id: "+orderId;

                                runOnUiThread(() -> {
                                    orderidView.setText(orderIdText);
                                    orderidView.setVisibility(View.VISIBLE);
                                });

                                runOnUiThread(() -> {
                                    Context context = getApplicationContext();
                                    CharSequence text = "Order booked!";
                                    int duration = Toast.LENGTH_LONG;
                                    Toast toast = Toast.makeText(context, text, duration);
                                    toast.show();
                                });
                            } catch (IOException e) {
                                e.printStackTrace();

                                runOnUiThread(() -> {
                                    Context context = getApplicationContext();
                                    CharSequence text = "Order can't be booked!";
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
                }catch (NullPointerException e){
                    runOnUiThread(() -> {
                        Context context = getApplicationContext();
                        CharSequence text = "Please login or create account first!";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    });

                    Intent openMainActivity = new Intent(TakeFoodActivity.this, MainActivity.class);
                    startActivity(openMainActivity);
                    finish();
                }
            }
        });
    }
}