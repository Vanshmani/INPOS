package com.programmerworld.getlatitudelongitudecoordinatesfromaddress;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import android.widget.Button;
import android.widget.ListView;
//widgets variables to “call” the widgets used to create the layout
Button btnPaired;
ListView devicelist;
btnPaired = (Button)findViewById(R.id.button);
devicelist = (ListView)findViewById(R.id.listView);
import java.util.Set;
import java.util.ArrayList;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView
import android.widget.AdapterView.OnClickListener
import android.widget.TextView;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
//Creating variables to control bluetooth
private BluetoothAdapter myBluetooth = null;
private Set pairedDevices;
//checking bluetooth adapter
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if(myBluetooth == null)
        {
        //Show a mensag. that thedevice has no bluetooth adapter
        Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
        //finish apk
        finish();
        }
        else
        {
        if (myBluetooth.isEnabled())
        { }
        else
        {
        //Ask to the user turn the bluetooth on
        Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(turnBTon,1);
        }
        }
        // Listen -->OnClickListener Api
        btnPaired.setOnClickListener(new View.OnClickListener() {
//Override
public void onClick(View v)
        {
        pairedDevicesList(); //method that will be called
        }
        });
private void pairedDevicesList()
        {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0)
        {
        for(BluetoothDevice bt : pairedDevices)
        {
        list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
        }
        }
        else
        {
        Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

        }
        import android.bluetooth.BluetoothSocket;
        import android.content.Intent;
        import android.view.View;
        import android.widget.Button;
        import android.widget.SeekBar;
        import android.widget.TextView;
        import android.widget.Toast;
        import android.app.ProgressDialog;
        import android.bluetooth.BluetoothAdapter;
        import android.bluetooth.BluetoothDevice;
        import android.os.AsyncTask;
        import java.io.IOException;
        import java.util.UUID;
        Button btnOn, btnOff, btnDis;
        SeekBar brightness;
        String address = null;
private ProgressDialog progress;
        BluetoothAdapter myBluetooth = null;
        BluetoothSocket btSocket = null;
private boolean isBtConnected = false;
static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
// Class for starting the connection
private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
{
    private boolean ConnectSuccess = true; //if it's here, it's almost connected

    //Override
    protected void onPreExecute()
    {
        progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
    }

    //Override
    protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
    {
        try
        {
            if (btSocket == null || !isBtConnected)
            {
                myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                btSocket.connect();//start connection
            }
        }
        catch (IOException e)
        {
            ConnectSuccess = false;//if the try failed, you can check the exception here
        }
        return null;
    }
    //Override
    protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
    {
        super.onPostExecute(result);

        if (!ConnectSuccess)
        {
            msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
            finish();
        }
        else
        {
            msg("Connected.");
            isBtConnected = true;
        }
        progress.dismiss();
    }
}

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextTextPersonName);
        textView = findViewById(R.id.textView);
    }

    public void buttonGetCoordinates(View view){
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(editText.getText().toString(), 1);

            if (addressList != null){
                double doubleLat = addressList.get(0).getLatitude();
                double doubleLong = addressList.get(0).getLongitude();
                textView.setText("Latitude: " + String.valueOf(doubleLat)
                        + " | " + "Longitude: " + String.valueOf(doubleLong));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
    <?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">

<TextView
        android:id="@+id/textView"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Hello World!"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintLeft_toLeftOf="parent"
                app:layout_constraintRight_toRightOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

<Button
        android:id="@+id/button"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="116dp"
                android:layout_marginTop="188dp"
                android:onClick="buttonGetCoordinates"
                android:text="Get Coordinates"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

<EditText
        android:id="@+id/editTextTextPersonName"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginStart="94dp"
                android:layout_marginTop="61dp"
                android:ems="10"
                android:hint="Address"
                android:inputType="textPersonName"
                android:minHeight="48dp"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
        package="com.programmerworld.getlatitudelongitudecoordinatesfromaddress">

<application
        android:allowBackup="true"
                android:icon="@mipmap/ic_launcher"
                android:label="@string/app_name"
                android:roundIcon="@mipmap/ic_launcher_round"
                android:supportsRtl="true"
                android:theme="@style/Theme.GetLatitudeLongitudeCoordinatesFromAddress">
<activity
            android:name=".MainActivity"
                    android:exported="true">
<intent-filter>
<action android:name="android.intent.action.MAIN" />

<category android:name="android.intent.category.LAUNCHER" />
</intent-filter>
</activity>
</application>

</manifest>
        plugins {
        id 'com.android.application'
        }

        android {
        compileSdk 31

        defaultConfig {
        applicationId "com.programmerworld.getlatitudelongitudecoordinatesfromaddress"
        minSdk 31
        targetSdk 31
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        }

        buildTypes {
        release {
        minifyEnabled false
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
        }
        compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
        }
        }

        dependencies {

        implementation 'androidx.appcompat:appcompat:1.4.1'
        implementation 'com.google.android.material:material:1.5.0'
        implementation 'androidx.constraintlayout:constraintlayout:2.1.3'
        testImplementation 'junit:junit:4.+'
        androidTestImplementation 'androidx.test.ext:junit:1.1.3'
        androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
        }