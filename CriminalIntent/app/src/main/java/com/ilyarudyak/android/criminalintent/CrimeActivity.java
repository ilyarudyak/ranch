package com.ilyarudyak.android.criminalintent;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.ilyarudyak.android.criminalintent.model.Crime;
import com.ilyarudyak.android.criminalintent.model.CrimeLab;
import com.ilyarudyak.android.criminalintent.ui.CrimePagerAdapter;

import java.util.ArrayList;
import java.util.UUID;


public class CrimeActivity extends Activity {

    private ViewPager mViewPager;
    private  ArrayList<Crime> mCrimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mCrimes = CrimeLab.get(this).getCrimes();

        FragmentManager fm = getFragmentManager();
        mViewPager.setAdapter(new CrimePagerAdapter(fm, mCrimes));
        startAdapterWithCurrentItem();
    }

    private void startAdapterWithCurrentItem() {
        UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crime, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class CrimeFragment extends Fragment {

        public static final String EXTRA_CRIME_ID = "criminalintent.CRIME_ID";

        private Crime mCrime;
        private Button mDateButton;
        private CheckBox mSolvedCheckBox;

        private EditText mTitleField;

        public static CrimeFragment newInstance(UUID crimeId) {
            Bundle args = new Bundle();
            args.putSerializable(EXTRA_CRIME_ID, crimeId);

            CrimeFragment fragment = new CrimeFragment();
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            mCrime = new Crime();
//            UUID crimeId = (UUID)getActivity().getIntent()
//                    .getSerializableExtra(EXTRA_CRIME_ID);
            UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
            mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        }

        public CrimeFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_crime, container, false);

            mTitleField = (EditText)rootView.findViewById(R.id.crime_title);
            mTitleField.setText(mCrime.getTitle());
            mTitleField.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence c, int start, int before, int count) {
                    mCrime.setTitle(c.toString());
                }

                public void beforeTextChanged(CharSequence c, int start, int count, int after) {
                    // this space intentionally left blank
                }

                public void afterTextChanged(Editable c) {
                    // this one too
                }
            });

            mDateButton = (Button)rootView.findViewById(R.id.crime_date);
            mDateButton.setText(mCrime.getDate().toString());
            mDateButton.setEnabled(false);

            mSolvedCheckBox = (CheckBox)rootView.findViewById(R.id.crime_solved);
            mSolvedCheckBox.setChecked(mCrime.isSolved());
            mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // set the crime's solved property
                    mCrime.setSolved(isChecked);
                }
            });

            return rootView;
        }

        @Override
        public void onPause() {
            super.onPause();
            CrimeLab.get(getActivity()).saveCrimes();
        }
    }
}




















