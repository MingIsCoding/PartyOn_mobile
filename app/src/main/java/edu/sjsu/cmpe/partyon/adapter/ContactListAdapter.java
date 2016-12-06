package edu.sjsu.cmpe.partyon.adapter;

import android.content.pm.ApplicationInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.entities.User;
import edu.sjsu.cmpe.partyon.holder.ContactItemViewHolder;

/**
 * Created by Ming on 11/25/16.
 */

public class ContactListAdapter extends BaseAdapter {
    private List<User> followList;
    private Set<User> mSelectedSet;
    private List<ContactItemViewHolder> mHolderCtrls;
    private FragmentActivity mContent;
    private boolean isCheckable = false;
    public ContactListAdapter(List<User> followList, FragmentActivity content, boolean checkable){
        this.followList = followList;
        this.mContent = content;
        this.mHolderCtrls = new ArrayList<>();
        this.mSelectedSet = new HashSet<>();
        this.isCheckable = checkable;
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
    public View getView(int i, View itemView, ViewGroup viewGroup) {
        ContactItemViewHolder holder;
        final View iView = itemView;
        final User u = getItem(i);
        if (itemView == null) {
            itemView = View.inflate(mContent,
                    R.layout.item_contact_list, null);

            holder = new ContactItemViewHolder(itemView, mSelectedSet.contains(u));
        }else{
            holder = (ContactItemViewHolder) itemView.getTag();
        }
        mHolderCtrls.add(holder);
        if (isCheckable){
            holder.getmContactCheckbox().setVisibility(View.VISIBLE);
        }else {
            holder.getmContactCheckbox().setVisibility(View.GONE);
        }

        try {
            holder.getmContactTextView().setText(u.fetchIfNeeded().getUsername());
            Picasso.with(mContent).load(u.getProfilePicSmall()).into(holder.getmContactIcon());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.getmContactIcon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContent, "iv_icon_click", Toast.LENGTH_SHORT).show();
            }
        });
        holder.getmContactTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContent,"iv_text_click",Toast.LENGTH_SHORT).show();
            }
        });
        holder.getmContactCheckbox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox)view;
                if(!cb.isChecked()){
                    mSelectedSet.remove(u);
                }else {
                    mSelectedSet.add(u);
                }
                Log.d("adapter",":"+ mSelectedSet.size());
            }
        });
        return itemView;
    }

    public List<ContactItemViewHolder> getmHolderCtrls() {
        return mHolderCtrls;
    }

    public void setmHolderCtrls(List<ContactItemViewHolder> mHolderCtrls) {
        this.mHolderCtrls = mHolderCtrls;
    }

    public Set<User> getmSelectedSet() {
        return mSelectedSet;
    }

    public void setmSelectedSet(Set<User> mSelectedSet) {
        this.mSelectedSet = mSelectedSet;
    }
}