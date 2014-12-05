package com.ilyarudyak.android.criminalintent.data;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by ilyarudyak on 05/12/14.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private static final String TAG =
            DatePickerFragment.class.getSimpleName();
    //        public static final String CRIME_ID =
//                "criminalintent.CRIME_ID";
    public static final String EXTRA_DATE =
            "criminalintent.EXTRA_DATE";

    private UUID crimeId;
    private Date mDate;


//        public static DatePickerFragment newInstance(UUID crimeID) {
//            Bundle args = new Bundle();
//            args.putSerializable(CRIME_ID, crimeID);
//
//            DatePickerFragment fragment = new DatePickerFragment();
//            fragment.setArguments(args);
//
//            return fragment;
//        }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

//            crimeId = (UUID) getArguments().getSerializable(CRIME_ID);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Log.d(TAG, month + "/" + day + "/" + year);

        mDate = new GregorianCalendar(year, month, day).getTime();
        sendResult(Activity.RESULT_OK);

//            Crime crime = CrimeLab.get(getActivity()).getCrime(crimeId);
//            crime.setDate(date);
    }

    private void sendResult(int resultCode) {
        if (getTargetFragment() == null)
            return;
        Intent i = new Intent();
        i.putExtra(EXTRA_DATE, mDate);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, i);
    }
}
