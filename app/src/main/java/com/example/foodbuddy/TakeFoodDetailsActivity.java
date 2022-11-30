package com.example.foodbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TakeFoodDetailsActivity extends AppCompatActivity {
    String userToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_food_details);

        userToken = Utility.getUserToken();
        Button shareFoodButton = findViewById(R.id.shareButton);

        shareFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareFoodButtonOnClick();
            }
        });
    }

    public void shareFoodButtonOnClick(){
        TextView orderIDView = findViewById(R.id.editOrderID);
        String orderID = orderIDView.getText().toString();

        RadioGroup radioGroup1 = findViewById(R.id.radioGroup1);
        int selectedRadioButtonID = radioGroup1.getCheckedRadioButtonId();
        int portion, veg, piece;

        switch (selectedRadioButtonID){
            case R.id.fullMealRadioButton:
                System.out.println("Full meal button selected! "+selectedRadioButtonID);
                portion = 1;
                break;

            case R.id.shareMealRadioButton:
                System.out.println("Share meal button selected! "+selectedRadioButtonID);
                portion = 0;
                break;

            default:
                System.out.println("Other button selected! "+selectedRadioButtonID);
                portion = 0;
                break;
        }

        RadioGroup radioGroup2 = findViewById(R.id.radioGroup2);
        selectedRadioButtonID = radioGroup2.getCheckedRadioButtonId();

        switch (selectedRadioButtonID){
            case R.id.veg5RadioButton:
                veg = 1;
                piece = 0;
                break;

            case R.id.veg8RadioButton:
                veg = 1;
                piece = 1;
                break;

            case R.id.nonveg5RadioButton:
                veg = 0;
                piece = 0;
                break;

            case R.id.nonveg8RadioButton:
                veg = 0;
                piece = 1;
                break;

            default:
                veg = 1;
                piece = 0;
                break;
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("token", userToken)
                .add("portion", String.valueOf(portion))
                .add("order_id", orderID)
                .add("veg", String.valueOf(veg))
                .add("piece", String.valueOf(piece))
                .build();

        String hostAddress = getString(R.string.web_server_address);
        final String url = hostAddress+"/foodapp/sharing/user/givefood/";

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Thread thread = new Thread(() -> {
            try  {
                try{
                    Response response = client.newCall(request).execute();

                    // Do something with the response.
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Utility.setUserToken(jsonObject.getString("token"));

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

        Intent openMainActivity = new Intent(TakeFoodDetailsActivity.this, MainActivity.class);
        startActivity(openMainActivity);
    }
}