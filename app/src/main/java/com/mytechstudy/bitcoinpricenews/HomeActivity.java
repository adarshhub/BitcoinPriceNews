package com.mytechstudy.bitcoinpricenews;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    ViewPager viewPager;
    TabLayout tabLayout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    android.support.v7.widget.Toolbar toolbar;
    Spinner spinner;
    List<String> rates = new ArrayList<>();
    FirebaseDatabase database;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;
    int c;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MobileAds.initialize(this,"ca-app-pub-7395760078906378~4004211891");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        viewPager = findViewById(R.id.homeviewpager);
        tabLayout = findViewById(R.id.hometablayout);
        c=0;
        FragmantAdapter fragmantAdapter = new FragmantAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmantAdapter);
        tabLayout.setupWithViewPager(viewPager,true);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navview);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.maindrawer);
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerToggle.syncState();
        spinner = findViewById(R.id.toolbarspinner);
        getSupportActionBar().setTitle("Bitcoin price");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        database =FirebaseDatabase.getInstance();
        database.getReference("Rates").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren())
                {
                    rates.add(data.getKey());
                }

                ArrayAdapter<String > adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, rates);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                sharedPreferences = getSharedPreferences("rates", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                String tempcurrency = sharedPreferences.getString("rate","0");
                if (tempcurrency.equalsIgnoreCase("0")) {
                    editor.putString("rate", "ARS");
                    editor.apply();
                }
                else
                {
                    ArrayAdapter tempadapter = (ArrayAdapter) spinner.getAdapter();
                    spinner.setSelection(tempadapter.getPosition(tempcurrency));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String newprice = adapterView.getItemAtPosition(i).toString();
        editor.putString("rate",newprice);
        editor.apply();
        Toast.makeText(getBaseContext(),newprice,Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.nav_draw_btc:
                viewPager.setCurrentItem(1);
                break;
            case R.id.nav_draw_home:
                viewPager.setCurrentItem(0);
                break;
            case R.id.nav_draw_ethereum:
                viewPager.setCurrentItem(2);
                break;
            case R.id.nav_draw_dash:
                viewPager.setCurrentItem(3);
                break;
            case R.id.nav_draw_ripple:
                viewPager.setCurrentItem(4);
                break;
            case R.id.nav_draw_litecoin:
                viewPager.setCurrentItem(6);
                break;
            case R.id.nav_draw_bch:
                viewPager.setCurrentItem(5);
        }
        return true;
    }
}
