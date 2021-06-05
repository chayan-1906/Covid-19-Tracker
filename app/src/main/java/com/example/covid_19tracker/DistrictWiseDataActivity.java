package com.example.covid_19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static com.example.covid_19tracker.Constants.STATE_NAME;

public class DistrictWiseDataActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayoutDistrictWiseData;
    private EditText editTextDistrictWiseSearch;
    private RecyclerView recyclerViewDistrictName;

    private ArrayList<DistrictWiseModel> districtWiseModelArrayList;
    private DistrictWiseAdapter districtWiseAdapter;

    private String stringState;
    private String stringDistrict;
    private String string_confirmed;
    private String str_confirmed_new;
    private String string_active;
    private String string_recovered;
    private String string_recovered_new;
    private String string_death;
    private String string_death_new;

    private final MainActivity mainActivity = new MainActivity ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_district_wise_data );

        swipeRefreshLayoutDistrictWiseData = findViewById ( R.id.swipeRefreshLayoutDistrictWiseData );
        editTextDistrictWiseSearch = findViewById ( R.id.editTextDistrictWiseSearch );
        recyclerViewDistrictName = findViewById ( R.id.recyclerViewDistrictName );

        recyclerViewDistrictName.setHasFixedSize ( true );
        recyclerViewDistrictName.setLayoutManager ( new LinearLayoutManager ( this ) );

        districtWiseModelArrayList = new ArrayList<> (  );
        districtWiseAdapter = new DistrictWiseAdapter ( DistrictWiseDataActivity.this, districtWiseModelArrayList );
        recyclerViewDistrictName.setAdapter ( districtWiseAdapter );

        Intent intent = getIntent ();
        stringState = intent.getStringExtra ( STATE_NAME );
        Objects.requireNonNull ( getSupportActionBar ( ) ).setTitle ( "Region/District" );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );
        getSupportActionBar ().setDisplayShowHomeEnabled ( true );

        fetchDistrictWiseData();

        swipeRefreshLayoutDistrictWiseData.setOnRefreshListener ( () -> {
            fetchDistrictWiseData();
            swipeRefreshLayoutDistrictWiseData.setRefreshing ( false );
        } );

        editTextDistrictWiseSearch.addTextChangedListener ( new TextWatcher ( ) {
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

    private void filter(String s) {
        ArrayList<DistrictWiseModel> filteredList = new ArrayList<> (  );
        for (DistrictWiseModel districtWiseModelItem : districtWiseModelArrayList)
            if (districtWiseModelItem.getDistrict ().toLowerCase ().contains ( s.toLowerCase () ))
                filteredList.add ( districtWiseModelItem );
        districtWiseAdapter.filterList ( filteredList, s );
    }

    private void fetchDistrictWiseData() {
        mainActivity.showDialog ( this );
        final RequestQueue requestQueue = Volley.newRequestQueue ( this );
        String apiUrl = "https://api.covid19india.org/v2/state_district_wise.json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest ( Request.Method.GET, apiUrl, null, response -> {
            try {
                int flag = 0;
                districtWiseModelArrayList.clear ();
                for (int i = 0; i < response.length (); i++) {
                    JSONObject jsonObjectState = response.getJSONObject ( i );
                    if (stringState.toLowerCase ().equals ( jsonObjectState.getString ( "state" ).toLowerCase () )) {
                        JSONArray jsonArrayDistrict = jsonObjectState.getJSONArray ( "districtData" );
                        for (int j = 0; j < jsonArrayDistrict.length (); j++) {
                            JSONObject jsonObjectDistrict = jsonArrayDistrict.getJSONObject ( j );
                            stringDistrict = jsonObjectDistrict.getString ( "district" );
                            string_confirmed = jsonObjectDistrict.getString ( "confirmed" );
                            string_active = jsonObjectDistrict.getString ( "active" );
                            string_death = jsonObjectDistrict.getString ( "deceased" );
                            string_recovered = jsonObjectDistrict.getString ( "recovered" );
                            JSONObject jsonObjectDistrictNew = jsonObjectDistrict.getJSONObject ( "delta" );
                            str_confirmed_new = jsonObjectDistrictNew.getString ( "confirmed" );
                            string_recovered_new = jsonObjectDistrictNew.getString ( "recovered" );
                            string_death_new = jsonObjectDistrictNew.getString ( "deceased" );

                            // Creating am object of our statewise model class and passing the values in the constructor
                            DistrictWiseModel districtWiseModel = new DistrictWiseModel ( stringDistrict, string_confirmed, string_active, string_recovered, string_death, str_confirmed_new, string_recovered_new, string_death_new );
                            // Adding data to ArrayList
                            districtWiseModelArrayList.add ( districtWiseModel );
                        }
                        flag = 1;
                    }
                    if (flag==1)
                        break;
                }
                Collections.sort ( districtWiseModelArrayList, (o1, o2) -> {
                    if (Integer.parseInt ( o1.getConfirmed () ) > Integer.parseInt ( o2.getConfirmed () ))
                        return -1;
                    else
                        return 1;
                } );
                Handler handler = new Handler (  );
                handler.postDelayed ( () -> {
                    districtWiseAdapter.notifyDataSetChanged ();
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