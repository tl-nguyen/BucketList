package com.telerikacademy.jasmine.thebucketlistapp.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

import com.telerikacademy.jasmine.thebucketlistapp.R;
import com.telerikacademy.jasmine.thebucketlistapp.activities.fragments.BraggersFragment;
import com.telerikacademy.jasmine.thebucketlistapp.activities.fragments.GoalsFragment;
import com.telerikacademy.jasmine.thebucketlistapp.activities.fragments.IdeasFragment;

import java.util.Locale;

/**
 * A {@link android.support.v13.app.FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public SectionsPagerAdapter(Context ctx, FragmentManager fm) {
        super(fm);
        this.context = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();

        switch (position) {
            case 0:
                return new GoalsFragment();
            case 1:
                return new BraggersFragment();
            case 2:
                return new IdeasFragment();
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return this.context.getString(R.string.goalsPageTitle).toUpperCase(l);
            case 1:
                return this.context.getString(R.string.braggersPageTitle).toUpperCase(l);
            case 2:
                return this.context.getString(R.string.ideasPageTitle).toUpperCase(l);
        }
        return null;
    }
}
