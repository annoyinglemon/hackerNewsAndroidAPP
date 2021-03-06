package com.kurt.lemond.hackernews;


public class WebViewFragment {

//    private Toolbar toolbar;
//    private WebView wVArticle;
//    private ProgressBar pbWebArticle;
//    private TextView tvOffline;
//    boolean isUndoClicked = false;
//    boolean isGoOnlineClicked = false;
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String NEWS_OBJECT = "news_object";
//
//    // TODO: Rename and change types of parameters
//    private NewsObject mArticle;
//
//    public WebViewFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @return A new instance of fragment WebViewFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static WebViewFragment newInstance(NewsObject param1) {
//        WebViewFragment fragment = new WebViewFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(NEWS_OBJECT, param1);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mArticle = (NewsObject) getArguments().getSerializable(NEWS_OBJECT);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        final View view = inflater.inflate(R.layout.fragment_web_view, container, false);
//        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//
//        LayoutInflater mInflater = LayoutInflater.from(getContext());
//        View mCustomView = mInflater.inflate(R.layout.article_title_view, null);
//        TextView tvArticleTitle = (TextView) mCustomView.findViewById(R.id.tvArticleTitle);
//        tvArticleTitle.setText(mArticle.getNewsTitle());
//        TextView tvArticleURL = (TextView) mCustomView.findViewById(R.id.tvArticleURL);
//        tvArticleURL.setText(Uri.parse(mArticle.getNewsURL()).getAuthority());
//        toolbar.removeAllViews();
//        toolbar.addView(mCustomView);
//        if (mArticle.getLocalPath() != null)
//            toolbar.inflateMenu(R.menu.menu_down_saved);
//        else
//            toolbar.inflateMenu(R.menu.menu_down);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.action_save:
//                        DatabaseAdapter databaseAdapter = new DatabaseAdapter(getContext());
//                        if (databaseAdapter.getNews(mArticle.getNewsID()) == null) {
//                            if (saveAsHTML()) {
//                                mArticle.setLocalPath(getContext().getApplicationInfo().dataDir + File.separator + "saved_articles" + File.separator + mArticle.getNewsID() + ".mht");
//                                if (databaseAdapter.insertIntoActualExpenses(mArticle) > -1) {
//                                    Toast.makeText(getActivity(), "Article " + mArticle.getNewsID() + " is successfully saved.", Toast.LENGTH_SHORT).show();
//
//                                    ((MainActivity) getContext()).addNewsToList(mArticle);
//                                    ((MainActivity) getContext()).setClickedNewsType(MainActivity.SAVED_STORIES);
//                                    if (toolbar.getMenu().getItem(0).getItemId() == R.id.action_up) {
//                                        toolbar.getMenu().clear();
//                                        toolbar.inflateMenu(R.menu.menu_up_saved);
//                                    } else {
//                                        toolbar.getMenu().clear();
//                                        toolbar.inflateMenu(R.menu.menu_down_saved);
//                                    }
//                                }
//                            } else
//                            Toast.makeText(getActivity(), "Article " + mArticle.getNewsID() + " is not successfully saved, try again.", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(getActivity(), "Article " + mArticle.getNewsID() + " already exists.", Toast.LENGTH_SHORT).show();
//                        }
//                        return true;
//                    case R.id.action_delete:
//                        isUndoClicked = false;
//                        final DatabaseAdapter databaseAdapter2 = new DatabaseAdapter(getContext());
//                                Toast.makeText(getActivity(), "Article deleted", Toast.LENGTH_SHORT).show();
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (!isUndoClicked) {
//                                    if (deleteHTML()) {
//                                        if (databaseAdapter2.deleteArticle(mArticle.getNewsID()) > 0) {
//                                            ((MainActivity) getContext()).deleteNewsFromList(mArticle.getNewsID());
//                                            ((MainActivity) getContext()).setClickedNewsType(MainActivity.TOP_STORIES);
//                                            if (toolbar.getMenu().getItem(0).getItemId() == R.id.action_up) {
//                                                toolbar.getMenu().clear();
//                                                toolbar.inflateMenu(R.menu.menu_up);
//                                            } else {
//                                                toolbar.getMenu().clear();
//                                                toolbar.inflateMenu(R.menu.menu_down);
//                                            }
//                                        }
//                                    } else
//                                        Toast.makeText(getContext(), "Article " + mArticle.getNewsID() + " is not successfully deleted.", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }, 2350);
//                        break;
//                    case R.id.action_down:
////                        toolbar.getMenu().clear();
//                        ((MainActivity) getContext()).collapseFragment();
//                        return true;
//                    case R.id.action_up:
////                        toolbar.getMenu().clear();
//                        ((MainActivity) getContext()).expandFragment();
//                        return true;
//                    case R.id.action_share:
//                        Intent share = new Intent(android.content.Intent.ACTION_SEND);
//                        share.setType("text/plain");
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//                            share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
//                        else
//                            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                        share.putExtra(Intent.EXTRA_SUBJECT, mArticle.getNewsTitle());
//                        share.putExtra(Intent.EXTRA_TEXT, mArticle.getNewsURL());
//                        startActivity(Intent.createChooser(share, "Share this Link"));
//                        return true;
//                    case R.id.action_copy:
//                        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//                        ClipData clip = ClipData.newPlainText("URL", mArticle.getNewsURL());
//                        clipboard.setPrimaryClip(clip);
//                        Toast.makeText(getActivity(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.action_open:
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mArticle.getNewsURL())));
//                        return true;
//                }
//                return false;
//            }
//        });
//
//
//        wVArticle = (WebView) view.findViewById(R.id.wVArticle);
//
//        pbWebArticle = (ProgressBar) view.findViewById(R.id.pbWebArticle);
//
//        tvOffline = (TextView) view.findViewById(R.id.tvOffline);
//        wVArticle.getSettings().setJavaScriptEnabled(true);
//        wVArticle.getSettings().setLoadWithOverviewMode(true);
//        wVArticle.getSettings().setUseWideViewPort(true);
//        wVArticle.getSettings().setBuiltInZoomControls(true);
//        wVArticle.setWebChromeClient(new WebChromeClient() {
//            public void onProgressChanged(WebView view, int progress) {
//                pbWebArticle.setProgress(progress);
//                if (progress == 100) {
//                    pbWebArticle.setProgress(100);
//                    pbWebArticle.setVisibility(View.GONE);
//                    if (mArticle.getLocalPath() != null && !isGoOnlineClicked){
//                        tvOffline.setVisibility(View.VISIBLE);
//                        tvOffline.setTranslationY(-1000f);
//                        tvOffline.animate().translationYBy(1000f).setDuration(600);
//                    }
//                } else {
//                    pbWebArticle.setVisibility(View.VISIBLE);
//                    pbWebArticle.setProgress(progress);
//                }
//            }
//        });
//        wVArticle.setWebViewClient(new MyWebViewClient());
//        if (mArticle.getLocalPath() != null)
//            wVArticle.loadUrl("file://" + mArticle.getLocalPath());
//        else
//            wVArticle.loadUrl(mArticle.getNewsURL());
//
//        return view;
//    }
//
//    private class MyWebViewClient extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
//
//        @Override
//        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//            if(mArticle.getLocalPath()!=null && isNetworkAvailable()) {
//                final Snackbar snackbar = Snackbar.make(view, "Oops! Looks like this webpage is unavailable during offline mode.", Snackbar.LENGTH_LONG)
//                        .setAction("GO ONLINE", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                tvOffline.setVisibility(View.GONE);
//                                wVArticle.loadUrl(mArticle.getNewsURL());
//                                isGoOnlineClicked = true;
//                            }
//                        })
//                        .setActionTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
//                        .setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE);
//                snackbar.show();
//            }else if(mArticle.getLocalPath()!=null && !isNetworkAvailable()){
//                final Snackbar snackbar = Snackbar.make(view, "Oops! Looks like this webpage is unavailable during offline mode.", Snackbar.LENGTH_LONG)
//                        .setDuration(BaseTransientBottomBar.LENGTH_INDEFINITE);
//                snackbar.show();
//            }
//            super.onReceivedError(view, errorCode, description, failingUrl);
//        }
//    }
//
//    public void replaceMenu(int menuRes) {
//        toolbar.getMenu().clear();
//        if (menuRes != 0)
//            toolbar.inflateMenu(menuRes);
//    }
//
//
//    public boolean saveAsHTML() {
//        File dataDir = new File(getContext().getApplicationInfo().dataDir + File.separator + "saved_articles");
//        dataDir.mkdirs();
//        File mhtml = new File(dataDir, mArticle.getNewsID() + ".mht");
//        wVArticle.saveWebArchive(mhtml.getAbsolutePath());
//        return mhtml.exists();
//    }
//
//    public boolean deleteHTML() {
//        File dataDir = new File(getContext().getApplicationInfo().dataDir + File.separator + "saved_articles");
//        File mhtml = new File(dataDir, mArticle.getNewsID() + ".mht");
//        return mhtml.delete();
//    }
//
//    private boolean isNetworkAvailable() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//    }
}
