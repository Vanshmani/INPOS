package project.bluetoothterminal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.p000v4.app.ShareCompat;
import android.support.p000v4.content.ContextCompat;
import android.support.p000v4.content.FileProvider;
import android.support.p003v7.app.AlertDialog;
import android.support.p003v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import p004uk.p005co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import p004uk.p005co.deanwild.materialshowcaseview.MaterialShowcaseView;
import p004uk.p005co.deanwild.materialshowcaseview.ShowcaseConfig;
import project.bluetoothterminal.Utils;

public class ChatRoom extends AppCompatActivity {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int Ads_Counter_Limit = 30;
    private static final String SHOWCASE_ID = "sequence showcase";
    private static final String TAG = "ChatRoom";
    private static final char space = '-';
    Boolean Select_ASCII = true;
    public Activity activity;
    AdRequest adRequest;
    private CustomArrayAdapter adapter;
    String address = null;
    Button btnSend;
    int[] buttons = {0, C0605R.C0607id.button1, C0605R.C0607id.button2, C0605R.C0607id.button3, C0605R.C0607id.button4, C0605R.C0607id.button5, C0605R.C0607id.button6, C0605R.C0607id.button7, C0605R.C0607id.button8, C0605R.C0607id.button9, C0605R.C0607id.button10, C0605R.C0607id.button11, C0605R.C0607id.button12, C0605R.C0607id.button13, C0605R.C0607id.button14, C0605R.C0607id.button15, C0605R.C0607id.button16, C0605R.C0607id.button17, C0605R.C0607id.button18, C0605R.C0607id.button19, C0605R.C0607id.button20};
    CheckBox chkAutoScroll;
    List<OneComment> commentList = new ArrayList();
    EditText editMsg;
    Boolean isAscii = true;
    public boolean isConnecting = true;
    Boolean isReceive_ASCII = false;
    public boolean isTerminating = false;
    LinearLayout ll_buttonArea;
    LinearLayout ll_buttonRow1;
    LinearLayout ll_buttonRow2;
    LinearLayout ll_buttonRow3;
    LinearLayout ll_buttonRow4;
    ListView lstChat;
    AdView mAdView_footer;
    private final BroadcastReceiver mBTReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                switch (intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE)) {
                    case 10:
                        Log.w("BT", "STATE_OFF");
                        ChatRoom.this.alert("Alert", "Bluetooth is turned off");
                        return;
                    case 11:
                        Log.w("BT", "STATE_TURNING_ON");
                        return;
                    case 12:
                        Log.w("BT", "STATE_ON");
                        return;
                    case 13:
                        Log.w("BT", "STATE_TURNING_OFF");
                        return;
                    default:
                        return;
                }
            }
        }
    };
    private BluetoothChatService mChatService = null;
    /* access modifiers changed from: private */
    public String mConnectedDeviceName = null;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    switch (message.arg1) {
                        case 2:
                            ChatRoom chatRoom = ChatRoom.this;
                            chatRoom.isConnecting = true;
                            chatRoom.actionBarSetup("Connecting...");
                            return;
                        case 3:
                            ChatRoom chatRoom2 = ChatRoom.this;
                            chatRoom2.isConnecting = false;
                            chatRoom2.actionBarSetup("Connected to " + ChatRoom.this.mConnectedDeviceName);
                            ChatRoom.this.txtReceive.setText("");
                            ChatRoom.this.presentShowcaseSequence();
                            if (ChatRoom.this.progressDialog.isShowing()) {
                                ChatRoom.this.progressDialog.dismiss();
                            }
                            if (ChatRoom.this.isTerminating) {
                                ChatRoom.this.finish();
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                case 2:
                    String str = (String) message.obj;
                    if (str != null) {
                        if (ChatRoom.this.isAscii.booleanValue()) {
                            if (!ChatRoom.this.isReceive_ASCII.booleanValue()) {
                                String format = DateFormat.getDateTimeInstance().format(new Date());
                                ChatRoom.this.isReceive_ASCII = true;
                                if (ChatRoom.this.txtReceive.length() != 0) {
                                    TextView textView = ChatRoom.this.txtReceive;
                                    ChatRoom.appendColoredText(textView, "\n[" + format + "] ASCII:\n", -7829368);
                                } else {
                                    TextView textView2 = ChatRoom.this.txtReceive;
                                    ChatRoom.appendColoredText(textView2, "[" + format + "] ASCII:\n", -7829368);
                                }
                            }
                            ChatRoom.this.txtReceive.append(str);
                        } else {
                            if (ChatRoom.this.isReceive_ASCII.booleanValue()) {
                                String format2 = DateFormat.getDateTimeInstance().format(new Date());
                                ChatRoom.this.isReceive_ASCII = false;
                                if (ChatRoom.this.txtReceive.length() != 0) {
                                    TextView textView3 = ChatRoom.this.txtReceive;
                                    ChatRoom.appendColoredText(textView3, "\n[" + format2 + "] HEX:\n", -7829368);
                                } else {
                                    TextView textView4 = ChatRoom.this.txtReceive;
                                    ChatRoom.appendColoredText(textView4, "[" + format2 + "] HEX:\n", -7829368);
                                }
                            }
                            ChatRoom.this.txtReceive.append(ChatRoom.asciiToHex(str));
                        }
                        ChatRoom.this.txtReceive.setFocusable(false);
                        if (ChatRoom.this.chkAutoScroll.isChecked()) {
                            ChatRoom.this.scrollview.fullScroll(130);
                            return;
                        }
                        return;
                    }
                    return;
                case 4:
                    String unused = ChatRoom.this.mConnectedDeviceName = message.getData().getString(Constants.DEVICE_NAME);
                    if (ChatRoom.this.activity != null) {
                        ChatRoom chatRoom3 = ChatRoom.this;
                        chatRoom3.actionBarSetup("Connected to " + ChatRoom.this.mConnectedDeviceName);
                        return;
                    }
                    return;
                case 5:
                    if (ChatRoom.this.activity != null) {
                        String string = message.getData().getString(Constants.TOAST);
                        if (string != null && string.equals("Fail")) {
                            Log.d("Toast", "Fail");
                            ChatRoom.this.actionBarSetup("Not Connected");
                            ChatRoom.this.setResult(0, new Intent());
                            if (ChatRoom.this.progressDialog.isShowing()) {
                                ChatRoom.this.progressDialog.dismiss();
                            }
                            if (ChatRoom.this.isFinishing() || ChatRoom.this.isTerminating) {
                                ChatRoom.this.finish();
                                return;
                            } else {
                                ChatRoom.this.alert("Error!", "May be your HC-05 or HC-06 device is not availbale for connection Or you are trying to connect with other device");
                                return;
                            }
                        } else if (string != null && string.equals("Disconnected")) {
                            ChatRoom.this.setResult(0, new Intent());
                            if (!ChatRoom.this.isFinishing()) {
                                Toast.makeText(ChatRoom.this.activity, "You are disconnected", 0).show();
                            }
                            ChatRoom.this.finish();
                            return;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public InterstitialAd mInterstitialAd;
    private StringBuffer mOutStringBuffer;
    Menu menu;
    /* access modifiers changed from: private */
    public PrefManager prefManager;
    ProgressDialog progressDialog;
    ScrollView scrollview;
    TextView txtReceive;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) C0605R.layout.chatroom);
        registerReceiver(this.mBTReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
        this.prefManager = new PrefManager(this);
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("Please wait...");
        this.mAdView_footer = (AdView) findViewById(C0605R.C0607id.adViewfooter);
        this.adRequest = new AdRequest.Builder().addTestDevice(getResources().getString(C0605R.string.Test_adUnit_Id)).build();
        this.mAdView_footer.setAdListener(new AdListener() {
            public void onAdLoaded() {
                ChatRoom.this.mAdView_footer.setVisibility(0);
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
                ChatRoom.this.mInterstitialAd.show();
            }

            public void onAdOpened() {
                ChatRoom.this.prefManager.setChatRoomAds_Counter(-1);
            }
        });
        if (this.prefManager.getKeepscreen()) {
            Log.d(TAG, "Keep true");
            getWindow().addFlags(128);
        }
        this.scrollview = (ScrollView) findViewById(C0605R.C0607id.SCROLLER_ID);
        this.chkAutoScroll = (CheckBox) findViewById(C0605R.C0607id.chkAutoScroll);
        for (final int i = 1; i <= 20; i++) {
            Button button = (Button) findViewById(this.buttons[i]);
            button.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    ChatRoom.this.showPopup(i);
                    if (MainActivity.mIsPremium == null || MainActivity.mIsPremium.booleanValue()) {
                        return false;
                    }
                    if (ChatRoom.this.prefManager.getChatRoomAds_Counter() >= 30) {
                        ChatRoom.this.mInterstitialAd.loadAd(ChatRoom.this.adRequest);
                    }
                    ChatRoom.this.prefManager.setChatRoomAds_Counter(ChatRoom.this.prefManager.getChatRoomAds_Counter());
                    Log.d("Ads_Load_Counter", "" + ChatRoom.this.prefManager.getChatRoomAds_Counter());
                    return false;
                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (ChatRoom.this.prefManager.getButton_name(i) != null) {
                        ChatRoom.this.sendMSG_Perameter(ChatRoom.this.prefManager.getButton_command(i), ChatRoom.this.prefManager.getButton_CR(i), ChatRoom.this.prefManager.getButton_LF(i), ChatRoom.this.prefManager.getSend_ASCII(i));
                    } else {
                        ChatRoom.this.showPopup(i);
                    }
                    if (MainActivity.mIsPremium != null && !MainActivity.mIsPremium.booleanValue()) {
                        if (ChatRoom.this.prefManager.getChatRoomAds_Counter() >= 30) {
                            ChatRoom.this.mInterstitialAd.loadAd(ChatRoom.this.adRequest);
                        }
                        ChatRoom.this.prefManager.setChatRoomAds_Counter(ChatRoom.this.prefManager.getChatRoomAds_Counter());
                        Log.d("Ads_Load_Counter", "" + ChatRoom.this.prefManager.getChatRoomAds_Counter());
                    }
                }
            });
            if (this.prefManager.getButton_name(i) != null) {
                button.setText(this.prefManager.getButton_name(i), (TextView.BufferType) null);
            }
        }
        this.ll_buttonArea = (LinearLayout) findViewById(C0605R.C0607id.ll_btnArea);
        this.ll_buttonRow1 = (LinearLayout) findViewById(C0605R.C0607id.ll_buttonRow1);
        this.ll_buttonRow2 = (LinearLayout) findViewById(C0605R.C0607id.ll_buttonRow2);
        this.ll_buttonRow3 = (LinearLayout) findViewById(C0605R.C0607id.ll_buttonRow3);
        this.ll_buttonRow4 = (LinearLayout) findViewById(C0605R.C0607id.ll_buttonRow4);
        this.txtReceive = (TextView) findViewById(C0605R.C0607id.txtReceive);
        this.btnSend = (Button) findViewById(C0605R.C0607id.buttonSend);
        this.editMsg = (EditText) findViewById(C0605R.C0607id.editMessage);
        this.lstChat = (ListView) findViewById(C0605R.C0607id.lstChat);
        this.activity = this;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.adapter = new CustomArrayAdapter(getApplicationContext(), C0605R.layout.listitem_discuss, this.commentList);
        this.lstChat.setAdapter(this.adapter);
        this.editMsg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                if (z) {
                    Log.d("editMsg", "OnFocus");
                    ChatRoom.this.txtReceive.setFocusable(false);
                    return;
                }
                Log.d("editMsg", "Focus Lost");
            }
        });
        if (this.mChatService == null) {
            setupChat();
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.address = extras.getString("MAC_Add");
        }
        try {
            this.mChatService.connect(defaultAdapter.getRemoteDevice(this.address), true);
        } catch (IllegalArgumentException unused) {
            alert("Unable to connect!", "Your are trying to connect is invalid bluetooth address");
        }
        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(C0605R.C0607id.main_relative);
        relativeLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (relativeLayout.getRootView().getHeight() - relativeLayout.getHeight() > relativeLayout.getRootView().getHeight() / 3) {
                    ChatRoom.this.ll_buttonArea.setVisibility(8);
                    if (ChatRoom.this.chkAutoScroll.isChecked()) {
                        ChatRoom.this.txtReceive.setFocusable(false);
                        ChatRoom.this.scrollview.fullScroll(130);
                        return;
                    }
                    return;
                }
                ChatRoom.this.ll_buttonArea.setVisibility(0);
                if (ChatRoom.this.chkAutoScroll.isChecked()) {
                    ChatRoom.this.txtReceive.setFocusable(false);
                    ChatRoom.this.scrollview.fullScroll(130);
                }
            }
        });
        if (this.prefManager.getSend_ASCII().booleanValue()) {
            this.btnSend.setText(C0605R.string.btn_send_ASCII);
            this.editMsg.setHint(C0605R.string.enter_ASCII_Command);
            this.editMsg.setInputType(524288);
            this.editMsg.setFilters(new InputFilter[0]);
        } else {
            this.btnSend.setText(C0605R.string.btn_send_HEX);
            this.editMsg.setHint(C0605R.string.enter_HEX_Command);
            this.editMsg.setInputType(524288);
            this.editMsg.setFilters(new InputFilter[]{new Utils.InputFilterHex()});
        }
        this.btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ChatRoom.this.editMsg.getText().toString().length() != 0) {
                    ChatRoom.this.sendMSG_Perameter(ChatRoom.this.editMsg.getText().toString(), ChatRoom.this.prefManager.getButton_CR(), ChatRoom.this.prefManager.getButton_LF(), ChatRoom.this.prefManager.getSend_ASCII());
                    ChatRoom.this.editMsg.setText("");
                }
                if (MainActivity.mIsPremium != null && !MainActivity.mIsPremium.booleanValue()) {
                    if (ChatRoom.this.prefManager.getChatRoomAds_Counter() >= 30) {
                        ChatRoom.this.mInterstitialAd.loadAd(ChatRoom.this.adRequest);
                    }
                    ChatRoom.this.prefManager.setChatRoomAds_Counter(ChatRoom.this.prefManager.getChatRoomAds_Counter());
                    Log.d("Ads_Load_Counter", "" + ChatRoom.this.prefManager.getChatRoomAds_Counter());
                }
            }
        });
        this.btnSend.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                ChatRoom.this.showPopup_sending();
                if (MainActivity.mIsPremium == null || MainActivity.mIsPremium.booleanValue()) {
                    return false;
                }
                if (ChatRoom.this.prefManager.getChatRoomAds_Counter() >= 30) {
                    ChatRoom.this.mInterstitialAd.loadAd(ChatRoom.this.adRequest);
                }
                ChatRoom.this.prefManager.setChatRoomAds_Counter(ChatRoom.this.prefManager.getChatRoomAds_Counter());
                Log.d("Ads_Load_Counter", "" + ChatRoom.this.prefManager.getChatRoomAds_Counter());
                return false;
            }
        });
        this.editMsg.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() != 0 || i != 66 || ChatRoom.this.editMsg.getText().toString().length() == 0) {
                    return false;
                }
                ChatRoom.this.sendMSG_Perameter(ChatRoom.this.editMsg.getText().toString(), ChatRoom.this.prefManager.getButton_CR(), ChatRoom.this.prefManager.getButton_LF(), true);
                ChatRoom.this.editMsg.setText("");
                return true;
            }
        });
        this.editMsg.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (!ChatRoom.this.prefManager.getSend_ASCII().booleanValue()) {
                    ChatRoom.this.Write_HEX_String(editable);
                }
            }
        });
        this.lstChat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
                String charSequence = ((TextView) view.findViewById(C0605R.C0607id.comment)).getText().toString();
                if (charSequence.contains("ASCII: ")) {
                    charSequence = charSequence.replaceAll("ASCII: ", "");
                }
                if (charSequence.contains("HEX: ")) {
                    charSequence = charSequence.replaceAll("HEX: ", "");
                }
                ClipData newPlainText = ClipData.newPlainText("Clip", charSequence);
                Toast.makeText(ChatRoom.this.getApplicationContext(), "Copied", 0).show();
                ((ClipboardManager) ChatRoom.this.getSystemService("clipboard")).setPrimaryClip(newPlainText);
                return true;
            }
        });
    }

    public void sendMSG_Perameter(String str, Boolean bool, Boolean bool2, Boolean bool3) {
        if (bool3.booleanValue()) {
            sendMessage(str, true, bool, bool2);
            return;
        }
        String[] split = str.split("-");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (split[i].length() == 1) {
                split[i] = "0" + split[i];
            }
            sb.append(split[i]);
        }
        sendMessage(sb.toString(), bool3, bool, bool2);
    }

    /* access modifiers changed from: private */
    public void actionBarSetup(String str) {
        getSupportActionBar().setSubtitle((CharSequence) str);
    }

    public static void appendColoredText(TextView textView, String str, int i) {
        int length = textView.getText().length();
        textView.append(str);
        ((Spannable) textView.getText()).setSpan(new ForegroundColorSpan(i), length, textView.getText().length(), 0);
    }

    public void onStart() {
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.prefManager.getFont_Size() == 0) {
            this.txtReceive.setTextSize(14.0f);
        } else {
            this.txtReceive.setTextSize((float) this.prefManager.getFont_Size());
        }
        getWindow().clearFlags(128);
        if (this.prefManager.getKeepscreen()) {
            Log.d("Keep", "true");
            getWindow().addFlags(128);
        }
        if (MainActivity.mIsPremium == null || MainActivity.mIsPremium.booleanValue()) {
            this.mAdView_footer.setVisibility(8);
        } else {
            this.mAdView_footer.loadAd(this.adRequest);
        }
        if (this.prefManager.getButtonRow() == 0) {
            this.ll_buttonRow1.setVisibility(8);
            this.ll_buttonRow2.setVisibility(8);
            this.ll_buttonRow3.setVisibility(8);
            this.ll_buttonRow4.setVisibility(8);
        } else if (this.prefManager.getButtonRow() == 1) {
            this.ll_buttonRow1.setVisibility(0);
            this.ll_buttonRow2.setVisibility(8);
            this.ll_buttonRow3.setVisibility(8);
            this.ll_buttonRow4.setVisibility(8);
        } else if (this.prefManager.getButtonRow() == 2) {
            this.ll_buttonRow1.setVisibility(0);
            this.ll_buttonRow2.setVisibility(0);
            this.ll_buttonRow3.setVisibility(8);
            this.ll_buttonRow4.setVisibility(8);
        } else if (this.prefManager.getButtonRow() == 3) {
            this.ll_buttonRow1.setVisibility(0);
            this.ll_buttonRow2.setVisibility(0);
            this.ll_buttonRow3.setVisibility(0);
            this.ll_buttonRow4.setVisibility(8);
        } else if (this.prefManager.getButtonRow() == 4) {
            this.ll_buttonRow1.setVisibility(0);
            this.ll_buttonRow2.setVisibility(0);
            this.ll_buttonRow3.setVisibility(0);
            this.ll_buttonRow4.setVisibility(0);
        } else {
            this.ll_buttonRow1.setVisibility(0);
            this.ll_buttonRow2.setVisibility(8);
            this.ll_buttonRow3.setVisibility(8);
            this.ll_buttonRow4.setVisibility(8);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu2) {
        getMenuInflater().inflate(C0605R.C0609menu.menu2, menu2);
        this.menu = menu2;
        return true;
    }

    private void updateMenuTitles() {
        MenuItem findItem = this.menu.findItem(C0605R.C0607id.action_new);
        if (this.isAscii.booleanValue()) {
            findItem.setTitle("Ascii");
        } else {
            findItem.setTitle("Hex");
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == C0605R.C0607id.action_clear) {
            TextView textView = this.txtReceive;
            if (textView != null) {
                textView.setText("");
            }
            this.adapter.clear();
            if (this.isReceive_ASCII.booleanValue()) {
                this.isReceive_ASCII = false;
            } else {
                this.isReceive_ASCII = true;
            }
            return true;
        } else if (itemId == C0605R.C0607id.action_hex) {
            if (this.isAscii.booleanValue()) {
                this.isAscii = false;
                updateMenuTitles();
                this.isReceive_ASCII = true;
            }
            return true;
        } else if (itemId != C0605R.C0607id.action_setting) {
            switch (itemId) {
                case C0605R.C0607id.action_Ascii:
                    if (!this.isAscii.booleanValue()) {
                        this.isAscii = true;
                        updateMenuTitles();
                        this.isReceive_ASCII = false;
                    }
                    return true;
                case C0605R.C0607id.action_SendLog:
                    if (getLog() == null || getLog().isEmpty()) {
                        Toast.makeText(this, "No Log", 0).show();
                    } else {
                        wrtieFileOnInternalStorage("Bluetooth Terminal HC-05.txt", getLog());
                        startActivity(ShareCompat.IntentBuilder.from(this).setType("application/txt").setSubject("Bluetooth Terminal HC-05 Log file").setStream(FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, new File(getCacheDir(), "Bluetooth Terminal HC-05.txt"))).setChooserTitle((CharSequence) "Send Log file").createChooserIntent().addFlags(524288).addFlags(1));
                    }
                    return true;
                case C0605R.C0607id.action_Tips:
                    MaterialShowcaseView.resetSingleUse(this, SHOWCASE_ID);
                    presentShowcaseSequence();
                    return true;
                case C0605R.C0607id.action_View_More:
                    startActivity(new Intent(this, View_More.class));
                    return true;
                default:
                    return super.onOptionsItemSelected(menuItem);
            }
        } else {
            startActivity(new Intent(this, setting_prefs.class));
            return true;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mChatService != null) {
            Log.d("Chat Service", "Stop");
            this.mChatService.stop();
        }
        try {
            unregisterReceiver(this.mBTReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = getLayoutInflater().inflate(C0605R.layout.terminate_dialog, (ViewGroup) null);
        builder.setView(inflate);
        builder.setTitle((CharSequence) "Confirmation");
        builder.setMessage((CharSequence) "Do you want to Terminate? ");
        builder.setPositiveButton((CharSequence) "Yes", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ChatRoom.this.isConnecting) {
                    ChatRoom.this.progressDialog.show();
                    ChatRoom chatRoom = ChatRoom.this;
                    chatRoom.isTerminating = true;
                    chatRoom.actionBarSetup("Cancelling...");
                    return;
                }
                ChatRoom.this.finish();
                ChatRoom.super.onBackPressed();
            }
        });
        builder.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        final AdView adView = (AdView) inflate.findViewById(C0605R.C0607id.adView_exit_dialog);
        adView.setAdListener(new AdListener() {
            public void onAdLoaded() {
                adView.setVisibility(0);
            }
        });
        if (MainActivity.mIsPremium == null || MainActivity.mIsPremium.booleanValue()) {
            adView.setVisibility(8);
        } else {
            adView.loadAd(this.adRequest);
        }
        builder.create().show();
    }

    private void setupChat() {
        Log.d(Constants.TAG, "setupChat()");
        this.mChatService = new BluetoothChatService(this, this.mHandler);
        this.mOutStringBuffer = new StringBuffer("");
    }

    private void sendMessage(String str, Boolean bool, Boolean bool2, Boolean bool3) {
        if (this.mChatService.getState() != 3) {
            Toast.makeText(this, C0605R.string.not_connected, 0).show();
        } else if (str.length() <= 0) {
        } else {
            if (bool.booleanValue()) {
                this.commentList.add(new OneComment(false, str, true));
                this.adapter.notifyDataSetChanged();
                if (bool2.booleanValue() && bool3.booleanValue()) {
                    str = str + Constants.CRLF;
                } else if (bool2.booleanValue() && !bool3.booleanValue()) {
                    str = str + Constants.f58CR;
                } else if (!bool2.booleanValue() && bool3.booleanValue()) {
                    str = str + Constants.f59LF;
                }
                this.mChatService.write(str.getBytes());
                return;
            }
            try {
                this.commentList.add(new OneComment(false, str.replaceAll("(.{2})(?!$)", "$1-"), false));
                this.adapter.notifyDataSetChanged();
                if (bool2.booleanValue() && bool3.booleanValue()) {
                    str = str + Constants.H_CRLF;
                } else if (bool2.booleanValue() && !bool3.booleanValue()) {
                    str = str + Constants.H_CR;
                } else if (!bool2.booleanValue() && bool3.booleanValue()) {
                    str = str + Constants.H_LF;
                }
                int length = str.length();
                byte[] bArr = new byte[(length / 2)];
                for (int i = 0; i < length; i += 2) {
                    bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
                }
                this.mChatService.write(bArr);
            } catch (NumberFormatException e) {
                Log.d("toHex NumberFormatExce", e.getMessage());
                Toast.makeText(this, "Invalid Value", 0).show();
            } catch (StringIndexOutOfBoundsException e2) {
                Log.d("StringIndexOutofbound", e2.getMessage());
            }
            this.mOutStringBuffer.setLength(0);
        }
    }

    public void showPopup(int i) {
        final Dialog dialog = new Dialog(this, C0605R.style.Theme_Dialog);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(C0605R.layout.button_setting);
        getWindow().setLayout(-1, -1);
        Button button = (Button) dialog.findViewById(C0605R.C0607id.btnCancel);
        Button button2 = (Button) dialog.findViewById(C0605R.C0607id.btnSave);
        final EditText editText = (EditText) dialog.findViewById(C0605R.C0607id.btn_name);
        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(C0605R.C0607id.radioSend_type);
        final EditText editText2 = (EditText) dialog.findViewById(C0605R.C0607id.btn_command);
        final CheckBox checkBox = (CheckBox) dialog.findViewById(C0605R.C0607id.btn_chkCR);
        final CheckBox checkBox2 = (CheckBox) dialog.findViewById(C0605R.C0607id.btn_chkLF);
        final AdView adView = (AdView) dialog.findViewById(C0605R.C0607id.adView_button_setting);
        adView.setAdListener(new AdListener() {
            public void onAdLoaded() {
                adView.setVisibility(0);
            }
        });
        if (MainActivity.mIsPremium == null || MainActivity.mIsPremium.booleanValue()) {
            adView.setVisibility(8);
        } else {
            adView.loadAd(this.adRequest);
        }
        if (this.prefManager.getButton_name(i) == null) {
            for (int i2 = 1; i2 <= 20; i2++) {
                if (i2 == i) {
                    editText.setText("Btn " + i);
                }
            }
            ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
            this.Select_ASCII = true;
        } else {
            editText.setText(this.prefManager.getButton_name(i));
            editText2.setText(this.prefManager.getButton_command(i));
            checkBox.setChecked(this.prefManager.getButton_CR(i).booleanValue());
            checkBox2.setChecked(this.prefManager.getButton_LF(i).booleanValue());
            if (this.prefManager.getSend_ASCII(i).booleanValue()) {
                editText2.setHint(C0605R.string.enter_ASCII_Command);
                editText2.setInputType(524288);
                editText2.setFilters(new InputFilter[0]);
                ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
                this.Select_ASCII = true;
            } else {
                editText2.setHint(C0605R.string.enter_HEX_Command);
                editText2.setInputType(524288);
                editText2.setFilters(new InputFilter[]{new Utils.InputFilterHex()});
                ((RadioButton) radioGroup.getChildAt(1)).setChecked(true);
                this.Select_ASCII = false;
            }
        }
        editText2.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (!ChatRoom.this.Select_ASCII.booleanValue()) {
                    ChatRoom.this.Write_HEX_String(editable);
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = (RadioButton) dialog.findViewById(i);
                if (radioButton.getText().equals("ASCII")) {
                    editText2.setInputType(655360);
                    editText2.setFilters(new InputFilter[0]);
                    editText2.setText("");
                    editText2.setHint("Enter ASCII Command");
                    ChatRoom.this.Select_ASCII = true;
                } else if (radioButton.getText().equals("HEX")) {
                    editText2.setInputType(655360);
                    editText2.setFilters(new InputFilter[]{new Utils.InputFilterHex()});
                    editText2.setText("");
                    editText2.setHint("Enter HEX Command");
                    ChatRoom.this.Select_ASCII = false;
                }
            }
        });
        final Dialog dialog2 = dialog;
        final int i3 = i;
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (editText.getText().length() <= 0) {
                    editText.setError("Must Enter Name for Button");
                } else if (editText2.getText().length() <= 0) {
                    editText2.setError("Must Enter Command for Button");
                } else {
                    if (editText.getText().length() > 0 && editText2.getText().length() > 0) {
                        RadioGroup radioGroup = radioGroup;
                        int indexOfChild = radioGroup.indexOfChild(dialog2.findViewById(radioGroup.getCheckedRadioButtonId()));
                        ChatRoom.this.prefManager.setButton_name(i3, editText.getText().toString());
                        ChatRoom.this.prefManager.setButton_command(i3, editText2.getText().toString());
                        ChatRoom.this.prefManager.setButton_CR(i3, Boolean.valueOf(checkBox.isChecked()));
                        ChatRoom.this.prefManager.setButton_LF(i3, Boolean.valueOf(checkBox2.isChecked()));
                        if (indexOfChild == 0) {
                            ChatRoom.this.prefManager.setSend_ASCII(i3, true);
                        } else if (indexOfChild == 1) {
                            ChatRoom.this.prefManager.setSend_ASCII(i3, false);
                        }
                        ChatRoom chatRoom = ChatRoom.this;
                        Button button = (Button) chatRoom.findViewById(chatRoom.buttons[i3]);
                        if (ChatRoom.this.prefManager.getButton_name(i3) != null) {
                            button.setText(ChatRoom.this.prefManager.getButton_name(i3), (TextView.BufferType) null);
                        }
                    }
                    dialog2.dismiss();
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void showPopup_sending() {
        final Dialog dialog = new Dialog(this, C0605R.style.Theme_Dialog);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(true);
        dialog.setContentView(C0605R.layout.send_setting_dialog);
        getWindow().setLayout(-1, -1);
        Button button = (Button) dialog.findViewById(C0605R.C0607id.btnCancel);
        Button button2 = (Button) dialog.findViewById(C0605R.C0607id.btnSave);
        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(C0605R.C0607id.rdSend_type);
        final CheckBox checkBox = (CheckBox) dialog.findViewById(C0605R.C0607id.chkCR);
        final CheckBox checkBox2 = (CheckBox) dialog.findViewById(C0605R.C0607id.chkLF);
        final AdView adView = (AdView) dialog.findViewById(C0605R.C0607id.adView_button_setting);
        adView.setAdListener(new AdListener() {
            public void onAdLoaded() {
                adView.setVisibility(0);
            }
        });
        if (MainActivity.mIsPremium == null || MainActivity.mIsPremium.booleanValue()) {
            adView.setVisibility(8);
        } else {
            adView.loadAd(this.adRequest);
        }
        checkBox.setChecked(this.prefManager.getButton_CR().booleanValue());
        checkBox2.setChecked(this.prefManager.getButton_LF().booleanValue());
        if (this.prefManager.getSend_ASCII().booleanValue()) {
            ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
        } else {
            ((RadioButton) radioGroup.getChildAt(1)).setChecked(true);
        }
        final Dialog dialog2 = dialog;
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RadioGroup radioGroup = radioGroup;
                int indexOfChild = radioGroup.indexOfChild(dialog2.findViewById(radioGroup.getCheckedRadioButtonId()));
                if (indexOfChild == 0) {
                    ChatRoom.this.prefManager.setSend_ASCII(true);
                    ChatRoom.this.btnSend.setText(C0605R.string.btn_send_ASCII);
                    ChatRoom.this.editMsg.setHint(C0605R.string.enter_ASCII_Command);
                    ChatRoom.this.editMsg.setInputType(524288);
                    ChatRoom.this.editMsg.setFilters(new InputFilter[0]);
                    ChatRoom.this.editMsg.setText("");
                } else if (indexOfChild == 1) {
                    ChatRoom.this.prefManager.setSend_ASCII(false);
                    ChatRoom.this.btnSend.setText(C0605R.string.btn_send_HEX);
                    ChatRoom.this.editMsg.setHint(C0605R.string.enter_HEX_Command);
                    ChatRoom.this.editMsg.setInputType(524288);
                    ChatRoom.this.editMsg.setFilters(new InputFilter[]{new Utils.InputFilterHex()});
                    ChatRoom.this.editMsg.setText("");
                }
                ChatRoom.this.prefManager.setButton_CR(Boolean.valueOf(checkBox.isChecked()));
                ChatRoom.this.prefManager.setButton_LF(Boolean.valueOf(checkBox2.isChecked()));
                dialog2.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /* access modifiers changed from: private */
    public static String asciiToHex(String str) {
        char[] charArray = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char hexString : charArray) {
            try {
                String hexString2 = Integer.toHexString(hexString);
                if (hexString2.length() == 1) {
                    sb.append("0");
                    sb.append(hexString2);
                    sb.append(" ");
                } else {
                    sb.append(hexString2);
                    sb.append(" ");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void Write_HEX_String(Editable editable) {
        if (editable.length() > 0 && editable.length() % 3 == 0) {
            char charAt = editable.charAt(editable.length() - 1);
            char charAt2 = editable.charAt(editable.length() - 2);
            if ('-' == charAt) {
                editable.delete(editable.length() - 1, editable.length());
            }
            if ('-' == charAt2) {
                editable.delete(editable.length() - 2, editable.length());
            }
        }
        if (editable.length() > 0 && editable.length() % 3 == 0) {
            editable.insert(editable.length() - 1, String.valueOf(space));
        }
    }

    public void wrtieFileOnInternalStorage(String str, String str2) {
        try {
            File file = new File(getCacheDir(), str);
            Log.d("CacheDir", getCacheDir().toString());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(str2.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            Log.d("Exception", e.toString());
            e.printStackTrace();
        }
    }

    public String getLog() {
        String str;
        if (this.txtReceive.length() == 0 && this.commentList.size() == 0) {
            Toast.makeText(this, "No Log", 0).show();
            return null;
        }
        if (this.txtReceive.length() != 0) {
            str = "Received Log:\n\n" + this.txtReceive.getText().toString() + Constants.f59LF;
        } else {
            str = "No received Log!\n";
        }
        if (this.commentList.size() != 0) {
            String str2 = str + "\nSent Log:\n\n";
            for (int i = 0; i < this.adapter.getCount(); i++) {
                str2 = str2 + Html.fromHtml(this.commentList.get(i).getComment()).toString() + Constants.f59LF;
            }
            return str2;
        }
        return str + "\nNo Sent Log!\n";
    }

    /* access modifiers changed from: private */
    public void presentShowcaseSequence() {
        try {
            View findViewById = findViewById(C0605R.C0607id.action_new);
            ShowcaseConfig showcaseConfig = new ShowcaseConfig();
            showcaseConfig.setDelay(200);
            MaterialShowcaseSequence materialShowcaseSequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);
            materialShowcaseSequence.setOnItemShownListener(new MaterialShowcaseSequence.OnSequenceItemShownListener() {
                public void onShow(MaterialShowcaseView materialShowcaseView, int i) {
                }
            });
            materialShowcaseSequence.setConfig(showcaseConfig);
            materialShowcaseSequence.addSequenceItem(new MaterialShowcaseView.Builder(this).setTarget(findViewById).setDismissText((CharSequence) "GOT IT").setDismissTextColor(ContextCompat.getColor(this, C0605R.color.colorAccent)).setContentText((CharSequence) "Click here to change Received data format: ASCII or HEX.").build());
            materialShowcaseSequence.addSequenceItem(new MaterialShowcaseView.Builder(this).setTarget(this.btnSend).setDismissTextColor(ContextCompat.getColor(this, C0605R.color.colorAccent)).setDismissText((CharSequence) "GOT IT").setContentText((CharSequence) "Long press here to change Send data type: ASCII or HEX.").build());
            materialShowcaseSequence.addSequenceItem(new MaterialShowcaseView.Builder(this).setTarget(findViewById(this.buttons[1])).setDismissText((CharSequence) "GOT IT").setDismissTextColor(ContextCompat.getColor(this, C0605R.color.colorAccent)).setContentText((CharSequence) "Long press here, for Button Setting").build());
            materialShowcaseSequence.start();
        } catch (Exception e) {
            Log.d("Showcase Exception", e.toString());
        }
    }

    public void alert(String str, String str2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(C0605R.C0606drawable.error);
        builder.setTitle(str);
        builder.setCancelable(false);
        builder.setMessage(str2);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ChatRoom.this.finish();
            }
        });
        builder.show();
    }
}
