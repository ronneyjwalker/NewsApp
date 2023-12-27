package com.ronakjain.newsaggregator;

import android.view.View;

import androidx.annotation.NonNull;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ArticleViewHolder extends RecyclerView.ViewHolder {

    ImageView newsPicture;
    TextView pageNumber, newsDate, newsDescription, newsWriter, newsHeadline;

    public ArticleViewHolder(@NonNull View itemView) {
        super(itemView);
        newsHeadline = itemView.findViewById(R.id.newsHeadline);
        newsDescription = itemView.findViewById(R.id.newsDescription);
        newsDate = itemView.findViewById(R.id.newsDate);
        newsPicture = itemView.findViewById(R.id.newsImage);
        pageNumber = itemView.findViewById(R.id.newsPageNo);
        newsWriter = itemView.findViewById(R.id.newsWriter);
    }

}
