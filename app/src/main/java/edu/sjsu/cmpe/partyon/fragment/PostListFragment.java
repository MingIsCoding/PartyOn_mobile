package edu.sjsu.cmpe.partyon.fragment;

import android.app.ListActivity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.activities.StatusAdapter;
import edu.sjsu.cmpe.partyon.adapter.PostListAdapter;
import edu.sjsu.cmpe.partyon.entities.Post;

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
    private static final String TAG ="PostListFragment";
    private RecyclerView mPostListRecyclerView;
    private PostListAdapter mPostListAdapter;
    private List<Post> postList;
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

        postList = new ArrayList<>();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);
        mPostListRecyclerView = (RecyclerView)view.findViewById(R.id.post_list_recyclerView);
        mPostListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPostListAdapter = new PostListAdapter(getActivity(),postList);
        mPostListRecyclerView.setAdapter(mPostListAdapter);
        fetchData();
        return view;

    }
    private void fetchData(){

        ParseQuery<Post> query = new ParseQuery<Post>("Post");
        query.orderByDescending("CreatedAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
//                Log.d(TAG,"post got:"+posts.size());
                if(e == null){
                    for(Post p : posts){
                        postList.add(p);
                    }
                    mPostListAdapter.notifyDataSetChanged();
                }else{
                    e.printStackTrace();
                    //Problem detected
                }
            }
        });
    }
}