package com.example.covid_19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Objects;

public class WorldDataActivity extends AppCompatActivity {

    /* Android UI... */
    private SwipeRefreshLayout swipeRefreshLayout;
    private PieChart pieChart;
    private TextView textViewNoOfConfirmedCasesWorld;
    private TextView textViewConfirmedCases_NewWorld;
    private TextView textViewNoOfActiveCasesWorld;
    private TextView textViewNoOfRecoveredCasesWorld;
    private TextView textViewRecoveredCases_NewWorld;
    private TextView textViewNoOfDeathCasesWorld;
    private TextView textViewDeathCases_NewWorld;
    private TextView textViewWorldDataTests;
    private LinearLayout linearLayoutCountryData;

    /* Variables... */
    private String string_confirmed;
    private String string_confirmed_new;
    private String string_active;
    private String string_recovered;
    private String string_recovered_new;
    private String string_death;
    private String string_death_new;
    private String string_tests;

    private final MainActivity mainActivity = new MainActivity ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_world_data );

        Objects.requireNonNull ( getSupportActionBar ( ) ).setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayShowHomeEnabled ( true );
        Objects.requireNonNull ( getSupportActionBar ( ) ).setTitle ( "Covid-19 Tracker (World)" );

        swipeRefreshLayout = findViewById ( R.id.swipeRefreshLayout );
        pieChart = findViewById ( R.id.pieChart );
        textViewNoOfConfirmedCasesWorld = findViewById ( R.id.textViewNoOfConfirmedCasesWorld );
        textViewConfirmedCases_NewWorld = findViewById ( R.id.textViewConfirmedCases_NewWorld );
        textViewNoOfActiveCasesWorld = findViewById ( R.id.textViewNoOfActiveCasesWorld );
        textViewNoOfRecoveredCasesWorld = findViewById ( R.id.textViewNoOfRecoveredCasesWorld );
        textViewRecoveredCases_NewWorld = findViewById ( R.id.textViewRecoveredCases_NewWorld );
        textViewNoOfDeathCasesWorld = findViewById ( R.id.textViewNoOfDeathCasesWorld );
        textViewDeathCases_NewWorld = findViewById ( R.id.textViewDeathCases_NewWorld );
        textViewWorldDataTests = findViewById ( R.id.textViewWorldDataTests );
        linearLayoutCountryData = findViewById ( R.id.linearLayoutCountryData );

        fetchWorldData();

        swipeRefreshLayout.setOnRefreshListener ( () -> {
            fetchWorldData ();
            swipeRefreshLayout.setRefreshing ( false );
        } );

        linearLayoutCountryData.setOnClickListener ( v -> startActivity ( new Intent ( WorldDataActivity.this, CountryWiseDataActivity.class ) ) );
    }

    private void fetchWorldData() {

        mainActivity.showDialog ( this );

        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        String apiUrl = "https://corona.lmao.ninja/v2/all";
        pieChart.clearChart ();
        @SuppressLint("SetTextI18n") JsonObjectRequest jsonObjectRequest = new JsonObjectRequest ( Request.Method.GET, apiUrl, null, response -> {
            /* fetch the data from API and storing into string */
            try {
                string_confirmed = response.getString ( "cases" );
                string_confirmed_new = response.getString ( "todayCases" );
                string_active = response.getString ( "active" );
                string_recovered = response.getString ( "recovered" );
                string_recovered_new = response.getString ( "todayRecovered" );
                string_death = response.getString ( "deaths" );
                string_death_new = response.getString ( "todayDeaths" );
                string_tests = response.getString ( "tests" );

                Handler delayToShoProgress = new Handler (  );
                delayToShoProgress.postDelayed ( () -> {
                    textViewNoOfConfirmedCasesWorld.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_confirmed ) ) );
                    textViewConfirmedCases_NewWorld.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_confirmed_new ) ) );
                    textViewNoOfActiveCasesWorld.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_active ) ) );
                    textViewNoOfRecoveredCasesWorld.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_recovered ) ) );
                    textViewRecoveredCases_NewWorld.setText ( "+" + NumberFormat.getInstance ().format ( Integer.parseInt ( string_recovered_new ) ) );
                    textViewNoOfDeathCasesWorld.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_death ) ) );
                    textViewDeathCases_NewWorld.setText ( "+" + NumberFormat.getInstance ().format ( Integer.parseInt ( string_death_new ) ) );
                    textViewWorldDataTests.setText ( NumberFormat.getInstance ().format ( Long.parseLong ( string_tests ) ) );

                    pieChart.addPieSlice ( new PieModel ( "Active", Integer.parseInt ( string_active ), Color.parseColor ( "#78DBF3" ) ) );
                    pieChart.addPieSlice ( new PieModel ( "Recovered", Integer.parseInt ( string_recovered ), Color.parseColor ( "#7EC544" ) ) );
                    pieChart.addPieSlice ( new PieModel ( "Deceased", Integer.parseInt ( string_death ), Color.parseColor ( "#F6404F" ) ) );
                    pieChart.startAnimation (  );
                    mainActivity.dismissDialog ();
                    }, 1000 );
            } catch (JSONException e) {
                e.printStackTrace ( );
            }
        }, error -> {

        } );
        requestQueue.add ( jsonObjectRequest );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId ()==android.R.id.home)
            finish ();
        return super.onOptionsItemSelected ( item );
    }
}
