package edu.sjsu.cmpe.partyon.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.entities.Party;

/**
 * Created by Ming on 10/15/16.
 */

public class PartyItemViewHolder extends RecyclerView.ViewHolder {
    public TextView mTitleTextView;
    private TextView mPartyNameView;
    private TextView mDescriptionView;
    public PartyItemViewHolder(View v){
        super(v);
        mPartyNameView = (TextView)v.findViewById(R.id.party_item_name);
        mDescriptionView = (TextView)v.findViewById(R.id.party_item_description);
        //mTitleTextView = (TextView)itemView;
    }
    public void bindParty(Party p){
        mPartyNameView.setText(p.getName());
        //mDescriptionView.setText(p.getDescription());
    }
}
