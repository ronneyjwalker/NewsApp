package com.ronakjain.newsaggregator;

import java.io.InputStream;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.text.method.ScrollingMovementMethod;

import androidx.annotation.NonNull;

import android.view.ViewGroup;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {
    private final ArrayList<ArticleDetails> articleDetailsList;
    private final MainActivity mainActivity;

    public ArticleAdapter(MainActivity mainActivity, ArrayList<ArticleDetails> articles) {
        this.mainActivity = mainActivity;
        this.articleDetailsList = articles;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder articleViewHolder, int pos) {
        ArticleDetails articleDetail = articleDetailsList.get(pos);
        articleViewHolder.newsHeadline.setText(articleDetail.getNewsTitle());
        articleViewHolder.newsDate.setText(articleDetail.dateTimeFormatter());
        articleViewHolder.newsWriter.setText(articleDetail.getNewsWriter());
        articleViewHolder.newsPicture.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        articleViewHolder.newsPicture.setOnClickListener(var -> imageClick(articleDetail.getNewsLink()));
        articleViewHolder.newsDescription.setMovementMethod(new ScrollingMovementMethod());
        articleViewHolder.newsDescription.setText(articleDetail.getNewsDescription());
        articleViewHolder.newsHeadline.setOnClickListener(var -> imageClick(articleDetail.getNewsLink()));
        articleViewHolder.newsDescription.setOnClickListener(var -> imageClick(articleDetail.getNewsLink()));
        int articlePageNo = pos + 1;
        String slide = new StringBuilder().append(articlePageNo).append(" out of ").append(getItemCount()).toString();
        articleViewHolder.pageNumber.setText(slide);

        if (!articleDetail.getImageLink().equals("null")) {
            articleViewHolder.newsPicture.setImageResource(R.drawable.loading);
            new Image(articleViewHolder.newsPicture, mainActivity).execute(articleDetail.getImageLink());
        }
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_layout, parent, false));
    }

    @Override
    public int getItemCount() {
        return articleDetailsList.size();
    }

    private void imageClick(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        mainActivity.startActivity(browserIntent);
    }

    private class Image extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        MainActivity mainActivity;

        public Image(ImageView imageView, MainActivity mainActivity) {
            this.imageView = imageView;
            this.mainActivity = mainActivity;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap icon;
            try {
                InputStream in = new java.net.URL(strings[0]).openStream();
                icon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                icon = null;
                e.printStackTrace();
            }
            return icon;
        }

        protected void onPostExecute(Bitmap result) {
            if (result == null) {
                imageView.setImageResource(mainActivity.getResources().getIdentifier("brokenimage", "drawable", mainActivity.getPackageName()));
            } else {
                imageView.setImageBitmap(result);
            }
        }
    }
}
