package project.bluetoothterminal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FAQs extends AppCompatActivity {
    Button btnSend_Mail;
    PrefManager prefManager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0605R.layout.faq);
        this.prefManager = new PrefManager(this);
        this.btnSend_Mail = (Button) findViewById(C0605R.C0607id.btnSend_Mail);
        this.btnSend_Mail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FAQs.this.sendEmail();
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
    }

    /* access modifiers changed from: protected */
    public void sendEmail() {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse("mailto:it@memighty.com?subject=FAQ - Bluetooth Terminal HC-05&body="));
            startActivity(intent);
        } catch (Exception unused) {
            Toast.makeText(getApplicationContext(), " You don't have any app for mail", 0).show();
        }
    }
}
