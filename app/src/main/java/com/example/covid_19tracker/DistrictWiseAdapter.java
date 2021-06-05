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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.covid_19tracker.Constants.DISTRICT_ACTIVE;
import static com.example.covid_19tracker.Constants.DISTRICT_CONFIRMED;
import static com.example.covid_19tracker.Constants.DISTRICT_CONFIRMED_NEW;
import static com.example.covid_19tracker.Constants.DISTRICT_DEATH;
import static com.example.covid_19tracker.Constants.DISTRICT_DEATH_NEW;
import static com.example.covid_19tracker.Constants.DISTRICT_NAME;
import static com.example.covid_19tracker.Constants.DISTRICT_RECOVERED;
import static com.example.covid_19tracker.Constants.DISTRICT_RECOVERED_NEW;

public class DistrictWiseAdapter extends RecyclerView.Adapter<DistrictWiseAdapter.MyViewHolder> {

    private final Context context;
    private ArrayList<DistrictWiseModel> districtWiseModelArrayList;

    private String searchText = "";
    private SpannableStringBuilder spannableStringBuilder;

    public DistrictWiseAdapter(Context context, ArrayList<DistrictWiseModel> districtWiseModelArrayList) {
        this.context = context;
        this.districtWiseModelArrayList = districtWiseModelArrayList;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from ( context ).inflate ( R.layout.layout_state_wise, parent, false );
        return new MyViewHolder ( view );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull DistrictWiseAdapter.MyViewHolder holder, int position) {
        DistrictWiseModel districtWiseModel = districtWiseModelArrayList.get ( position );
        String districtName = districtWiseModel.getDistrict ();
        String districtTotal = districtWiseModel.getConfirmed ();
        String districtRank = String.valueOf ( position + 1 );
        int districtIntTotal = Integer.parseInt ( districtTotal );
        holder.textViewDistrictRank.setText( districtRank + "." );
        holder.textViewDistrictTotalCases.setText ( NumberFormat.getInstance ().format ( districtIntTotal ) );
        if (searchText.length () > 0) {
            spannableStringBuilder = new SpannableStringBuilder ( districtName );
            Pattern pattern = Pattern.compile ( searchText.toLowerCase () );
            Matcher matcher = pattern.matcher ( districtName.toLowerCase () );
            while (matcher.find ()) {
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan ( Color.rgb ( 52, 195, 235 ) );
                spannableStringBuilder.setSpan ( foregroundColorSpan, matcher.start (), matcher.end (), Spanned.SPAN_INCLUSIVE_EXCLUSIVE );
            }
            holder.textViewDistrictName.setText ( spannableStringBuilder );
        } else {
            holder.textViewDistrictName.setText ( districtName );
        }

        holder.linearLayoutDistrictWise.setOnClickListener ( v -> {
            DistrictWiseModel districtWiseModelClickedItem = districtWiseModelArrayList.get ( position );
            Intent intentDistrictWise = new Intent ( context, EachDistrictDataActivity.class );
            intentDistrictWise.putExtra ( DISTRICT_NAME, districtWiseModelClickedItem.getDistrict () );
            intentDistrictWise.putExtra ( DISTRICT_CONFIRMED, districtWiseModelClickedItem.getConfirmed () );
            intentDistrictWise.putExtra ( DISTRICT_CONFIRMED_NEW, districtWiseModelClickedItem.getNewConfirmed () );
            intentDistrictWise.putExtra ( DISTRICT_ACTIVE, districtWiseModelClickedItem.getActive () );
            intentDistrictWise.putExtra ( DISTRICT_DEATH, districtWiseModelClickedItem.getDeceased () );
            intentDistrictWise.putExtra ( DISTRICT_DEATH_NEW, districtWiseModelClickedItem.getNewDeceased () );
            intentDistrictWise.putExtra ( DISTRICT_RECOVERED, districtWiseModelClickedItem.getRecovered () );
            intentDistrictWise.putExtra ( DISTRICT_RECOVERED_NEW, districtWiseModelClickedItem.getNewRecovered () );
            context.startActivity ( intentDistrictWise );
        } );
    }

    @Override
    public int getItemCount() {
        return districtWiseModelArrayList==null ? 0 : districtWiseModelArrayList.size ();
    }

    public void filterList(ArrayList<DistrictWiseModel> filteredList, String searchText) {
        districtWiseModelArrayList = filteredList;
        this.searchText = searchText;
        notifyDataSetChanged ();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout linearLayoutDistrictWise;
        private final TextView textViewDistrictRank;
        private final TextView textViewDistrictName;
        private final TextView textViewDistrictTotalCases;

        public MyViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super ( itemView );
            linearLayoutDistrictWise = itemView.findViewById ( R.id.linearLayoutStateWise );
            textViewDistrictRank = itemView.findViewById ( R.id.textViewStateRank );
            textViewDistrictName = itemView.findViewById ( R.id.textViewStatewiseName );
            textViewDistrictTotalCases = itemView.findViewById ( R.id.textViewStatewiseConfirmed );
        }
    }
}
