package project.bluetoothterminal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class setting_prefs extends AppCompatActivity {
    static AdRequest adRequest;
    static InterstitialAd mInterstitialAd;
    private final int Ads_Setting_Counter_Limit = 5;
    AdView mAdView;
    PrefManager prefManager;
    TextView txtAdsFree_tag;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0605R.layout.setting_prefs);
        this.prefManager = new PrefManager(this);
        this.txtAdsFree_tag = (TextView) findViewById(C0605R.C0607id.txtAdfree_Tag);
        this.mAdView = (AdView) findViewById(C0605R.C0607id.adViewfooter_Setting);
        adRequest = new AdRequest.Builder().addTestDevice(getResources().getString(C0605R.string.Test_adUnit_Id)).build();
        this.mAdView.setAdListener(new AdListener() {
            public void onAdLoaded() {
                setting_prefs.this.mAdView.setVisibility(0);
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(C0605R.string.fullpage_ad_unit_id));
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed() {
            }

            public void onAdFailedToLoad(int i) {
            }

            public void onAdLeftApplication() {
            }

            public void onAdLoaded() {
                setting_prefs.mInterstitialAd.show();
            }

            public void onAdOpened() {
                setting_prefs.this.prefManager.setAd_setting_page_Counter(-1);
            }
        });
        getFragmentManager().beginTransaction().replace(C0605R.C0607id.pref_setting, new MainPreferenceFragment()).commit();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        Log.w("Setting page", "On resume");
        getWindow().clearFlags(128);
        if (this.prefManager.getKeepscreen()) {
            Log.d("Keep", "true");
            getWindow().addFlags(128);
        }
        if (MainActivity.mIsPremium != null && !MainActivity.mIsPremium.booleanValue()) {
            this.mAdView.loadAd(adRequest);
            if (this.prefManager.getAd_setting_page_Counter() >= 5) {
                mInterstitialAd.loadAd(adRequest);
            }
            PrefManager prefManager2 = this.prefManager;
            prefManager2.setAd_setting_page_Counter(prefManager2.getAd_setting_page_Counter());
            Log.d("Ads_Counter_setting", "" + this.prefManager.getAd_setting_page_Counter());
        } else if (MainActivity.mIsPremium == null || !MainActivity.mIsPremium.booleanValue()) {
            this.mAdView.setVisibility(8);
        } else {
            this.mAdView.setVisibility(8);
            this.txtAdsFree_tag.setVisibility(0);
        }
    }

    public static class MainPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            addPreferencesFromResource(C0605R.xml.setting_prefs);
            PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
        }

        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        public void onResume() {
            super.onResume();
            for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
                Preference preference = getPreferenceScreen().getPreference(i);
                if (preference instanceof PreferenceGroup) {
                    PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                    for (int i2 = 0; i2 < preferenceGroup.getPreferenceCount(); i2++) {
                        Preference preference2 = preferenceGroup.getPreference(i2);
                        updatePreference(preference2, preference2.getKey());
                    }
                } else {
                    updatePreference(preference, preference.getKey());
                }
            }
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            Log.d("Update", "OK");
            updatePreference(findPreference(str), str);
            if (str.equals("ButtonRow") && MainActivity.mIsPremium != null && !MainActivity.mIsPremium.booleanValue()) {
                setting_prefs.mInterstitialAd.loadAd(setting_prefs.adRequest);
            }
        }

        private void updatePreference(Preference preference, String str) {
            if (preference != null && (preference instanceof ListPreference)) {
                ListPreference listPreference = (ListPreference) preference;
                listPreference.setSummary(listPreference.getEntry());
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
