package com.technophile.lazyloading;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RvImageGridADP.OnLoadMoreListener, SearchView.OnQueryTextListener {


    private static final String SAVE_ALL_PICS = "pics";
    private static final String SAVE_LAST_PAGE_LOADED = "page";
    private static final String SAVE_TOTAL_ITEMS_LOADED = "totalItemsLoaded";
    private static final String SAVE_SEARCH_QUERY = "search";
    private static final int MAX_PICS = 30;
    private int pageCount = 1, totalItemsLoaded = 0;
    private RvImageGridADP rvImageGridADP;
    private Call<FlickrModel> flickrModelCallback;
    private ProgressBar progress_bar;
    private String oldSearchQuery, newSearchQuery;
    private ArrayList<FlickrModel.Photo> picturesArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rv_img_grid = (RecyclerView) findViewById(R.id.rv_img_grid);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rv_img_grid.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        } else {
            rv_img_grid.setLayoutManager(new GridLayoutManager(MainActivity.this, 4));
        }
        rvImageGridADP = new RvImageGridADP(MainActivity.this, rv_img_grid, MainActivity.this);
        if (savedInstanceState != null) {
            pageCount = savedInstanceState.getInt(SAVE_LAST_PAGE_LOADED);
            totalItemsLoaded = savedInstanceState.getInt(SAVE_TOTAL_ITEMS_LOADED);
            oldSearchQuery = savedInstanceState.getString(SAVE_SEARCH_QUERY);
            picturesArrayList = (ArrayList<FlickrModel.Photo>) savedInstanceState.getSerializable(SAVE_ALL_PICS);
            if (totalItemsLoaded >= MAX_PICS) {
                rvImageGridADP.setLoadNoMore();
            }
            rvImageGridADP.addData(picturesArrayList);
        }
        rv_img_grid.setAdapter(rvImageGridADP);

        if (totalItemsLoaded < 5) {
            getFlickrPictures();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_toolbar, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            if (searchManager != null) {
                searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
            }
            searchView.setOnQueryTextListener(MainActivity.this);
            if (!TextUtils.isEmpty(oldSearchQuery)) {
                searchItem.expandActionView();
                searchView.setQuery(oldSearchQuery, true);
                searchView.requestFocus();
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SAVE_ALL_PICS, picturesArrayList);
        outState.putInt(SAVE_LAST_PAGE_LOADED, pageCount);
        outState.putString(SAVE_SEARCH_QUERY, newSearchQuery);
        outState.putInt(SAVE_TOTAL_ITEMS_LOADED, picturesArrayList.size());
        if (progress_bar.getVisibility() == View.VISIBLE) {
            flickrModelCallback.cancel();
        }
        super.onSaveInstanceState(outState);
    }

    private void getFlickrPictures() {
        progress_bar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APiInterface.REST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APiInterface service = retrofit.create(APiInterface.class);
        String API_KEY = "9f89151d82e427401680cd48dd2d5cf5";
        flickrModelCallback = service.getPictures(API_KEY, "5", String.valueOf(pageCount), "json", "1");
        flickrModelCallback.enqueue(new Callback<FlickrModel>() {
            @Override
            public void onResponse(@NonNull Call<FlickrModel> call, @NonNull Response<FlickrModel> response) {
                progress_bar.setVisibility(View.GONE);
                FlickrModel responseModel = response.body();
                if (responseModel != null && responseModel.getStat().equals("ok")) {
                    if (!responseModel.getPhotos().getPhoto().isEmpty()) {
                        picturesArrayList.addAll(responseModel.getPhotos().getPhoto());
                        pageCount++;
                        if (picturesArrayList.size() >= MAX_PICS) {
                            rvImageGridADP.setLoadNoMore();
                        }
                        rvImageGridADP.addData(picturesArrayList);
                        rvImageGridADP.setLoaded();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<FlickrModel> call, @NonNull Throwable t) {
                progress_bar.setVisibility(View.GONE);
                if (!isNetworkAvailable()) {
                    Toast.makeText(MainActivity.this, "No Internet Connectivity", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onLoadMore() {
        getFlickrPictures();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        newSearchQuery = newText;
        progress_bar.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(newText)) {
            ArrayList<FlickrModel.Photo> tempArray = new ArrayList<>();
            for (FlickrModel.Photo photo : picturesArrayList) {
                String title = photo.getTitle();
                if (!TextUtils.isEmpty(title) && title.toLowerCase().contains(newText.toLowerCase())) {
                    tempArray.add(photo);
                }
            }
            rvImageGridADP.preventLoading(true);
            rvImageGridADP.addData(tempArray);
        } else {
            rvImageGridADP.preventLoading(false);
            rvImageGridADP.addData(picturesArrayList);
        }
        progress_bar.setVisibility(View.GONE);
        return false;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
