package com.ilyarudyak.android.criminalintent;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
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

        public static final String TAG = CrimeFragment.class.getSimpleName();
        public static final String EXTRA_CRIME_ID = "criminalintent.CRIME_ID";
        private static final int REQUEST_CONTACT = 2;

        private Crime mCrime;
        private Button mDateButton;
        private CheckBox mSolvedCheckBox;
        private EditText mTitleField;
        private Button mSuspectButton;
        private Button mCallSuspect;
        private Button mReportButton;

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

            mSuspectButton = (Button) rootView.findViewById(R.id.crime_suspectButton);
            mSuspectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_PICK,
                            ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(i, REQUEST_CONTACT);
                }
            });
            if (mCrime.getSuspect() != null) {
                mSuspectButton.setText(mCrime.getSuspect());
            }

            mCallSuspect = (Button) rootView.findViewById(R.id.crime_callButton);
            mCallSuspect.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (mCrime.getSuspectPhone() != null) {
                        Uri number = Uri.parse("tel:" + mCrime.getSuspectPhone());
                        Intent i = new Intent(Intent.ACTION_DIAL);
                        i.setData(number);
                        startActivity(i);
                    }
                }
            });

            mReportButton = (Button) rootView.findViewById(R.id.crime_reportButton);
            mReportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                    i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                    startActivity(i);
                }
            });


            return rootView;
        }

        @Override
        public void onPause() {
            super.onPause();
            CrimeLab.get(getActivity()).saveCrimes();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode != Activity.RESULT_OK) return;

            if (requestCode == REQUEST_CONTACT) {
                Uri contactUri = data.getData();
                String[] queryFields = new String[]{ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
                Cursor c = getActivity().getContentResolver()
                        .query(contactUri, queryFields, null, null, null);

                if (c.getCount() == 0) {
                    c.close();
                    return;
                }

                c.moveToFirst();
                String suspect = c.getString(c.getColumnIndex(ContactsContract
                        .Contacts.DISPLAY_NAME_PRIMARY));
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);

                // get suspect phone number
                String suspectId =
                        c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                Log.d(TAG, "suspect id = " + suspectId);
                mCallSuspect.setText(getSuspectPhone(suspectId));

                c.close();
            }
        }

        private String getSuspectPhone(String suspectId) {

            Cursor phones = getActivity().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " +
                    suspectId, null, null);

            try {
                while (phones.moveToNext()) {
                    String number = phones.getString(phones.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.d(TAG, "phone number: " + number);
                    mCrime.setSuspectPhone(number);
                    return number;
//                int type = phones.getInt(phones.getColumnIndex(
//                        ContactsContract.CommonDataKinds.Phone.TYPE));
//                switch (type) {
//                    case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
//                        // do something with the Home number here...
//                        Log.d(TAG, "home number: " + number);
//                        break;
//                    case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
//                        // do something with the Mobile number here...
//                        Log.d(TAG, "home number: " + number);
//                        break;
//                    case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
//                        // do something with the Work number here...
//                        Log.d(TAG, "home number: " + number);
//                        break;
//                }
                }
            } finally {
                if (phones != null)
                    phones.close();
            }

            return "no phone found";
        }

        // -------------------- crime report ----------------------

        private String getCrimeReport() {
            String solvedString = null;
            if (mCrime.isSolved()) {
                solvedString = getString(R.string.crime_report_solved);
            } else {
                solvedString = getString(R.string.crime_report_unsolved);
            }

            String dateFormat = "EEE, MMM dd";
            String dateString = DateFormat.format(dateFormat,
                    mCrime.getDate()).toString();

            String suspect = mCrime.getSuspect();
            if (suspect == null) {
                suspect = getString(R.string.crime_report_no_suspect);
            } else {
                suspect = getString(R.string.crime_report_suspect, suspect);
            }

            String report = getString(R.string.crime_report, mCrime.getTitle(),
                    dateString, solvedString, suspect);

            return report;
        }
    }
}




















