package com.ronakjain.newsaggregator;

import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NewsArticleDownloader {

    private static final String TAG = "NewsArticleDownloader";

    public static MainActivity mainActivity;
    private static final String ARTICLE_API_URL = "https://newsapi.org/v2/top-headlines?";
    private static final String API_KEY = "dc06a4d132dd4a86979f6a0f840ff432";
    private static String NEWS_SOURCE;
    private static RequestQueue requestQueue;

    NewsArticleDownloader(MainActivity mainActivity, String newsSourceName) {
        this.mainActivity = mainActivity;
        this.NEWS_SOURCE = newsSourceName;
    }

    public void downloadNewsArticle() {

        requestQueue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(ARTICLE_API_URL).buildUpon();
        buildURL.appendQueryParameter("sources", NEWS_SOURCE);
        buildURL.appendQueryParameter("apiKey", API_KEY);

        String finalApiUrl = buildURL.build().toString();

        Response.Listener<JSONObject> listener =
                response -> handleResults(response.toString());

        Response.ErrorListener error = error1 -> {
            Log.d(TAG, "downloadNewsArticle: ");
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(new String(error1.networkResponse.data));
                Log.d(TAG, "downloadNewsArticle: " + jsonObject);
                handleResults(null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        JsonObjectRequest jsonArrayRequest =
                new JsonObjectRequest(Request.Method.GET, finalApiUrl,
                        null, listener, error) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("User-Agent", "News-App");
                        return headers;
                    }
                };

        requestQueue.add(jsonArrayRequest);

    }

    public void handleResults(final String jsonString) {
        final ArrayList<ArticleDetails> w = parseArticlesJSON(jsonString);
        mainActivity.runOnUiThread(() -> mainActivity.addArticle(w));
    }

    private ArrayList<ArticleDetails> parseArticlesJSON(String s) {
        try {
            JSONObject articleJsonObject = new JSONObject(s);
            JSONArray newsJsonArray = articleJsonObject.getJSONArray("articles");
            ArrayList<ArticleDetails> articles = new ArrayList<>();

            for (int i = 0; i < newsJsonArray.length(); i++) {
                JSONObject articleObject = (JSONObject) newsJsonArray.get(i);
                JSONObject sourceObject = (JSONObject) articleObject.getJSONObject("source");
                String urlToImage = articleObject.getString("urlToImage");
                String description = articleObject.getString("description");
                String newsName = sourceObject.getString("name");
                String url = articleObject.getString("url");
                String publishedAt = articleObject.getString("publishedAt");
                String author = articleObject.getString("author");
                String title = articleObject.getString("title");
                String content = articleObject.getString("content");

                ArticleDetails article = new ArticleDetails(author, title, description, url, urlToImage,
                        publishedAt, content, newsName);
                articles.add(article);
            }

            return articles;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
