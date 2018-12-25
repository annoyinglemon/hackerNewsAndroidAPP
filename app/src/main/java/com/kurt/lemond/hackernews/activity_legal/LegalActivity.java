package com.kurt.lemond.hackernews.activity_legal;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.webkit.WebView;

import com.kurt.lemond.hackernews.R;

public class LegalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        WebView wvLegalNotices = (WebView) findViewById(R.id.wvLegalNotices);
        wvLegalNotices.loadUrl("file:///android_asset/software_notices.html");
    }

}
