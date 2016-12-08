package edu.sjsu.cmpe.partyon.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.activities.BadgeInfoActivity;
import edu.sjsu.cmpe.partyon.activities.MessageBoxActivity;
import edu.sjsu.cmpe.partyon.activities.WalletActivity;
import edu.sjsu.cmpe.partyon.adapter.ProfileAdapter;
import edu.sjsu.cmpe.partyon.config.AppData;
import edu.sjsu.cmpe.partyon.utilities.Blur;
import edu.sjsu.cmpe.partyon.utilities.ProfileItem;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PersonalInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private List<Profile> mProfile = new ArrayList<Profile>();
    private List<ProfileItem> mItemList;
    private ListView mPersonalList;
    private ImageView mProfilePicture;
    private LinearLayout mPersonal_info_layout;
    private TextView mUsername;
    private static final String TAG = "PersonalInfoFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final static int ITEM_PROFILE = 0;
    private final static int ITEM_WALLET = 1;
    private final static int ITEM_PHOTOS = 2;
    private final static int ITEM_QR_CODE = 3;
    private final static int ITEM_BADGE = 4;
    private final static int ITEM_MSGBOX = 5;
    final Target backgroundTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.d(TAG, "onBitmapLoaded");
            mPersonal_info_layout.setBackground(
                    new BitmapDrawable(getContext().getResources(), bitmap));
        }

        @Override
        public void onBitmapFailed(final Drawable errorDrawable) {
            Log.d(TAG, "FAILED");
        }

        @Override
        public void onPrepareLoad(final Drawable placeHolderDrawable) {
            Log.d(TAG, "Prepare Load");
            mPersonal_info_layout.setBackground(placeHolderDrawable);

        }
    };
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public PersonalInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalInfoFragment newInstance(String param1, String param2) {
        PersonalInfoFragment fragment = new PersonalInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            populateProfileList();      //if added in onCreateView it repopulates everytime

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_personal_list, container, false);
        mProfilePicture = (ImageView) view.findViewById(R.id.profilePicture);
        mUsername = (TextView) view.findViewById(R.id.personal_info_name_view);
        mUsername.setText(AppData.getUser().getUsername());
        //Query for Image URL in DATABASE
        //ParseUser user= new ParseUser.getCurrentUser();
        String url = ParseUser.getCurrentUser().getString("profilePicSmall");
        Picasso.with(getActivity()).load(url).into(mProfilePicture);
        mPersonal_info_layout = (LinearLayout) view.findViewById(R.id.personal_info_layout);
//        Blurry.with(getContext()).radius(25).sampling(2).onto((ViewGroup) mPersonal_info_layout);

        Picasso.with(getActivity()).load(url).transform(new Blur(getContext(), 20)).into(backgroundTarget);


        // For showing the List Items (basically List View)
        mPersonalList = (ListView) view.findViewById(R.id.profileList);
        ArrayAdapter<ProfileItem> profileAdapter = new ProfileAdapter(getContext(), mItemList);
        mPersonalList.setAdapter(profileAdapter);

        mPersonalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("TAG", "Clicked row number:" + position);
                ProfileItem currentItem = mItemList.get(position);
                switch (currentItem.getId()){
                    case ITEM_MSGBOX:
                        Intent msgIn = new Intent(getContext(), MessageBoxActivity.class);
                        startActivity(msgIn);
                        break;
                    case ITEM_WALLET:
                        Intent walletIn = new Intent(getContext(), WalletActivity.class);
                        startActivity(walletIn);
                        break;
                    case ITEM_BADGE:
                        Intent badgeIn = new Intent(getContext(), BadgeInfoActivity.class);
                        startActivity(badgeIn);
                        break;
                }
            }
        });
        return view;
    }

    // To fill the items inside the list

    private void populateProfileList() {
        mItemList = new ArrayList<>();
        mItemList.add(new ProfileItem(ITEM_PROFILE, "My Profile", R.drawable.ic_perm_identity_black_24dp));
        mItemList.add(new ProfileItem(ITEM_MSGBOX, "Message Box", R.drawable.ic_message_black_24dp));
        mItemList.add(new ProfileItem(ITEM_BADGE, "My Badge", R.drawable.ic_card_giftcard_black_24dp));
        mItemList.add(new ProfileItem(ITEM_PHOTOS, "My Post", R.drawable.ic_perm_media_black_24dp));
        mItemList.add(new ProfileItem(ITEM_WALLET, "My Wallet", R.drawable.ic_picture_in_picture_black_24dp));

    }


}