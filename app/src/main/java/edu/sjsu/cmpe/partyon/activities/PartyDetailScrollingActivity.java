package edu.sjsu.cmpe.partyon.activities;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.config.AppData;
import edu.sjsu.cmpe.partyon.entities.Party;

public class PartyDetailScrollingActivity extends CloseableActivity {
    private Party mParty;
    private Bundle mBundle;
    private static final String TAG = "PartyDetail";
    private TextView mPartyName, mAccessTypeView;

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
    }
    private void initViews(){
        //mPartyName = (TextView)findViewById(R.id.partyNameView);
        mAccessTypeView = (TextView)findViewById(R.id.partyAccessTypeView);
        //mPartyName.setText(mParty.getName());
    }
    private void initData(){
        mParty = ParseObject.create(Party.class);
        String partyID = mBundle.getString(AppData.OBJ_PARTY_ID);
        Log.v(TAG,"OBJ_PARTY_ID: "+partyID);
        Log.v(TAG,"OBJ_PARTY_NAME: "+mBundle.getString(AppData.OBJ_PARTY_NAME));

//        party.setObjectId(bundle.getString(AppData.OBJ_PARTY_ID));
        mParty.setName(mBundle.getString(AppData.OBJ_PARTY_NAME));
        getSupportActionBar().setTitle(mParty.getName());
//        this.getActionBar().hide();
//        mAccessTypeView.setText(party.getObjectId());
        ParseQuery<Party> query = ParseQuery.getQuery(Party.class);
        query.orderByAscending("_created_at");
        query.getInBackground(partyID, new GetCallback<Party>() {
            @Override
            public void done(Party object, ParseException e) {
                if(e==null && object != null){
                    mParty = object;
                    Log.d(TAG,"mParty.getAddress():"+mParty.getAddress());
                    //mAccessTypeView.setText(mParty.getAddress());
                }else {
                    e.printStackTrace();
                }
            }
        });

    }
}
