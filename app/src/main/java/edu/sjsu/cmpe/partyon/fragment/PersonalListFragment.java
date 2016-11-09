package edu.sjsu.cmpe.partyon.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.adapter.ProfileAdapter;
import edu.sjsu.cmpe.partyon.adapter.ProfilePicAdapter;
import edu.sjsu.cmpe.partyon.entities.Profile;
import edu.sjsu.cmpe.partyon.entities.User;

import static android.R.attr.data;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonalListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonalListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private List<Profile> mProfile = new ArrayList<Profile>();
    private ListView mPersonalList;
    private ImageView mProfilePicture;
    private static final String TAG= "Click Register";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PersonalListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalListFragment newInstance(String param1, String param2) {
        PersonalListFragment fragment = new PersonalListFragment();
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

        View view= inflater.inflate(R.layout.fragment_personal_list, container, false);
        Log.v(ARG_PARAM1,"------- this is the personal Profile fragment and it works");

        // For showing the profile pic in the upper ImageView
        mProfilePicture = (ImageView) view.findViewById(R.id.profilePicture);

        //Query for Image URL in DATABASE
        //ParseUser user= new ParseUser.getCurrentUser();
        String url = ParseUser.getCurrentUser().getString("profilePicSmall");
        Picasso.with(getActivity()).load(url).into(mProfilePicture);


//        ParseQuery<User> query = new ParseQuery<User>("User");
//        query.whereEqualTo("username", user.getUsername());
//        query.findInBackground(new FindCallback<User>() {
//            @Override
//            public void done(List<User> objects, ParseException e) {

//                if(e==null){
////                    String url=
//                    //successful
//                    Picasso.with(getActivity()).load("http://i.imgur.com/DvpvklR.png").into(mProfilePicture);
//                }else{
//
//                }
//            }
//        });


        // For showing the List Items (basically List View)
        mPersonalList = (ListView) view.findViewById(R.id.profileList);
        ArrayAdapter<Profile> profileAdapter= new ProfileAdapter(getContext(),mProfile);
        mPersonalList.setAdapter(profileAdapter);

        mPersonalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Log.v("TAG", "Clicked row number:" + position);
               Profile currentItem = mProfile.get(position);
               String message = "Clicked on " + position + " item " + currentItem.getTitle();
               Toast.makeText(PersonalListFragment.this.getActivity(), message, Toast.LENGTH_LONG).show();
               /*Fragment does not extend context so add getActivity to make it work in a fragment(Fragment is not an activity)*/
           }
       });
        return view;
    }

    // To fill the items inside the list

    private void populateProfileList(){
        mProfile.add(new Profile("My Wallet",R.drawable.ic_card_giftcard_black_24dp));
        mProfile.add(new Profile("My Profile",R.drawable.ic_perm_identity_black_24dp));
        mProfile.add(new Profile("My Photos",R.drawable.ic_perm_media_black_24dp));
        mProfile.add(new Profile("My Posts",R.drawable.ic_picture_in_picture_black_24dp));
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
