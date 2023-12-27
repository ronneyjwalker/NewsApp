package com.ronakjain.newsaggregator;

import android.net.Uri;

import org.json.JSONArray;

import com.android.volley.AuthFailureError;

import org.json.JSONObject;

import java.util.Map;
import java.util.HashMap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

public class NewsSourceDownloader {
    private static RequestQueue requestQueue;
    public static MainActivity mainActivity;
    private static final String NEWS_SOURCE_API_URL = "https://newsapi.org/v2/sources?";
    private static final String API_KEY = "dc06a4d132dd4a86979f6a0f840ff432";

    public static void downloadNewsSource(MainActivity main) {
        mainActivity = main;
        requestQueue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(NEWS_SOURCE_API_URL).buildUpon();
        buildURL.appendQueryParameter("apiKey", API_KEY);

        String finalApiUrl = buildURL.build().toString();

        Response.Listener<JSONObject> listener = response -> parseTopics(response.toString());

        Response.ErrorListener error = error1 -> mainActivity.ErrorDownload();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, finalApiUrl, null, listener, error) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "News-App");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private static void parseTopics(String s) {
        try {
            MainActivity.categoryList.clear();
            MainActivity.categoryList.add("All");
            JSONObject jsonObject = new JSONObject(s);
            JSONArray sourcesJsonArray = jsonObject.getJSONArray("sources");
            for (int i = 0; i < sourcesJsonArray.length(); i++) {
                JSONObject sourceObject = sourcesJsonArray.getJSONObject(i);

                String topic = sourceObject.getString("category");

                MainActivity.totalItems.add(new NewsSource(sourceObject.getString("id"), sourceObject.getString("name"),
                        sourceObject.getString("category"), sourceObject.getString("url")));

                MainActivity.addTopic(topic);
            }
            mainActivity.loadDrawer(true);
            mainActivity.makeMenu();
            mainActivity.changeTitle(sourcesJsonArray.length());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
