package com.codedev.demo;



import static com.codedev.demo.SplashActivity.catList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;



public class categoryActivity extends AppCompatActivity {

    private GridView catGrid;

    private FirebaseAuth auth;
    private AdView mAdView;
    private ImageView img1,img2,img3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                super.onAdClicked();


            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                super.onAdFailedToLoad(adError);
                mAdView.loadAd(adRequest);
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                super.onAdLoaded();

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                super.onAdOpened();
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Categories");


        catGrid = findViewById(R.id.catGridview);

        CatGridAdapter adapter = new CatGridAdapter(catList);
        catGrid.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //process your onClick here
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(categoryActivity.this,MainActivity.class));

            categoryActivity.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
  @Override
    public void onBackPressed(){
      new AlertDialog.Builder(this)
              .setTitle("Really Exit?")
              .setMessage("Are you sure you want to exit?")
              .setNegativeButton(android.R.string.no, null)
              .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                  public void onClick(DialogInterface arg0, int arg1) {
                      categoryActivity.super.onBackPressed();
                      Intent a =new Intent(Intent.ACTION_MAIN);
                      a.addCategory(Intent.CATEGORY_HOME);
                      a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                      startActivity(a);
                  }
              }).create().show();
  }



}