package com.mytechstudy.bitcoinpricenews;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FeedActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView headline,paragraph,cardText;
    CardView cardView;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        toolbar = findViewById(R.id.feedtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cardView = findViewById(R.id.feedcardView);
        cardText = findViewById(R.id.cardText);
        cardView.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();
        MobileAds.initialize(this,"ca-app-pub-7395760078906378~4004211891");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7395760078906378/4419268575");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        headline = findViewById(R.id.feedHeadline);
        paragraph = findViewById(R.id.feedParagraph);
        headline.setText(intent.getStringExtra("Headline"));
        paragraph.setText(intent.getStringExtra("Paragraph"));
        DatabaseReference hideCardView = FirebaseDatabase.getInstance().getReference("Feed").child("Cardview");
        hideCardView.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                cardText.setText(dataSnapshot.child("text").getValue().toString());
                if (dataSnapshot.hasChild("clik")) {
                    if ((long) dataSnapshot.child("clik").getValue() == 1) {
                        cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent chosser, intent1 = new Intent(Intent.ACTION_VIEW);
                                intent1.setData(Uri.parse(dataSnapshot.child("url").getValue().toString()));
                                chosser = Intent.createChooser(intent1, "Select To Open");
                                startActivity(chosser);

                            }
                        });
                    }
                }
                if (dataSnapshot.hasChild("visible")) {
                    if ((long) dataSnapshot.child("visible").getValue() == 1) {
                        cardView.setVisibility(View.VISIBLE);
                        cardText.setEnabled(true);
                    } else {
                        cardView.setVisibility(View.INVISIBLE);
                        cardView.setEnabled(false);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mInterstitialAd.isLoaded())
        {
            mInterstitialAd.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
