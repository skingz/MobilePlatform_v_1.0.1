package com.leading.xmpp_client;

import com.leading.xmpp_client.tools.Constants;
import com.leading.xmpp_client.tools.LogUtil;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

public class NotificationSettingsActivity extends PreferenceActivity {
	 private static final String LOGTAG = LogUtil.makeLogTag(NotificationSettingsActivity.class);

	    public NotificationSettingsActivity() { }

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setPreferenceScreen(createPreferenceHierarchy());
	        setPreferenceDependencies();

	        CheckBoxPreference notifyPref = (CheckBoxPreference) getPreferenceManager()
	                .findPreference(Constants.SETTINGS_NOTIFICATION_ENABLED);
	        if (notifyPref.isChecked()) {
	            notifyPref.setTitle("Notifications Enabled");
	        } else {
	            notifyPref.setTitle("Notifications Disabled");
	        }
	    }

	    private PreferenceScreen createPreferenceHierarchy() {
	        Log.d(LOGTAG, "createSettingsPreferenceScreen()...");

	        PreferenceManager preferenceManager = getPreferenceManager();
	        preferenceManager
	                .setSharedPreferencesName(Constants.SHARED_PREFERENCE_NAME);
	        preferenceManager.setSharedPreferencesMode(Context.MODE_PRIVATE);

	        PreferenceScreen root = preferenceManager.createPreferenceScreen(this);

	        //        PreferenceCategory prefCat = new PreferenceCategory(this);
	        //        // inlinePrefCat.setTitle("");
	        //        root.addPreference(prefCat);

	        CheckBoxPreference notifyPref = new CheckBoxPreference(this);
	        notifyPref.setKey(Constants.SETTINGS_NOTIFICATION_ENABLED);
	        notifyPref.setTitle("Notifications Enabled");
	        notifyPref.setSummaryOn("Receive push messages");
	        notifyPref.setSummaryOff("Do not receive push messages");
	        notifyPref.setDefaultValue(Boolean.TRUE);
	        notifyPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
	                    public boolean onPreferenceChange(Preference preference,
	                            Object newValue) {
	                        boolean checked = Boolean.valueOf(newValue.toString());
	                        if (checked) {
	                            preference.setTitle("Notifications Enabled");
	                        } else {
	                            preference.setTitle("Notifications Disabled");
	                        }
	                        return true;
	                    }
	                });

	        CheckBoxPreference soundPref = new CheckBoxPreference(this);
	        soundPref.setKey(Constants.SETTINGS_SOUND_ENABLED);
	        soundPref.setTitle("Sound");
	        soundPref.setSummary("Play a sound for notifications");
	        soundPref.setDefaultValue(Boolean.TRUE);
	        // soundPref.setDependency(Constants.SETTINGS_NOTIFICATION_ENABLED);

	        CheckBoxPreference vibratePref = new CheckBoxPreference(this);
	        vibratePref.setKey(Constants.SETTINGS_VIBRATE_ENABLED);
	        vibratePref.setTitle("Vibrate");
	        vibratePref.setSummary("Vibrate the phone for notifications");
	        vibratePref.setDefaultValue(Boolean.TRUE);
	        // vibratePref.setDependency(Constants.SETTINGS_NOTIFICATION_ENABLED);
	        
	        CheckBoxPreference holdPref = new CheckBoxPreference(this);
	        holdPref.setKey(Constants.SETTINGS_HOLD_SERVICE_ENABLED);
	        holdPref.setTitle("Hold background service");
	        holdPref.setSummary("Keep background notification service from being disabled");
	        holdPref.setDefaultValue(Boolean.TRUE);
	        // soundPref.setDependency(Constants.SETTINGS_NOTIFICATION_ENABLED);

	        root.addPreference(notifyPref);
	        root.addPreference(soundPref);
	        root.addPreference(vibratePref);
	        root.addPreference(holdPref);

	        //        prefCat.addPreference(notifyPref);
	        //        prefCat.addPreference(soundPref);
	        //        prefCat.addPreference(vibratePref);
	        //        root.addPreference(prefCat);

	        return root;
	    }

	    private void setPreferenceDependencies() {
	        Preference soundPref = getPreferenceManager().findPreference(
	                Constants.SETTINGS_SOUND_ENABLED);
	        if (soundPref != null) {
	            soundPref.setDependency(Constants.SETTINGS_NOTIFICATION_ENABLED);
	        }
	        Preference vibratePref = getPreferenceManager().findPreference(
	                Constants.SETTINGS_VIBRATE_ENABLED);
	        if (vibratePref != null) {
	            vibratePref.setDependency(Constants.SETTINGS_NOTIFICATION_ENABLED);
	        }
	    }

}
