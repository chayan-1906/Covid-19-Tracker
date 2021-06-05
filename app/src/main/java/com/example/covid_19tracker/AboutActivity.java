package com.example.covid_19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.Objects;

public class AboutActivity extends AppCompatActivity {

    private ImageView imageViewYouTube;
    private ImageView imageViewFacebook;
    private ImageView imageViewInstagram;
    private ImageView imageViewLinkedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_about );

        Objects.requireNonNull ( getSupportActionBar ( ) ).setTitle ( "About" );
        getSupportActionBar ().setDisplayShowHomeEnabled ( true );
        getSupportActionBar ().setDisplayHomeAsUpEnabled ( true );

        imageViewYouTube = findViewById ( R.id.imageViewYouTube );
        imageViewFacebook = findViewById ( R.id.imageViewFacebook );
        imageViewInstagram = findViewById ( R.id.imageViewInstagram );
        imageViewLinkedIn = findViewById ( R.id.imageViewLinkedIn );
        
        imageViewYouTube.setOnClickListener ( v -> startActivity ( new Intent ( Intent.ACTION_VIEW, Uri.parse ( "https://www.youtube.com/channel/UCbuhjYDMON-hE4uGD1BAMSQ" ) ) ) );

        imageViewFacebook.setOnClickListener ( v -> startActivity ( new Intent ( Intent.ACTION_VIEW, Uri.parse ( "https://www.facebook.com/padmanabha.das.94" ) ) ) );

        imageViewInstagram.setOnClickListener ( v -> startActivity ( new Intent ( Intent.ACTION_VIEW, Uri.parse ( "https://www.instagram.com/pdas_1906/" ) ) ) );

        imageViewLinkedIn.setOnClickListener ( v -> startActivity ( new Intent ( Intent.ACTION_VIEW, Uri.parse ( "https://www.linkedin.com/in/padmanabha-das-59bb2019b/" ) ) ) );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId ()==android.R.id.home)
            finish ();
        return super.onOptionsItemSelected ( item );
    }
}
