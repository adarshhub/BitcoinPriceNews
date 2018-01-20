package com.mytechstudy.bitcoinpricenews;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class EthereumFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    FirebaseDatabase database;
    TextView ethprice;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageView ethup,ethdown;
    RecyclerView recyclerView;

    public EthereumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ethereum, container, false);
        ethprice = view.findViewById(R.id.ethprice);
        database = FirebaseDatabase.getInstance();
        swipeRefreshLayout = view.findViewById(R.id.ethswipe);
        recyclerView = view.findViewById(R.id.Ethrecycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        ethup = view.findViewById(R.id.ethup);
        ethdown = view.findViewById(R.id.ethdown);
        ethdown.setVisibility(View.INVISIBLE);
        ethup.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setprice();
        initrecyclerView();
    }

    private void initrecyclerView() {

        DatabaseReference newsRef = FirebaseDatabase.getInstance().getReference("Feed").child("Ethereum");

        newsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<News> news = new ArrayList<>();
                for (DataSnapshot document : dataSnapshot.getChildren()) {
                    News data;
                    data = document.getValue(News.class);
                    news.add(data);
                }
                class EthNews extends RecyclerView.Adapter<MyViewHolder>{
                    List<News> list= new ArrayList<>();

                    EthNews(List<News> list)
                    {
                        this.list = list;

                    }

                    @Override
                    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.newsrow,parent,false);
                        return new MyViewHolder(view);
                    }

                    @Override
                    public void onBindViewHolder(final MyViewHolder holder, int position) {
                        holder.paragraph.setText(list.get(position).getNewsParagraph());
                        holder.headline.setText(list.get(position).getNewsHeadline());
                        setImage(holder,list.get(position).getNewsImage());
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent newsIntent = new Intent(getContext(),FeedActivity.class);
                                newsIntent.putExtra("Headline",list.get(holder.getAdapterPosition()).getNewsHeadline());
                                newsIntent.putExtra("Paragraph",list.get(holder.getAdapterPosition()).getNewsParagraph());
                                startActivity(newsIntent);

                            }
                        });

                    }

                    private void setImage(final MyViewHolder holder, String newsImage) {
                        StorageReference sRef = FirebaseStorage.getInstance().getReference().child("Images/"+newsImage+".jpg");
                        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getContext()).load(uri.toString()).into(holder.image);
                            }
                        });
                    }


                    @Override
                    public int getItemCount() {
                        return list.size();
                    }
                }
                EthNews ethNews = new EthNews(news);
                recyclerView.setAdapter(ethNews);


            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setprice() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("rates", Context.MODE_PRIVATE);
        String currency=sharedPreferences.getString("rate","ARS");
        priceincurrency(currency);

    }

    public void priceincurrency(final String currency)
    {
        database.getReference("Coins").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,  dataSnapshot.child("ETH").getValue()+"?convert="+currency, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONObject jsonObject= response.getJSONObject(0);
                            String price = String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(jsonObject.getInt("price_"+currency.toLowerCase())));
                            ethprice.setText(price);
                            if (jsonObject.getInt("percent_change_1h")<0)
                                ethdown.setVisibility(View.VISIBLE);
                            else
                                ethup.setVisibility(View.VISIBLE);
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