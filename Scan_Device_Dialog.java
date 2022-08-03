package project.bluetoothterminal;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.HashSet;
import java.util.Set;

public class Scan_Device_Dialog extends Activity {
    Button btnCancel;
    Button btnRefresh;
    ListView lstScan;
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
            MainActivity.mBluetoothAdapter.cancelDiscovery();
            String charSequence = ((TextView) view).getText().toString();
            boolean unused = Scan_Device_Dialog.this.pairDevice(charSequence.substring(charSequence.length() - 17));
        }
    };
    /* access modifiers changed from: private */
    public ArrayAdapter<String> mNewDevicesArrayAdapter;
    /* access modifiers changed from: private */
    public final Set<String> mNewDevicesSet = new HashSet();
    /* access modifiers changed from: private */
    public final Set<BluetoothDevice> mNewDevicesSet_BT = new HashSet();
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.bluetooth.adapter.action.DISCOVERY_STARTED".equals(action)) {
                Scan_Device_Dialog.this.setTitle("Scanning new devices...");
                Log.d("TAG", "Start Discovery()");
                Scan_Device_Dialog.this.progressBar.setVisibility(0);
                Scan_Device_Dialog.this.lstScan.setVisibility(8);
                Scan_Device_Dialog.this.mNewDevicesArrayAdapter.clear();
                Scan_Device_Dialog.this.mNewDevicesSet.clear();
                Scan_Device_Dialog.this.mNewDevicesSet_BT.clear();
            }
            if ("android.bluetooth.device.action.FOUND".equals(action)) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (bluetoothDevice != null) {
                    String address = bluetoothDevice.getAddress();
                    if (!Scan_Device_Dialog.this.mNewDevicesSet.contains(address) && !MainActivity.mPairedDevicesSet.contains(address)) {
                        Scan_Device_Dialog.this.mNewDevicesSet.add(address);
                        Scan_Device_Dialog.this.mNewDevicesSet_BT.add(bluetoothDevice);
                        String name = bluetoothDevice.getName();
                        if (name == null || name.isEmpty()) {
                            name = Scan_Device_Dialog.this.getString(C0605R.string.empty_device_name);
                        }
                        ArrayAdapter access$100 = Scan_Device_Dialog.this.mNewDevicesArrayAdapter;
                        access$100.add(name + 10 + bluetoothDevice.getAddress());
                        Scan_Device_Dialog.this.lstScan.setVisibility(0);
                        Log.d("TAG", "Device Found");
                        return;
                    }
                    return;
                }
                Log.e("TAG", "Could not get parcelable extra from device: android.bluetooth.device.extra.DEVICE");
            } else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                Log.d("TAG", "Finish Discovery()");
                Scan_Device_Dialog.this.progressBar.setVisibility(8);
                Scan_Device_Dialog.this.lstScan.setVisibility(0);
                if (Scan_Device_Dialog.this.mNewDevicesSet.isEmpty()) {
                    Scan_Device_Dialog.this.mNewDevicesArrayAdapter.add(Scan_Device_Dialog.this.getResources().getText(C0605R.string.none_found).toString());
                    Scan_Device_Dialog.this.lstScan.setEnabled(false);
                }
                Scan_Device_Dialog.this.setTitle("New Scan Devices");
            } else if ("android.bluetooth.device.action.BOND_STATE_CHANGED".equals(action)) {
                int intExtra = intent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", Integer.MIN_VALUE);
                int intExtra2 = intent.getIntExtra("android.bluetooth.device.extra.PREVIOUS_BOND_STATE", Integer.MIN_VALUE);
                Log.d("Bluetooth Bond Stat", "" + intExtra);
                if (intExtra == 12 && intExtra2 == 11) {
                    Scan_Device_Dialog.this.finish();
                } else if (!(intExtra == 10 && intExtra2 == 12) && intExtra == 11) {
                    Log.d("Bonding", "Called");
                }
            }
        }
    };
    PrefManager prefManager;
    ProgressBar progressBar;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.prefManager = new PrefManager(this);
        setContentView(C0605R.layout.scan_device_dialog);
        setFinishOnTouchOutside(false);
        this.btnCancel = (Button) findViewById(C0605R.C0607id.btn_cancel);
        this.btnRefresh = (Button) findViewById(C0605R.C0607id.btn_Refresh);
        this.mNewDevicesArrayAdapter = new ArrayAdapter<>(this, C0605R.layout.device_name_scan);
        this.lstScan = (ListView) findViewById(C0605R.C0607id.lstScan);
        this.lstScan.setAdapter(this.mNewDevicesArrayAdapter);
        this.lstScan.setOnItemClickListener(this.mDeviceClickListener);
        this.progressBar = (ProgressBar) findViewById(C0605R.C0607id.progress);
        registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.device.action.FOUND"));
        registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED"));
        registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_STARTED"));
        registerReceiver(this.mReceiver, new IntentFilter("android.bluetooth.device.action.BOND_STATE_CHANGED"));
        getWindow().setFlags(4, 4);
        MainActivity.doDiscovery();
        this.btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Scan_Device_Dialog.this.finish();
            }
        });
        this.btnRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MainActivity.doDiscovery();
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
    public void onDestroy() {
        super.onDestroy();
        if (MainActivity.mBluetoothAdapter != null) {
            MainActivity.mBluetoothAdapter.cancelDiscovery();
        }
        unregisterReceiver(this.mReceiver);
    }

    /* access modifiers changed from: private */
    public boolean pairDevice(String str) {
        BluetoothDevice remoteDevice = MainActivity.mBluetoothAdapter.getRemoteDevice(str);
        try {
            Log.d("pairDevice()", "Start Pairing...");
            remoteDevice.getClass().getMethod("createBond", (Class[]) null).invoke(remoteDevice, (Object[]) null);
            return true;
        } catch (Exception e) {
            Log.e("pairDevice()", e.getMessage());
            return false;
        }
    }
}
