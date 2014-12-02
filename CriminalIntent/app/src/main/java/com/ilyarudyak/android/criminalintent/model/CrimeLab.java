package com.ilyarudyak.android.criminalintent.model;

import android.content.Context;
import android.util.Log;

import com.ilyarudyak.android.criminalintent.data.CriminalIntentJSONSerializer;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by ilyarudyak on 02/12/14.
 */
public class CrimeLab {

    private static final String TAG = CrimeLab.class.getSimpleName();
    private static final String FILENAME = "crimes.json";

    private ArrayList<Crime> mCrimes;
    private CriminalIntentJSONSerializer mSerializer;

    private static CrimeLab sCrimeLab;
    private Context mAppContext;

    private CrimeLab(Context appContext) {
        mAppContext = appContext;
        mCrimes = new ArrayList<Crime>();
        mSerializer = new CriminalIntentJSONSerializer(
                mAppContext, FILENAME);
    }

    public static CrimeLab get(Context c) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    public Crime getCrime(UUID id) {
        for (Crime c : mCrimes) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }

    public void addCrime(Crime c) {
        mCrimes.add(c);
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public boolean saveCrimes() {

        try {
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "crimes saved to file");
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error saving crimes: " + e);
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "Error saving crimes: " + e);
            return false;
        }

    }
}















