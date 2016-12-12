package edu.sjsu.cmpe.partyon.holder;

import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.adapter.ContactListAdapter;
import edu.sjsu.cmpe.partyon.config.App;
import edu.sjsu.cmpe.partyon.utilities.BadgeTool;

/**
 * Created by Ming on 11/25/16.
 */

public class ContactItemViewHolder{
    private CircularImageView mContactIcon;
    private TextView mContactTextView;
    private ContactListAdapter mAdapter;
    private FragmentActivity mContent;
    private CheckBox mContactCheckbox;
    private boolean isSelected;
/*    public void switchCheckState(){
        if(isSelected){
            mContactCheckbox.setChecked(false);
            mContactCheckbox.setVisibility(View.GONE);
            isSelected = false;
        }else {
            mContactCheckbox.setChecked(true);
            mContactCheckbox.setVisibility(View.VISIBLE);
            isSelected = true;
        }
    }*/
    public ContactItemViewHolder(View view, boolean isChecked) {
        mContactIcon = (CircularImageView) view.findViewById(R.id.contact_icon);
        mContactTextView = (TextView) view.findViewById(R.id.contact_name);
        mContactCheckbox = (CheckBox)view.findViewById(R.id.contact_checkbox);
        this.isSelected = isChecked;
        mContactIcon.setBorderColor(BadgeTool.getLevelColor(App.getUser().getPoints()));
        if(this.isSelected){
            mContactCheckbox.setChecked(true);
            view.setBackgroundColor(ContextCompat.getColor(
            mContent,R.color.wallet_holo_blue_light));
        }else {
            mContactCheckbox.setChecked(false);
            view.setBackgroundColor(0);
        }
        view.setTag(this);
        /*mAdapter = adapter;
        mContent = content;*/

    }

    public ImageView getmContactIcon() {
        return mContactIcon;
    }

    public void setmContactIcon(CircularImageView mContactIcon) {
        this.mContactIcon = mContactIcon;
    }

    public TextView getmContactTextView() {
        return mContactTextView;
    }

    public void setmContactTextView(TextView mContactTextView) {
        this.mContactTextView = mContactTextView;
    }

    public CheckBox getmContactCheckbox() {
        return mContactCheckbox;
    }

    public void setmContactCheckbox(CheckBox mContactCheckbox) {
        this.mContactCheckbox = mContactCheckbox;
    }

    public boolean isSelected() {
        return mContactCheckbox.isSelected();
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
