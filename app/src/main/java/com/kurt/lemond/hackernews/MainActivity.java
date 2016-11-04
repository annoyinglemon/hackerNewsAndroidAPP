package com.kurt.lemond.hackernews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements WebViewFragment.OnFragmentInteractionListener {

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static final String TOP_STORIES = "https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty";
    public static final String NEW_STORIES = "https://hacker-news.firebaseio.com/v0/newstories.json?print=pretty";
    public static final String BEST_STORIES = "https://hacker-news.firebaseio.com/v0/beststories.json?print=pretty";
    public static final String SAVED_STORIES = "saved_stories";


    private ArrayList<Long> topStoryIds = new ArrayList<>();
    private ArrayList<Long> newStoryIds = new ArrayList<>();
    private ArrayList<Long> bestStoryIds = new ArrayList<>();

    /**
     * code of last loaded story
     */
    private Long lastLoadedStoryCode_top;
    private Long lastLoadedStoryCode_new;
    private Long lastLoadedStoryCode_best;
    /**
     * code of last unloaded story that about to be loaded
     */
    private Long lastUnLoadedStoryCode_top;
    private Long lastUnLoadedStoryCode_new;
    private Long lastUnLoadedStoryCode_best;
    /**
     * index of last loaded story in topStoryIds arraylist
     */
    private int loadedStoryIndex_top = 0;
    private int loadedStoryIndex_new = 0;
    private int loadedStoryIndex_best = 0;
    /**
     * for checking if loading happens
     */
    private boolean mIsLoadingArticle_top = false;
    private boolean mIsLoadingArticle_new = false;
    private boolean mIsLoadingArticle_best = false;
    /**
     * for checking if all articles are loaded
     */
    private boolean isDoneLoadingAll_top = false;
    private boolean isDoneLoadingAll_new = false;
    private boolean isDoneLoadingAll_best = false;

    /**
     * for checking if an article is clicked
     */
    boolean isAnArticleClicked = false;
    /**
     * for checking if bottom sheet is up
     */
    boolean slid_up = false;

    /**
     * variable for storing which type of story should display
     */
    private String storyType = TOP_STORIES;

    /**
     * variable for storing the clicked news object
     */
    private String mClickedArticleType = TOP_STORIES;

    private boolean isGetTopListRan = false;
    private boolean isGetNewListRan = false;
    private boolean isGetBestListRan = false;

    private WebViewFragment mFragment;

    private RelativeLayout rlvTop, rlvNew, rlvBest, rlvSaved;
    private SwipeRefreshLayout srlTop, srlNew, srlBest, srlSaved;
    private RecyclerView rvTop, rvNew, rvBest, rvSaved;
    private ContentLoadingProgressBar pbTop, pbNew, pbBest, pbSaved;
    private TextView tvErrorTop, tvErrorNew, tvErrorBest, tvErrorSaved;

    private NewsAdapter topAdapter, newAdapter, bestAdapter, savedAdapter;

    private LinearLayoutManager topLayoutManager, newLayoutManager, bestLayoutManager, savedLayoutManager;

    private BottomSheetBehavior bottomSheetBehavior;
    private FrameLayout bottom_sheet;
    private Animation slide_up, slide_down;
    private Spinner ddStory;

    private boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Esphimere-Light.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.ic_hacker_news);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hacker_news);
        getSupportActionBar().setTitle("Hacker News");

        /** one relative layout and child views for each news category: Top, New, Best and Saved **/
        //TOP
        rlvTop = (RelativeLayout) findViewById(R.id.rlvTop);
        srlTop = (SwipeRefreshLayout) findViewById(R.id.srlTop);
        rvTop = (RecyclerView) findViewById(R.id.rvTop);
        pbTop = (ContentLoadingProgressBar) findViewById(R.id.pbTop);
        tvErrorTop = (TextView) findViewById(R.id.tvErrorTop);
        //NEW
        rlvNew = (RelativeLayout) findViewById(R.id.rlvNew);
        srlNew = (SwipeRefreshLayout) findViewById(R.id.srlNew);
        rvNew = (RecyclerView) findViewById(R.id.rvNew);
        pbNew = (ContentLoadingProgressBar) findViewById(R.id.pbNew);
        tvErrorNew = (TextView) findViewById(R.id.tvErrorNew);
        //BEST
        rlvBest = (RelativeLayout) findViewById(R.id.rlvBest);
        srlBest = (SwipeRefreshLayout) findViewById(R.id.srlBest);
        rvBest = (RecyclerView) findViewById(R.id.rvBest);
        pbBest = (ContentLoadingProgressBar) findViewById(R.id.pbBest);
        tvErrorBest = (TextView) findViewById(R.id.tvErrorBest);
        //SAVED
        rlvSaved = (RelativeLayout) findViewById(R.id.rlvSaved);
        srlSaved = (SwipeRefreshLayout) findViewById(R.id.srlSaved);
        rvSaved = (RecyclerView) findViewById(R.id.rvSaved);
        pbSaved = (ContentLoadingProgressBar) findViewById(R.id.pbSaved);
        tvErrorSaved = (TextView) findViewById(R.id.tvErrorSaved);

        bottom_sheet = (FrameLayout) findViewById(R.id.bottom_sheet);
        slide_up = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_up);
        slide_up.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                slid_up = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        slide_down = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_down);
        slide_down.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                slid_up = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                WebViewFragment webViewFragment = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.bottom_sheet);
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        isAnArticleClicked = true;
                        bottomSheetBehavior.setHideable(false);
                        bottomSheetBehavior.setPeekHeight(getSoftButtonsBarHeight() + getSupportActionBar().getHeight() / 2 + 25);
                        if (webViewFragment != null) {
                            if (mClickedArticleType.equalsIgnoreCase(SAVED_STORIES))
                                webViewFragment.replaceMenu(R.menu.menu_down_saved);
                            else
                                webViewFragment.replaceMenu(R.menu.menu_down);
                        }
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        if (webViewFragment != null) {
                            if (mClickedArticleType.equalsIgnoreCase(SAVED_STORIES))
                                webViewFragment.replaceMenu(R.menu.menu_up_saved);
                            else
                                webViewFragment.replaceMenu(R.menu.menu_up);
                        }
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
//                    case BottomSheetBehavior.STATE_SETTLING:
//                        if(webViewFragment!=null&&bottomSheetBehavior.getState()!=BottomSheetBehavior.STATE_DRAGGING)
//                            webViewFragment.replaceMenu(0);
//                        break;
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        srlTop.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNews();
            }
        });
        srlNew.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNews();
            }
        });
        srlBest.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNews();
            }
        });
        srlSaved.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNews();
            }
        });

        topLayoutManager = new LinearLayoutManager(this);
        rvTop.setLayoutManager(topLayoutManager);
        topAdapter = new NewsAdapter(this);
        rvTop.setAdapter(topAdapter);
        rvTop.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    int visibleItemCount = topLayoutManager.getChildCount();
                    int totalItemCount = topLayoutManager.getItemCount();
                    int pastVisiblesItems = topLayoutManager.findFirstVisibleItemPosition();

                    if (!mIsLoadingArticle_top && !isDoneLoadingAll_top) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            new GetTopNewsList().execute();
                        }
                    }
                    if (((visibleItemCount + pastVisiblesItems) >= totalItemCount) && isAnArticleClicked && isDoneLoadingAll_top) {
                        bottom_sheet.startAnimation(slide_down);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (isAnArticleClicked) {
                        if (slid_up) {
                            bottom_sheet.startAnimation(slide_down);
                        }
                    }
                } else if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    if (isAnArticleClicked) {
                        if (!slid_up) {
                            bottom_sheet.startAnimation(slide_up);
                        }
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        newLayoutManager = new LinearLayoutManager(this);
        rvNew.setLayoutManager(newLayoutManager);
        newAdapter = new NewsAdapter(this);
        rvNew.setAdapter(newAdapter);
        rvNew.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    int visibleItemCount = newLayoutManager.getChildCount();
                    int totalItemCount = newLayoutManager.getItemCount();
                    int pastVisiblesItems = newLayoutManager.findFirstVisibleItemPosition();

                    if (!mIsLoadingArticle_new && !isDoneLoadingAll_new) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            new GetNewNewsList().execute();
                        }
                    }
                    if (((visibleItemCount + pastVisiblesItems) >= totalItemCount) && isAnArticleClicked && isDoneLoadingAll_new) {
                        bottom_sheet.startAnimation(slide_down);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (isAnArticleClicked) {
                        if (slid_up) {
                            bottom_sheet.startAnimation(slide_down);
                        }
                    }
                } else if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    if (isAnArticleClicked) {
                        if (!slid_up) {
                            bottom_sheet.startAnimation(slide_up);
                        }
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        bestLayoutManager = new LinearLayoutManager(this);
        rvBest.setLayoutManager(bestLayoutManager);
        bestAdapter = new NewsAdapter(this);
        rvBest.setAdapter(bestAdapter);
        rvBest.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    int visibleItemCount = bestLayoutManager.getChildCount();
                    int totalItemCount = bestLayoutManager.getItemCount();
                    int pastVisiblesItems = bestLayoutManager.findFirstVisibleItemPosition();

                    if (!mIsLoadingArticle_best && !isDoneLoadingAll_best) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            new GetBestNewsList().execute();
                        }
                    }
                    if (((visibleItemCount + pastVisiblesItems) >= totalItemCount) && isAnArticleClicked && isDoneLoadingAll_best) {
                        bottom_sheet.startAnimation(slide_down);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (isAnArticleClicked) {
                        if (slid_up) {
                            bottom_sheet.startAnimation(slide_down);
                        }
                    }
                } else if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    if (isAnArticleClicked) {
                        if (!slid_up) {
                            bottom_sheet.startAnimation(slide_up);
                        }
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        savedLayoutManager = new LinearLayoutManager(this);
        rvSaved.setLayoutManager(savedLayoutManager);
        savedAdapter = new NewsAdapter(this);
        rvSaved.setAdapter(savedAdapter);
        rvSaved.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    int visibleItemCount = savedLayoutManager.getChildCount();
                    int totalItemCount = savedLayoutManager.getItemCount();
                    int pastVisiblesItems = savedLayoutManager.findFirstVisibleItemPosition();

                    if (((visibleItemCount + pastVisiblesItems) >= totalItemCount) && isAnArticleClicked) {
                        bottom_sheet.startAnimation(slide_down);
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (isAnArticleClicked) {
                        if (slid_up) {
                            bottom_sheet.startAnimation(slide_down);
                        }
                    }
                } else if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    if (isAnArticleClicked) {
                        if (!slid_up) {
                            bottom_sheet.startAnimation(slide_up);
                        }
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED || bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_SETTLING) {
            collapseFragment();
        } else{
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            showToast("Tap back button again to exit");

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.menu_story);

        //Here, you get access to the view of your item, in this case, the layout of the item has a FrameLayout as root view but you can change it to whatever you use
        ddStory = (Spinner) item.getActionView();

        ddStory.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 4;
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
                switch (position) {
                    case 0:
                        tvStory.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_top, 0, 0, 0);
                        tvStory.setText("TOP");
                        break;
                    case 1:
                        tvStory.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_new, 0, 0, 0);
                        tvStory.setText("NEW");
                        break;
                    case 2:
                        tvStory.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_best, 0, 0, 0);
                        tvStory.setText("BEST");
                        break;
                    case 3:
                        tvStory.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_saved, 0, 0, 0);
                        tvStory.setText("SAVED");
                        break;
                }
                return item;
            }

        });
        ddStory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String chosenString = TOP_STORIES;
                switch (position) {
                    case 0:
                        chosenString = TOP_STORIES;
                        break;
                    case 1:
                        chosenString = NEW_STORIES;
                        break;
                    case 2:
                        chosenString = BEST_STORIES;
                        break;
                    case 3:
                        chosenString = SAVED_STORIES;
                }

                if (!chosenString.equalsIgnoreCase(storyType)) {
//                    private RelativeLayout rlvTop, rlvNew, rlvBest, rlvSaved;
                    switch (storyType) {
                        case TOP_STORIES:
                            rlvTop.setVisibility(View.GONE);
                            break;
                        case NEW_STORIES:
                            rlvNew.setVisibility(View.GONE);
                            break;
                        case BEST_STORIES:
                            rlvBest.setVisibility(View.GONE);
                            break;
                        case SAVED_STORIES:
                            rlvSaved.setVisibility(View.GONE);
                            break;
                    }
                    storyType = chosenString;
                    switch (storyType) {
                        case TOP_STORIES:
                            if (!isNetworkAvailable()) {
                                showToast("No internet connection");
                                rlvTop.setVisibility(View.GONE);
                                new GetNewsFromDB().execute();
                                ddStory.setSelection(3);
                            } else {
                                rlvTop.setVisibility(View.VISIBLE);
                                if (!isGetTopListRan)
                                    new GetTopNewsList().execute();
                            }
                            break;
                        case NEW_STORIES:
                            if (!isNetworkAvailable()) {
                                showToast("No internet connection");
                                rlvNew.setVisibility(View.GONE);
                                new GetNewsFromDB().execute();
                                ddStory.setSelection(3);
                            } else {
                                rlvNew.setVisibility(View.VISIBLE);
                                if (!isGetNewListRan)
                                    new GetNewNewsList().execute();
                            }
                            break;
                        case BEST_STORIES:
                            if (!isNetworkAvailable()) {
                                showToast("No internet connection");
                                rlvBest.setVisibility(View.GONE);
                                new GetNewsFromDB().execute();
                                ddStory.setSelection(3);
                            } else {
                                rlvBest.setVisibility(View.VISIBLE);
                                if (!isGetBestListRan)
                                    new GetBestNewsList().execute();
                            }
                            break;
                        case SAVED_STORIES:
                            rlvSaved.setVisibility(View.VISIBLE);
                            new GetNewsFromDB().execute();
                            break;
                    }
//                    refreshNews();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (!isNetworkAvailable()) {
            Toast.makeText(MainActivity.this, "No internet connection.", Toast.LENGTH_SHORT).show();
            rlvTop.setVisibility(View.GONE);
            new GetNewsFromDB().execute();
            ddStory.setSelection(3);
        } else {
            rlvTop.setVisibility(View.VISIBLE);
            new GetTopNewsList().execute();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_story) {
            return true;
        } else if(id==android.R.id.home){
            // home button from toolbar clicked
            View dialog_about = LayoutInflater.from(this).inflate(R.layout.dialog_about, null);
            ImageView ivGPlus = (ImageView) dialog_about.findViewById(R.id.ivGPlus);
            ImageView ivTwitter = (ImageView) dialog_about.findViewById(R.id.ivTwitter);
            Button bnReview = (Button) dialog_about.findViewById(R.id.bnReview);
            TextView tvLegal =(TextView) dialog_about.findViewById(R.id.tvLegal);
            ivGPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/104794761449407529898")));
                }
            });
            ivTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/annoyingLemond")));
                }
            });
            bnReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /* This code assumes you are inside an activity */
                     Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                     Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);

                    if (getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0) {
                        startActivity(rateAppIntent);
                    } else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName())));
                            /* handle your error case: the device has no way to handle market urls */
                    }
                }
            });
            tvLegal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, LegalActivity.class));
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("About");
            builder.setView(dialog_about);
            builder.setCancelable(true);
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void collapseFragment() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void expandFragment() {
        if (!slid_up && isAnArticleClicked) {
            bottom_sheet.startAnimation(slide_up);
        }
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }


    public void refreshNews() {
        if (storyType.equalsIgnoreCase(TOP_STORIES)) {
            topAdapter.clear();
            topAdapter.setLoadedAll(false);
            topStoryIds.clear();
            lastLoadedStoryCode_top = null;
            lastUnLoadedStoryCode_top = null;
            loadedStoryIndex_top = 0;
            mIsLoadingArticle_top = false;
            isDoneLoadingAll_top = false;
            new GetTopNewsList().execute();
        } else if (storyType.equalsIgnoreCase(NEW_STORIES)) {
            newAdapter.clear();
            newAdapter.setLoadedAll(false);
            newStoryIds.clear();
            lastLoadedStoryCode_new = null;
            lastUnLoadedStoryCode_new = null;
            loadedStoryIndex_new = 0;
            mIsLoadingArticle_new = false;
            isDoneLoadingAll_new = false;
            new GetNewNewsList().execute();
        } else if (storyType.equalsIgnoreCase(BEST_STORIES)) {
            bestAdapter.clear();
            bestAdapter.setLoadedAll(false);
            bestStoryIds.clear();
            lastLoadedStoryCode_best = null;
            lastUnLoadedStoryCode_best = null;
            loadedStoryIndex_best = 0;
            mIsLoadingArticle_best = false;
            isDoneLoadingAll_best = false;
            new GetBestNewsList().execute();
        } else {
            new GetNewsFromDB().execute();
        }
//        else
//            new GetNewsFromDB().execute();

    }

    @SuppressLint("NewApi")
    private int getSoftButtonsBarHeight() {
        boolean hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0 && !hasMenuKey) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void openNewsArticle(int position) {
        NewsAdapter mAdapter = topAdapter;
        switch (storyType) {
            case TOP_STORIES:
                mAdapter = topAdapter;
                mClickedArticleType = TOP_STORIES;
                break;
            case NEW_STORIES:
                mAdapter = newAdapter;
                mClickedArticleType = NEW_STORIES;
                break;
            case BEST_STORIES:
                mAdapter = bestAdapter;
                mClickedArticleType = BEST_STORIES;
                break;
            case SAVED_STORIES:
                mAdapter = savedAdapter;
                mClickedArticleType = SAVED_STORIES;
                break;
        }
        if (mAdapter.getNews(position) != null) {
            isAnArticleClicked = true;
            expandFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            mFragment = WebViewFragment.newInstance(mAdapter.getNews(position));
            ft.replace(R.id.bottom_sheet, mFragment);
            ft.commit();
        }
    }

    public void showToast(String message){
        SuperActivityToast.create(this, new Style(),
                Style.TYPE_STANDARD)
                .setText(message)
                .setTextColor(ContextCompat.getColor(this, R.color.grey_white_1000))
                .setDuration(Style.DURATION_SHORT)
                .setFrame(Style.FRAME_STANDARD)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setAnimations(Style.ANIMATIONS_FADE)
                .show();
    }


    /***********************************
     * ASYNCTASKS
     ***********************************/

    class GetTopNewsList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            tvErrorTop.setText("An error occurred.");
            tvErrorTop.setVisibility(View.GONE);
            if (topStoryIds == null) {
                srlTop.setVisibility(View.GONE);
                pbTop.setVisibility(View.VISIBLE);
            }
            mIsLoadingArticle_top = true;
            isGetTopListRan = true;
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... urls) {
            try {
                if (topStoryIds.size() == 0) {
                    JSONArray jsonArray = new JSONArray(HttpHandler.makeServiceCall(TOP_STORIES));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        topStoryIds.add(jsonArray.getLong(i));
                    }
                    /**
                     * below commented code is for test only
                     */
//                        for (int i = 0; i < 45; i++) {
//                            topStoryIds.add(jsonArray.getLong(i));
//                        }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (srlTop.isRefreshing())
                srlTop.setRefreshing(false);
            if (topStoryIds != null && topStoryIds.size() > 0) {
                //check if loadedStoryIndex + 20 is greater than topStoryIds.size
                if ((loadedStoryIndex_top + 20) < topStoryIds.size()) {
                    lastUnLoadedStoryCode_top = topStoryIds.get((loadedStoryIndex_top + 20) - 1);
                    int stopper = loadedStoryIndex_top;
                    for (int i = loadedStoryIndex_top; i < stopper + 20; i++) {
                        new GetTopArticle().execute(topStoryIds.get(i));
                        lastLoadedStoryCode_top = topStoryIds.get(i);
                        if (i == (loadedStoryIndex_top + 20) - 1) {
                            loadedStoryIndex_top = i;
                        }
                    }
                } else {
                    lastUnLoadedStoryCode_top = topStoryIds.get((topStoryIds.size()) - 1);
                    for (int i = loadedStoryIndex_top; i < topStoryIds.size(); i++) {
                        new GetTopArticle().execute(topStoryIds.get(i));
                        lastLoadedStoryCode_top = topStoryIds.get(i);
                        if (i == (topStoryIds.size() - 1)) {
                            loadedStoryIndex_top = i;
                            isDoneLoadingAll_top = true;
                        }
                    }
                }
                if (lastLoadedStoryCode_top.floatValue() == lastUnLoadedStoryCode_top.floatValue()) {
                    mIsLoadingArticle_top = false;
                }
            } else {
                pbTop.setVisibility(View.GONE);
                tvErrorTop.setVisibility(View.VISIBLE);
            }

        }
    }

    class GetNewNewsList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            tvErrorNew.setText("An error occurred.");
            tvErrorNew.setVisibility(View.GONE);
            if (newStoryIds == null) {
                srlNew.setVisibility(View.GONE);
                pbNew.setVisibility(View.VISIBLE);
            }
            mIsLoadingArticle_new = true;
            isGetNewListRan = true;
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... urls) {
            try {
                if (newStoryIds.size() == 0) {
                    JSONArray jsonArray = new JSONArray(HttpHandler.makeServiceCall(NEW_STORIES));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        newStoryIds.add(jsonArray.getLong(i));
                    }
                    /**
                     * below commented code is for test only
                     */
//                        for (int i = 0; i < 45; i++) {
//                            topStoryIds.add(jsonArray.getLong(i));
//                        }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (srlNew.isRefreshing())
                srlNew.setRefreshing(false);
            if (newStoryIds != null && newStoryIds.size() > 0) {
                //check if loadedStoryIndex + 20 is greater than newStoryIds.size
                if ((loadedStoryIndex_new + 20) < newStoryIds.size()) {
                    lastUnLoadedStoryCode_new = newStoryIds.get((loadedStoryIndex_new + 20) - 1);
                    int stopper = loadedStoryIndex_new;
                    for (int i = loadedStoryIndex_new; i < stopper + 20; i++) {
                        new GetNewArticle().execute(newStoryIds.get(i));
                        lastLoadedStoryCode_new = newStoryIds.get(i);
                        if (i == (loadedStoryIndex_new + 20) - 1) {
                            loadedStoryIndex_new = i;
                        }
                    }
                } else {
                    lastUnLoadedStoryCode_new = newStoryIds.get((newStoryIds.size()) - 1);
                    for (int i = loadedStoryIndex_new; i < newStoryIds.size(); i++) {
                        new GetNewArticle().execute(newStoryIds.get(i));
                        lastLoadedStoryCode_new = newStoryIds.get(i);
                        if (i == (newStoryIds.size() - 1)) {
                            loadedStoryIndex_new = i;
                            isDoneLoadingAll_new = true;
                        }
                    }
                }
                if (lastLoadedStoryCode_new.floatValue() == lastUnLoadedStoryCode_new.floatValue()) {
                    mIsLoadingArticle_new = false;
                }
            } else {
                pbNew.setVisibility(View.GONE);
                tvErrorNew.setVisibility(View.VISIBLE);
            }
        }
    }

    class GetBestNewsList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            tvErrorBest.setText("An error occurred.");
            tvErrorBest.setVisibility(View.GONE);
            if (bestStoryIds == null) {
                srlBest.setVisibility(View.GONE);
                pbBest.setVisibility(View.VISIBLE);
            }
            mIsLoadingArticle_best = true;
            isGetBestListRan = true;
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... urls) {
            try {
                if (bestStoryIds.size() == 0) {
                    JSONArray jsonArray = new JSONArray(HttpHandler.makeServiceCall(BEST_STORIES));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        bestStoryIds.add(jsonArray.getLong(i));
                    }
                    /**
                     * below commented code is for test only
                     */
//                        for (int i = 0; i < 45; i++) {
//                            topStoryIds.add(jsonArray.getLong(i));
//                        }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (srlBest.isRefreshing())
                srlBest.setRefreshing(false);
            if (bestStoryIds != null && bestStoryIds.size() > 0) {
                //check if loadedStoryIndex + 20 is greater than bestStoryIds.size
                if ((loadedStoryIndex_best + 20) < bestStoryIds.size()) {
                    lastUnLoadedStoryCode_best = bestStoryIds.get((loadedStoryIndex_best + 20) - 1);
                    int stopper = loadedStoryIndex_best;
                    for (int i = loadedStoryIndex_best; i < stopper + 20; i++) {
                        new GetBestArticle().execute(bestStoryIds.get(i));
                        lastLoadedStoryCode_best = bestStoryIds.get(i);
                        if (i == (loadedStoryIndex_best + 20) - 1) {
                            loadedStoryIndex_best = i;
                        }
                    }
                } else {
                    lastUnLoadedStoryCode_best = bestStoryIds.get((bestStoryIds.size()) - 1);
                    for (int i = loadedStoryIndex_best; i < bestStoryIds.size(); i++) {
                        new GetBestArticle().execute(bestStoryIds.get(i));
                        lastLoadedStoryCode_best = bestStoryIds.get(i);
                        if (i == (bestStoryIds.size() - 1)) {
                            loadedStoryIndex_best = i;
                            isDoneLoadingAll_best = true;
                        }
                    }
                }
                if (lastLoadedStoryCode_best.floatValue() == lastUnLoadedStoryCode_best.floatValue()) {
                    mIsLoadingArticle_best = false;
                }
            } else {
                pbNew.setVisibility(View.GONE);
                tvErrorNew.setVisibility(View.VISIBLE);
            }
        }
    }

    class GetTopArticle extends AsyncTask<Long, Void, NewsObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected NewsObject doInBackground(Long... params) {
            try {
                String articleURL = "https://hacker-news.firebaseio.com/v0/item/" + params[0] + ".json?print=pretty";
                JSONObject jsonObject = new JSONObject(HttpHandler.makeServiceCall(articleURL));
                return new NewsObject(jsonObject.getLong("id"), jsonObject.getString("title"), jsonObject.getString("url"), jsonObject.getString("by"), jsonObject.getInt("score"), jsonObject.getLong("time"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(NewsObject newsObject) {
            super.onPostExecute(newsObject);
            if (newsObject != null) {
                pbTop.setVisibility(View.GONE);
                topAdapter.addNews(newsObject);
                srlTop.setVisibility(View.VISIBLE);
                if (isDoneLoadingAll_top) {
                    topAdapter.setLoadedAll(true);
                }
            } else {
                pbTop.setVisibility(View.GONE);
                if (topAdapter.isEmpty())
                    tvErrorTop.setVisibility(View.VISIBLE);
            }
        }
    }

    class GetNewArticle extends AsyncTask<Long, Void, NewsObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected NewsObject doInBackground(Long... params) {
            try {
                String articleURL = "https://hacker-news.firebaseio.com/v0/item/" + params[0] + ".json?print=pretty";
                JSONObject jsonObject = new JSONObject(HttpHandler.makeServiceCall(articleURL));
                return new NewsObject(jsonObject.getLong("id"), jsonObject.getString("title"), jsonObject.getString("url"), jsonObject.getString("by"), jsonObject.getInt("score"), jsonObject.getLong("time"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(NewsObject newsObject) {
            super.onPostExecute(newsObject);
            if (newsObject != null) {
                pbNew.setVisibility(View.GONE);
                newAdapter.addNews(newsObject);
                srlNew.setVisibility(View.VISIBLE);
                if (isDoneLoadingAll_new) {
                    newAdapter.setLoadedAll(true);
                }
            } else {
                pbNew.setVisibility(View.GONE);
                if (newAdapter.isEmpty())
                    tvErrorNew.setVisibility(View.VISIBLE);
            }
        }
    }

    class GetBestArticle extends AsyncTask<Long, Void, NewsObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected NewsObject doInBackground(Long... params) {
            try {
                String articleURL = "https://hacker-news.firebaseio.com/v0/item/" + params[0] + ".json?print=pretty";
                JSONObject jsonObject = new JSONObject(HttpHandler.makeServiceCall(articleURL));
                return new NewsObject(jsonObject.getLong("id"), jsonObject.getString("title"), jsonObject.getString("url"), jsonObject.getString("by"), jsonObject.getInt("score"), jsonObject.getLong("time"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(NewsObject newsObject) {
            super.onPostExecute(newsObject);
            if (newsObject != null) {
                pbBest.setVisibility(View.GONE);
                bestAdapter.addNews(newsObject);
                srlBest.setVisibility(View.VISIBLE);
                if (isDoneLoadingAll_best) {
                    bestAdapter.setLoadedAll(true);
                }
            } else {
                pbBest.setVisibility(View.GONE);
                if (bestAdapter.isEmpty())
                    tvErrorBest.setVisibility(View.VISIBLE);
            }
        }
    }

    public void addNewsToList(NewsObject newsObject) {
        savedAdapter.addNews(newsObject);
    }

    public void deleteNewsFromList(long newsID) {
        if (savedAdapter.getItemCount() > 1) {
            savedAdapter.deleteNews(newsID);
            if(savedAdapter.getItemCount() == 1){
                tvErrorSaved.setText("No news articles were saved.");
                srlSaved.setVisibility(View.GONE);
                pbSaved.setVisibility(View.GONE);
                tvErrorSaved.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setClickedNewsType(String type) {
        this.mClickedArticleType = type;
    }

    class GetNewsFromDB extends AsyncTask<Void, Void, ArrayList<NewsObject>> {
        @Override
        protected void onPreExecute() {
            if (rlvSaved.getVisibility() == View.GONE)
                rlvSaved.setVisibility(View.VISIBLE);
            tvErrorSaved.setText("No news articles were saved.");
            tvErrorSaved.setVisibility(View.GONE);
            srlSaved.setVisibility(View.GONE);
            pbSaved.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<NewsObject> doInBackground(Void... params) {
            return new DatabaseAdapter(MainActivity.this).getAllNews();
        }

        @Override
        protected void onPostExecute(ArrayList<NewsObject> newsObjects) {
            pbSaved.setVisibility(View.GONE);
            if (srlSaved.isRefreshing())
                srlSaved.setRefreshing(false);
            if (!newsObjects.isEmpty()) {
                savedAdapter.clear();
                srlSaved.setVisibility(View.VISIBLE);
                for (NewsObject news : newsObjects) {
                    savedAdapter.addNews(news);
                }
                savedAdapter.setLoadedAll(true);
            } else {
                tvErrorSaved.setVisibility(View.VISIBLE);
            }
            super.onPostExecute(newsObjects);
        }
    }

//    public interface ClickListener {
//        void onClick(View view, int position);
//
//        void onLongClick(View view, int position);
//    }

//    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
//
//        private GestureDetector gestureDetector;
//        private MainActivity.ClickListener clickListener;
//
//        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener) {
//            this.clickListener = clickListener;
//            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
//                @Override
//                public boolean onSingleTapUp(MotionEvent e) {
//                    return true;
//                }
//
//                @Override
//                public void onLongPress(MotionEvent e) {
//                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
//                    if (child != null && clickListener != null) {
//                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
//                    }
//                }
//            });
//        }
//
//        @Override
//        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//            View child = rv.findChildViewUnder(e.getX(), e.getY());
//            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
//                clickListener.onClick(child, rv.getChildPosition(child));
//            }
//            return false;
//        }
//
//        @Override
//        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//        }
//
//        @Override
//        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//        }
//    }

}
