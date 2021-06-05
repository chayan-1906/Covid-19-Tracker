package com.example.covid_19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class CountryWiseDataActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayoutCountryWiseData;
    private EditText editTextCountryWiseSearch;
    private RecyclerView recyclerViewCountryName;

    private CountryWiseAdapter countryWiseAdapter;
    private ArrayList<CountryWiseModel> countryWiseModelArrayList;

    private String stringCountry;
    private String string_confirmed;
    private String str_confirmed_new;
    private String string_active;
    private String string_recovered;
    private String string_death;
    private String string_death_new;
    private String string_tests;

    private final MainActivity mainActivity = new MainActivity ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_country_wise_data );

        Objects.requireNonNull ( getSupportActionBar ( ) ).setTitle ( "World Data (Select Country)" );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayShowHomeEnabled ( true );

        swipeRefreshLayoutCountryWiseData = findViewById ( R.id.swipeRefreshLayoutCountryWiseData );
        editTextCountryWiseSearch = findViewById ( R.id.editTextCountryWiseSearch );
//        cardViewCountryName = findViewById ( R.id.cardViewCountryName );
        recyclerViewCountryName = findViewById ( R.id.recyclerViewCountryName );

        countryWiseModelArrayList = new ArrayList<> (  );
        countryWiseAdapter = new CountryWiseAdapter ( CountryWiseDataActivity.this, countryWiseModelArrayList );
        recyclerViewCountryName.setHasFixedSize ( true );
        recyclerViewCountryName.setLayoutManager ( new LinearLayoutManager ( this ) );
        recyclerViewCountryName.setAdapter ( countryWiseAdapter );

        fetchCountryWiseData();

        swipeRefreshLayoutCountryWiseData.setOnRefreshListener ( () -> {
            fetchCountryWiseData ();
            swipeRefreshLayoutCountryWiseData.setRefreshing ( false );
        } );

        editTextCountryWiseSearch.addTextChangedListener ( new TextWatcher ( ) {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString ());
            }
        } );
    }

    private void filter(String text) {
        ArrayList<CountryWiseModel> filteredList = new ArrayList<> (  );
        for (CountryWiseModel countryWiseModelItem : countryWiseModelArrayList)
            if (countryWiseModelItem.getCountry ().toLowerCase ().contains ( text.toLowerCase () ))
                filteredList.add ( countryWiseModelItem );
        countryWiseAdapter.filterList(filteredList, text);
    }

    private void fetchCountryWiseData() {
        mainActivity.showDialog ( this );
        final RequestQueue requestQueue = Volley.newRequestQueue ( this );
        String apiUrl = "https://corona.lmao.ninja/v2/countries";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest ( Request.Method.GET, apiUrl, null, response -> {
            try {
                countryWiseModelArrayList.clear ();
                for (int i = 0; i < response.length (); i++) {
                    JSONObject jsonObjectCountry = response.getJSONObject ( i );
                    stringCountry = jsonObjectCountry.getString ( "country" );
                    string_confirmed = jsonObjectCountry.getString ( "cases" );
                    str_confirmed_new = jsonObjectCountry.getString ( "todayCases" );
                    string_active = jsonObjectCountry.getString ( "active" );
                    string_recovered = jsonObjectCountry.getString ( "recovered" );
                    string_death = jsonObjectCountry.getString ( "deaths" );
                    string_death_new = jsonObjectCountry.getString ( "todayDeaths" );
                    string_tests = jsonObjectCountry.getString ( "tests" );
                    JSONObject flagObject = jsonObjectCountry.getJSONObject ( "countryInfo" );
                    String flagUrl = flagObject.getString ( "flag" );

                    CountryWiseModel countryWiseModel = new CountryWiseModel ( stringCountry, string_confirmed, str_confirmed_new, string_active, string_death, string_death_new, string_recovered, string_tests, flagUrl );
                    countryWiseModelArrayList.add ( countryWiseModel );
                }
                Collections.sort ( countryWiseModelArrayList, (o1, o2) -> {
                    if (Integer.parseInt ( o1.getConfirmed () ) > Integer.parseInt ( o2.getConfirmed () ))
                        return -1;
                    else
                        return 1;
                } );
                Handler handler = new Handler (  );
                handler.postDelayed ( () -> {
                    countryWiseAdapter.notifyDataSetChanged ();
                    mainActivity.dismissDialog ();
                }, 1000 );
            } catch (JSONException e) {
                e.printStackTrace ( );
            }
        }, Throwable::printStackTrace );
        requestQueue.add ( jsonArrayRequest );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId ()==android.R.id.home)
            finish ();
        return super.onOptionsItemSelected ( item );
    }
}
