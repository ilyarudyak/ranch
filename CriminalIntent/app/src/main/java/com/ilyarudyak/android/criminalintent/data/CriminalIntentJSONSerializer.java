package com.ilyarudyak.android.criminalintent.data;

import android.content.Context;

import com.ilyarudyak.android.criminalintent.model.Crime;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by ilyarudyak on 02/12/14.
 */
public class CriminalIntentJSONSerializer {

    private Context mContext;
    private String mFilename;

    public CriminalIntentJSONSerializer(Context c, String f) {
        mContext = c;
        mFilename = f;
    }

    public void saveCrimes(ArrayList<Crime> crimes)
            throws JSONException, FileNotFoundException {

        // Build an array in JSON
        JSONArray array = new JSONArray();
        for (Crime c : crimes)
            array.put(c.toJSON());

        // write the file to disk
        PrintWriter out = null;

        try {
            out = new PrintWriter(mContext.openFileOutput(
                     mFilename, Context.MODE_PRIVATE));
            out.print(array.toString());
        } finally {
            if (out != null)
                out.close();
        }

    }
}




















