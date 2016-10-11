package com.example.lemon.hackernews;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Lemon on 10/1/2016.
 */

public class NewsAdapter extends  RecyclerView.Adapter<NewsAdapter.ItemViewHolder> {

    private ArrayList<NewsObject> newsList;

    private int lastPosition = -1;

    private Context mContext;

    private boolean isLoadedAll = false;

    public NewsAdapter(Context context) {
        this.newsList = new ArrayList<>();
        this.mContext = context;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvPoints, tvTime, tvCommentCount;
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
            tvCommentCount = (TextView) view.findViewById(R.id.tvCommentCount);
            llLoading  = (LinearLayout) view.findViewById(R.id.llLoading);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            llErrorOccurred  = (LinearLayout) view.findViewById(R.id.llErrorOccurred);
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
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        if(position==newsList.size()){
            holder.llLoading.setVisibility(View.VISIBLE);
            holder.rvCardCase.setVisibility(View.GONE);
            if(isLoadedAll){
                holder.progressBar.setVisibility(View.GONE);
                holder.llDone.setVisibility(View.VISIBLE);
                holder.bnRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((MainActivity)((AppCompatActivity) mContext)).refreshNews();
                    }
                });
            }
        }else {
            holder.rvCardCase.setVisibility(View.VISIBLE);
            holder.llLoading.setVisibility(View.GONE);
            NewsObject news = newsList.get(position);
            holder.tvTitle.setText(news.getNewsTitle());
            holder.tvPoints.setText(news.getNewsScore() + " by " + news.getNewsAuthor());
            holder.tvCommentCount.setText(news.getCommentCount()+"");
            holder.tvTime.setText(getDateDiff(news.getCreationDate()));
            setAnimation(holder.rvCardCase, position);
        }

    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size()+1;
    }

    public void addNews(NewsObject newsObject){
        this.newsList.add(newsObject);
//        notifyDataSetChanged();
        notifyItemInserted(this.newsList.size() - 1);
    }

    public NewsObject getNews(int position){
        if(position<this.newsList.size())
            return this.newsList.get(position);
        else
            return null;
    }

    public void clear(){
        this.newsList.clear();
        notifyDataSetChanged();
    }

    public void setLoadedAll(boolean loadedAll) {
        isLoadedAll = loadedAll;
        notifyItemChanged(newsList.size()+1);
    }



    public boolean isEmpty(){
        return this.newsList.size() == 0;
    }

    String getDateDiff(long newsDate){
        Calendar now = Calendar.getInstance();
        //get seconds
        long periodSeconds = (now.getTimeInMillis()/1000) - newsDate;
        //get days
        if((((periodSeconds/60)/60)/24)>=1){
            periodSeconds = ((periodSeconds/60)/60)/24;
            if(periodSeconds==1)
                return periodSeconds  + " day ago";
            else
                return periodSeconds  + " days ago";
        }
        //get hour
        else if(((periodSeconds/60)/60)>=1){
            periodSeconds = (periodSeconds/60)/60;
            if(periodSeconds==1)
                return periodSeconds  + " hour ago";
            else
                return periodSeconds  + " hours ago";
        }
        //get minutes
        else if((periodSeconds/60)>=60){
            periodSeconds = periodSeconds/60;
            if(periodSeconds==1)
                return periodSeconds  + " minute ago";
            else
                return periodSeconds  + " minutes ago";
        }
        else{
            if(periodSeconds==1)
                return periodSeconds  + " second ago";
            else
                return periodSeconds  + " seconds ago";
        }
    }

}
