package com.kurt.lemond.hackernews.activity_main.ui

import android.os.Bundle
import com.kurt.lemond.hackernews.R
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kurt.lemond.hackernews.activity_main.ui.fragment.BestStoriesFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
        return fragmentInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
                .add(R.id.frameLayout_main_container, BestStoriesFragment.newInstance())
                .commit()

    }

}
