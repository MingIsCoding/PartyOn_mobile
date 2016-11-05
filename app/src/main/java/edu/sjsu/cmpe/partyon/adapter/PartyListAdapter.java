package edu.sjsu.cmpe.partyon.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.activities.PartyDetailActivity;
import edu.sjsu.cmpe.partyon.config.AppData;
import edu.sjsu.cmpe.partyon.entities.Party;
import edu.sjsu.cmpe.partyon.holder.PartyItemViewHolder;

/**
 * Created by Ming on 10/15/16.
 */

public class PartyListAdapter extends RecyclerView.Adapter<PartyItemViewHolder>{
    private List<Party> partyList;
    private FragmentActivity mContent;
    private static String TAG = "PartyListAdapter";
    public PartyListAdapter(FragmentActivity content, List<Party> list){
        mContent = content;
        partyList = list;
    }

    @Override
    public PartyItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContent);
        View view = layoutInflater.inflate(R.layout.item_party_list,parent,false);
        return new PartyItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PartyItemViewHolder holder, int position) {
        final Party party = partyList.get(position);
        holder.bindParty(party);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,party.getName()+" is clicked");
                    /*Toast.makeText(getActivity(),
                            party.getName(),
                            Toast.LENGTH_SHORT).show();*/

                /*Intent intent = new Intent(mContent, PartyDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(AppData.OBJ_PARTY_ID,party.getObjectId().toString());
                bundle.putString(AppData.OBJ_PARTY_NAME,party.getName().toString());
                intent.putExtras(bundle);
                mContent.startActivity(intent);*/
                RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
                v.setLayoutParams(lp);
                v.setBackgroundColor(ContextCompat.getColor(mContent,R.color.colorPrimary));
            }
        });
    }

    @Override
    public int getItemCount() {
        return partyList.size();
    }
}
