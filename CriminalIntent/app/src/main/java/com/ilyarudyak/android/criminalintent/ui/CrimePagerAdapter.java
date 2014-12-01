package com.ilyarudyak.android.criminalintent.ui;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.ilyarudyak.android.criminalintent.CrimeActivity;
import com.ilyarudyak.android.criminalintent.model.Crime;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by ilyarudyak on 02/12/14.
 */
public class CrimePagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Crime> mCrimes;

    public CrimePagerAdapter(FragmentManager fm, ArrayList<Crime> mCrimes) {
        super(fm);
        this.mCrimes = mCrimes;
    }

    @Override
    public Fragment getItem(int position) {
        UUID crimeId =  mCrimes.get(position).getId();
        return CrimeActivity.CrimeFragment.newInstance(crimeId);
    }

    @Override
    public int getCount() {
        return mCrimes.size();
    }
}
