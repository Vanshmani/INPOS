package project.bluetoothterminal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomArrayAdapter_Paired extends BaseAdapter implements ListAdapter {
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public ArrayList<String> list = new ArrayList<>();

    public long getItemId(int i) {
        return 0;
    }

    public CustomArrayAdapter_Paired(ArrayList<String> arrayList, Context context2) {
        this.list = arrayList;
        this.context = context2;
    }

    public void setListData(ArrayList<String> arrayList) {
        this.list = arrayList;
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int i) {
        return this.list.get(i);
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(C0605R.layout.device_name, (ViewGroup) null);
        }
        ImageButton imageButton = (ImageButton) view.findViewById(C0605R.C0607id.imgUnpair);
        if (this.list.get(i).equals(viewGroup.getResources().getText(C0605R.string.none_paired).toString())) {
            imageButton.setVisibility(8);
        } else {
            imageButton.setVisibility(0);
        }
        ((TextView) view.findViewById(C0605R.C0607id.txtPaird)).setText(this.list.get(i));
        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomArrayAdapter_Paired.this.context);
                builder.setMessage("Are you sure you want to Unpair ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.unpairDevice((String) CustomArrayAdapter_Paired.this.list.get(i));
                        CustomArrayAdapter_Paired.this.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });
        return view;
    }
}
