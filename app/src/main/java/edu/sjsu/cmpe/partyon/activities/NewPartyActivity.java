package edu.sjsu.cmpe.partyon.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.config.AppData;
import edu.sjsu.cmpe.partyon.entities.Location;
import edu.sjsu.cmpe.partyon.entities.Party;

public class NewPartyActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener{

    private final static String TAG = "NewPartyActivity";
    private EditText nameField;
    private EditText descriptionField;
    private EditText startDateTimeField, endDateTimeField,addressField;
    private Date startDateTime, endDateTime;
    private Spinner ageGroupSpinner, partyTypeSpinner, capacitySpinner;
    private ImageButton placePikerBtn;
    private Switch privatePartySwitch;
    private SlideDateTimeListener slideDateTimeListener;
    private SimpleDateFormat mDateTimeFormatter = new SimpleDateFormat("MM/dd hh:mm");
    private boolean isWorkingOnStartDateTime = false;
    private static int PLACE_PICKER_REQUEST = 1;
    private Place partyPlace;
    private Menu createMenu;
    private Party creatingParty;
//    private AVLoadingIndicatorView avLoader;
    private ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        addListeners();
    }

    private void initViews(){
        setContentView(R.layout.activity_new_party);
        nameField = (EditText)findViewById(R.id.nameField);
        descriptionField = (EditText)findViewById(R.id.descriptionField);
        startDateTimeField = (EditText)findViewById(R.id.startDateTimeField);
        startDateTimeField.getBackground().clearColorFilter();
        endDateTimeField = (EditText)findViewById(R.id.endDateTimeField);
        endDateTimeField.getBackground().clearColorFilter();
        addressField = (EditText)findViewById(R.id.addressField);
        createMenu = (Menu)findViewById(R.id.action_createMenu);
        privatePartySwitch = (Switch)findViewById(R.id.accessTypeSwitch);
        ageGroupSpinner = (Spinner)findViewById(R.id.ageGroupSpinner);
        capacitySpinner = (Spinner)findViewById(R.id.capacityRangeSpinner);
        partyTypeSpinner = (Spinner)findViewById(R.id.partyTypeSpinner);
        placePikerBtn = (ImageButton)findViewById(R.id.placePickerBtn);
//        avLoader = (AVLoadingIndicatorView)findViewById(R.id.loadingSpinner);
        progressBar = new ProgressDialog(this);

    }

    private void addListeners(){
        startDateTimeField.setOnFocusChangeListener(this);
        startDateTimeField.setOnClickListener(this);
        endDateTimeField.setOnFocusChangeListener(this);
        endDateTimeField.setOnClickListener(this);
        placePikerBtn.setOnClickListener(this);
        slideDateTimeListener = new SlideDateTimeListener() {
            @Override
            public void onDateTimeSet(Date date)
            {
                if(isWorkingOnStartDateTime){
                    startDateTime = date;
                    startDateTimeField.setText(mDateTimeFormatter.format(date));
                }else{
                    endDateTime = date;
                    endDateTimeField.setText(mDateTimeFormatter.format(date));
                }
            }
            // Optional cancel listener
            @Override
            public void onDateTimeCancel()
            {
//                Toast.makeText(NewPartyActivity.this,
//                        "Canceled", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_party, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_createMenu){
            startLoadingAnim("Saving...");
            saveNewParty();

        }
        return super.onOptionsItemSelected(item);
    }
    private boolean validateForm() throws Exception {
        if(nameField.getText().toString() == null || nameField.getText().toString().equals("")){
            throw new Exception("Name is required.");
        }
        if(descriptionField.getText().toString() == null || descriptionField.getText().toString().equals("")) {
            throw new Exception("Description is required");
        }
        return true;
    }
    private Party convertFormToPartyObj(){
        Party party = new Party();
        party.setName(nameField.getText().toString());
        if(privatePartySwitch.isChecked())
            party.setAccessType(1);
        else
            party.setAccessType(0);

        party.setAgeRangeStart(18);
        party.setAgeRangeEnd(30);
        party.setStartDateTime(startDateTime);
        party.setEndDateTime(endDateTime);
        party.setCapacityRangeStart(0);
        party.setCapacityRangeEnd(100);
        party.setDescription(descriptionField.getText().toString());
        party.setAddress(addressField.getText().toString());
        return party;
        //if(ageGroupSpinner.getSelectedItem().toString().equals(""))
    }
    private void saveLocation() throws Exception {
        if(partyPlace == null)
            throw new Exception("Address is not valid.");
        Location location = new Location();
        location.setName(partyPlace.getName().toString());
        location.setLatitude(partyPlace.getLatLng().latitude);
        location.setLongitude(partyPlace.getLatLng().longitude);
//        location.setStreet();
    }
    private void saveNewParty(){
        try {
            if(!validateForm()){
                stopLoadingAnim();
                return;
            }
        } catch (Exception e) {
            Toast.makeText(NewPartyActivity.this,
                    e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            stopLoadingAnim();
            return;
        }
        //save location first

        creatingParty = convertFormToPartyObj();
        creatingParty.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                stopLoadingAnim();

                Toast.makeText(NewPartyActivity.this,
                        "New party has been saved."+ creatingParty.getObjectId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewPartyActivity.this, PartyDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(AppData.OBJ_PARTY_ID,creatingParty.getObjectId().toString());
                bundle.putString(AppData.OBJ_PARTY_NAME,creatingParty.getName().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
    private void showDateTimePicker(Date initDate){
        Date initialDate;
        if(initDate != null){
            initialDate = initDate;
        }else
            initialDate = new Date();
        SlideDateTimePicker sdt = new SlideDateTimePicker(getSupportFragmentManager());
        sdt.setListener(slideDateTimeListener);
        sdt.setInitialDate(initialDate);
                //.setMinDate(minDate)
                //.setMaxDate(maxDate)
                //.setIs24HourTime(true)
                //.setTheme(SlideDateTimePicker.HOLO_DARK)
                //.setIndicatorColor(Color.parseColor("#990000"))
//        sdt.build();

        sdt.show();

    }
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.startDateTimeField){
            isWorkingOnStartDateTime = true;
            showDateTimePicker(startDateTime);
        }else if(view.getId() == R.id.endDateTimeField){
            isWorkingOnStartDateTime = false;
            showDateTimePicker(startDateTime);
        }else if(view.getId() == R.id.placePickerBtn){
            PlacePicker.IntentBuilder ppBuilder = new PlacePicker.IntentBuilder();
            Intent intent;
            try {
                intent = ppBuilder.build(this);
                startActivityForResult(intent,PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(getApplicationContext(),data);
                String address = place.getAddress().toString();
                partyPlace = place;
                addressField.setText(address);
            }
        }

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if(view.getId() == R.id.startDateTimeField && view.isFocused()){
            isWorkingOnStartDateTime = true;
            showDateTimePicker(startDateTime);
        }else if(view.getId() == R.id.endDateTimeField && view.isFocused()){
            isWorkingOnStartDateTime = false;
            showDateTimePicker(endDateTime);
        }
    }
    private void startLoadingAnim(String msg){
        progressBar.setMessage(msg);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();
        progressBarStatus = 0;
//        avLoader.show();
    }
    private void stopLoadingAnim(){
        progressBar.hide();
//        avLoader.hide();
    }
}
