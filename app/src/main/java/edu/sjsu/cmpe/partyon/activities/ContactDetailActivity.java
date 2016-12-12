package edu.sjsu.cmpe.partyon.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.entities.User;
import edu.sjsu.cmpe.partyon.fragment.PostListFragment;
import edu.sjsu.cmpe.partyon.utilities.Blur;

import static android.R.attr.fragment;

public class ContactDetailActivity extends CloseableActivity {

    private String mContactID;
    private String mContactUsername;
    private ImageView mContactProfilePic;
    private TextView mContactLevelView;
    private LinearLayout mContactInfoLayout;
    private User mContact;
    private PostListFragment mPostListFragment;

    private final String TAG = "ContactDetailActivity";
    private final Target backgroundTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.d(TAG, "onBitmapLoaded");
            mContactInfoLayout.setBackground(
                    new BitmapDrawable(getResources(), bitmap));
        }

        @Override
        public void onBitmapFailed(final Drawable errorDrawable) {
            Log.d(TAG, "FAILED");
        }

        @Override
        public void onPrepareLoad(final Drawable placeHolderDrawable) {
            Log.d(TAG, "Prepare Load");
            mContactInfoLayout.setBackground(placeHolderDrawable);

        }
    };
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
        Bundle bundle = getIntent().getExtras();
        mContactID = bundle.getString(User.ATT_USER_ID);
        mContactUsername = bundle.getString(User.ATT_USER_USERNAME);
        System.out.println("mContactID:"+mContactID);
        getSupportActionBar().setTitle(mContactUsername);
        mContactProfilePic = (ImageView)findViewById(R.id.contact_profilePicture);
        mContactLevelView = (TextView) findViewById(R.id.contact_info_level_view);
        mContactInfoLayout = (LinearLayout)findViewById(R.id.contact_info_layout);

        loadContact();
        loadPosts();
    }
    private void loadPosts(){
        mPostListFragment = PostListFragment.newInstance(null,mContactID);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.personal_post_list_fragment, mPostListFragment);
        ft.commit();
    }
    private void loadContact(){
        ParseQuery<User> query = ParseQuery.getQuery("_User");
        query.getInBackground(mContactID, new GetCallback<User>() {
            public void done(User user, ParseException e) {
                if (e == null) {
                    mContact = user;
                    doAfterLoading();
                } else {
                    e.printStackTrace();
                    // something went wrong
                }
            }
        });
    }
    private void doAfterLoading(){
        Log.d(TAG,mContact.getProfilePicSmall());
        Picasso.with(this).load(mContact.getProfilePicSmall()).into(mContactProfilePic);
        Picasso.with(this).load(mContact.getProfilePicSmall()).
                transform(new Blur(this, 20)).into(backgroundTarget);

    }
}
