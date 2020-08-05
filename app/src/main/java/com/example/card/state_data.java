package com.example.card;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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

public class state_data extends AppCompatActivity {

    private RecyclerView recyclerView;
    private data_adapter adapt;
    private List<model> d_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MaterialComponents_Light_NoActionBar);
        setContentView(R.layout.activity_state_data);


        data_india(); //setting Indian Data in card
        get_state_data();


        //adapt.notifyDataSetChanged();

        final FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(R.id.floating2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast=Toast.makeText(getApplicationContext(),"Updating Data...",Toast.LENGTH_SHORT);
                toast.show();
                data_india();
                get_state_data();

            }
        });

        final EditText search_text=(EditText)findViewById(R.id.search_bar);

        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

    }
    void filter(String search)
    {
        ArrayList<model> arrayList=new ArrayList<>();
        for(model item:d_list){
            if(item.getStateName().toLowerCase().contains(search.toLowerCase())){
                arrayList.add(item);
            }
        }
        adapt.filerList(arrayList);

    }


    void data_india() {
        final TextView tc, tr, tcc, td;
        final TextView tci, tri, tcci, tdi;
        tc = (TextView) findViewById(R.id.totalIndia);
        tr = (TextView) findViewById(R.id.recoveredIndia);
        tcc = (TextView) findViewById(R.id.currentIndia);
        td = (TextView) findViewById(R.id.deathsIndia);

        tci = (TextView) findViewById(R.id.totalIndiaInc);
        tri = (TextView) findViewById(R.id.recoveredIndiaInc);
        tcci = (TextView) findViewById(R.id.currentIndiaInc);
        tdi = (TextView) findViewById(R.id.deathsIndiaInc);


        String url_india = "https://api.covidindiatracker.com/total.json";

        final RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);

        if (!check_for_internet()) {
            display_snackbar_no_internet(findViewById(R.id.constraint1));
        } else {
            //Toast toast=Toast.makeText(getApplicationContext(),"Updating Data...",Toast.LENGTH_SHORT);
            //toast.show();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url_india, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        System.out.println("Success Url");
                        //Toast.makeText(getApplicationContext(),"Loaded",Toast.LENGTH_SHORT).show();
                        tc.setText(NumberFormat.getInstance().format(response.getInt("confirmed")));
                        tcc.setText(NumberFormat.getInstance().format(response.getInt("active")));
                        tr.setText(NumberFormat.getInstance().format(response.getInt("recovered")));
                        td.setText(NumberFormat.getInstance().format(response.getInt("deaths")));


                        tci.setText(getformattedString(response.getInt("cChanges")));
                        tri.setText(getformattedString(response.getInt("rChanges")));
                        tcci.setText(getformattedString(response.getInt("aChanges")));
                        tdi.setText(getformattedString(response.getInt("dChanges")));
                    } catch (JSONException e) {
                        System.out.println("Error in Loading");
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Failure India Data");
                }
            });
            requestQueue.add(jsonObjectRequest);
        }
    }

    String getformattedString(int n)
    {
        if(n>0){
            return "+"+NumberFormat.getInstance().format(n);
        }
        else
            return NumberFormat.getInstance().format(n);
    }

    //TODO
    void get_state_data()
    {
        d_list=new ArrayList<>();
        getStateDataFromURL();
        recyclerView =(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        adapt=new data_adapter(getApplicationContext(),d_list);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //recyclerView.setAdapter(adapt);
        final ProgressBar progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        new CountDownTimer(4000, 1000) {

            @Override
            public void onTick(long l) {
                System.out.println("E");progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapt);
                adapt.notifyItemChanged(0);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }.start();



    }



    void getStateDataFromURL()
    {
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        String url = "https://covid-19india-api.herokuapp.com/v2.0/state_data";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    setIndiaDataToTextViews(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error importing JSON data,pasing Indian states data");
            }
        });
        requestQueue.add(stringRequest);
    }




    void setIndiaDataToTextViews(String url) throws JSONException {

        String state,tc,tcc,rc,dc;
        //state=tc=tcc=rc=dc="";
        JSONArray response=new JSONArray(url);
        JSONObject jsonObject;
        JSONArray arr=response.getJSONObject(1).getJSONArray("state_data");
        for(int i=0;i<arr.length();++i) {
            try {
                jsonObject = arr.getJSONObject(i);
                state = jsonObject.getString("state");
                tc = "Total: " + jsonObject.getString("confirmed");
                tcc = "Current: " + jsonObject.getString("active");
                dc = "Deaths: " + jsonObject.getString("deaths");
                rc = "Recovered: " + jsonObject.getString("recovered");
                d_list.add(new model(tc, tcc, rc, dc, state));
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("Error Found");
            }
        }
    }


    boolean check_for_internet(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    void display_snackbar_no_internet(View view)
    {
        String error_message="No Internet";

        Snackbar snackbar = Snackbar.make(view,error_message,Snackbar.LENGTH_INDEFINITE).setAnchorView(R.id.floating2);
        snackbar.setAnchorView(R.id.floating2);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!check_for_internet()){
                            display_snackbar_no_internet(view);
                        }
                        else
                        {
                            Toast toast=Toast.makeText(getApplicationContext(),"Welcome Back",Toast.LENGTH_SHORT);
                            toast.show();
                            data_india();
                            get_state_data();
                        }
                    }
                });
                snackbar.show();

    }

}