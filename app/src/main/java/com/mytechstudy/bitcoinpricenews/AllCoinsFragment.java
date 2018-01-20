package com.mytechstudy.bitcoinpricenews;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;


public class AllCoinsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    TextView btcprice,ethprice,bchprice,xrpprice,dashprice,ltcprice;
    FirebaseDatabase database;
    SwipeRefreshLayout swipeRefreshLayout;
    Handler handler = new Handler();
    ImageView btcup,btcdown,ethup,ethdown,bchup,bchdown,dashup,dashdown,xrpup,xrpdown,ltcup,ltcdown;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            setprice();
            handler.postDelayed(runnable,8000);
        }
    };


    public AllCoinsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_all_coins, container, false);
        btcprice = view.findViewById(R.id.bitprice);
        ethprice = view.findViewById(R.id.ethereumprice);
        bchprice = view.findViewById(R.id.bitcoincashprice);
        xrpprice = view.findViewById(R.id.rippleprice);
        dashprice = view.findViewById(R.id.dashprice);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        database = FirebaseDatabase.getInstance();
        ltcprice = view.findViewById(R.id.litecoinprice);
        btcdown = view.findViewById(R.id.allbtcdown);
        btcup = view.findViewById(R.id.allbtcup);
        ltcdown = view.findViewById(R.id.allltcdown);
        ltcup = view.findViewById(R.id.allltcup);
        dashdown = view.findViewById(R.id.alldashdown);
        dashup = view.findViewById(R.id.alldashup);
        xrpdown = view.findViewById(R.id.allxrpdown);
        xrpup = view.findViewById(R.id.allxrpup);
        bchdown = view.findViewById(R.id.allbchdown);
        bchup = view.findViewById(R.id.allbchup);
        ethdown = view.findViewById(R.id.allethdown);
        ethup = view.findViewById(R.id.allethup);
        btcup.setVisibility(View.INVISIBLE);
        btcdown.setVisibility(View.INVISIBLE);
        ethup.setVisibility(View.INVISIBLE);
        ethdown.setVisibility(View.INVISIBLE);
        dashup.setVisibility(View.INVISIBLE);
        dashdown.setVisibility(View.INVISIBLE);
        xrpup.setVisibility(View.INVISIBLE);
        xrpdown.setVisibility(View.INVISIBLE);
        ltcup.setVisibility(View.INVISIBLE);
        ltcdown.setVisibility(View.INVISIBLE);
        bchup.setVisibility(View.INVISIBLE);
        bchdown.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        handler.postDelayed(runnable,0);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    private void setprice() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("rates", Context.MODE_PRIVATE);
        String currency=sharedPreferences.getString("rate","ARS");

        priceincurrency("BTC",currency);

        priceincurrency("BCH",currency);

        priceincurrency("DASH",currency);

        priceincurrency("LTC",currency);

        priceincurrency("XRP",currency);

        priceincurrency("ETH",currency);

    }

    public void priceincurrency(final String coin, final String currency)
    {

        database.getReference("Coins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,  dataSnapshot.child(coin).getValue()+"?convert="+currency, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONObject jsonObject= response.getJSONObject(0);
                            String price = String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(jsonObject.getInt("price_"+currency.toLowerCase())));
                            int trade = jsonObject.getInt("percent_change_1h");
                            switch (coin)
                            {
                                case "BTC":
                                    btcprice.setText(price);
                                    if (trade < 0)
                                        btcdown.setVisibility(View.VISIBLE);
                                    else
                                        btcup.setVisibility(View.VISIBLE);
                                    break;
                                case "BCH":
                                    bchprice.setText(price);
                                    if (trade < 0)
                                        bchdown.setVisibility(View.VISIBLE);
                                    else
                                        bchup.setVisibility(View.VISIBLE);
                                    break;
                                case "DASH":
                                    dashprice.setText(price);
                                    if (trade < 0)
                                        dashdown.setVisibility(View.VISIBLE);
                                    else
                                        dashup.setVisibility(View.VISIBLE);
                                    break;
                                case "LTC":
                                    ltcprice.setText(price);
                                    if (trade < 0)
                                        ltcdown.setVisibility(View.VISIBLE);
                                    else
                                        ltcup.setVisibility(View.VISIBLE);
                                    break;
                                case "XRP":
                                    xrpprice.setText(price);
                                    if (trade < 0)
                                        xrpdown.setVisibility(View.VISIBLE);
                                    else
                                        xrpup.setVisibility(View.VISIBLE);
                                    break;
                                case "ETH":
                                    ethprice.setText(price);
                                    if (trade < 0)
                                        ethdown.setVisibility(View.VISIBLE);
                                    else
                                        ethup.setVisibility(View.VISIBLE);
                                    break;
                                default:
                            }
                            if (swipeRefreshLayout.isRefreshing())
                                swipeRefreshLayout.setRefreshing(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                requestQueue.add(jsonArrayRequest);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onRefresh() {
        setprice();

    }


}
