package edu.sjsu.cmpe.partyon.adapter;

import android.content.pm.ApplicationInfo;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;

import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.entities.User;
import edu.sjsu.cmpe.partyon.fragment.ContactListFragment;
import edu.sjsu.cmpe.partyon.holder.ContactItemViewHolder;

/**
 * Created by Ming on 11/25/16.
 */

public class ContactListAdapter extends BaseAdapter {
    private List<User> followList;
    private FragmentActivity mContent;
    public ContactListAdapter(List<User> followList, FragmentActivity content){
        this.followList = followList;
        this.mContent = content;
    }
    @Override
    public int getCount() {
        return followList.size();
    }

    @Override
    public User getItem(int i) {
        return (User)followList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContent,
                    R.layout.item_contact_list, null);
            new ContactItemViewHolder(view);
        }
        ContactItemViewHolder holder = (ContactItemViewHolder) view.getTag();
        User u = getItem(i);
//        holder.mContactIcon.setImageDrawable();
        try {
            holder.mContactTextView.setText(u.fetchIfNeeded().getUsername());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.mContactIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContent, "iv_icon_click", Toast.LENGTH_SHORT).show();
            }
        });
        holder.mContactTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContent,"iv_text_click",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    class ContactItemViewHolder{
        private ImageView mContactIcon;
        private TextView mContactTextView;

        public ContactItemViewHolder(View view) {
            mContactIcon = (ImageView) view.findViewById(R.id.contact_icon);
            mContactTextView = (TextView) view.findViewById(R.id.contact_name);
            view.setTag(this);
        }
    }
}
