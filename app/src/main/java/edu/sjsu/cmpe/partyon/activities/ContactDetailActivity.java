package edu.sjsu.cmpe.partyon.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.entities.User;

public class ContactDetailActivity extends CloseableActivity {

    private String mContactID;
    private String mContactUsername;
    @Override
    int getToolBarID() {
        return R.id.contact_detail_toolbar;
    }

    @Override
    int getResourceID() {
        return R.layout.activity_contact_detail;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_wallet);
        Bundle bundle = getIntent().getExtras();
        mContactID = bundle.getString(User.ATT_USER_ID);
        mContactUsername = bundle.getString(User.ATT_USER_USERNAME);
        getSupportActionBar().setTitle(mContactUsername);
    }
}
