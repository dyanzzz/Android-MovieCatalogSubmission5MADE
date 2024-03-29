package com.submission.moviecatalogsubmission5made.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.submission.moviecatalogsubmission5made.R;
import com.submission.moviecatalogsubmission5made.fragments.favorite.MovieFavoriteFragment;
import com.submission.moviecatalogsubmission5made.fragments.favorite.TvShowFavoriteFragment;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

public class FavoriteFragment extends Fragment {

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        String movie = getResources().getString(R.string.movie_favorite);
        String tv_show = getResources().getString(R.string.tvshow_favorite);

        TabLayout tabLayout = view.findViewById(R.id.tablayout_id);
        tabLayout.addTab(tabLayout.newTab().setText(movie));
        tabLayout.addTab(tabLayout.newTab().setText(tv_show));

        ViewPager viewPager = view.findViewById(R.id.viewpager_id);

        viewPager.setAdapter(new PagerAdapter
                (getFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    class PagerAdapter extends FragmentStatePagerAdapter {
        final int mNumOfTabs;

        @SuppressLint("WrongConstant")
        PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.mNumOfTabs = NumOfTabs;
        }


        @NotNull
        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                return new MovieFavoriteFragment();
            } else {
                return  new TvShowFavoriteFragment();
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }

}
