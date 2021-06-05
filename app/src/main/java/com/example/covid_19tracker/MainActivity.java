package com.example.covid_19tracker;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    /* Android UI... */
    private SwipeRefreshLayout swipeRefreshLayout;
    private PieChart pieChart;
    private TextView textViewNoOfConfirmedCases;
    private TextView textViewConfirmedCases_New;
    private TextView textViewNoOfActiveCases;
    private TextView textViewNoOfRecoveredCases;
    private TextView textViewRecoveredCases_New;
    private TextView textViewNoOfDeathCases;
    private TextView textViewDeathCases_New;
    private TextView textViewNoOfSamplesCases;
    private TextView textViewSamplesCases_New;
    private TextView textViewLastUpdateTime;
    private TextView textViewLastUpdateDate;
    private LinearLayout linearLayoutStateData;
    private LinearLayout linearLayoutWorldData;
    private ProgressDialog progressDialog;

    /* Variables... */
    private String string_confirmed;
    private String string_confirmed_new;
    private String string_active;
    private String string_recovered;
    private String string_recovered_new;
    private String string_death;
    private String string_death_new;
    private String string_tests;
    private String string_tests_new;
    private String string_last_update_time;
    private boolean doubleBackToExitPressedOnce;
    private Toast backPressToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        Objects.requireNonNull ( getSupportActionBar ( ) ).setTitle ( "Covid-19 Tracker (India)" );

        swipeRefreshLayout = findViewById ( R.id.swipeRefreshLayout );
        pieChart = findViewById ( R.id.pieChart );
        textViewNoOfConfirmedCases = findViewById ( R.id.textViewNoOfConfirmedCases );
        textViewConfirmedCases_New = findViewById ( R.id.textViewConfirmedCases_New );
        textViewNoOfActiveCases = findViewById ( R.id.textViewNoOfActiveCases );
        textViewNoOfRecoveredCases = findViewById ( R.id.textViewNoOfRecoveredCases );
        textViewRecoveredCases_New = findViewById ( R.id.textViewRecoveredCases_New );
        textViewNoOfDeathCases = findViewById ( R.id.textViewNoOfDeathCases );
        textViewDeathCases_New = findViewById ( R.id.textViewDeathCases_New );
        textViewNoOfSamplesCases = findViewById ( R.id.textViewNoOfSamplesCases );
        textViewSamplesCases_New = findViewById ( R.id.textViewSamplesCases_New );
        textViewLastUpdateTime = findViewById ( R.id.textViewLastUpdateTime );
        textViewLastUpdateDate = findViewById ( R.id.textViewLastUpdateDate );
        linearLayoutStateData = findViewById ( R.id.linearLayoutStateData );
        linearLayoutWorldData = findViewById ( R.id.linearLayoutWorldData );

        doubleBackToExitPressedOnce = false;

        /* Fetch data from API...*/
        fetchData();

        swipeRefreshLayout.setOnRefreshListener ( () -> {
            fetchData ();
            swipeRefreshLayout.setRefreshing ( false );
        } );

        linearLayoutStateData.setOnClickListener ( v -> startActivity ( new Intent ( MainActivity.this, StateWiseDataActivity.class ) ) );

        linearLayoutWorldData.setOnClickListener ( v -> startActivity ( new Intent ( MainActivity.this, WorldDataActivity.class ) ) );
    }

    private void fetchData() {
        showDialog ( this );
        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        String apiUrl = "https://api.covid19india.org/data.json";

        pieChart.clearChart ();

        @SuppressLint("SetTextI18n") JsonObjectRequest jsonObjectRequest = new JsonObjectRequest ( Request.Method.GET, apiUrl, null, response -> {
            /* As the data of the json are in nested array, so we need to define the array from which we want to fetch the data */
            JSONArray jsonArrayAllState;
            JSONArray jsonArrayTestData;

            try {
                jsonArrayAllState = response.getJSONArray ( "statewise" );
                jsonArrayTestData = response.getJSONArray ( "tested" );
                JSONObject jsonObjectDataIndia = jsonArrayAllState.getJSONObject ( 0 );
                JSONObject jsonObjectTestDataIndia = jsonArrayTestData.getJSONObject ( jsonArrayTestData.length () - 1 );

                /* Fetching data for India and storing it in a String */
                string_confirmed = jsonObjectDataIndia.getString ( "confirmed" );   /* Confirmed cases in India */
                string_confirmed_new = jsonObjectDataIndia.getString ( "deltaconfirmed" );  /* New Confirmed cases from last update time */

                string_active = jsonObjectDataIndia.getString ( "active" ) ;    /* Active cases in India */

                string_recovered = jsonObjectDataIndia.getString ( "recovered" );    /* Total recovered cases India */
                string_recovered_new = jsonObjectDataIndia.getString ( "deltarecovered" );    /* New recovered cases from last update time */

                string_death = jsonObjectDataIndia.getString ( "deaths" );    /* Total deaths in India */
                string_death_new = jsonObjectDataIndia.getString ( "deltadeaths" );    /* New death cases from last update time */

                string_last_update_time = jsonObjectDataIndia.getString ( "lastupdatedtime" );     /* Last update date and time */

                string_tests = jsonObjectTestDataIndia.getString ( "totalsamplestested" );    /* Total samples tested cases in India */
                string_tests_new = jsonObjectTestDataIndia.getString ( "samplereportedtoday" );    /* New samples tested today */

                Handler handler = new Handler (  );
                handler.postDelayed ( () -> {
                    /* Setting text in textview */
                    textViewNoOfConfirmedCases.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_confirmed ) ) );
                    textViewConfirmedCases_New.setText ( "+" + NumberFormat.getInstance ().format ( Integer.parseInt ( string_confirmed_new ) ) );

                    textViewNoOfActiveCases.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_active ) ) );

                    textViewNoOfRecoveredCases.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_recovered ) ) );
                    textViewRecoveredCases_New.setText ( "+" + NumberFormat.getInstance ().format ( Integer.parseInt ( string_recovered_new ) ) );

                    textViewNoOfDeathCases.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_death ) ) );
                    textViewDeathCases_New.setText ( "+" + NumberFormat.getInstance ().format ( Integer.parseInt ( string_death_new ) ) );

                    if (string_tests.isEmpty ())
                        textViewNoOfSamplesCases.setText ( "0" );
                    else
                        textViewNoOfSamplesCases.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_tests ) ) );
                    if (string_tests_new.isEmpty ())
                        textViewSamplesCases_New.setText ( "0" );
                    else
                        textViewSamplesCases_New.setText ( "+" + NumberFormat.getInstance ().format ( Integer.parseInt ( string_tests_new ) ) );

                    textViewLastUpdateDate.setText( formatDate ( string_last_update_time, 1 ) );
                    textViewLastUpdateTime.setText( formatDate ( string_last_update_time, 2 ) );

                    pieChart.addPieSlice(new PieModel("Active", Integer.parseInt( string_active ), Color.parseColor("#78DBF3")));
                    pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt( string_recovered ), Color.parseColor("#7EC544")));
                    pieChart.addPieSlice(new PieModel("Deaths", Integer.parseInt( string_death ), Color.parseColor("#F6404F")));
                    pieChart.startAnimation (  );
                    dismissDialog ();
                }, 1000 );
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }, error -> {

        } );
        requestQueue.add ( jsonObjectRequest );
    }

    public void showDialog(Context context) {
        /* setting up progress dialog */
        progressDialog = new ProgressDialog (context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void dismissDialog() {
        progressDialog.dismiss();
    }

    @SuppressLint("SimpleDateFormat")
    public String formatDate (String stringDate, int testCase ) {
        Date date;
        String dateFormat;
        try {
            date = new SimpleDateFormat ( "dd/MM/yyyy HH:mm", Locale.US ).parse ( stringDate );
            if (testCase == 0) {
                dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a").format( Objects.requireNonNull ( date ) );
                return dateFormat;
            } else if (testCase == 1) {
                dateFormat = new SimpleDateFormat("dd MMM yyyy").format( Objects.requireNonNull ( date ) );
                return dateFormat;
            } else if (testCase == 2) {
                dateFormat = new SimpleDateFormat("hh:mm a").format( Objects.requireNonNull ( date ) );
                return dateFormat;
            } else {
                Log.d("error", "Wrong input! Choose from 0 to 2");
                return "Error";
            }
        } catch (ParseException e) {
            e.printStackTrace ( );
            return stringDate;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater ();
        menuInflater.inflate ( R.menu.menu_main, menu );
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId ();
        switch (id) {
            case R.id.menu_info:
                startActivity ( new Intent ( MainActivity.this, AboutActivity.class ) );
        }
        return super.onOptionsItemSelected ( item );
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            backPressToast.cancel ();
            super.onBackPressed ( );
            return;
        }
        doubleBackToExitPressedOnce = true;
        backPressToast = FancyToast.makeText ( getApplicationContext (), "Click again to exit", FancyToast.LENGTH_SHORT, FancyToast.WARNING, false );
        backPressToast.show ();

        new Handler (  ).postDelayed ( () -> doubleBackToExitPressedOnce = false, 2500 );
    }
}
