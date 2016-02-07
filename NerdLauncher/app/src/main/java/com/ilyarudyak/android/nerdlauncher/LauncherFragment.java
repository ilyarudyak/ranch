package com.ilyarudyak.android.nerdlauncher;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LauncherFragment extends Fragment {

    public static final String TAG = LauncherFragment.class.getSimpleName();

    @Bind(R.id.fragment_nerd_launcher_recycler_view)
    RecyclerView mRecyclerView;

    public static LauncherFragment newInstance() {
        return new LauncherFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launcher, container, false);

        // set up butter knife
        ButterKnife.bind(this, view);

        // set up recycler view
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new ActivityAdapter(getActivities()));

        return view;
    }

    // helper methods
    private List<ResolveInfo> getActivities() {
        List<ResolveInfo> activities = new ArrayList<>();
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = getActivity().getPackageManager();
        activities = pm.queryIntentActivities(startupIntent, 0);
        sortActivities(activities);
        return activities;
    }
    private void sortActivities(List<ResolveInfo> activities) {
        final PackageManager pm = getActivity().getPackageManager();
        Comparator<ResolveInfo> comparator = new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo lhs, ResolveInfo rhs) {
                return lhs.loadLabel(pm).toString().compareToIgnoreCase(rhs.loadLabel(pm).toString());
            }
        };
        Collections.sort(activities, comparator);
    }

    // RecyclerView classes
    private class LauncherHolder extends RecyclerView.ViewHolder {

        private TextView mNameTextView;

        public LauncherHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView;
        }

        public void bindActivity(ResolveInfo resolveInfo) {
            PackageManager pm = getActivity().getPackageManager();
            mNameTextView.setText(resolveInfo.loadLabel(pm).toString());
        }
    }
    private class ActivityAdapter extends RecyclerView.Adapter<LauncherHolder> {

        private List<ResolveInfo> mActivities;

        public ActivityAdapter(List<ResolveInfo> activities) {
            mActivities = activities;
        }

        @Override
        public LauncherHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new LauncherHolder(view);
        }

        @Override
        public void onBindViewHolder(LauncherHolder holder, int position) {
            ResolveInfo resolveInfo = mActivities.get(position);
            holder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount() {
            return mActivities.size();
        }
    }

}


















