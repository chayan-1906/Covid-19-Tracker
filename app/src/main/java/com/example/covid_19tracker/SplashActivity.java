package com.example.covid_19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    private String version;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String appUrl;

    private boolean splashLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_splash );

        Objects.requireNonNull ( getSupportActionBar ( ) ).hide ();
        getWindow ().setNavigationBarColor ( ContextCompat.getColor ( SplashActivity.this, R.color.primaryLightColor ) );

        checkForUpdate();
    }

    private void checkForUpdate() {
        try {
            version = this.getPackageManager ().getPackageInfo ( getPackageName (), 0 ).versionName;
            firebaseDatabase = FirebaseDatabase.getInstance ();
            databaseReference = firebaseDatabase.getReference ( "Version" ).child ( "versionNumber" );
            databaseReference.addListenerForSingleValueEvent ( new ValueEventListener ( ) {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    String versionName = (String) snapshot.getValue (  );
                    if (!versionName.equals ( version )) {
                        AlertDialog.Builder alertBuilderObj = new AlertDialog.Builder( SplashActivity.this);
                        alertBuilderObj.setTitle ( "New Version Available!" );
                        alertBuilderObj.setMessage ( "Please update app to get latest version for continuous use" );
                        alertBuilderObj.setPositiveButton ( "UPDATE", (dialog, which) -> {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance ( ).getReference ( "Version" ).child ( "appUrl" );
                            databaseReference.addListenerForSingleValueEvent ( new ValueEventListener ( ) {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot1) {
                                    appUrl  = snapshot1.getValue (  ).toString ();
                                    startActivity ( new Intent ( Intent.ACTION_VIEW, Uri.parse ( appUrl ) ) );
                                    finish ();
                                }
                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            } );
                        } );
                        alertBuilderObj.setNegativeButton ( "EXIT", (dialog, which) -> finish () );
                        alertBuilderObj.setCancelable ( false );
//                        alertBuilderObj.setCanceledOnTouchOutside ( false );
                        alertBuilderObj.show ();

                    } else {
                        showSplashScreen();
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            } );
        } catch (Exception e) {

        }
    }

    private void showSplashScreen() {
        if (!splashLoaded) {
            setContentView(R.layout.activity_splash);
            int secondsDelayed = 1;
            new Handler ().postDelayed( () -> {
                Intent intent = new Intent (getApplicationContext (), MainActivity.class);
                startActivity(intent);
                finish();
            },secondsDelayed * 1700);
            splashLoaded = true;
        } else {
            Intent goToMainActivity = new Intent ( SplashActivity.this, MainActivity.class );
            goToMainActivity.setFlags ( Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
            startActivity ( goToMainActivity );
            finish ();
        }
    }
}
