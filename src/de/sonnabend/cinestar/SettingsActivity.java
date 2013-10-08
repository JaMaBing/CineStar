package de.sonnabend.cinestar;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener
{
	public static final String INTERVAL_KEY = "interval";
	public static final int INTERVAL_MIN = 30;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings);
	}
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key)
	{
	}
	@Override
	public void onContentChanged() {
		super.onContentChanged();
    }
}