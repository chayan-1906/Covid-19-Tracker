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

import static com.example.covid_19tracker.Constants.COUNTRY_ACTIVE;
import static com.example.covid_19tracker.Constants.COUNTRY_CONFIRMED;
import static com.example.covid_19tracker.Constants.COUNTRY_DECEASED;
import static com.example.covid_19tracker.Constants.COUNTRY_NAME;
import static com.example.covid_19tracker.Constants.COUNTRY_NEW_CONFIRMED;
import static com.example.covid_19tracker.Constants.COUNTRY_NEW_DECEASED;
import static com.example.covid_19tracker.Constants.COUNTRY_RECOVERED;
import static com.example.covid_19tracker.Constants.COUNTRY_TESTS;

public class EachCountryDataActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private PieChart pieChart;
    private TextView textViewNoOfConfirmedCasesCountry;
    private TextView textViewConfirmedCases_NewCountry;
    private TextView textViewNoOfActiveCasesCountry;
    private TextView textViewNoOfRecoveredCasesCountry;
    private TextView textViewRecoveredCases_NewCountry;
    private TextView textViewNoOfDeathCasesCountry;
    private TextView textViewDeathCases_NewCountry;
    private TextView textViewTotalTestsCountry;

    /* Variables... */
    private String string_countryname;
    private String string_confirmed;
    private String string_confirmed_new;
    private String string_active;
    private String string_recovered;
    private String string_death;
    private String string_death_new;
    private String string_total_tests;

    private final MainActivity mainActivity = new MainActivity ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_each_country_data );

        Intent intentWorldDataActivity = getIntent ();
        string_countryname = intentWorldDataActivity.getStringExtra(COUNTRY_NAME);
        string_confirmed = intentWorldDataActivity.getStringExtra(COUNTRY_CONFIRMED);
        string_confirmed_new = intentWorldDataActivity.getStringExtra(COUNTRY_NEW_CONFIRMED);
        string_active = intentWorldDataActivity.getStringExtra(COUNTRY_ACTIVE);
        string_death = intentWorldDataActivity.getStringExtra(COUNTRY_DECEASED);
        string_death_new = intentWorldDataActivity.getStringExtra(COUNTRY_NEW_DECEASED);
        string_recovered = intentWorldDataActivity.getStringExtra(COUNTRY_RECOVERED);
        string_total_tests = intentWorldDataActivity.getStringExtra(COUNTRY_TESTS);

        Objects.requireNonNull ( getSupportActionBar ( ) ).setTitle ( string_countryname );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayShowHomeEnabled ( true );

        swipeRefreshLayout = findViewById ( R.id.swipeRefreshLayout );
        pieChart = findViewById ( R.id.pieChart );
        textViewNoOfConfirmedCasesCountry = findViewById ( R.id.textViewNoOfConfirmedCasesCountry );
        textViewConfirmedCases_NewCountry = findViewById ( R.id.textViewConfirmedCases_NewCountry );
        textViewNoOfActiveCasesCountry = findViewById ( R.id.textViewNoOfActiveCasesCountry );
        textViewNoOfRecoveredCasesCountry = findViewById ( R.id.textViewNoOfRecoveredCasesCountry );
        textViewRecoveredCases_NewCountry = findViewById ( R.id.textViewRecoveredCases_NewCountry );
        textViewNoOfDeathCasesCountry = findViewById ( R.id.textViewNoOfDeathCasesCountry );
        textViewDeathCases_NewCountry = findViewById ( R.id.textViewDeathCases_NewCountry );
        textViewTotalTestsCountry = findViewById ( R.id.textViewTotalTestsCountry );

        loadCountryData();

        swipeRefreshLayout.setOnRefreshListener ( () -> {
            loadCountryData ();
            swipeRefreshLayout.setRefreshing ( false );
        } );
    }

    @SuppressLint("SetTextI18n")
    private void loadCountryData() {
        /* Show dialog */
        mainActivity.showDialog(this);
        pieChart.clearChart ();
        Handler handler = new Handler();
        handler.postDelayed ( () -> {
            textViewNoOfConfirmedCasesCountry.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_confirmed ) ) );
            textViewConfirmedCases_NewCountry.setText ( "+" + NumberFormat.getInstance ().format ( Integer.parseInt ( string_confirmed_new ) ) );
            textViewNoOfActiveCasesCountry.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_active ) ) );
            textViewNoOfDeathCasesCountry.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_death ) ) );
            textViewDeathCases_NewCountry.setText ( "+" + NumberFormat.getInstance ().format ( Integer.parseInt ( string_death_new ) ) );
            textViewNoOfRecoveredCasesCountry.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_recovered ) ) );
            textViewRecoveredCases_NewCountry.setText ( "N/A" );
            textViewTotalTestsCountry.setText ( NumberFormat.getInstance ().format ( Integer.parseInt ( string_total_tests ) ) );

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
