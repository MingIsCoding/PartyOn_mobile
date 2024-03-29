package edu.sjsu.cmpe.partyon.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.config.App;
import edu.sjsu.cmpe.partyon.entities.Like;
import edu.sjsu.cmpe.partyon.entities.Location;
import edu.sjsu.cmpe.partyon.entities.Party;
import edu.sjsu.cmpe.partyon.entities.Post;
import edu.sjsu.cmpe.partyon.entities.Reply;
import edu.sjsu.cmpe.partyon.entities.Ticket;
import edu.sjsu.cmpe.partyon.entities.Transaction;
import edu.sjsu.cmpe.partyon.entities.User;
import edu.sjsu.cmpe.partyon.fragment.ContactListFragment;
import edu.sjsu.cmpe.partyon.fragment.PartyListFragment;
import edu.sjsu.cmpe.partyon.fragment.PersonalInfoFragment;
import edu.sjsu.cmpe.partyon.fragment.PostListFragment;
import edu.sjsu.cmpe.partyon.services.NotificationService;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
//    private SectionsPagerAdapter mSectionsPagerAdapter;
    private List<String> mTitles = Arrays.asList("Post","Party","Contact","Info");
    private List<Fragment> mContents = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;
    private static int REQUEST_CREATE_NEW_PARTY = 1001;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    //private ViewPagerIndicator mIndicator;
    private TabLayout mTabLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //initParseAdapter();
        if(User.getCurrentUser() == null && !App.isDevMode){
            initThirdParties();
            Intent in = new Intent(this, LoginActivity.class);
            startActivity(in);
        }else {
            printLoggedInUserInfo();
            initViews();
            initData();
            //initService();
            initAlarm();
            mViewPager.setAdapter(mAdapter);
        }
    }

    private void printLoggedInUserInfo() {
        if(!App.isDevMode)
            Log.d(TAG,"username:"+User.getCurrentUser().getUsername());
    }

    private void initThirdParties() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
    private void initService(){
        Log.d(TAG,"initService");
        Intent in = new Intent(getBaseContext(), NotificationService.class);
        in.putExtra(NotificationService.RECEIVER_ID, App.getUser().getObjectId());
        startService(in);
    }
    private void initAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 15);
        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),(30*1000),broadcast);
    }
    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTabLayout = (TabLayout)findViewById(R.id.tabs);
        mViewPager = (ViewPager)findViewById(R.id.id_viewpager);
//        mIndicator = (ViewPagerIndicator)findViewById(R.id.id_indicator);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //mIndicator.scroll(position,positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        //mTabLayout.setSelectedTabIndicatorHeight(10);


    }

    private void initData() {


        PartyListFragment partyListFragment = PartyListFragment.newInstance("1","2");
/*        for(String title : mTitles){
            VpSimpleFragment newInstance = VpSimpleFragment.newInstance(title);
            mContents.add(newInstance);
        }*/
        //fragment new instance for the post list
        PostListFragment postInstance = PostListFragment.newInstance(null,null);
         //VpSimpleFragment postInstance = VpSimpleFragment.newInstance("Posts Tab");
//        VpSimpleFragment contactInstance = VpSimpleFragment.newInstance("Contact Tab");
        ContactListFragment contactInstance = ContactListFragment.newInstance("0","1");
        PersonalInfoFragment meInstance = PersonalInfoFragment.newInstance("1","2");
// adding for the post status method(navdeep) change accordingly.
        mContents.add(0,postInstance);
        //mContents.add(0,new MapPartyListFragment());
        mContents.add(1,partyListFragment);
        mContents.add(2,contactInstance);
        mContents.add(3,meInstance);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mContents.get(position);
            }

            @Override
            public int getCount() {
                return mContents.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles.get(position);
            }
        };
    }

/*    private void initParseAdapter(){
        if(!App.isParseAdapterInitiated){
            //Log.v(TAG, "on Creating..................................");
            Parse.enableLocalDatastore(this);
            Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                    .applicationId(App.backendServerAppID)
                    //.clientKey("test")
                    .server(App.backendServerURL).build());
            App.isParseAdapterInitiated = true;
            ParseObject.registerSubclass(User.class);
            ParseObject.registerSubclass(Party.class);
            ParseObject.registerSubclass(User.class);
            ParseObject.registerSubclass(Location.class);
            ParseObject.registerSubclass(Post.class);
            ParseObject.registerSubclass(Ticket.class);
            ParseObject.registerSubclass(Reply.class);
            ParseObject.registerSubclass(Like.class);
            ParseObject.registerSubclass(Transaction.class);
        }
    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CREATE_NEW_PARTY && resultCode == NewPartyActivity.RESULT_SUCCESS){
            Intent in = new Intent(MainActivity.this, PartyDetailScrollingActivity.class);
            in.putExtras(data.getExtras());
            startActivity(in);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_new_party){
            Intent i = new Intent(MainActivity.this, NewPartyActivity.class);//NewPartyActivity
                startActivityForResult(i,REQUEST_CREATE_NEW_PARTY);
        }else if(id == R.id.action_search_on_map){
            Intent i = new Intent(this, MapSearchActivity.class);
            startActivity(i);
        }// for user logout(nav)
        else if(id == R.id.logoutUser){
            Log.d(TAG, ParseUser.getCurrentUser().getUsername() + "is logging off.");
            ParseUser.logOut();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);  //redirect to login Activity
            intent.putExtra(LoginActivity.OP_CODE,LoginActivity.OP_LOG_OUT);
            startActivity(intent);

        }
        /*else if(id == R.id.action_new_post){
            Intent in = new Intent(MainActivity.this, NewPostActivity.class);
            startActivity(in);
        }*/// for clicking a new picture(currently in the menu tab) will be moved according to progress-nav
        else if(id == R.id.takePicture){
            if(App.getUser().getOngoingParty() == null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Not in a party yet");
                builder.setMessage("You can only post when you are in a party");
                builder.setPositiveButton("Search Parties", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(MainActivity.this, MapSearchActivity.class);
                        startActivity(i);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else {
                Intent in = new Intent(MainActivity.this, NewPictureActivity.class);
                startActivity(in);
            }

        }/*else if(id == R.id.horizontalTest){
            Intent intent = new Intent(MainActivity.this, TrademarkActivity.class);
            startActivity(intent);
        }
        else if(id ==R.id.updateStatus){
            Intent intent = new Intent(this, UpdateActivity.class);
            startActivity(intent);
        }*/

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}
