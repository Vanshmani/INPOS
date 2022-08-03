package project.bluetoothterminal;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class About extends AppCompatActivity {
    AdRequest adRequest_about;
    LinearLayout btnMore_From_Us;
    LinearLayout btnRate_us;
    LinearLayout btnShare;
    AdView mAdView_about;
    PrefManager prefManager;
    TextView txtVersion;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0605R.layout.about);
        this.prefManager = new PrefManager(this);
        this.mAdView_about = (AdView) findViewById(C0605R.C0607id.adView_about);
        this.adRequest_about = new AdRequest.Builder().addTestDevice(getResources().getString(C0605R.string.Test_adUnit_Id)).build();
        this.mAdView_about.setAdListener(new AdListener() {
            public void onAdLoaded() {
                About.this.mAdView_about.setVisibility(0);
            }
        });
        this.txtVersion = (TextView) findViewById(C0605R.C0607id.txtVersion);
        this.txtVersion.setText(BuildConfig.VERSION_NAME);
        this.btnRate_us = (LinearLayout) findViewById(C0605R.C0607id.btnRate_us);
        this.btnMore_From_Us = (LinearLayout) findViewById(C0605R.C0607id.btnMore_From_Us);
        this.btnShare = (LinearLayout) findViewById(C0605R.C0607id.btnShare);
        this.btnRate_us.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                About.this.launchMarketApp();
            }
        });
        this.btnMore_From_Us.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                About.this.launchMarket_More_Apps();
            }
        });
        this.btnShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.SUBJECT", "Bluetooth Terminal HC-05");
                intent.putExtra("android.intent.extra.TEXT", "\nHey,\nCheck out this\n" + "https://play.google.com/store/apps/details?id=project.bluetoothterminal&hl=en \n");
                About.this.startActivity(Intent.createChooser(intent, "Share app with"));
            }
        });
    }

    /* access modifiers changed from: private */
    public void launchMarketApp() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(this, " unable to find market app", 1).show();
        }
    }

    /* access modifiers changed from: private */
    public void launchMarket_More_Apps() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://search?q=pub:mightyIT")));
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(this, " unable to find market app", 1).show();
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
            this.mAdView_about.setVisibility(8);
        } else {
            this.mAdView_about.loadAd(this.adRequest_about);
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
