package com.ronakjain.newsaggregator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private String[] colorsArray = {"#ad152e", "#d68c45", "#f4d35e", "#60785c", "#77a0a9", "#6c596e", "#ff8c9f"};
    private ListView drawerMainListView;

    private ArticleAdapter articleAdapter;

    static final ArrayList<String> categoryList = new ArrayList<>();
    static final ArrayList<NewsSource> totalItems = new ArrayList<>();
    private ArrayList<ArticleDetails> articleDetailsList = new ArrayList<>();
    static final ArrayList<NewsSource> selectedNewsSourceList = new ArrayList<>();

    private Menu menuOptions;

    private String selectedTitle;
    private DrawerLayout drawerMainLayout;
    private ActionBarDrawerToggle drawerMainToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerMainLayout = findViewById(R.id.main_layout_drawer);
        drawerMainListView = findViewById(R.id.drawerItems);

        drawerMainListView.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_layout_item, totalItems));

        drawerMainListView.setOnItemClickListener((parent, view, position, id) -> {
            selectItem(position);
            drawerMainLayout.closeDrawer(drawerMainListView);
        });

        drawerMainToggle = new ActionBarDrawerToggle(this, drawerMainLayout, R.string.openDrawer, R.string.closeDrawer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }


        articleAdapter = new ArticleAdapter(this, articleDetailsList);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(articleAdapter);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerMainToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menuOptions = menu;
        NewsSourceDownloader.downloadNewsSource(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerMainToggle.onOptionsItemSelected(item)) {
            return true;
        }

        for (int i = 0; i < menuOptions.size(); i++) {
            if (menuOptions.getItem(i).getTitle().equals(item.getTitle())) {
                reloadSources(categoryList.get(i));
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerMainToggle.onConfigurationChanged(newConfig);
    }

    public void loadDrawer(boolean isAllSelected) {
        if (isAllSelected) {
            selectedNewsSourceList.addAll(totalItems);
        }
        drawerMainListView.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_layout_item, selectedNewsSourceList));
        drawerMainLayout.setScrimColor(Color.WHITE);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void selectItem(int position) {
        viewPager.setBackground(null);
        selectedTitle = selectedNewsSourceList.get(position).getName();
        NewsArticleDownloader newsArticleDownloader = new NewsArticleDownloader(this, selectedNewsSourceList.get(position).getIdentification());
        newsArticleDownloader.downloadNewsArticle();

        drawerMainLayout.closeDrawer(drawerMainListView);
    }

    public void ErrorDownload() {
        Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
    }

    public static void addTopic(String t) {
        categoryList.add(t);
    }

    public void addArticle(ArrayList<ArticleDetails> w) {
        articleDetailsList.clear();
        articleDetailsList.addAll(w);
        articleAdapter.notifyDataSetChanged();
        setTitle(selectedTitle);
        viewPager.setCurrentItem(0);
    }

    private void reloadSources(String newsCategory) {
        selectedNewsSourceList.clear();
        if (newsCategory.equals("All")) {
            selectedNewsSourceList.addAll(totalItems);
        } else {
            for (int i = 0; i < totalItems.size(); i++) {
                if (totalItems.get(i).getType().equals(newsCategory)) {
                    selectedNewsSourceList.add(totalItems.get(i));
                }
            }
        }
        changeTitle(selectedNewsSourceList.size());
        loadDrawer(false);
    }

    public void changeTitle(int num) {
        String temp = new StringBuilder().append(getString(R.string.app_name)).append(" (").append(num).append(")").toString();
        setTitle(temp);
    }

    public void makeMenu() {
        menuOptions.clear();

        Set<String> set = new LinkedHashSet<String>();

        set.addAll(categoryList);
        categoryList.clear();
        categoryList.addAll(set);
        for (int i = 0; i < categoryList.size(); i++) {
            menuOptions.add(categoryList.get(i));
            if (i > 0) {
                int newIndex = i - 1;
                MenuItem menuItem = menuOptions.getItem(i);
                SpannableString spanString = new SpannableString(menuItem.getTitle().toString());
                spanString.setSpan(new ForegroundColorSpan(Color.parseColor(colorsArray[newIndex])), 0, spanString.length(), 0);
                menuItem.setTitle(spanString);
            }
        }
        hideKeyboard();
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) return;

        View view = getCurrentFocus();

        if (view == null) view = new View(this);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}