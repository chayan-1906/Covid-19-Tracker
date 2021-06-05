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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.covid_19tracker.Constants.STATE_ACTIVE;
import static com.example.covid_19tracker.Constants.STATE_CONFIRMED;
import static com.example.covid_19tracker.Constants.STATE_CONFIRMED_NEW;
import static com.example.covid_19tracker.Constants.STATE_DEATH;
import static com.example.covid_19tracker.Constants.STATE_DEATH_NEW;
import static com.example.covid_19tracker.Constants.STATE_LAST_UPDATE;
import static com.example.covid_19tracker.Constants.STATE_NAME;
import static com.example.covid_19tracker.Constants.STATE_RECOVERED;
import static com.example.covid_19tracker.Constants.STATE_RECOVERED_NEW;

public class StateWiseAdapter extends RecyclerView.Adapter<StateWiseAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<StateWiseModel> arrayListStateWiseModel;
    private String searchText = "";
    private SpannableStringBuilder spannableStringBuilder;

    public StateWiseAdapter(Context context, ArrayList<StateWiseModel> arrayList) {
        this.context = context;
        this.arrayListStateWiseModel = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from ( context ).inflate ( R.layout.layout_state_wise, parent, false );
        return new ViewHolder ( view );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StateWiseAdapter.ViewHolder holder, int position) {
        StateWiseModel currentItem = arrayListStateWiseModel.get ( position );
        String stateName = currentItem.getState ();
        String stateTotal = currentItem.getConfirmed ();
        String stateRank = String.valueOf ( position + 1 );
        int stateIntTotal = Integer.parseInt ( stateTotal );
        holder.textViewStateRank.setText( stateRank + "." );
        holder.textViewStatewiseConfirmed.setText ( NumberFormat.getInstance ().format ( stateIntTotal ) );
        if (searchText.length () > 0) {
            spannableStringBuilder = new SpannableStringBuilder ( stateName );
            Pattern pattern = Pattern.compile ( searchText.toLowerCase () );
            Matcher matcher = pattern.matcher ( stateName.toLowerCase () );
            while (matcher.find ()) {
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan ( Color.rgb ( 52, 195, 235 ) );
                spannableStringBuilder.setSpan ( foregroundColorSpan, matcher.start (), matcher.end (), Spanned.SPAN_INCLUSIVE_EXCLUSIVE );
            }
            holder.textViewStatewiseName.setText ( spannableStringBuilder );
        } else {
            holder.textViewStatewiseName.setText ( stateName );
        }

        holder.linearLayoutStateWise.setOnClickListener ( v -> {
            StateWiseModel clickedItem = arrayListStateWiseModel.get ( position );
            Intent intentPerState = new Intent ( context, EachStateDataActivity.class );
            intentPerState.putExtra ( STATE_NAME, clickedItem.getState () );
            intentPerState.putExtra ( STATE_CONFIRMED, clickedItem.getConfirmed() );
            intentPerState.putExtra ( STATE_CONFIRMED_NEW, clickedItem.getConfirmedNew () );
            intentPerState.putExtra ( STATE_ACTIVE, clickedItem.getActive() );
            intentPerState.putExtra ( STATE_DEATH, clickedItem.getDeath() );
            intentPerState.putExtra ( STATE_DEATH_NEW, clickedItem.getDeathNew() );
            intentPerState.putExtra ( STATE_RECOVERED, clickedItem.getRecovered() );
            intentPerState.putExtra ( STATE_RECOVERED_NEW, clickedItem.getRecoveredNew() );
            intentPerState.putExtra ( STATE_LAST_UPDATE, clickedItem.getLastUpdate() );
            context.startActivity ( intentPerState );
        } );
    }

    public void filterList(ArrayList<StateWiseModel> filteredList, String searchText) {
        arrayListStateWiseModel = filteredList;
        this.searchText = searchText;
        notifyDataSetChanged ();
    }

    @Override
    public int getItemCount() {
        return arrayListStateWiseModel==null ? 0 : arrayListStateWiseModel.size ();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout linearLayoutStateWise;
        private final TextView textViewStateRank;
        private final TextView textViewStatewiseName;
        private final TextView textViewStatewiseConfirmed;

        public ViewHolder(@NonNull View itemView) {
            super ( itemView );
            linearLayoutStateWise = itemView.findViewById ( R.id.linearLayoutStateWise );
            textViewStateRank = itemView.findViewById ( R.id.textViewStateRank );
            textViewStatewiseName = itemView.findViewById ( R.id.textViewStatewiseName );
            textViewStatewiseConfirmed = itemView.findViewById ( R.id.textViewStatewiseConfirmed );
        }
    }
}
