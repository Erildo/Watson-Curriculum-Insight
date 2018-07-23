package com.example.dell.ora;

import android.app.SearchManager;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class Maino extends AppCompatActivity {
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private FirebaseFirestore fb;
    private FloatingActionButton myFab;
    private JobsFragment jobsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maino);
        toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        jobsFragment=new JobsFragment();
        fb = FirebaseFirestore.getInstance();

        myFab = findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Maino.this,JobPost.class));
            }
        });
        tabLayout =  findViewById(R.id.tabLayout);
        viewPager =  findViewById(R.id.viewPG);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                                              @Override
                                              public void onPageSelected(int position) {
               if(position == 0) {
              myFab.setVisibility(View.VISIBLE);
                } else {
               myFab.setVisibility(View.GONE);
                   }        }
                                          });
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
 final SearchView searchView=(SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                Query querySearch = fb.collection("jobs").whereEqualTo("title", text);
                if (!text.trim().isEmpty()){
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    List<Fragment> fragments = getSupportFragmentManager().getFragments();
                    for (Fragment fragment : fragments) {
                        String fragmentTag = fragment.getTag();
                        if (fragmentTag != null) {
                            String lastCharacter = fragmentTag.substring(fragmentTag.length() - 1);
                            if (lastCharacter.equals("0")) {
                                JobsFragment jobsFragment = (JobsFragment) fragmentManager.findFragmentByTag(fragmentTag);
                                jobsFragment.jobView(querySearch);
                            }
                        }
                    }
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            onBackPressed();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        Maino.ViewPagerAdapter adapter = new Maino.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new JobsFragment(), "Jobs");
        adapter.addFragment(new ChartFragment(), "Statistika");
        adapter.addFragment(new AnalyzerFragment(), "Analizo");
        adapter.addFragment(new AboutUs(), "About Us");
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
