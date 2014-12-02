package com.ilyarudyak.android.criminalintent;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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


    /**
     * A fragment containing a list view of all crimes
     * using CrimeAdaptor and list of crimes from a singleton CrimeLab.
     *
     * We may start detail fragment in onListItemClick() and
     * we are hearing for changes in list of crimes from
     * detail fragment in onResume().
     */
    public static class CrimeListFragment extends ListFragment {

        private static final String TAG = "CrimeListFragment";

        private ArrayList<Crime> mCrimes;

        public CrimeListFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // set options menu
            setHasOptionsMenu(true);

            getActivity().setTitle(R.string.crimes_title);
            mCrimes = CrimeLab.get(getActivity()).getCrimes();
            CrimeAdapter adapter = new CrimeAdapter(getActivity(), mCrimes);
            setListAdapter(adapter);
        }

//        @Override
//        public void onResume() {
//            super.onResume();
//            Log.d(TAG, "notify adapter that data changed...");
//            ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
//        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Crime c = (Crime)(getListAdapter()).getItem(position);
            Log.d(TAG, c.getTitle() + " was clicked");

            // start an instance of CrimeActivity
            Intent i = new Intent(getActivity(), CrimeActivity.class);
            i.putExtra(CrimeActivity.CrimeFragment.EXTRA_CRIME_ID, c.getId());
//            startActivity(i);
            startActivityForResult(i, 0);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
        }

        // -------------------- menu --------------------

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.fragment_crime_list, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            return super.onOptionsItemSelected(item);
        }
    }
}













