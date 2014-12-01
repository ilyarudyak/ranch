package com.ilyarudyak.android.criminalintent;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
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


public class CrimeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new CrimeFragment())
                    .commit();
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

        private Crime mCrime;
        private Button mDateButton;
        private CheckBox mSolvedCheckBox;

        private EditText mTitleField;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mCrime = new Crime();
        }

        public CrimeFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_crime, container, false);

            mTitleField = (EditText)rootView.findViewById(R.id.crime_title);
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
            mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // set the crime's solved property
                    mCrime.setSolved(isChecked);
                }
            });

            return rootView;
        }
    }
}




















