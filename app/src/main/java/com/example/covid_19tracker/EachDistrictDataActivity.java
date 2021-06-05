package com.example.covid_19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;
import java.util.Objects;

import static com.example.covid_19tracker.Constants.DISTRICT_ACTIVE;
import static com.example.covid_19tracker.Constants.DISTRICT_CONFIRMED;
import static com.example.covid_19tracker.Constants.DISTRICT_CONFIRMED_NEW;
import static com.example.covid_19tracker.Constants.DISTRICT_DEATH;
import static com.example.covid_19tracker.Constants.DISTRICT_DEATH_NEW;
import static com.example.covid_19tracker.Constants.DISTRICT_NAME;
import static com.example.covid_19tracker.Constants.DISTRICT_RECOVERED;
import static com.example.covid_19tracker.Constants.DISTRICT_RECOVERED_NEW;

public class EachDistrictDataActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private PieChart pieChart;
    private TextView textViewNoOfConfirmedCasesDistrict;
    private TextView textViewConfirmedCases_NewDistrict;
    private TextView textViewNoOfActiveCasesDistrict;
    private TextView textViewNoOfRecoveredCasesDistrict;
    private TextView textViewRecoveredCases_NewDistrict;
    private TextView textViewNoOfDeathCasesDistrict;
    private TextView textViewDeathCases_NewDistrict;

    /* Variables... */
    private String string_districtname;
    private String string_confirmed;
    private String string_confirmed_new;
    private String string_active;
    private String string_recovered;
    private String string_recovered_new;
    private String string_death;
    private String string_death_new;

    private final MainActivity mainActivity = new MainActivity ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_each_district_data );

        Intent intent = getIntent ();
        string_districtname =   intent.getStringExtra ( DISTRICT_NAME );
        string_confirmed = intent.getStringExtra ( DISTRICT_CONFIRMED );
        string_confirmed_new = intent.getStringExtra ( DISTRICT_CONFIRMED_NEW );
        string_active = intent.getStringExtra ( DISTRICT_ACTIVE );
        string_death = intent.getStringExtra ( DISTRICT_DEATH );
        string_death_new = intent.getStringExtra ( DISTRICT_DEATH_NEW );
        string_recovered = intent.getStringExtra ( DISTRICT_RECOVERED );
        string_recovered_new = intent.getStringExtra ( DISTRICT_RECOVERED_NEW );

        Objects.requireNonNull ( getSupportActionBar ( ) ).setTitle ( string_districtname );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayShowHomeEnabled ( true );

        swipeRefreshLayout = findViewById ( R.id.swipeRefreshLayout );
        pieChart = findViewById ( R.id.pieChart );
        textViewNoOfConfirmedCasesDistrict = findViewById ( R.id.textViewNoOfConfirmedCasesDistrict );
        textViewConfirmedCases_NewDistrict = findViewById ( R.id.textViewConfirmedCases_NewDistrict );
        textViewNoOfActiveCasesDistrict = findViewById ( R.id.textViewNoOfActiveCasesDistrict );
        textViewNoOfRecoveredCasesDistrict = findViewById ( R.id.textViewNoOfRecoveredCasesDistrict );
        textViewRecoveredCases_NewDistrict = findViewById ( R.id.textViewRecoveredCases_NewDistrict );
        textViewNoOfDeathCasesDistrict = findViewById ( R.id.textViewNoOfDeathCasesDistrict );
        textViewDeathCases_NewDistrict = findViewById ( R.id.textViewDeathCases_NewDistrict );

        loadDistrictData();

        swipeRefreshLayout.setOnRefreshListener ( () -> {
            loadDistrictData ();
            swipeRefreshLayout.setRefreshing ( false );
        } );
    }

    @SuppressLint("SetTextI18n")
    private void loadDistrictData() {
        /* Show dialog */
        mainActivity.showDialog(this);
        pieChart.clearChart ();
        Handler handler = new Handler();
        handler.postDelayed ( () -> {
            textViewNoOfConfirmedCasesDistrict.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_confirmed ) ) );
            textViewConfirmedCases_NewDistrict.setText ( "+" + NumberFormat.getInstance ().format ( Integer.parseInt ( string_confirmed_new ) ) );
            textViewNoOfActiveCasesDistrict.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_active ) ) );
            textViewNoOfDeathCasesDistrict.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_death ) ) );
            textViewDeathCases_NewDistrict.setText ( "+" + NumberFormat.getInstance ().format ( Integer.parseInt ( string_death_new ) ) );
            textViewNoOfRecoveredCasesDistrict.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_recovered ) ) );
            textViewRecoveredCases_NewDistrict.setText ( "+" + NumberFormat.getInstance ().format ( Integer.parseInt ( string_recovered_new ) ) );

            pieChart.addPieSlice ( new PieModel ("Active", Integer.parseInt ( string_active ), Color.parseColor ("#78DBF3")));
            pieChart.addPieSlice ( new PieModel ("Recovered", Integer.parseInt ( string_recovered ), Color.parseColor ("#7EC544")));
            pieChart.addPieSlice ( new PieModel ("Deceased", Integer.parseInt ( string_death ), Color.parseColor ("#F6404F")));
            pieChart.startAnimation();
            mainActivity.dismissDialog();
        },1000);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId ()==android.R.id.home)
            finish ();
        return super.onOptionsItemSelected ( item );
    }
}
