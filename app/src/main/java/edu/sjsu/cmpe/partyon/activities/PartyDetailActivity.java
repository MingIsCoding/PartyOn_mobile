package edu.sjsu.cmpe.partyon.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.config.AppData;
import edu.sjsu.cmpe.partyon.entities.Party;

public class PartyDetailActivity extends AppCompatActivity {
    private final static String TAG = "PartyDetailActivity";
    private String partyID;
    private Party party;
    private TextView mPartyName, mAccessTypeView;
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_detail);
        bundle = getIntent().getExtras();
        initData();
        initView();

    }

    private void initView(){
        mPartyName = (TextView)findViewById(R.id.partyNameView);
        mAccessTypeView = (TextView)findViewById(R.id.partyAccessTypeView);
        mPartyName.setText(party.getName());
        setupToolBar();
    }
    private void setupToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        if (toolbar == null) return;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void initData(){
        party = ParseObject.create(Party.class);
        String partyID = bundle.getString(AppData.OBJ_PARTY_ID);
        Log.v(TAG,"OBJ_PARTY_ID: "+partyID);
        Log.v(TAG,"OBJ_PARTY_NAME: "+bundle.getString(AppData.OBJ_PARTY_NAME));

//        party.setObjectId(bundle.getString(AppData.OBJ_PARTY_ID));
        party.setName(bundle.getString(AppData.OBJ_PARTY_NAME));
//        this.getActionBar().hide();
//        mAccessTypeView.setText(party.getObjectId());
        ParseQuery<Party> query = ParseQuery.getQuery(Party.class);
        query.orderByAscending("_created_at");
        query.getInBackground(partyID, new GetCallback<Party>() {
            @Override
            public void done(Party object, ParseException e) {
                if(e==null && object != null){
                    party = object;
                    mAccessTypeView.setText(party.getAddress());
                }else {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
