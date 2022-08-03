package project.bluetoothterminal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ContactUs extends AppCompatActivity {
    static AdRequest adRequest_ContactUs;
    static AdView mAdView_ContactUs;
    TextView email;
    PrefManager prefManager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0605R.layout.contactus);
        this.prefManager = new PrefManager(this);
        mAdView_ContactUs = (AdView) findViewById(C0605R.C0607id.adView_Contactus);
        adRequest_ContactUs = new AdRequest.Builder().addTestDevice(getResources().getString(C0605R.string.Test_adUnit_Id)).build();
        mAdView_ContactUs.setAdListener(new AdListener() {
            public void onAdLoaded() {
                ContactUs.mAdView_ContactUs.setVisibility(0);
            }
        });
        this.email = (TextView) findViewById(C0605R.C0607id.txtEmail);
        this.email.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ContactUs.this.sendEmail();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        getWindow().clearFlags(128);
        if (this.prefManager.getKeepscreen()) {
            Log.d("Keep", "true");
            getWindow().addFlags(128);
        }
        if (MainActivity.mIsPremium == null || MainActivity.mIsPremium.booleanValue()) {
            mAdView_ContactUs.setVisibility(8);
        } else {
            mAdView_ContactUs.loadAd(adRequest_ContactUs);
        }
    }

    /* access modifiers changed from: protected */
    public void sendEmail() {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse("mailto:it@memighty.com?subject=Contact US - Bluetooth Terminal HC-05&body="));
            startActivity(intent);
        } catch (Exception unused) {
            Toast.makeText(getApplicationContext(), " You don't have any app for mail", 0).show();
        }
    }
}
