package com.example.flextrack_ianation.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.flextrack_ianation.fragments.DailyProgressFragment;
import com.example.flextrack_ianation.fragments.MonthlyProgressFragment;
import com.example.flextrack_ianation.fragments.WeeklyProgressFragment;

public class ProgressPagerAdapter extends FragmentPagerAdapter {
    
    private static final int NUM_PAGES = 3;
    private static final String[] TAB_TITLES = new String[]{"Daily", "Weekly", "Monthly"};
    
    public ProgressPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }
    
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DailyProgressFragment();
            case 1:
                return new WeeklyProgressFragment();
            case 2:
                return new MonthlyProgressFragment();
            default:
                return new DailyProgressFragment();
        }
    }
    
    @Override
    public int getCount() {
        return NUM_PAGES;
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }
} 