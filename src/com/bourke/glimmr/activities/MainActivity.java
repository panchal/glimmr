package com.bourke.glimmr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;

import com.androidquery.AQuery;

import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends BaseActivity {

    private static final String TAG = "Glimmr/MainActivity";

    public static final int PHOTOSTREAM_PAGE = 0;
    public static final int CONTACTS_PAGE = 1;
    public static final int GROUPS_PAGE = 2;

    //TODO: add to R.strings
    public static final String[] CONTENT =
        new String[] { "You", "Contacts", "Groups" };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME,
                Context.MODE_PRIVATE);
        mOAuth = loadAccessToken(prefs);

        if (mOAuth == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            setContentView(R.layout.main);
            mAq = new AQuery(this);
            initViewPager();
            getSupportActionBar().setDisplayShowHomeEnabled(false);

            if (savedInstanceState != null) {
                mStackLevel = savedInstanceState.getInt("level");
            }
        }
    }

    private void initViewPager() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        GlimmrPagerAdapter adapter = new GlimmrPagerAdapter(
                getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        TabPageIndicator indicator = (TabPageIndicator) findViewById(
                R.id.indicator);
        indicator.setOnPageChangeListener(this);
        indicator.setViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    class GlimmrPagerAdapter extends FragmentPagerAdapter {
        public GlimmrPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public SherlockFragment getItem(int position) {
            switch (position) {
                case PHOTOSTREAM_PAGE:
                    return PhotoStreamGridFragment.newInstance();
                case CONTACTS_PAGE:
                    return ContactsGridFragment.newInstance();
                case GROUPS_PAGE:
                    return GroupListFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return MainActivity.CONTENT.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return MainActivity.CONTENT[position % MainActivity.CONTENT.length]
                .toUpperCase();
        }
    }
}
