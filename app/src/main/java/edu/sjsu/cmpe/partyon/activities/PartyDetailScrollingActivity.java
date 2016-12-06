package edu.sjsu.cmpe.partyon.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.config.AppData;
import edu.sjsu.cmpe.partyon.entities.Party;
import edu.sjsu.cmpe.partyon.entities.Ticket;
import edu.sjsu.cmpe.partyon.entities.User;
import edu.sjsu.cmpe.partyon.fragment.PostListFragment;

public class PartyDetailScrollingActivity extends CloseableActivity {
    public final static String OP_CODE = "op_code";
    public final static String OP_TICKET = "op_ticket";
    public final static int ACCEPT_INVITATION = 100;
    private Party mParty;
    private Bundle mBundle;
    private static final String TAG = "PartyDetail";
    private TextView mPartyName, mAccessTypeView;
    private PostListFragment mPostListFragment;
    private Ticket mTicket;
    private String mPartyID;
    private final static int REQUEST_PEOPLE_PICK = 2;
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    private Button mQuickBtn1,mQuickBtn2,mQuickBtn3; // the relationship between current user and the party; join, check-in, quit

    @Override
    int getToolBarID() {
        return R.id.party_detail_toolbar;
    }

    @Override
    int getResourceID() {
        return R.layout.activity_party_detail_scrolling;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
        initViews();
        initData();
        executeAttachedProcess();
    }
    private void initViews(){
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collasping_toolbar_layout);
        //mPartyName = (TextView)findViewById(R.id.partyNameView);
        mAccessTypeView = (TextView)findViewById(R.id.partyAccessTypeView);
        //mPartyName.setText(mParty.getName());
        mPostListFragment = (PostListFragment)getSupportFragmentManager()
                .findFragmentById(R.id.party_post_list_fragment);
        mQuickBtn1 = (Button)findViewById(R.id.quickBtn1);
        mQuickBtn2 = (Button)findViewById(R.id.quickBtn2);
        mQuickBtn3 = (Button)findViewById(R.id.quickBtn3);
    }
    private void initData(){
        mParty = ParseObject.create(Party.class);
        mPartyID = mBundle.getString(Party.OBJ_PARTY_ID);
        Log.v(TAG,"OBJ_PARTY_ID: "+mPartyID);
        Log.v(TAG,"OBJ_PARTY_NAME: "+mBundle.getString(Party.OBJ_PARTY_NAME));

        mParty.setName(mBundle.getString(AppData.OBJ_PARTY_NAME));
        //getSupportActionBar().setTitle(mParty.getName());
        mCollapsingToolbarLayout.setTitle(mParty.getName());
        ParseQuery<Party> query = ParseQuery.getQuery(Party.class);
        query.orderByAscending("_created_at");
        query.getInBackground(mPartyID, new GetCallback<Party>() {
            @Override
            public void done(Party object, ParseException e) {
                if(e==null && object != null){
                    mParty = object;
                    Log.d(TAG,"mParty.getAddress():"+mParty.getAddress());
                    partyLoaded();
                    //mAccessTypeView.setText(mParty.getAddress());
                }else {
                    e.printStackTrace();
                }
            }
        });



    }
    private void executeAttachedProcess(){
        if(getIntent().getIntExtra(OP_CODE,0) == ACCEPT_INVITATION){
            mTicket = AppData.getTicketFromMsgListByID(getIntent().getStringExtra(OP_TICKET));
            mTicket.setState(Ticket.STATE_UNCHECKED_IN);
            mTicket.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    updateQuickButtons();
                    Toast.makeText(PartyDetailScrollingActivity.this,
                            "Please check in when you are in the party.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void partyLoaded(){
        Log.d(TAG,"mParty.getAccessType():"+mParty.getAccessType());
        if(mParty.getAccessType() == 1){
            mCollapsingToolbarLayout.setTitle(mParty.getName()+"(p)");
            Log.d(TAG,"mParty.getAccessType(): private");
        }
        if(mTicket == null){
            ParseQuery<Ticket> ticketQuery = ParseQuery.getQuery(Ticket.class);
            ticketQuery.whereEqualTo("partyID",mPartyID);
            ticketQuery.whereEqualTo("receiverID",AppData.getUser().getObjectId());
            ticketQuery.getFirstInBackground(new GetCallback<Ticket>() {
                @Override
                public void done(Ticket t, ParseException e) {
                    if(t!=null){
                        mTicket = t;
                    }
                    updateQuickButtons();
                }
            });
        }

    }
    public void updateQuickButtons(){
        if(mTicket == null) {
            if(mParty.getAccessType() == 1){
                mQuickBtn1.setText("REQUEST");
            }else{
                mQuickBtn1.setText("JOIN");
            }
        }else if(mTicket.getState() == Ticket.STATE_REQUESTED){
            mQuickBtn1.setText("UN-REQUEST");
            mQuickBtn2.setText("SHARE");
            mQuickBtn3.setText("BUTTON3");
        }else if(mTicket.getState() == Ticket.STATE_INVITED){
            mQuickBtn1.setText("JOIN");
            mQuickBtn2.setText("REFUSE");
            mQuickBtn3.setText("SHARE");
        }else if(mTicket.getState() == Ticket.STATE_UNCHECKED_IN){
            mQuickBtn1.setText("CHECK-IN");
            mQuickBtn2.setText("CANCEL");
            mQuickBtn3.setText("SHARE");
        }else if(mTicket.getState() == Ticket.STATE_CHECKED_IN){
            mQuickBtn1.setText("LEAVE");
            mQuickBtn2.setText("POST");
            mQuickBtn3.setText("RATE");
        }
    }
    public void updateConnection(View btn) {
        if(btn.getId() == R.id.quickBtn1){
            if(mTicket == null) {
                Ticket t = new Ticket();
                t.setPartyID(mParty.getObjectId());
                t.setParty(mParty);
                t.setReceiver(AppData.getUser());
                t.setReceiverID(AppData.getUser().getObjectId());
                if(mParty.getAccessType() == 1){
                    t.setState(Ticket.STATE_REQUESTED);
                }else {
                    t.setState(Ticket.STATE_UNCHECKED_IN);
                }

                t.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(PartyDetailScrollingActivity.this,
                                    "A request has been sent.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                mTicket = t;
            }else if(mTicket.getState() == Ticket.STATE_REQUESTED){//un-request
                try {
                    mTicket.delete();
                    mTicket = null;
                    updateQuickButtons();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else if(mTicket.getState() == Ticket.STATE_INVITED){//accept invitation
                mTicket.setState(Ticket.STATE_UNCHECKED_IN);
                mTicket.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(PartyDetailScrollingActivity.this,
                                "Please check in when you are in the party.", Toast.LENGTH_SHORT).show();
                    }
                });
            }else if(mTicket.getState() == Ticket.STATE_UNCHECKED_IN){//check in
                mTicket.setState(Ticket.STATE_CHECKED_IN);
                AppData.getUser().setOngoingParty(mParty);
                AppData.getUser().saveInBackground();
                mTicket.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(PartyDetailScrollingActivity.this,
                                "You already checked in, enjoy!", Toast.LENGTH_SHORT).show();
                    }
                });
            }else if(mTicket.getState() == Ticket.STATE_CHECKED_IN){//leave
                mTicket.setState(Ticket.STATE_LEFT);
                AppData.getUser().setOngoingParty(null);
                AppData.getUser().saveInBackground();
                mTicket.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(PartyDetailScrollingActivity.this,
                                "You already left the party!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else if(btn.getId() == R.id.quickBtn2){
            if(mTicket == null) {//share
                Toast.makeText(PartyDetailScrollingActivity.this,
                                    "share this party.", Toast.LENGTH_SHORT).show();
            }else if(mTicket.getState() == Ticket.STATE_REQUESTED){//un-request
                Toast.makeText(PartyDetailScrollingActivity.this,
                        "share this party.", Toast.LENGTH_SHORT).show();
            }else if(mTicket.getState() == Ticket.STATE_INVITED){//refuse invitation
                try {
                    mTicket.delete();
                    mTicket = null;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else if(mTicket.getState() == Ticket.STATE_UNCHECKED_IN){//cancel
                try {
                    mTicket.delete();
                    mTicket = null;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else if(mTicket.getState() == Ticket.STATE_CHECKED_IN){//new post
                Toast.makeText(PartyDetailScrollingActivity.this,
                                "Take a photo", Toast.LENGTH_SHORT).show();

            }
        }else if(btn.getId() == R.id.quickBtn3){

        }

        updateQuickButtons();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_party_detail, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_invite_friends){
            Intent in = new Intent(this,PeoplePickerActivity.class);
            startActivityForResult(in,REQUEST_PEOPLE_PICK);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"requestCode"+requestCode+" resultCode:"+resultCode);
        if(requestCode == REQUEST_PEOPLE_PICK && resultCode == PeoplePickerActivity.RESULT_SUCCESS){
            Log.d(TAG,"get selected people:"+AppData.selectedPeople.size());
            pushTicket();
        }
    }
    private void pushTicket(){
        if(AppData.selectedPeople == null || AppData.selectedPeople.size() == 0){
            return;
        }
        for(final User u : AppData.selectedPeople){
            Ticket ticket = new Ticket();
            ticket.setSender(AppData.getUser());
            ticket.setSenderID(AppData.getUser().getObjectId());
            ticket.setReceiver(u);
            ticket.setReceiverID(u.getObjectId());
            ticket.setParty(mParty);
            ticket.setPartyID(mParty.getObjectId());
            ticket.setState(Ticket.STATE_INVITED);
            ticket.setMsgState(Ticket.STATE_MSG_UNNOTIFIED);
            ticket.setMsg(AppData.getUser().getUsername()+" is inviting you to "+mParty.getName());
            ticket.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    AppData.selectedPeople.remove(u);
                    if(AppData.selectedPeople.size() == 0){
                        Toast.makeText(PartyDetailScrollingActivity.this,
                                "All invitations have been sent.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
