package project.bluetoothterminal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/* renamed from: project.bluetoothterminal.support */
public class C0621support extends AppCompatActivity {
    AdRequest adRequest_support;
    Button btnSend_Mail;
    AdView mAdView_support;
    PrefManager prefManager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0605R.layout.support);
        this.prefManager = new PrefManager(this);
        this.mAdView_support = (AdView) findViewById(C0605R.C0607id.adView_support);
        this.adRequest_support = new AdRequest.Builder().addTestDevice(getResources().getString(C0605R.string.Test_adUnit_Id)).build();
        this.mAdView_support.setAdListener(new AdListener() {
            public void onAdLoaded() {
                C0621support.this.mAdView_support.setVisibility(0);
            }
        });
        this.btnSend_Mail = (Button) findViewById(C0605R.C0607id.btnSend_Mail_for_support);
        this.btnSend_Mail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                C0621support.this.sendEmail();
            }
        });
    }

    /* access modifiers changed from: protected */
    public void sendEmail() {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse("mailto:it@memighty.com?subject=Support - Bluetooth Terminal HC-05&body="));
            startActivity(intent);
        } catch (Exception unused) {
            Toast.makeText(getApplicationContext(), " You don't have any app for mail", 0).show();
        }
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
            this.mAdView_support.setVisibility(8);
        } else {
            this.mAdView_support.loadAd(this.adRequest_support);
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
