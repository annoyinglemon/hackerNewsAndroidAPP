package com.example.lemon.hackernews;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TOP_STORIES = "https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty";
    private ArrayList<Long> topStoryIds;

    private SwipeRefreshLayout srlNews;
    private RecyclerView rvNews;
    private NewsAdapter mAdapter = new NewsAdapter();
    private ContentLoadingProgressBar pbNews;
    private TextView tvNoNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        srlNews = (SwipeRefreshLayout) findViewById(R.id.srlNews);
        rvNews = (RecyclerView) findViewById(R.id.rvNews);
        pbNews = (ContentLoadingProgressBar) findViewById(R.id.pbNews);
        tvNoNetwork = (TextView) findViewById(R.id.tvNoNetwork);

        srlNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.clear();
                new InitialNewsFetch().execute(TOP_STORIES);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvNews.setLayoutManager(mLayoutManager);
        rvNews.setItemAnimator(new DefaultItemAnimator());
        rvNews.setAdapter(mAdapter);
        new InitialNewsFetch().execute(TOP_STORIES);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.menu_story);

        //Here, you get access to the view of your item, in this case, the layout of the item has a FrameLayout as root view but you can change it to whatever you use
        Spinner ddStory = (Spinner)item.getActionView();

        String[] items = new String[] { "Chai Latte", "Green Tea", "Black Tea" };

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_dropdown_item, items);

        ddStory.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View item = LayoutInflater.from(MainActivity.this).inflate(R.layout.dropdown_item, null);
                TextView tvStory = (TextView) item.findViewById(R.id.tvStory);
                switch (position){
                    case 0:
                        tvStory.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_top,0,0,0);
                        tvStory.setText("TOP");
                        break;
                    case 1:
                        tvStory.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_new,0,0,0);
                        tvStory.setText("NEW");
                        break;
                    case 2:
                        tvStory.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_best,0,0,0);
                        tvStory.setText("BEST");
                        break;
                }
                return item;
            }
        });
        //Then you access to your control by finding it in the rootView
//        Spinner ddStory = (Spinner) rootView.findViewById(R.id.ddStory);
        //And from here you can do whatever you want with your control
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_story) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class InitialNewsFetch extends AsyncTask<String, Void, Void> {


        @Override
        protected void onPreExecute() {
            tvNoNetwork.setVisibility(View.GONE);
            srlNews.setVisibility(View.GONE);
            if(!srlNews.isRefreshing())
                pbNews.setVisibility(View.VISIBLE);
            topStoryIds = new ArrayList<>();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... urls) {

            try {
                JSONArray jsonArray = new JSONArray(HttpHandler.makeServiceCall(urls[0]));
                for(int i=0;i<jsonArray.length();i++){
                    topStoryIds.add(jsonArray.getLong(i));
                    Log.i("News ID:", Long.toString(jsonArray.getLong(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(srlNews.isRefreshing())
                srlNews.setRefreshing(false);
            if(topStoryIds!=null&&topStoryIds.size()>0) {
                for(int i=0; i<20; i++){
                    new GetArticle().execute(topStoryIds.get(i));
                }
            }else{
                pbNews.setVisibility(View.GONE);
                tvNoNetwork.setVisibility(View.VISIBLE);
            }

        }
    }

    class GetArticle extends AsyncTask<Long, Void, NewsObject>{

        //        https://hacker-news.firebaseio.com/v0/item/8863.json?print=pretty

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected NewsObject doInBackground(Long... params) {
            try {
                String articleURL = "https://hacker-news.firebaseio.com/v0/item/"+ params[0] +".json?print=pretty";
                JSONObject jsonObject = new JSONObject(HttpHandler.makeServiceCall(articleURL));
                return new NewsObject(jsonObject.getLong("id"),jsonObject.getString("title"),jsonObject.getString("url"), jsonObject.getString("by"), jsonObject.getInt("score"), jsonObject.getInt("descendants"), jsonObject.getLong("time"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(NewsObject newsObject) {
            super.onPostExecute(newsObject);
            if(newsObject!=null) {
                pbNews.setVisibility(View.GONE);
                mAdapter.addNews(newsObject);
                srlNews.setVisibility(View.VISIBLE);
            }else{
                pbNews.setVisibility(View.GONE);
                if(mAdapter.isEmpty())
                    tvNoNetwork.setVisibility(View.VISIBLE);
            }

        }
    }

}
