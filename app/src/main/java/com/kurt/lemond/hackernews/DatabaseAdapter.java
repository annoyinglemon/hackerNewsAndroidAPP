package com.kurt.lemond.hackernews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by kurt_capatan on 10/13/2016.
 */
public class DatabaseAdapter {

    DBHelper helper;
    private Context mContext;

    public DatabaseAdapter(Context context) {
        this.mContext = context;
        helper = new DBHelper(context);
    }


    public ArrayList<NewsObject> getAllNews() {
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        String[] columns = {DBHelper.NEWS_ID, DBHelper.NEWS_URL, DBHelper.NEWS_TITLE, DBHelper.NEWS_AUTHOR, DBHelper.NEWS_SCORE, DBHelper.NEWS_CREATION_DATE, DBHelper.NEWS_LOCAL_PATH};
        Cursor cursor = sqlDB.query(DBHelper.TABLE_NEWS, columns, null, null, null, null, null);
        ArrayList<NewsObject> newssss = new ArrayList<NewsObject>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String url = cursor.getString(1);
            String title = cursor.getString(2);
            String author = cursor.getString(3);
            int score = cursor.getInt(4);
            long date = cursor.getLong(5);
            String path = cursor.getString(6);

            NewsObject newsObject = new NewsObject(id, title, url, author, score, date, path);
            newssss.add(newsObject);
        }
        cursor.close();
        helper.close();
        return newssss;
    }

    public long insertIntoActualExpenses(NewsObject newsObject) {
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        ContentValues cValues = new ContentValues();
        cValues.put(DBHelper.NEWS_ID, newsObject.getNewsID());
        cValues.put(DBHelper.NEWS_TITLE, newsObject.getNewsTitle());
        cValues.put(DBHelper.NEWS_URL, newsObject.getNewsURL());
        cValues.put(DBHelper.NEWS_AUTHOR, newsObject.getNewsAuthor());
        cValues.put(DBHelper.NEWS_SCORE, newsObject.getNewsScore());
        cValues.put(DBHelper.NEWS_CREATION_DATE, newsObject.getCreationDate());
        cValues.put(DBHelper.NEWS_LOCAL_PATH, newsObject.getLocalPath());
        long id = sqlDB.insert(DBHelper.TABLE_NEWS, null, cValues);
        helper.close();
        return id;
    }

    public int deleteArticle(long newsID){
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        String[] whereArgs = {Long.toString(newsID)};
        int result = sqlDB.delete(DBHelper.TABLE_NEWS, DBHelper.NEWS_ID + "=?", whereArgs);
        helper.close();
        return result;
    }

    public NewsObject getNews(long newsID) {
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        String[] columns = {DBHelper.NEWS_ID, DBHelper.NEWS_URL, DBHelper.NEWS_TITLE, DBHelper.NEWS_AUTHOR, DBHelper.NEWS_SCORE, DBHelper.NEWS_CREATION_DATE, DBHelper.NEWS_LOCAL_PATH};
        String[] selArgs = {newsID + ""};
        Cursor cursor = sqlDB.query(DBHelper.TABLE_NEWS, columns, DBHelper.NEWS_ID + "=?", selArgs, null, null, null);
        NewsObject newsObject = null;
        if (cursor.moveToFirst()) {
            newsObject = new NewsObject();
            newsObject.setNewsID(cursor.getLong(0));
            newsObject.setNewsURL(cursor.getString(1));
            newsObject.setNewsTitle(cursor.getString(2));
            newsObject.setNewsAuthor(cursor.getString(3));
            newsObject.setNewsScore(cursor.getInt(4));
            newsObject.setCreationDate(cursor.getLong(5));
            newsObject.setLocalPath(cursor.getString(6));
        }
        cursor.close();
        helper.close();
        return newsObject;

    }

    public static class DBHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "hacker_news_articles";
        private static final int DATABASE_VERSION = 1;

        private static final String TABLE_NEWS = "tbl_news";

        private static final String NEWS_ID = "news_id";
        private static final String NEWS_URL = "news_url";
        private static final String NEWS_TITLE = "news_title";
        private static final String NEWS_AUTHOR = "news_author";
        private static final String NEWS_SCORE = "news_score";
        private static final String NEWS_CREATION_DATE = "news_creation_date";
        private static final String NEWS_LOCAL_PATH = "news_local_path";


        private static final String CREATE_TABLE_NEWS = "CREATE TABLE " + TABLE_NEWS +
                " (" + NEWS_ID + " INTEGER PRIMARY KEY, " +
                NEWS_URL + " TEXT COLLATE NOCASE, " +
                NEWS_TITLE + " TEXT COLLATE NOCASE, " +
                NEWS_AUTHOR + " TEXT COLLATE NOCASE, " +
                NEWS_SCORE + " INTEGER, " +
                NEWS_CREATION_DATE + " INTEGER, " +
                NEWS_LOCAL_PATH + " TEXT)";

        private static final String DROP_TABLE_NEWS = "DROP TABLE IF EXISTS " + TABLE_NEWS;

        private Context context;

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_NEWS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                db.execSQL(DROP_TABLE_NEWS);
                onCreate(db);
            } catch (SQLException e) {

            }
        }

    }
}
