package com.example.lemon.hackernews;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public NewsAdapter() {
        this.newsList = new ArrayList<>();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle, tvPoints, tvTime, tvCommentCount;
        public RelativeLayout rvCardCase;
        public LinearLayout llLoading, llErrorOccurred;
        public ProgressBar progressBar;
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
        }else {
            holder.llLoading.setVisibility(View.GONE);
            holder.rvCardCase.setVisibility(View.VISIBLE);
            NewsObject news = newsList.get(position);
            holder.tvTitle.setText(news.getNewsTitle());
            holder.tvPoints.setText(news.getNewsScore() + " by " + news.getNewsAuthor());
            holder.tvCommentCount.setText(news.getCommentCount()+"");
            holder.tvTime.setText(getDateDiff(news.getCreationDate()));
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size()+1;
    }

    public void addNews(NewsObject newsObject){
        this.newsList.add(newsObject);
        notifyDataSetChanged();
    }

    public void clear(){
        this.newsList.clear();
        notifyDataSetChanged();
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
