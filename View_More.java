package project.bluetoothterminal;

import android.content.Intent;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class View_More extends AppCompatActivity {
    static AdRequest adRequest_ViewMore;
    static AdView mAdView_ViewMore;
    PrefManager prefManager;
    TextView txtAbout;
    TextView txtContact;
    TextView txtFAQs;
    TextView txtSupport;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0605R.layout.view_more);
        this.prefManager = new PrefManager(this);
        mAdView_ViewMore = (AdView) findViewById(C0605R.C0607id.adView_ViewMore);
        adRequest_ViewMore = new AdRequest.Builder().addTestDevice(getResources().getString(C0605R.string.Test_adUnit_Id)).build();
        mAdView_ViewMore.setAdListener(new AdListener() {
            public void onAdLoaded() {
                View_More.mAdView_ViewMore.setVisibility(0);
            }
        });
        this.txtContact = (TextView) findViewById(C0605R.C0607id.txtContact);
        this.txtFAQs = (TextView) findViewById(C0605R.C0607id.txtFAQs);
        this.txtSupport = (TextView) findViewById(C0605R.C0607id.txtSupport);
        this.txtAbout = (TextView) findViewById(C0605R.C0607id.txtAbout);
        this.txtContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                View_More.this.startActivity(new Intent(View_More.this, ContactUs.class));
            }
        });
        this.txtFAQs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                View_More.this.startActivity(new Intent(View_More.this, FAQs.class));
            }
        });
        this.txtSupport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                View_More.this.startActivity(new Intent(View_More.this, C0621support.class));
            }
        });
        this.txtAbout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                View_More.this.startActivity(new Intent(View_More.this, About.class));
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
            mAdView_ViewMore.setVisibility(8);
        } else {
            mAdView_ViewMore.loadAd(adRequest_ViewMore);
        }
    }
}
