package edu.sjsu.cmpe.partyon.holder;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.adapter.ContactListAdapter;

/**
 * Created by Ming on 11/25/16.
 */

public class ContactItemViewHolder{
    private ImageView mContactIcon;
    private TextView mContactTextView;
    private ContactListAdapter mAdapter;
    private FragmentActivity mContent;

    public ContactItemViewHolder(View view,
                                 ContactListAdapter adapter, FragmentActivity content) {
        mContactIcon = (ImageView) view.findViewById(R.id.contact_icon);
        mContactTextView = (TextView) view.findViewById(R.id.contact_name);
        mAdapter = adapter;
        mContent = content;

    }
}
