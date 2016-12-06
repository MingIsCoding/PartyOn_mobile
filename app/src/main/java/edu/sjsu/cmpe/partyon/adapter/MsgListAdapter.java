package edu.sjsu.cmpe.partyon.adapter;

import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Toast;

import com.parse.ParseException;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.entities.Party;
import edu.sjsu.cmpe.partyon.entities.Ticket;
import edu.sjsu.cmpe.partyon.entities.User;
import edu.sjsu.cmpe.partyon.holder.ContactItemViewHolder;
import edu.sjsu.cmpe.partyon.holder.MsgViewHolder;

/**
 * Created by Ming on 11/25/16.
 */

public class MsgListAdapter extends BaseAdapter {
    private List<Ticket> ticketList;
    private FragmentActivity mContent;
    private boolean isCheckable = false;
    public MsgListAdapter(List<Ticket> tickets, FragmentActivity content){
        this.ticketList = tickets;
        if(ticketList == null){
            ticketList = new ArrayList<>();
        }
        this.mContent = content;
    }
    @Override
    public int getCount() {
        return ticketList.size();
    }

    @Override
    public Ticket getItem(int i) {
        return ticketList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View itemView, ViewGroup viewGroup) {
        MsgViewHolder holder;
        if (itemView == null) {
            itemView = View.inflate(mContent,
                    R.layout.item_msg_list, null);

            holder = new MsgViewHolder(itemView);
        }else{
            holder = (MsgViewHolder) itemView.getTag();
        }

        Ticket t = ticketList.get(i);
        try {
            holder.getmContactTextView().setText(((Party)t.getParty().fetchIfNeeded()).getName());
            if(t.getMsgState() == Ticket.STATE_MSG_UNNOTIFIED){
                holder.getmContactTextView().setTypeface(null, Typeface.BOLD);
                holder.getmContactTextView().setTextColor(mContent.getResources().getColor(R.color.colorPrimaryDark));
                holder.getmContactIcon().setColorFilter(ContextCompat.getColor(mContent,R.color.colorPrimary));
            }
            //Picasso.with(mContent).load(t.getProfilePicSmall()).into(holder.getmContactIcon());
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
        return itemView;
    }



}