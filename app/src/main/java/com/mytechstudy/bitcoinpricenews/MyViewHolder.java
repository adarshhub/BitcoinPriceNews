package com.mytechstudy.bitcoinpricenews;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



public class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView image;
    TextView headline,paragraph;
    public MyViewHolder(View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.newsImage);
        headline = itemView.findViewById(R.id.newsHeadline);
        paragraph = itemView.findViewById(R.id.newsParagraph);
    }
}
