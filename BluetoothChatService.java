package project.bluetoothterminal;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BluetoothChatService {
    private static final String TAG = "BluetoothChatService";
    /* access modifiers changed from: private */
    public final BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    /* access modifiers changed from: private */
    public ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    private AcceptThread mSecureAcceptThread;
    /* access modifiers changed from: private */
    public int mState = 0;

    public BluetoothChatService(Context context, Handler handler) {
        this.mHandler = handler;
    }

    private synchronized void setState(int i) {
        Log.d(TAG, "setState() " + this.mState + " -> " + i);
        this.mState = i;
        this.mHandler.obtainMessage(1, i, -1).sendToTarget();
    }

    public synchronized int getState() {
        return this.mState;
    }

    public synchronized void start() {
        Log.d(TAG, "start");
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }
        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel();
            this.mConnectedThread = null;
        }
        setState(1);
        if (this.mSecureAcceptThread == null) {
            this.mSecureAcceptThread = new AcceptThread(true);
            this.mSecureAcceptThread.start();
        }
    }

    public synchronized void connect(BluetoothDevice bluetoothDevice, boolean z) {
        Log.d(TAG, "connect to: " + bluetoothDevice);
        Log.d("Connection state", this.mState + "");
        if (this.mState == 2 && this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }
        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel();
            this.mConnectedThread = null;
        }
        this.mConnectThread = new ConnectThread(bluetoothDevice, z);
        this.mConnectThread.start();
        setState(2);
    }

    public synchronized void connected(BluetoothSocket bluetoothSocket, BluetoothDevice bluetoothDevice, String str) {
        Log.d(TAG, "connected, Socket Type:" + str);
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }
        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel();
            this.mConnectedThread = null;
        }
        if (this.mSecureAcceptThread != null) {
            this.mSecureAcceptThread.cancel();
            this.mSecureAcceptThread = null;
        }
        this.mConnectedThread = new ConnectedThread(bluetoothSocket, str);
        this.mConnectedThread.start();
        Message obtainMessage = this.mHandler.obtainMessage(4);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME, bluetoothDevice.getName());
        obtainMessage.setData(bundle);
        this.mHandler.sendMessage(obtainMessage);
        setState(3);
    }

    public synchronized void stop() {
        Log.d(TAG, "stop");
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }
        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel();
            this.mConnectedThread = null;
        }
        if (this.mSecureAcceptThread != null) {
            this.mSecureAcceptThread.cancel();
            this.mSecureAcceptThread = null;
        }
        setState(0);
    }

    public void write(byte[] bArr) {
        synchronized (this) {
            if (this.mState == 3) {
                ConnectedThread connectedThread = this.mConnectedThread;
                connectedThread.write(bArr);
            }
        }
    }

    /* access modifiers changed from: private */
    public void connectionFailed() {
        Message obtainMessage = this.mHandler.obtainMessage(5);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Fail");
        obtainMessage.setData(bundle);
        this.mHandler.sendMessage(obtainMessage);
    }

    /* access modifiers changed from: private */
    public void connectionLost() {
        Message obtainMessage = this.mHandler.obtainMessage(5);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Disconnected");
        obtainMessage.setData(bundle);
        this.mHandler.sendMessage(obtainMessage);
    }

    private class AcceptThread extends Thread {
        private String mSocketType;
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread(boolean z) {
            BluetoothServerSocket bluetoothServerSocket;
            this.mSocketType = z ? "Secure" : "Insecure";
            if (z) {
                try {
                    bluetoothServerSocket = BluetoothChatService.this.mAdapter.listenUsingRfcommWithServiceRecord(Constants.NAME_SECURE, Constants.MY_UUID_SECURE);
                } catch (IOException e) {
                    Log.e(BluetoothChatService.TAG, "Socket Type: " + this.mSocketType + "listen() failed", e);
                    bluetoothServerSocket = null;
                }
            } else {
                bluetoothServerSocket = BluetoothChatService.this.mAdapter.listenUsingInsecureRfcommWithServiceRecord(Constants.NAME_INSECURE, Constants.MY_UUID_INSECURE);
            }
            this.mmServerSocket = bluetoothServerSocket;
        }

        public void run() {
            Log.d(BluetoothChatService.TAG, "Socket Type: " + this.mSocketType + "BEGIN mAcceptThread" + this);
            StringBuilder sb = new StringBuilder();
            sb.append("AcceptThread");
            sb.append(this.mSocketType);
            setName(sb.toString());
            while (BluetoothChatService.this.mState != 3) {
                try {
                    BluetoothSocket accept = this.mmServerSocket.accept();
                    if (accept != null) {
                        synchronized (BluetoothChatService.this) {
                            switch (BluetoothChatService.this.mState) {
                                case 0:
                                case 3:
                                    try {
                                        accept.close();
                                        break;
                                    } catch (IOException e) {
                                        Log.e(BluetoothChatService.TAG, "Could not close unwanted socket", e);
                                        break;
                                    }
                                case 1:
                                case 2:
                                    BluetoothChatService.this.connected(accept, accept.getRemoteDevice(), this.mSocketType);
                                    break;
                            }
                        }
                    }
                } catch (IOException e2) {
                    Log.e(BluetoothChatService.TAG, "Socket Type: " + this.mSocketType + " accept() failed", e2);
                }
            }
            Log.i(BluetoothChatService.TAG, "END mAcceptThread, socket Type: " + this.mSocketType);
        }

        public void cancel() {
            Log.d(BluetoothChatService.TAG, "Socket Type" + this.mSocketType + "cancel " + this);
            try {
                this.mmServerSocket.close();
            } catch (IOException e) {
                Log.e(BluetoothChatService.TAG, "Socket Type" + this.mSocketType + "close() of server failed", e);
            }
        }
    }

    private class ConnectThread extends Thread {
        private String mSocketType;
        private final BluetoothDevice mmDevice;
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice bluetoothDevice, boolean z) {
            BluetoothSocket bluetoothSocket;
            this.mmDevice = bluetoothDevice;
            this.mSocketType = z ? "Secure" : "Insecure";
            if (z) {
                try {
                    bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(Constants.MY_UUID_SECURE);
                } catch (IOException e) {
                    Log.e(BluetoothChatService.TAG, "Socket Type: " + this.mSocketType + "create() failed", e);
                    bluetoothSocket = null;
                }
            } else {
                bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(Constants.MY_UUID_INSECURE);
            }
            this.mmSocket = bluetoothSocket;
        }

        public void run() {
            Log.i(BluetoothChatService.TAG, "BEGIN mConnectThread SocketType:" + this.mSocketType);
            setName("ConnectThread" + this.mSocketType);
            BluetoothChatService.this.mAdapter.cancelDiscovery();
            try {
                this.mmSocket.connect();
                synchronized (BluetoothChatService.this) {
                    ConnectThread unused = BluetoothChatService.this.mConnectThread = null;
                }
                BluetoothChatService.this.connected(this.mmSocket, this.mmDevice, this.mSocketType);
            } catch (Exception e) {
                Log.d(BluetoothChatService.TAG, "Connection error " + e.toString());
                try {
                    this.mmSocket.close();
                } catch (Exception e2) {
                    Log.e(BluetoothChatService.TAG, "unable to close() " + this.mSocketType + " socket during connection failure", e2);
                }
                BluetoothChatService.this.connectionFailed();
            }
        }

        public void cancel() {
            try {
                this.mmSocket.close();
            } catch (Exception e) {
                Log.e(BluetoothChatService.TAG, "close() of connect " + this.mSocketType + " socket failed", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final BluetoothSocket mmSocket;

        public ConnectedThread(BluetoothSocket bluetoothSocket, String str) {
            InputStream inputStream;
            Log.d(BluetoothChatService.TAG, "create ConnectedThread: " + str);
            this.mmSocket = bluetoothSocket;
            OutputStream outputStream = null;
            try {
                inputStream = bluetoothSocket.getInputStream();
                try {
                    outputStream = bluetoothSocket.getOutputStream();
                } catch (IOException e) {
                    e = e;
                    Log.e(BluetoothChatService.TAG, "temp sockets not created", e);
                    this.mmInStream = inputStream;
                    this.mmOutStream = outputStream;
                }
            } catch (IOException e2) {
                e = e2;
                inputStream = null;
                Log.e(BluetoothChatService.TAG, "temp sockets not created", e);
                this.mmInStream = inputStream;
                this.mmOutStream = outputStream;
            }
            this.mmInStream = inputStream;
            this.mmOutStream = outputStream;
        }

        public void run() {
            Log.i(BluetoothChatService.TAG, "BEGIN mConnectedThread");
            byte[] bArr = new byte[512];
            while (true) {
                try {
                    int read = this.mmInStream.read(bArr);
                    int[] iArr = new int[read];
                    Log.d("Byte readed", "" + read);
                    for (int i = 0; i < read; i++) {
                        iArr[i] = bArr[i] & 255;
                        Log.d("tempbuffer ", "" + iArr[i]);
                    }
                    BluetoothChatService.this.mHandler.obtainMessage(2, read, -1, new String(iArr, 0, read).toString()).sendToTarget();
                } catch (IOException e) {
                    Log.e(BluetoothChatService.TAG, "disconnected", e);
                    BluetoothChatService.this.connectionLost();
                    return;
                }
            }
        }

        public void write(byte[] bArr) {
            try {
                this.mmOutStream.write(bArr);
                BluetoothChatService.this.mHandler.obtainMessage(3, -1, -1, bArr).sendToTarget();
            } catch (IOException e) {
                Log.e(BluetoothChatService.TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                this.mmSocket.close();
            } catch (IOException e) {
                Log.e(BluetoothChatService.TAG, "close() of connect socket failed", e);
            }
        }
    }
}
