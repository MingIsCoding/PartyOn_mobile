package edu.sjsu.cmpe.partyon.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.sjsu.cmpe.partyon.R;

public class MyProfileActivity extends CloseableActivity {

    @Override
    int getToolBarID() {
        return R.id.profile_toolbar;
    }

    @Override
    int getResourceID() {
        return R.layout.activity_my_profile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my_profile);
    }
}
