package edu.sjsu.cmpe.partyon.fragment;

import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.entities.Party;
import edu.sjsu.cmpe.partyon.entities.Ticket;

public class MsgDetailFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG_TICKET_ID = "ticket_id";
    private String mTicketID = "";
    private Ticket mTicket;
    private TextView mSenderNameView, mSentTimeView,mMsgContentView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MsgDetailFragment() {
        // Required empty public constructor
    }


    public static MsgDetailFragment newInstance(String param1, String param2) {
        MsgDetailFragment fragment = new MsgDetailFragment();
        Bundle args = new Bundle();
        args.putString(TAG_TICKET_ID, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void inflateInformation(){

        Log.d("detail msg",mTicket.getMsg());
        try {
            mSenderNameView.setText(mTicket.getSender().fetchIfNeeded().getUsername());
            mSentTimeView.setText(mTicket.getCreatedAt().toString());
            mMsgContentView.setText(mTicket.getMsg());
            updateTicketStateToRead();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void updateTicketStateToRead(){
        mTicket.setMsgState(Ticket.STATE_MSG_READ);
        mTicket.saveInBackground();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_msg_detail, container, false);
        mSenderNameView = (TextView)v.findViewById(R.id.sender_name_view);
        mSentTimeView = (TextView)v.findViewById(R.id.sent_time_view);
        mMsgContentView = (TextView)v.findViewById(R.id.msg_content_view);
        if (getArguments() != null) {
            mTicketID = getArguments().getString(TAG_TICKET_ID);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if(mTicketID!=null){
            ParseQuery<Ticket> query = ParseQuery.getQuery(Ticket.class);
            //query.orderByAscending("_created_at");
            query.getInBackground(mTicketID, new GetCallback<Ticket>() {
                @Override
                public void done(Ticket object, ParseException e) {
                    if(e==null && object != null){
                        mTicket = object;
                        inflateInformation();
                        //mAccessTypeView.setText(party.getAddress());
                    }else {
                        e.printStackTrace();
                    }
                }
            });
        }
        int width = getResources().getDimensionPixelSize(R.dimen.msg_detail_box_width);
        int height = getResources().getDimensionPixelSize(R.dimen.msg_detail_box_height);
        getDialog().getWindow().setLayout(width, height);
        return  v;
    }

    void closeDialog(View v){
        dismiss();
    }




}
