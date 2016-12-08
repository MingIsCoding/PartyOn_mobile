package edu.sjsu.cmpe.partyon.activities;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.adapter.MsgListAdapter;
import edu.sjsu.cmpe.partyon.config.AppData;
import edu.sjsu.cmpe.partyon.entities.Party;
import edu.sjsu.cmpe.partyon.entities.Ticket;
import edu.sjsu.cmpe.partyon.fragment.MsgDetailFragment;
import edu.sjsu.cmpe.partyon.holder.ContactItemViewHolder;
import edu.sjsu.cmpe.partyon.holder.MsgViewHolder;

public class MessageBoxActivity extends CloseableActivity {

    @Override
    int getToolBarID() {
        return R.id.message_box_toolbar;
    }
    DialogFragment mDetailDiaglogFragment;
    @Override
    int getResourceID() {
        return R.layout.activity_message_box;
    }
    private SwipeMenuListView mListView;
    private MsgListAdapter mAdapter;
    private List<Ticket> mMessageList;
    private Ticket mTicket;
    private final static String TAG = "MessageBoxActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_message_box);
        initVew();
        checkMessages();
    }
    private void initVew(){
        mListView = (SwipeMenuListView)findViewById(R.id.message_listView);
        if(AppData.messageList !=null && AppData.messageList.size() != 0){
            mMessageList = new ArrayList<>(AppData.messageList);
        }else {
            mMessageList = new ArrayList<>();
        }
        mAdapter = new MsgListAdapter(mMessageList,this);
        mListView.setAdapter(mAdapter);
// step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem acceptItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                acceptItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                acceptItem.setWidth(dp2px(90));
                // set item title
                acceptItem.setTitle("Accept");
                // set item title fontsize
                acceptItem.setTitleSize(18);
                // set item title font color
                acceptItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(acceptItem);
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setTitle("DELETE");
                // set item title fontsize
                deleteItem.setTitleSize(14);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0://accept and open
                        mTicket = mMessageList.get(index);
                        doAcceptAndOpenParty();
                        break;
                    case 1:
                        // delete
                        mTicket = mMessageList.get(index);
                        doDelete();
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });

        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {

                //((ContactItemViewHolder)view.getTag()).switchCheckState();
                return false;
            }

        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mTicket = mMessageList.get(i);
                mDetailDiaglogFragment =
                        MsgDetailFragment.newInstance(mMessageList.get(i).getObjectId(),null);
                Log.d(TAG,"selected"+i);
                mDetailDiaglogFragment.show(getFragmentManager(),"Message Detail");

            }
        });
    }
    private void checkMessages(){
        if(AppData.messageList == null || AppData.messageList.size() == 0){
            AppData.messageList = new ArrayList<>();
            ParseQuery query = ParseQuery.getQuery(AppData.OBJ_NAME_TICKET);
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<Ticket>() {
                @Override
                public void done(List<Ticket> objects, ParseException e) {
                    if (e == null) {
                        Log.v(TAG,"the parties size from server:" + objects.size());
                        for(Ticket t : objects){
                            mMessageList.add(t);
                            mAdapter.notifyDataSetChanged();
                            AppData.messageList = new ArrayList<Ticket>(objects);
                        }
                    } else {
                        Log.e(TAG,e.getMessage());
                    }
                }
            });
        }
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void acceptInvitation(View view) {
        doAcceptAndOpenParty();
    }

    public void refuseInvitation(View view) {
        doRefuseInvitation();
    }
    private void doRefuseInvitation(){
        mTicket.setState(Ticket.STATE_REQUESTED);
        mTicket.saveInBackground();
    }
    private void doAcceptAndOpenParty(){
        mTicket.setMsgState(Ticket.STATE_MSG_READ);
        mTicket.saveInBackground();
        Intent intent = new Intent(MessageBoxActivity.this,
                PartyDetailScrollingActivity.class);
        intent.putExtra(PartyDetailScrollingActivity.OP_CODE,
                PartyDetailScrollingActivity.ACCEPT_INVITATION);
        intent.putExtra(PartyDetailScrollingActivity.OP_TICKET,
                mTicket.getObjectId());
        Bundle bundle = new Bundle();
        bundle.putString(AppData.OBJ_PARTY_ID,mTicket.getPartyID().toString());
        bundle.putString(AppData.OBJ_PARTY_NAME,"Loading...");
        intent.putExtras(bundle);

        startActivity(intent);
    }
    private void doDelete(){
        mTicket.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                mMessageList.remove(mTicket);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
