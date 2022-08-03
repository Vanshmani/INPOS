package project.bluetoothterminal;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.p000v4.app.ActivityCompat;
import android.support.p000v4.view.PointerIconCompat;
import android.support.p000v4.view.ViewCompat;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import project.bluetoothterminal.util.IabHelper;
import project.bluetoothterminal.util.IabResult;
import project.bluetoothterminal.util.Inventory;

public class MainActivity extends AppCompatActivity {
    static final String ITEM_SKU = "bluetooth_terminal_hc05";
    static ListView lstPair;
    public static BluetoothAdapter mBluetoothAdapter = null;
    static Boolean mIsPremium;
    public static Set<String> mPairedDevicesSet = new HashSet();
    private final int Ads_Counter_Limit = 15;
    String Message = "This app does not use location information, but scanning for Bluetooth devices requires this permission.\n\nThe scan result can contain location information, so Google decided that starting with Android 6.0 apps have to request permission.";
    String Title = "Location permission required";
    public Activity activity;
    AdRequest adRequest;
    AdView mAdView;
    IabHelper mHelper;
    /* access modifiers changed from: private */
    public InterstitialAd mInterstitialAd;
    private final ArrayList<String> mPairedDevicesList = new ArrayList<>();
    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult iabResult, Inventory inventory) {
            if (iabResult.isFailure()) {
                Toast.makeText(MainActivity.this.getApplicationContext(), "Failed", 0).show();
                return;
            }
            MainActivity.mIsPremium = Boolean.valueOf(inventory.hasPurchase(MainActivity.ITEM_SKU));
            if (!MainActivity.mIsPremium.booleanValue()) {
                Log.d("HC-05 Terminal", "is not Premium");
                MainActivity.this.mAdView.loadAd(MainActivity.this.adRequest);
                return;
            }
            Log.d("HC-05 Terminal", "is Premium");
        }
    };
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.bluetooth.device.action.BOND_STATE_CHANGED".equals(intent.getAction())) {
                int intExtra = intent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", Integer.MIN_VALUE);
                int intExtra2 = intent.getIntExtra("android.bluetooth.device.extra.PREVIOUS_BOND_STATE", Integer.MIN_VALUE);
                if (intExtra == 12 && intExtra2 == 11) {
                    MainActivity.this.paired();
                } else if (intExtra == 10 && intExtra2 == 12) {
                    MainActivity.this.paired();
                }
            }
        }
    };
    private CustomArrayAdapter_Paired pairedDevicesArrayAdapter;
    PrefManager prefManager;
    /* access modifiers changed from: private */
    public boolean sentToSettings = false;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0605R.layout.activity_main);
        In_App_setup();
        this.prefManager = new PrefManager(this);
        this.activity = this;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        MobileAds.initialize(this, getResources().getString(C0605R.string.Admob_app_id));
        this.mAdView = (AdView) findViewById(C0605R.C0607id.adView);
        this.adRequest = new AdRequest.Builder().addTestDevice(getResources().getString(C0605R.string.Test_adUnit_Id)).build();
        this.mAdView.setAdListener(new AdListener() {
            public void onAdLoaded() {
                MainActivity.this.mAdView.setVisibility(0);
            }
        });
        this.mInterstitialAd = new InterstitialAd(this);
        this.mInterstitialAd.setAdUnitId(getResources().getString(C0605R.string.fullpage_ad_unit_id));
        this.mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed() {
            }

            public void onAdFailedToLoad(int i) {
            }

            public void onAdLeftApplication() {
            }

            public void onAdLoaded() {
                MainActivity.this.mInterstitialAd.show();
            }

            public void onAdOpened() {
                MainActivity.this.prefManager.setAd_mainlaunch_page_Counter(-1);
            }
        });
        lstPair = (ListView) findViewById(C0605R.C0607id.lstPair);
        this.pairedDevicesArrayAdapter = new CustomArrayAdapter_Paired(this.mPairedDevicesList, this);
        lstPair = (ListView) findViewById(C0605R.C0607id.lstPair);
        lstPair.setAdapter(this.pairedDevicesArrayAdapter);
        lstPair.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (MainActivity.mBluetoothAdapter.isEnabled()) {
                    MainActivity.mBluetoothAdapter.cancelDiscovery();
                    String obj = MainActivity.lstPair.getItemAtPosition(i).toString();
                    if (obj != null) {
                        String substring = obj.substring(obj.length() - 17);
                        Intent intent = new Intent(MainActivity.this, ChatRoom.class);
                        intent.putExtra("MAC_Add", substring.toUpperCase());
                        MainActivity.this.startActivityForResult(intent, 1);
                        return;
                    }
                    return;
                }
                MainActivity.this.startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 3);
            }
        });
        registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
        if (mBluetoothAdapter == null) {
            Toast.makeText(this.activity, "Bluetooth is not available", 1).show();
            this.activity.finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        BluetoothAdapter bluetoothAdapter = mBluetoothAdapter;
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
        unregisterReceiver(this.mReceiver);
    }

    public static void doDiscovery() {
        BluetoothAdapter bluetoothAdapter = mBluetoothAdapter;
        if (bluetoothAdapter == null) {
            return;
        }
        if (bluetoothAdapter.isDiscovering()) {
            Log.d("TAG", "Cancel Discovery()");
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothAdapter.startDiscovery();
            return;
        }
        mBluetoothAdapter.startDiscovery();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        getWindow().clearFlags(128);
        if (this.prefManager.getKeepscreen()) {
            Log.d("Keep", "true");
            getWindow().addFlags(128);
        } else {
            Log.d("Keep", "false");
        }
        Boolean bool = mIsPremium;
        if (bool == null || bool.booleanValue()) {
            this.mAdView.setVisibility(8);
        } else {
            this.mAdView.loadAd(this.adRequest);
            if (this.prefManager.getAd_mainlaunch_page_Counter() >= 15) {
                this.mInterstitialAd.loadAd(this.adRequest);
            }
        }
        PrefManager prefManager2 = this.prefManager;
        prefManager2.setAd_mainlaunch_page_Counter(prefManager2.getAd_mainlaunch_page_Counter());
        Log.d("Ads_Counter_MainLaunch", "" + this.prefManager.getAd_mainlaunch_page_Counter());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0605R.C0609menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId != C0605R.C0607id.action_View_More) {
            switch (itemId) {
                case C0605R.C0607id.action_scan /*2131296288*/:
                    checkForLocationPermission();
                    return true;
                case C0605R.C0607id.action_setting /*2131296289*/:
                    startActivity(new Intent(this, setting_prefs.class));
                    return true;
                default:
                    return super.onOptionsItemSelected(menuItem);
            }
        } else {
            startActivity(new Intent(this, View_More.class));
            return true;
        }
    }

    public void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 3);
            return;
        }
        paired();
        check_firstTime();
    }

    public void check_firstTime() {
        if (this.prefManager.isFirstTimeLaunch()) {
            alert_Dialog();
            Log.d("firsttime", "OK");
            this.prefManager.setFirstTimeLaunch(false);
        }
    }

    public void paired() {
        Log.d("Paired", "Call");
        mPairedDevicesSet.clear();
        this.mPairedDevicesList.clear();
        this.pairedDevicesArrayAdapter.notifyDataSetChanged();
        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
        if (bondedDevices.size() > 0) {
            lstPair.setEnabled(true);
            for (BluetoothDevice next : bondedDevices) {
                mPairedDevicesSet.add(next.getAddress());
                ArrayList<String> arrayList = this.mPairedDevicesList;
                arrayList.add(next.getName() + Constants.f59LF + next.getAddress());
                this.pairedDevicesArrayAdapter.notifyDataSetChanged();
            }
            return;
        }
        lstPair.setEnabled(false);
        this.mPairedDevicesList.add(getResources().getText(C0605R.string.none_paired).toString());
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 3) {
            if (i2 == -1) {
                paired();
                check_firstTime();
                return;
            }
            Log.d(Constants.TAG, "BT not enabled");
            Toast.makeText(this.activity, C0605R.string.bt_not_enabled_leaving, 0).show();
            this.activity.finish();
        }
    }

    public static void unpairDevice(String str) {
        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
        if (bondedDevices.size() > 0) {
            lstPair.setEnabled(true);
            for (BluetoothDevice next : bondedDevices) {
                if ((next.getName() + Constants.f59LF + next.getAddress()).equals(str)) {
                    try {
                        next.getClass().getMethod("removeBond", (Class[]) null).invoke(next, (Object[]) null);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
    }

    public void alert_Dialog() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(50, 50, 50, 50);
        TextView textView = new TextView(this);
        TextView textView2 = new TextView(this);
        textView.setText(C0605R.string.app_name_pro);
        textView.setTextSize(16.0f);
        textView.setTypeface(textView.getTypeface(), 1);
        textView.setTextColor(Color.parseColor("#0183FF"));
        textView2.setText(C0605R.string.pro_alert_message);
        textView2.setTextSize(16.0f);
        textView2.setPadding(0, 16, 0, 0);
        textView2.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        linearLayout.addView(textView);
        linearLayout.addView(textView2);
        new AlertDialog.Builder(this).setIcon(C0605R.C0606drawable.new_app).setTitle("Buy our New App!").setView(linearLayout).setCancelable(false).setPositiveButton("Buy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + MainActivity.this.getPackageName() + "pro")));
                dialogInterface.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    private void checkForLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.ACCESS_COARSE_LOCATION")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle((CharSequence) this.Title);
                builder.setMessage((CharSequence) this.Message);
                builder.setPositiveButton((CharSequence) "Grant", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, PointerIconCompat.TYPE_CONTEXT_MENU);
                    }
                });
                builder.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            } else if (this.prefManager.getPermission().booleanValue()) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle((CharSequence) this.Title);
                builder2.setMessage((CharSequence) this.Message);
                builder2.setPositiveButton((CharSequence) "Grant", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        boolean unused = MainActivity.this.sentToSettings = true;
                        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package", MainActivity.this.getPackageName(), (String) null));
                        MainActivity.this.startActivityForResult(intent, 0);
                        Toast.makeText(MainActivity.this.getBaseContext(), "Go to Permissions to Grant Location", 1).show();
                    }
                });
                builder2.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder2.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_COARSE_LOCATION"}, PointerIconCompat.TYPE_CONTEXT_MENU);
            }
            this.prefManager.setPermission();
            return;
        }
        startActivity(new Intent(getApplicationContext(), Scan_Device_Dialog.class));
    }

    public void In_App_setup() {
        this.mHelper = new IabHelper(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiQ9SZjuzZKUtwBnHeQXbCfSwq/P2HQYdAFKgzyLAWfPSQP7zsGz3uObNfC212CcFeodOYP67/5gHdaEFCxcWpqrsoXAv+vl/aOJ14AsmO1T4+v6XvV2D+DkaAEWFbZJIljygLhlXHrOTwE4CPegWhQkwXzus4BQI4IKGLltSqrD0DNO/YPgaWQm3ENWbNolfZR+Itbo52L4yXjZH/uMsu08++MPZcDMT2hnaVlaFbpYoPUtuimyLaZRk7FnubiOzzBF+btDNP8bkKJ9IKYiC4JIwxkPO6/CiCmkjB7Zb33s2d4vwwTufSM9rSQtinE6x+LzG0ila030z1NdkY3sP7QIDAQAB");
        this.mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult iabResult) {
                if (!iabResult.isSuccess()) {
                    Log.d("TAG", "In-app Billing setup failed: " + iabResult);
                    return;
                }
                Log.d("TAG", "In-app Billing is set up OK");
                MainActivity.this.mHelper.queryInventoryAsync(MainActivity.this.mReceivedInventoryListener);
            }
        });
    }
}
