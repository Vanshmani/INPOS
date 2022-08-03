package project.bluetoothterminal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class CustomArrayAdapter extends ArrayAdapter<OneComment> {
    private List<OneComment> comments = new ArrayList();

    public void clear() {
        this.comments.clear();
        notifyDataSetChanged();
    }

    public CustomArrayAdapter(Context context, int i, List<OneComment> list) {
        super(context, i);
        this.comments = list;
    }

    public int getCount() {
        return this.comments.size();
    }

    public OneComment getItem(int i) {
        return this.comments.get(i);
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(C0605R.layout.listitem_discuss, viewGroup, false);
        }
        LinearLayout linearLayout = (LinearLayout) view.findViewById(C0605R.C0607id.wrapper);
        OneComment item = getItem(i);
        TextView textView = (TextView) view.findViewById(C0605R.C0607id.comment);
        textView.setText(Html.fromHtml(item.isAscii ? "<small><small><font color='gray'>ASCII: </font></small></small>" : "<small><small><font color='gray'>HEX: </font></small></small>"), TextView.BufferType.SPANNABLE);
        textView.append(item.comment);
        textView.setBackgroundResource(item.left ? C0605R.C0606drawable.bg_msg_you : C0605R.C0606drawable.bg_msg_from);
        linearLayout.setGravity(item.left ? 3 : 5);
        return view;
    }

    public Bitmap decodeToBitmap(byte[] bArr) {
        return BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
    }
}
