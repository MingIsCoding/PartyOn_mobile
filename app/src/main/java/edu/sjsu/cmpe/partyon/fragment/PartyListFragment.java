package edu.sjsu.cmpe.partyon.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.adapter.PartyListAdapter;
import edu.sjsu.cmpe.partyon.config.App;
import edu.sjsu.cmpe.partyon.entities.Party;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PartyListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartyListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "PartyListFragment";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mPartyListRecyclerView;
    private PartyListAdapter mPartyListAdapter;
    private List<Party> partyList;

    public PartyListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PartyListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PartyListFragment newInstance(String param1, String param2) {
        PartyListFragment fragment = new PartyListFragment();
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
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_party_list, container, false);
        mPartyListRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_list_party);
        mPartyListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        prepareData();
        updateUI();
        return view;
    }



    private void updateUI(){
        mPartyListAdapter = new PartyListAdapter(getActivity(), partyList);
        mPartyListRecyclerView.setAdapter(mPartyListAdapter);
    }
    private void prepareData() {
        partyList = new ArrayList<Party>();
        ParseQuery query = ParseQuery.getQuery(App.OBJ_NAME_PARTY);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Party>() {
            @Override
            public void done(List<Party> objects, ParseException e) {
                if (e == null) {
                    Log.v(TAG,"the parties size from server:" + objects.size());
                    for(Party p : objects){
                        Log.v(TAG,p.getName());
                        partyList.add(p);
                        mPartyListAdapter.notifyDataSetChanged();
                    }
                } else {
                    // something went wrong
                }
            }
        });
    }

}
