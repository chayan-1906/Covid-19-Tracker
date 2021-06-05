package com.example.covid_19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class StateWiseDataActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayoutStateWiseData;
    private EditText editTextStateWiseSearch;

    private RecyclerView recyclerViewStateName;
    private StateWiseAdapter stateWiseAdapter;
    private ArrayList<StateWiseModel> arrayListStateWiseModel;

    private String stringState;
    private String string_confirmed;
    private String str_confirmed_new;
    private String string_active;
    private String string_recovered;
    private String string_recovered_new;
    private String string_death;
    private String string_death_new;
    private String string_last_update;

    private final MainActivity mainActivity = new MainActivity ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_state_wise_data );

        Objects.requireNonNull ( getSupportActionBar ( ) ).setTitle ( "Select State" );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayShowHomeEnabled ( true );

        swipeRefreshLayoutStateWiseData = findViewById ( R.id.swipeRefreshLayoutStateWiseData );
        editTextStateWiseSearch = findViewById ( R.id.editTextStateWiseSearch );

        recyclerViewStateName = findViewById ( R.id.recyclerViewStateName );
        recyclerViewStateName.setHasFixedSize ( true );
        recyclerViewStateName.setLayoutManager ( new LinearLayoutManager ( this ) );

        arrayListStateWiseModel = new ArrayList<> (  );
        stateWiseAdapter = new StateWiseAdapter ( StateWiseDataActivity.this, arrayListStateWiseModel );
        recyclerViewStateName.setAdapter ( stateWiseAdapter );

        fetchStateWiseData();

        /* Setting up Swipe RefreshLayout */
        swipeRefreshLayoutStateWiseData.setOnRefreshListener ( () -> {
            fetchStateWiseData ();
            swipeRefreshLayoutStateWiseData.setRefreshing ( false );
        } );

        /* Search */
        editTextStateWiseSearch.addTextChangedListener ( new TextWatcher ( ) {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter ( s.toString () );
            }
        } );
    }

    private void filter(String text) {
        ArrayList<StateWiseModel> filteredList = new ArrayList<> (  );
        for (StateWiseModel stateWiseModelItem : arrayListStateWiseModel)
            if (stateWiseModelItem.getState ().toLowerCase ().contains ( text.toLowerCase () ))
                filteredList.add ( stateWiseModelItem );
        stateWiseAdapter.filterList(filteredList, text);
    }

    private void fetchStateWiseData() {
        mainActivity.showDialog ( this );

        RequestQueue requestQueue = Volley.newRequestQueue ( this );
        String apiUrl = "https://api.covid19india.org/data.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest ( Request.Method.GET, apiUrl, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray ( "statewise" );
                arrayListStateWiseModel.clear ();

                for (int i = 1; i < jsonArray.length (); i++) {
                    JSONObject jsonObjectStatewise = jsonArray.getJSONObject ( i );
                    /* After fetching, storing the data into string variables */
                    stringState = jsonObjectStatewise.getString ( "state" );
                    string_confirmed = jsonObjectStatewise.getString ( "confirmed" );
                    str_confirmed_new = jsonObjectStatewise.getString ( "deltaconfirmed" );
                    string_active = jsonObjectStatewise.getString ( "active" );
                    string_death = jsonObjectStatewise.getString ( "deaths" );
                    string_death_new = jsonObjectStatewise.getString ( "deltadeaths" );
                    string_recovered = jsonObjectStatewise.getString ( "recovered" );
                    string_recovered_new = jsonObjectStatewise.getString ( "deltarecovered" );
                    string_last_update = jsonObjectStatewise.getString ( "lastupdatedtime" );

                    /* Creating an object of our statewise model class and passing the values in the constructor */
                    StateWiseModel stateWiseModel = new StateWiseModel ( stringState, string_confirmed, str_confirmed_new, string_active, string_death, string_death_new, string_recovered, string_recovered_new, string_last_update );
                    /* Adding data to our arrayList */
                    arrayListStateWiseModel.add ( stateWiseModel );
                }
                arrayListStateWiseModel.sort ( (o1, o2) -> {
                    if (Integer.parseInt ( o1.getConfirmed ( ) ) > Integer.parseInt ( o2.getConfirmed ( ) ))
                        return -1;
                    else
                        return 1;
                } );
                Handler handler = new Handler (  );
                handler.postDelayed ( () -> {
                    stateWiseAdapter.notifyDataSetChanged ();
                    mainActivity.dismissDialog ();
                }, 1000 );
            } catch (JSONException e) {
                e.printStackTrace ( );
            }
        }, Throwable::printStackTrace );
        requestQueue.add ( jsonObjectRequest );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId ()==android.R.id.home)
            finish ();
        return super.onOptionsItemSelected ( item );
    }
}