package edu.sjsu.cmpe.partyon.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.config.AppData;
import edu.sjsu.cmpe.partyon.entities.Location;
import edu.sjsu.cmpe.partyon.entities.Party;
import edu.sjsu.cmpe.partyon.entities.User;

public class UpdateActivity extends AppCompatActivity {
    private static final String TAG = "Navdeep Activity";
    protected EditText mStatusUpdate;
    protected Button mStatusUpdateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_update);
        // initParseAdapter();
        //initialize
        mStatusUpdate = (EditText) findViewById(R.id.updateStatusText);
        mStatusUpdateButton = (Button) findViewById(R.id.updateButton);

        //listen to button click
        mStatusUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the current User;
                // ParseUser user = ParseUser.getCurrentUser();


                //String currentUsername = User.getCurrentUser().getUsername();
//                Log.v(TAG, User.getCurrentUser().getUsername());
                //get the edit status updated into string
                String newStatus = mStatusUpdate.getText().toString();

                //save the status into parse
                ParseObject statusObject= new ParseObject("Status");
                statusObject.put("status",newStatus);
                //statusObject.put("username", currentUsername);
                statusObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            //successful storage

                            Toast.makeText(UpdateActivity.this, "Successfully Posted", Toast.LENGTH_LONG).show();

                            //Take the user to homepage

                            Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            //Error in storage
                            Toast.makeText(UpdateActivity.this, "Some error occurred", Toast.LENGTH_LONG).show();


                        }
                    }
                });



            }
        });



//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    private void initParseAdapter(){
        if(!AppData.isParseAdapterInitiated){
            //Log.v(TAG, "on Creating..................................");
            Parse.enableLocalDatastore(this);
            Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                    .applicationId(AppData.backendServerAppID)
                    //.clientKey("test")
                    .server(AppData.backendServerURL).build());
            AppData.isParseAdapterInitiated = true;
            ParseObject.registerSubclass(Party.class);
            ParseObject.registerSubclass(User.class);
            ParseObject.registerSubclass(Location.class);
        }
    }

}
