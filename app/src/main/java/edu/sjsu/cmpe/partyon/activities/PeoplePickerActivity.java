package edu.sjsu.cmpe.partyon.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.adapter.ContactListAdapter;
import edu.sjsu.cmpe.partyon.config.App;
import edu.sjsu.cmpe.partyon.entities.User;
import edu.sjsu.cmpe.partyon.holder.ContactItemViewHolder;
//import edu.sjsu.cmpe.partyon.holder.ContactItemViewHolder;

public class PeoplePickerActivity extends CloseableActivity {
    private List<User> mPeopleList;
    private ContactListAdapter mAdapter;
    private SwipeMenuListView mListView;
    private final static String TAG = "PeoplePickerActivity";
    public static int RESULT_SUCCESS = 102;


    @Override
    int getToolBarID() {
        return R.id.people_picker_toolbar;
    }

    @Override
    int getResourceID() {
        return R.layout.activity_people_picker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_people_picker);
        initView();
    }
    private void initView() {
        mListView = (SwipeMenuListView)findViewById(R.id.people_picker_listView);
        mListView.setChoiceMode(mListView.CHOICE_MODE_MULTIPLE);
        mPeopleList = new ArrayList<>(App.getUser().getFollows());
        if (mPeopleList == null) {
            mPeopleList = new ArrayList<User>();
        }
        mAdapter = new ContactListAdapter(mPeopleList, this, true);
        mListView.setAdapter(mAdapter);

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setTitle("UNFOLLOW");
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
                ContactItemViewHolder cth = mAdapter.getmHolderCtrls().get(i);
                Log.d(TAG,"selected"+i);

                /*if(!cth.isSelected()){
                    cth.switchCheckState();
                    mSelectedPeople.add(mPeopleList.get(i));
//                    mListView.setSelection(i);
                    Log.d(TAG,"selected"+i);
//                    mListView.setItemChecked(i, true);
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.wallet_holo_blue_light));
                }else {
                    mSelectedPeople.remove(mPeopleList.get(i));
                    view.setBackgroundColor(0);
                    cth.switchCheckState();
                }*/
            }
        });
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_people_picker, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_invite_finish){
            App.selectedPeople = new HashSet<>(mAdapter.getmSelectedSet());
            setResult(RESULT_SUCCESS);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
