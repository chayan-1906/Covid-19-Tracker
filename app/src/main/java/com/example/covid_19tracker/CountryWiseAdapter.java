package com.example.covid_19tracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.covid_19tracker.Constants.COUNTRY_ACTIVE;
import static com.example.covid_19tracker.Constants.COUNTRY_CONFIRMED;
import static com.example.covid_19tracker.Constants.COUNTRY_DECEASED;
import static com.example.covid_19tracker.Constants.COUNTRY_FLAGURL;
import static com.example.covid_19tracker.Constants.COUNTRY_NAME;
import static com.example.covid_19tracker.Constants.COUNTRY_NEW_CONFIRMED;
import static com.example.covid_19tracker.Constants.COUNTRY_NEW_DECEASED;
import static com.example.covid_19tracker.Constants.COUNTRY_RECOVERED;
import static com.example.covid_19tracker.Constants.COUNTRY_TESTS;

public class CountryWiseAdapter extends RecyclerView.Adapter<CountryWiseAdapter.MyViewHolder> {

    private final Context context;
    private ArrayList<CountryWiseModel> countryWiseModelArrayList;
    private SpannableStringBuilder spannableStringBuilder;
    private String searchText = "";

    public CountryWiseAdapter(Context context, ArrayList<CountryWiseModel> countryWiseModelArrayList) {
        this.context = context;
        this.countryWiseModelArrayList = countryWiseModelArrayList;
    }

    public void filterList(ArrayList<CountryWiseModel> filteredList, String searchText) {
        countryWiseModelArrayList = filteredList;
        this.searchText = searchText;
        notifyDataSetChanged ();
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from ( context ).inflate ( R.layout.layout_country_wise, parent, false );
        return new MyViewHolder ( view );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull CountryWiseAdapter.MyViewHolder holder, int position) {
        CountryWiseModel countryWiseModelCurrentItem = countryWiseModelArrayList.get ( position );
        String countryName = countryWiseModelCurrentItem.getCountry ();
        String countryTotal = countryWiseModelCurrentItem.getConfirmed ();
        String countryFlag = countryWiseModelCurrentItem.getFlag ();
        String countryRank = String.valueOf ( position + 1 );
        int countryIntTotal = Integer.parseInt ( countryTotal );
        holder.textViewCountryRank.setText( countryRank + "." );
        holder.textViewCountryWiseConfirmed.setText( NumberFormat.getInstance ().format ( countryIntTotal ) );
        if (searchText.length () > 0) {
            spannableStringBuilder = new SpannableStringBuilder ( countryName );
            Pattern pattern = Pattern.compile ( searchText.toLowerCase () );
            Matcher matcher = pattern.matcher ( countryName.toLowerCase () );
            while (matcher.find ()) {
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan ( Color.rgb ( 52, 195, 235 ) );
                spannableStringBuilder.setSpan ( foregroundColorSpan, matcher.start (), matcher.end (), Spanned.SPAN_INCLUSIVE_INCLUSIVE );
            }
            holder.textViewCountryName.setText ( spannableStringBuilder );
        } else {
            holder.textViewCountryName.setText ( countryName );
        }
        Glide.with ( context ).load ( countryFlag ).diskCacheStrategy ( DiskCacheStrategy.DATA ).into ( holder.imageViewCountryFlag );
        holder.linearLayoutCountryWise.setOnClickListener ( v -> {
            CountryWiseModel countryWiseModelClickedItem = countryWiseModelArrayList.get ( position );
            Intent intentPerCountry = new Intent ( context, EachCountryDataActivity.class );
            intentPerCountry.putExtra ( COUNTRY_NAME, countryWiseModelClickedItem.getCountry () );
            intentPerCountry.putExtra ( COUNTRY_CONFIRMED, countryWiseModelClickedItem.getConfirmed () );
            intentPerCountry.putExtra ( COUNTRY_NEW_CONFIRMED, countryWiseModelClickedItem.getNewConfirmed () );
            intentPerCountry.putExtra ( COUNTRY_ACTIVE, countryWiseModelClickedItem.getActive () );
            intentPerCountry.putExtra ( COUNTRY_RECOVERED, countryWiseModelClickedItem.getRecovered () );
            intentPerCountry.putExtra ( COUNTRY_DECEASED, countryWiseModelClickedItem.getDeceased () );
            intentPerCountry.putExtra ( COUNTRY_NEW_DECEASED, countryWiseModelClickedItem.getNewDeceased () );
            intentPerCountry.putExtra ( COUNTRY_TESTS, countryWiseModelClickedItem.getTests () );
            intentPerCountry.putExtra ( COUNTRY_FLAGURL, countryWiseModelClickedItem.getFlag () );
            context.startActivity ( intentPerCountry );
        } );
    }

    @Override
    public int getItemCount() {
        return countryWiseModelArrayList==null || countryWiseModelArrayList.isEmpty () ? 0 : countryWiseModelArrayList.size ();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout linearLayoutCountryWise;
        private final TextView textViewCountryRank;
        private final ImageView imageViewCountryFlag;
        private final TextView textViewCountryName;
        private final TextView textViewCountryWiseConfirmed;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super ( itemView );
            linearLayoutCountryWise = itemView.findViewById ( R.id.linearLayoutCountryWise );
            textViewCountryRank = itemView.findViewById ( R.id.textViewCountryRank );
            imageViewCountryFlag = itemView.findViewById ( R.id.imageViewCountryFlag );
            textViewCountryName = itemView.findViewById ( R.id.textViewCountryName );
            textViewCountryWiseConfirmed = itemView.findViewById ( R.id.textViewCountryWiseConfirmed );
        }
    }
}
