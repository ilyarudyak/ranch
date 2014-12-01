package com.ilyarudyak.android.criminalintent;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.ilyarudyak.android.criminalintent.data.CrimeAdapter;
import com.ilyarudyak.android.criminalintent.model.Crime;
import com.ilyarudyak.android.criminalintent.model.CrimeLab;

import java.util.ArrayList;


public class CrimeListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_list);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new CrimeListFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crime_list, menu);
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
    public static class CrimeListFragment extends ListFragment {

        private static final String TAG = "CrimeListFragment";

        private ArrayList<Crime> mCrimes;

        public CrimeListFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getActivity().setTitle(R.string.crimes_title);
            mCrimes = CrimeLab.get(getActivity()).getCrimes();
            CrimeAdapter adapter = new CrimeAdapter(getActivity(), mCrimes);
            setListAdapter(adapter);
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Crime c = (Crime)(getListAdapter()).getItem(position);
            Log.d(TAG, c.getTitle() + " was clicked");
        }


    }
}













