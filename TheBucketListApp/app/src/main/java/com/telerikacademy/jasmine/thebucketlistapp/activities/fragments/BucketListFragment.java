package com.telerikacademy.jasmine.thebucketlistapp.activities.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telerikacademy.jasmine.thebucketlistapp.R;

public class BucketListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bucket_list, container, false);
        return rootView;
    }
}