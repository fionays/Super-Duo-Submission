package it.jaschke.alexandria;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by saj on 27/01/15.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference pref = findPreference(getString(R.string.pref_startScreen_key));
        // Set the listener to watch the change
        pref.setOnPreferenceChangeListener(this);
        // Initialize the summary
        setPreferenceSummary(pref,
                PreferenceManager.getDefaultSharedPreferences(pref.getContext()).getString(pref.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference pref, Object value) {
        setPreferenceSummary(pref, value);
        return true;
    }

    /**
     * Set preference summary.
     */
    private void setPreferenceSummary(Preference pref, Object value) {
        String stringValue = value.toString();

        if (pref instanceof ListPreference) {
            ListPreference listPreference = (ListPreference)pref;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);

                Log.v("Settings", "summary = " + listPreference.getSummary());
            }
        }
    }
}
