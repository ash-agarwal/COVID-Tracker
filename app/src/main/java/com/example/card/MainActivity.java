package com.example.card;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floating1);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "Updating Data...", Toast.LENGTH_SHORT);
                toast.show();
                putWorldData();
            }
        });


        putWorldData();


        final Button button = (Button) findViewById(R.id.button);
        button.setEnabled(true);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getApplicationContext(), "Loading Data...Please Wait...", Toast.LENGTH_LONG);
                toast.show();

                button.setEnabled(false);
                Intent intent = new Intent(getApplicationContext(), state_data.class);
                startActivity(intent);

            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        final Button button = (Button) findViewById(R.id.button);
        button.setEnabled(true);
    }

    boolean check_for_internet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    void display_snackbar_no_internet(View view) {
        String error_message = "No Internet";

        Snackbar snackbar = Snackbar.make(view, error_message, Snackbar.LENGTH_INDEFINITE).setAnchorView(R.id.floating1);
        snackbar.setAnchorView(R.id.floating1);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!check_for_internet()) {
                    display_snackbar_no_internet(view);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Welcome Back", Toast.LENGTH_SHORT);
                    toast.show();
                    putWorldData();
                }
            }
        });
        snackbar.show();

    }

    //to format the values in terms of thousands(K) ; Millions(M) or Billions(B)
    String getFormattedNumber(long n) {

        String number = Long.toString(n);
        int nunber_length = number.length();


        String formattedString = "";

        if (n < 1000) {
            return number;
        } else if (n >= 1000 && n < 1000000) {
            formattedString = number.substring(0, nunber_length - 3) + "." + number.charAt(nunber_length - 3) + "K";
        } else if (n >= 1000000 && n < 1000000000) {
            formattedString = number.substring(0, nunber_length - 6) + "." + number.charAt(nunber_length - 6) + "M";
        } else {
            formattedString = number.substring(0, nunber_length - 9) + "." + number.charAt(nunber_length - 9) + "B";
        }
        return formattedString;

    }

    void putWorldData() {

        if (!check_for_internet())
            display_snackbar_no_internet(findViewById(R.id.constriant2));


        final TextView wt = (TextView) findViewById(R.id.totalWorld);
        final TextView wr = (TextView) findViewById(R.id.recoveredWorld);
        final TextView wd = (TextView) findViewById(R.id.deathsWorld);

        final TextView wti = (TextView) findViewById(R.id.totalWorldInc);
        final TextView wri = (TextView) findViewById(R.id.recoveredWorldInc);
        final TextView wdi = (TextView) findViewById(R.id.deathsWorldInc);

        //String world_url="https://corona-virus-stats.herokuapp.com/api/v1/cases/countries-search?search=World";
        String world_url = "https://corona.lmao.ninja/v2/all?yesterday";
        final RequestQueue requestQueue;

        requestQueue = Volley.newRequestQueue(this);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, world_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
                    wt.setText(getFormattedNumber(response.getLong("cases")));
                    wr.setText(getFormattedNumber(response.getLong("recovered")));
                    wd.setText(getFormattedNumber(response.getLong("deaths")));

                    long increase_temp = response.getLong("todayCases");

                    if(increase_temp<0)
                        wti.setText(getFormattedNumber(increase_temp));
                    else
                        wti.setText("+" + getFormattedNumber(increase_temp));

                    increase_temp = response.getLong("todayRecovered");
                    if(increase_temp<0)
                        wri.setText(getFormattedNumber(increase_temp));
                    else
                        wri.setText("+" + getFormattedNumber(increase_temp));

                    increase_temp = response.getLong("todayDeaths");
                    if(increase_temp<0)
                        wdi.setText(getFormattedNumber(increase_temp));
                    else
                        wdi.setText("+" + getFormattedNumber(increase_temp));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error World Data");
            }
        });
        requestQueue.add(jsonObjectRequest);


    }
}



