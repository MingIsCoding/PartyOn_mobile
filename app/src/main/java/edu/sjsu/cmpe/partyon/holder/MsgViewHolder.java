package edu.sjsu.cmpe.partyon.holder;

import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.adapter.ContactListAdapter;
import edu.sjsu.cmpe.partyon.adapter.MsgListAdapter;

/**
 * Created by Ming on 11/25/16.
 */

public class MsgViewHolder {
    private ImageView mContactIcon;
    private TextView mContactTextView;
    private MsgListAdapter mAdapter;
    private FragmentActivity mContent;
    public MsgViewHolder(View view) {
        mContactIcon = (ImageView) view.findViewById(R.id.msg_list_icon);
        mContactTextView = (TextView) view.findViewById(R.id.msg_list_name);
        view.setTag(this);
        /*mAdapter = adapter;
        mContent = content;*/
    }

    public ImageView getmContactIcon() {
        return mContactIcon;
    }

    public void setmContactIcon(ImageView mContactIcon) {
        this.mContactIcon = mContactIcon;
    }

    public TextView getmContactTextView() {
        return mContactTextView;
    }

    public void setmContactTextView(TextView mContactTextView) {
        this.mContactTextView = mContactTextView;
    }
}
