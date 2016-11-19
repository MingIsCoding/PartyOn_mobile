package edu.sjsu.cmpe.partyon.fragment;

import android.app.ListActivity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.activities.StatusAdapter;
import edu.sjsu.cmpe.partyon.adapter.PostListAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //PostListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView mPostListView;
    private Button mFragmentButton;
    private List<ParseObject> mStatus;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // private OnFragmentInteractionListener mListener;

    public PostListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostListFragment newInstance(String param1, String param2) {
        PostListFragment fragment = new PostListFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);
        //mFragmentButton = (Button) getActivity().findViewById(R.id.fragmentButton);
        mPostListView = (ListView) view.findViewById(R.id.list);

        Log.v(ARG_PARAM1,"this is the post fragment and it works");

//        mFragmentButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.v(ARG_PARAM1,"this is the post fragment and it works");
//               Toast.makeText(this, "this fragment works", Toast.LENGTH_LONG).show();
//            }
//        });

        //Using the adapter for the view
        //ParseQuery has a generic type of parse object
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Status");
        query.orderByDescending("CreatedAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> statusObject, ParseException e) {
                if(e == null){
                    //successful
                    mStatus = statusObject;

                    //call the adapter
                   StatusAdapter adapter = new StatusAdapter(getContext(), mStatus);     //take the context

                    //PostListAdapter adapter = new PostListAdapter(getContext(),mStatus));
                    mPostListView.setAdapter(adapter);

                }else{
                    //Problem detected
                }
            }
        });



        return view;

    }



//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

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

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
