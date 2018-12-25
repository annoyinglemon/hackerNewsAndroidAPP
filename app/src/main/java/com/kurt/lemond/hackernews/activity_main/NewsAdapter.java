package com.kurt.lemond.hackernews.activity_main;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kurt.lemond.hackernews.R;
import com.kurt.lemond.hackernews.Utils;
import com.kurt.lemond.hackernews.activity_main.repository.NewsObject;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Lemon on 10/1/2016.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ItemViewHolder> {

    private ArrayList<NewsObject> newsList;

    private int lastPosition = -1;

    private Context mContext;

    private boolean isLoadedAll = false;

    public NewsAdapter(Context context) {
        this.newsList = new ArrayList<>();
        this.mContext = context;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvPoints, tvTime, tvMenu;
        public RelativeLayout rvCardCase;
        public LinearLayout llLoading, llErrorOccurred, llDone;
        public ProgressBar progressBar;
        public Button bnRefresh;

        public ItemViewHolder(View view) {
            super(view);
            rvCardCase = (RelativeLayout) view.findViewById(R.id.rvCardCase);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvPoints = (TextView) view.findViewById(R.id.tvPoints);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvMenu = (TextView) view.findViewById(R.id.tvMenu);
            llLoading = (LinearLayout) view.findViewById(R.id.llLoading);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            llErrorOccurred = (LinearLayout) view.findViewById(R.id.llErrorOccurred);
            llDone = (LinearLayout) view.findViewById(R.id.llDone);
            bnRefresh = (Button) view.findViewById(R.id.bnRefresh);
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_row, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        if (position == newsList.size()) {
            holder.llLoading.setVisibility(View.VISIBLE);
            holder.rvCardCase.setVisibility(View.GONE);
            if (isLoadedAll) {
                holder.progressBar.setVisibility(View.GONE);
                holder.llDone.setVisibility(View.VISIBLE);
                holder.bnRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity) mContext).refreshNews();
                    }
                });
            }
        } else {
            final NewsObject news = newsList.get(position);
            RelativeLayout rvCardCase = holder.rvCardCase;
            holder.rvCardCase.setVisibility(View.VISIBLE);
            rvCardCase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.getAdapterPosition() != newsList.size())
                        ((MainActivity) mContext).openNewsArticle(holder.getAdapterPosition());
                }
            });
            holder.llLoading.setVisibility(View.GONE);
            holder.tvTitle.setText(news.getNewsTitle());
            holder.tvPoints.setText(news.getNewsScore() + " by " + news.getNewsAuthor());
            final TextView tvMenu = holder.tvMenu;
            tvMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(mContext, tvMenu);
                    popup.getMenuInflater()
                            .inflate(R.menu.menu_popup, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
//
                                case R.id.action_share:
                                    Intent share = new Intent(android.content.Intent.ACTION_SEND);
                                    share.setType("text/plain");
                                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
                                        share.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                                    else
                                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                    share.putExtra(Intent.EXTRA_SUBJECT, newsList.get(holder.getAdapterPosition()).getNewsTitle());
                                    share.putExtra(Intent.EXTRA_TEXT, newsList.get(holder.getAdapterPosition()).getNewsURL());
                                    mContext.startActivity(Intent.createChooser(share, "Share"));
                                    return true;
                                case R.id.action_copy:
                                    ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("URL", newsList.get(holder.getAdapterPosition()).getNewsURL());
                                    clipboard.setPrimaryClip(clip);
                                    Toast.makeText(mContext, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                                    return true;
                                case R.id.action_open:
                                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(newsList.get(holder.getAdapterPosition()).getNewsURL())));
                                    return true;
                            }
                            return true;
                        }
                    });
                    Utils.setForceShowIcon(popup);
                    popup.show();
                }
            });
            holder.tvTime.setText(getDateDiff(news.getCreationDate()));
            setAnimation(holder.rvCardCase, position);
        }

    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size() + 1;
    }

    public void addNews(NewsObject newsObject) {
        this.newsList.add(newsObject);
//        notifyDataSetChanged();
        notifyItemInserted(this.newsList.size() - 1);
    }

    public void deleteNews(long newsID){
        for(int i=0; i<newsList.size(); i++){
            if(newsList.get(i).getNewsID()==newsID){
                newsList.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }

    }

    public NewsObject getNews(int position) {
        if (position < this.newsList.size())
            return this.newsList.get(position);
        else
            return null;
    }

    public void clear() {
        this.newsList.clear();
        notifyDataSetChanged();
    }

    public void setLoadedAll(boolean loadedAll) {
        isLoadedAll = loadedAll;
        notifyItemChanged(newsList.size() + 1);
    }

    public boolean isEmpty() {
        return this.newsList.size() == 0;
    }

    String getDateDiff(long newsDate) {
        Calendar now = Calendar.getInstance();
        //get seconds
        long periodSeconds = (now.getTimeInMillis() / 1000) - newsDate;
        //get days
        if ((((periodSeconds / 60) / 60) / 24) >= 1) {
            periodSeconds = ((periodSeconds / 60) / 60) / 24;
            if (periodSeconds == 1)
                return periodSeconds + " day ago";
            else
                return periodSeconds + " days ago";
        }
        //get hour
        else if (((periodSeconds / 60) / 60) >= 1) {
            periodSeconds = (periodSeconds / 60) / 60;
            if (periodSeconds == 1)
                return periodSeconds + " hour ago";
            else
                return periodSeconds + " hours ago";
        }
        //get minutes
        else if ((periodSeconds / 60) >= 1) {
            periodSeconds = periodSeconds / 60;
            if (periodSeconds == 1)
                return periodSeconds + " minute ago";
            else
                return periodSeconds + " minutes ago";
        } else {
            if (periodSeconds == 1)
                return periodSeconds + " second ago";
            else
                return periodSeconds + " seconds ago";
        }
    }

}
