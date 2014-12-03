package com.ilyarudyak.android.criminalintent;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
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
        private ListView listView;

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

        // ----- set layout for empty crime list ------

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_list_crime, container, false);

            // this button is only for an empty view
            Button addCrime = (Button) rootView.findViewById(R.id.add_crime_button);
            addCrime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(createNewCrime(), 0);
                }
            });

            // set context menu
            listView = (ListView) rootView.findViewById(android.R.id.list);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new ContextMenuListener());


            return rootView;
        }

        // ----------------context menu --------------------

        private class ContextMenuListener implements
                AbsListView.MultiChoiceModeListener {

            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.crime_list_item_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_item_delete_crime:
                        CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
                        CrimeLab crimeLab = CrimeLab.get(getActivity());
                        for (int i = adapter.getCount() - 1; i >= 0; i--) {
                            if (listView.isItemChecked(i)) {
                                crimeLab.deleteCrime(adapter.getItem(i));
                            }
                        }
                        actionMode.finish();
                        adapter.notifyDataSetChanged();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        }

        // ----------------options menu --------------------

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.fragment_crime_list, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.menu_item_new_crime:
                    startActivityForResult(createNewCrime(), 0);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        private Intent createNewCrime() {

            Crime crime = new Crime();
            CrimeLab.get(getActivity()).addCrime(crime);
            Intent i = new Intent(getActivity(), CrimeActivity.class);
            return i.putExtra(CrimeActivity.CrimeFragment.EXTRA_CRIME_ID,
                    crime.getId());
        }
    }
}













