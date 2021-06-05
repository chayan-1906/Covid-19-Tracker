package com.example.covid_19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.Objects;

import static com.example.covid_19tracker.Constants.STATE_ACTIVE;
import static com.example.covid_19tracker.Constants.STATE_CONFIRMED;
import static com.example.covid_19tracker.Constants.STATE_CONFIRMED_NEW;
import static com.example.covid_19tracker.Constants.STATE_DEATH;
import static com.example.covid_19tracker.Constants.STATE_DEATH_NEW;
import static com.example.covid_19tracker.Constants.STATE_LAST_UPDATE;
import static com.example.covid_19tracker.Constants.STATE_NAME;
import static com.example.covid_19tracker.Constants.STATE_RECOVERED;
import static com.example.covid_19tracker.Constants.STATE_RECOVERED_NEW;

public class EachStateDataActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private PieChart pieChart;
    private TextView textViewNoOfConfirmedCasesState;
    private TextView textViewConfirmedCases_NewState;
    private TextView textViewNoOfActiveCasesState;
    private TextView textViewNoOfRecoveredCasesState;
    private TextView textViewRecoveredCases_NewState;
    private TextView textViewNoOfDeathCasesState;
    private TextView textViewDeathCases_NewState;
    private TextView textViewLastUpdateState;
    private LinearLayout linearLayoutDistrictData;
    private TextView textViewDistrictData;

    /* Variables... */
    private String string_statename;
    private String string_confirmed;
    private String string_confirmed_new;
    private String string_active;
    private String string_recovered;
    private String string_recovered_new;
    private String string_death;
    private String string_death_new;
    private String string_last_update_time;

    private final MainActivity mainActivity = new MainActivity ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_each_state_data );

        get_intent ();
        Objects.requireNonNull ( getSupportActionBar ( ) ).setTitle ( string_statename );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayShowHomeEnabled ( true );

        swipeRefreshLayout = findViewById ( R.id.swipeRefreshLayout );
        pieChart = findViewById ( R.id.pieChart );
        textViewNoOfConfirmedCasesState = findViewById ( R.id.textViewNoOfConfirmedCasesState );
        textViewConfirmedCases_NewState = findViewById ( R.id.textViewConfirmedCases_NewState );
        textViewNoOfActiveCasesState = findViewById ( R.id.textViewNoOfActiveCasesState );
        textViewNoOfRecoveredCasesState = findViewById ( R.id.textViewNoOfRecoveredCasesState );
        textViewRecoveredCases_NewState = findViewById ( R.id.textViewRecoveredCases_NewState );
        textViewNoOfDeathCasesState = findViewById ( R.id.textViewNoOfDeathCasesState );
        textViewDeathCases_NewState = findViewById ( R.id.textViewDeathCases_NewState );
        textViewLastUpdateState = findViewById ( R.id.textViewLastUpdateState );
        linearLayoutDistrictData = findViewById ( R.id.linearLayoutDistrictData );
        textViewDistrictData = findViewById ( R.id.textViewDistrictData );

        loadStateData();

        swipeRefreshLayout.setOnRefreshListener ( () -> {
            loadStateData ();
            swipeRefreshLayout.setRefreshing ( false );
        } );

        linearLayoutDistrictData.setOnClickListener ( v -> {
            Intent intentDistrictWiseDataActivity = new Intent ( EachStateDataActivity.this, DistrictWiseDataActivity.class );
            intentDistrictWiseDataActivity.putExtra ( STATE_NAME, string_statename );
            startActivity ( intentDistrictWiseDataActivity );
        } );
    }

    private void get_intent () {
        /* Fetching data which is passed from previous activity into this activity */
        Intent intent = getIntent ();
        string_statename = intent.getStringExtra ( STATE_NAME );
        string_confirmed = intent.getStringExtra( STATE_CONFIRMED );
        string_confirmed_new = intent.getStringExtra( STATE_CONFIRMED_NEW );
        string_active = intent.getStringExtra( STATE_ACTIVE );
        string_death = intent.getStringExtra( STATE_DEATH );
        string_death_new = intent.getStringExtra( STATE_DEATH_NEW );
        string_recovered = intent.getStringExtra( STATE_RECOVERED );
        string_recovered_new = intent.getStringExtra( STATE_RECOVERED_NEW );
        string_last_update_time = intent.getStringExtra( STATE_LAST_UPDATE );
    }

    @SuppressLint("SetTextI18n")
    private void loadStateData() {
        mainActivity.showDialog ( this );
        pieChart.clearChart ();
        Handler postDelayedToShowProgress = new Handler (  );
        postDelayedToShowProgress.postDelayed ( () -> {
            textViewNoOfConfirmedCasesState.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_confirmed ) ) );
            textViewConfirmedCases_NewState.setText ( "+" + NumberFormat.getInstance ().format ( Integer.parseInt ( string_confirmed_new ) ) );
            textViewNoOfActiveCasesState.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_active ) ) );
            textViewNoOfDeathCasesState.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_death ) ) );
            textViewDeathCases_NewState.setText ( "+" + NumberFormat.getInstance ().format ( Integer.parseInt ( string_death_new ) ) );
            textViewNoOfRecoveredCasesState.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_recovered ) ) );
            textViewRecoveredCases_NewState.setText ( "+" + NumberFormat.getInstance ().format ( Integer.parseInt ( string_recovered_new ) ) );
            String formatDate = mainActivity.formatDate ( string_last_update_time, 0 );
            textViewLastUpdateState.setText ( formatDate );

            textViewDistrictData.setText ( "District Data of " + string_statename );

            /* Setting up Pie Chart */
            pieChart.addPieSlice ( new PieModel ( "Active", Integer.parseInt ( string_active ), Color.parseColor ( "#78DBF3" ) ) );
            pieChart.addPieSlice ( new PieModel ( "Recovered", Integer.parseInt ( string_recovered ), Color.parseColor ( "#7EC544" ) ) );
            pieChart.addPieSlice ( new PieModel ( "Deceased", Integer.parseInt ( string_death ), Color.parseColor ( "#F6404F" ) ) );
            pieChart.startAnimation (  );
            mainActivity.dismissDialog ();
        }, 1000 );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId ()==android.R.id.home)
            finish ();
        return super.onOptionsItemSelected ( item );
    }
}
