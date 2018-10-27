package com.foodapp.android.foodapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.foodapp.android.foodapp.R;
import com.foodapp.android.foodapp.fragment.FavouriteFragment;
import com.foodapp.android.foodapp.fragment.IngredientSearchFragment;
import com.foodapp.android.foodapp.fragment.ShoppingListFragment;

public class MainActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * ViewPager that will host the section contents.
     */
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private NavigationView navView;

    ProgressBar loadbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // Keeps lifecycle of Fragment - Prevents destroy from shopping list to search
        mViewPager.setOffscreenPageLimit(2);

        // Set up the tabs with icons and text
        final int[] ICONS = new int[]{
                R.drawable.search1,
                R.drawable.favourite
        };
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);
//        tabLayout.getTabAt(0).setIcon(ICONS[0]).setText(R.string.tab_text_1);
//        tabLayout.getTabAt(1).setIcon(ICONS[1]).setText(R.string.tab_text_2);
//        tabLayout.getTabAt(2).setIcon(ICONS[0]).setText("SHOPPING LIST");

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });

    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch(menuItem.getItemId()) {

            case R.id.nav_logout:

                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                SharedPreference.clearUserName(getApplicationContext());
                finish();

                break;

            case R.id.messaging:
                Intent intent = new Intent(MainActivity.this, MessageInboxActivity.class);
                startActivity(intent);
            default:
                break;

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            SharedPreference.clearUserName(getApplicationContext());
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * FragmentPagerAdapter that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            switch (position){
                case 0:
                    IngredientSearchFragment fragment1 = new IngredientSearchFragment();
                    return fragment1;
                case 1:
                    FavouriteFragment fragment2 = new FavouriteFragment();
                    return fragment2;
                case 2:
                    ShoppingListFragment fragment3 = new ShoppingListFragment();
                    return fragment3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

//    //Refreshes activity if user unfavourites a recipe
//    @Override
//    public void onRestart()
//    {  // After a pause OR at startup
//        super.onRestart();
//        Intent intent = getIntent();
//        finish();
//        startActivity(intent);
//
//    }


    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            Log.i("DEAD","DEAD");
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }
}
