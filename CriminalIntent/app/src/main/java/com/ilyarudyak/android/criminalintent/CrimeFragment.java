package com.ilyarudyak.android.criminalintent;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.ilyarudyak.android.criminalintent.model.Photo;
import com.ilyarudyak.android.criminalintent.utility.PictureUtils;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * A placeholder fragment containing a simple view.
 */
public class CrimeFragment extends Fragment {

    public static final String TAG = CrimeFragment.class.getSimpleName();
    public static final String EXTRA_CRIME_ID = "criminalintent.CRIME_ID";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;
    private static final int REQUEST_CONTACT = 2;

    private Crime mCrime;
    private String mFilename;

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
//                String filename = UUID.randomUUID().toString() + ".jpeg";
//                Log.d(TAG, filename);

                // create file path in external storage
                File dir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);

                File outputFile = new File(dir, PictureUtils.generateFileName());
                mFilename = outputFile.toString();
                Log.d(TAG, mFilename);

                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));

                // start activity
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) rootView.findViewById(R.id.crime_imageView);


        // --------------- date and status buttons ------------------------

        mDateButton = (Button)rootView.findViewById(R.id.crime_date);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerFragment dialog = new DatePickerFragment();
                dialog.show(getFragmentManager(), "datePicker");
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
            }
        });
        mDateButton.setText(mCrime.getDate().toString());

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

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
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
        } else if (requestCode == REQUEST_PHOTO) {

            Log.d(TAG, "intent received ...");
            Photo p = new Photo(mFilename);
            mCrime.setPhoto(p);
            showPhoto();

        } else if (requestCode == REQUEST_CONTACT) {
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
            fetchSuspectPhone(suspectId);
            setCallSuspect();

            c.close();
        }
    }

    // --------------------- some helper methods --------------------------

    private void setCallSuspect() {
        if (mCrime.getSuspectPhone() != null)
            mCallSuspect.setText(mCrime.getSuspectPhone());
    }

    private void fetchSuspectPhone(String suspectId) {

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
            }
        } finally {
            if (phones != null)
                phones.close();
        }

    }

    private void showPhoto() {
        // (re)set the image button's image based on our photo
        Photo p = mCrime.getPhoto();
        BitmapDrawable b = null;
        if (p != null) {
            String path = p.getFilename();
            b = PictureUtils.getScaledDrawable(getActivity(), path);
        }
        mPhotoView.setImageDrawable(b);
    }

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
