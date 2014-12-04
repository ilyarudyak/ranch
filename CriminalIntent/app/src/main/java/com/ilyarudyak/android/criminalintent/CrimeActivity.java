package com.ilyarudyak.android.criminalintent;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.ilyarudyak.android.criminalintent.data.DatePickerFragment;
import com.ilyarudyak.android.criminalintent.model.Crime;
import com.ilyarudyak.android.criminalintent.model.CrimeLab;
import com.ilyarudyak.android.criminalintent.ui.CrimePagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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
        private static final int REQUEST_DATE = 0;
        private static final int REQUEST_PHOTO = 1;
        private static final int REQUEST_CONTACT = 2;

        private Crime mCrime;

        // title of crime
        private EditText mTitleField;
        // image view and button to take photo
        private ImageButton mPhotoButton;
        private ImageView mPhotoView;
        // date and status
        private Button mDateButton;
        private CheckBox mSolvedCheckBox;
        // suspect buttons
        private Button mSuspectButton;
        private Button mCallSuspect;
        // report button
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

            // --------------- title of the crime -----------------------------

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

            // --------------- image and button to take photo -----------------

            mPhotoButton = (ImageButton) rootView.findViewById(R.id.crime_imageButton);
            mPhotoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // create Intent to take a picture with build-in camera
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    // Create a random filename
                    String filename = UUID.randomUUID().toString() + ".jpg";

                    // create file path in external storage
                    File dir = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES);
                    File outputFile = new File(dir, "CameraContentDemo.jpeg");
                    i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));

                    // start activity
                    startActivityForResult(i, REQUEST_PHOTO);
                }
            });

            // --------------- date and status buttons ------------------------

            mDateButton = (Button)rootView.findViewById(R.id.crime_date);
            mDateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    DatePickerFragment.newInstance(mCrime.getId())
//                            .show(getFragmentManager(), "datePicker");
                    DatePickerFragment dialog = new DatePickerFragment();
                    dialog.show(getFragmentManager(), "datePicker");
                    dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                }
            });
            mDateButton.setText(mCrime.getDate().toString());
//            mDateButton.setEnabled(false);

            mSolvedCheckBox = (CheckBox)rootView.findViewById(R.id.crime_solved);
            mSolvedCheckBox.setChecked(mCrime.isSolved());
            mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // set the crime's solved property
                    mCrime.setSolved(isChecked);
                }
            });

            // --------------- group of suspect buttons -----------------------

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
            setCallSuspect();

            // --------------- report button ---------------------------------

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

        // we save our singleton to json file as often as we can
        // we use onPause() to guarantee that object is alive
        @Override
        public void onPause() {
            super.onPause();
            CrimeLab.get(getActivity()).saveCrimes();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode != Activity.RESULT_OK) return;

            if (requestCode == REQUEST_DATE) {
                Date date = (Date)data
                        .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                mCrime.setDate(date);
                mDateButton.setText(mCrime.getDate().toString());
            }
            else if (requestCode == REQUEST_CONTACT) {
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
                getSuspectPhone(suspectId);
                setCallSuspect();

                c.close();
            }
        }

        // --------------------- some helper methods --------------------------

        private void setCallSuspect() {
            if (mCrime.getSuspectPhone() != null)
                mCallSuspect.setText(mCrime.getSuspectPhone());
        }

        private void getSuspectPhone(String suspectId) {

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
//                    return number;
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

//            return getString(R.string.crime_no_phone_found);
        }

    // -------------------- crime report ----------------------

        private String getCrimeReport() {
            String solvedString;
            if (mCrime.isSolved()) solvedString = getString(R.string.crime_report_solved);
            else solvedString = getString(R.string.crime_report_unsolved);

            String dateFormat = "EEE, MMM dd";
            String dateString = DateFormat.format(dateFormat,
                    mCrime.getDate()).toString();

            String suspect = mCrime.getSuspect();
            if (suspect == null) {
                suspect = getString(R.string.crime_report_no_suspect);
            } else {
                suspect = getString(R.string.crime_report_suspect, suspect);
            }

            return getString(R.string.crime_report, mCrime.getTitle(),
                    dateString, solvedString, suspect);
        }
    }

    // -------------------- date picker ----------------------

//    public static class DatePickerFragment extends DialogFragment
//            implements DatePickerDialog.OnDateSetListener {
//
//        private static final String TAG =
//                DatePickerFragment.class.getSimpleName();
////        public static final String CRIME_ID =
////                "criminalintent.CRIME_ID";
//        public static final String EXTRA_DATE =
//                "criminalintent.EXTRA_DATE";
//
//        private UUID crimeId;
//        private Date mDate;
//
//
////        public static DatePickerFragment newInstance(UUID crimeID) {
////            Bundle args = new Bundle();
////            args.putSerializable(CRIME_ID, crimeID);
////
////            DatePickerFragment fragment = new DatePickerFragment();
////            fragment.setArguments(args);
////
////            return fragment;
////        }
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current date as the default date in the picker
//            final Calendar c = Calendar.getInstance();
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//
////            crimeId = (UUID) getArguments().getSerializable(CRIME_ID);
//
//            // Create a new instance of DatePickerDialog and return it
//            return new DatePickerDialog(getActivity(), this, year, month, day);
//        }
//
//        public void onDateSet(DatePicker view, int year, int month, int day) {
//            // Do something with the date chosen by the user
//            Log.d(TAG, month + "/" + day + "/" + year);
//
//            mDate = new GregorianCalendar(year, month, day).getTime();
//            sendResult(Activity.RESULT_OK);
//
////            Crime crime = CrimeLab.get(getActivity()).getCrime(crimeId);
////            crime.setDate(date);
//        }
//
//        private void sendResult(int resultCode) {
//            if (getTargetFragment() == null)
//                return;
//            Intent i = new Intent();
//            i.putExtra(EXTRA_DATE, mDate);
//            getTargetFragment()
//                    .onActivityResult(getTargetRequestCode(), resultCode, i);
//        }
//    }
}




















