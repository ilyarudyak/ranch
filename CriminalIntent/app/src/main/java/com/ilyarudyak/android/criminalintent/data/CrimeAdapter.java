package com.ilyarudyak.android.criminalintent.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ilyarudyak.android.criminalintent.R;
import com.ilyarudyak.android.criminalintent.model.Crime;

import java.util.ArrayList;

/**
 * Created by ilyarudyak on 02/12/14.
 */
public class CrimeAdapter extends ArrayAdapter<Crime>  {

    private Context mContext;

    public CrimeAdapter(Context context, ArrayList<Crime> crimes) {
        super(context, android.R.layout.simple_list_item_1, crimes);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // if we weren't given a view, inflate one
        if (null == convertView) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.list_item_crime, null);
        }

        // configure the view for this Crime
        Crime c = getItem(position);

        TextView titleTextView =
                (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
        titleTextView.setText(c.getTitle());
        TextView dateTextView =
                (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
        dateTextView.setText(c.getDate().toString());
        CheckBox solvedCheckBox =
                (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
        solvedCheckBox.setChecked(c.isSolved());

        return convertView;
    }
}
