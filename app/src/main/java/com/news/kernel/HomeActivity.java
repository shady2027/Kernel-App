package com.news.kernel;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.news.kernel.Models.NewsApiResponse;
import com.news.kernel.Models.NewsHeadlines;
import com.news.kernel.Models.SharedPref;
import com.news.kernel.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements SelectListener, View.OnClickListener {

    SearchView searchView;
   RecyclerView recyclerView;
   CustomAdapter adapter;
   ProgressDialog dialog;
   Button btn_all,btn_tech,btn_business,btn_entertainment,btn_health,btn_sports,btn_science;
   SharedPref sharedPref;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = new SharedPref(this);
        if(sharedPref.loadNightModeState() == true){
            setTheme(R.style.darktheme);
        }else {
            setTheme(R.style.AppTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        searchView = findViewById(R.id.search_view);
        searchView.setQueryHint("Search news");
        searchView.setIconifiedByDefault(false);



        //for searching news
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.setTitle("Fetching news articles of "+ query);
                dialog.show();
                RequestManager manager = new RequestManager(HomeActivity.this);
                manager.getNewsHeadlines(listener, "general",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        btn_all = findViewById(R.id.btn_all);
        btn_all.setOnClickListener(this);
        btn_tech = findViewById(R.id.btn_tech);
        btn_tech.setOnClickListener(this);
        btn_business = findViewById(R.id.btn_business);
        btn_business.setOnClickListener(this);
        btn_sports = findViewById(R.id.btn_sports);
        btn_sports.setOnClickListener(this);
        btn_health = findViewById(R.id.btn_health);
        btn_health.setOnClickListener(this);
        btn_science = findViewById(R.id.btn_science);
        btn_science.setOnClickListener(this);
        btn_entertainment = findViewById(R.id.btn_entertainment);
        btn_entertainment.setOnClickListener(this);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //set home selected
        bottomNavigationView.setSelectedItemId(R.id.home);
        //perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), SettingsBottomActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        dialog = new ProgressDialog(this);
        dialog.setTitle("Fetching news article..");
        dialog.show();

        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadlines(listener, "general",null);

    }

    // to dismiss the dialog after activity is destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
    }

    //when the user presses the back button, they will be redirected to the home screen
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private final  OnFetchDataListener<NewsApiResponse> listener = new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message) {
            if(list.isEmpty()){
                Toast.makeText(HomeActivity.this, "No data found!!", Toast.LENGTH_SHORT).show();
            }else {
                showNews(list);
                dialog.dismiss();
            }
        }

        @Override
        public void onError(String message) {
            Toast.makeText(HomeActivity.this, "An Error Occurred !!!", Toast.LENGTH_SHORT).show();
        }
    };

    private void showNews(List<NewsHeadlines> list) {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        adapter = new CustomAdapter(this,list,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void OnNewsClicked(NewsHeadlines headlines) {
        startActivity(new Intent(getApplicationContext(),DetailsActivity.class).putExtra("data",headlines));
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String category = button.getText().toString();
        dialog.setTitle("Fetching news articles of " + category);
        dialog.show();
        RequestManager manager = new RequestManager(this);
        manager.getNewsHeadlines(listener, category,null);

    }
}