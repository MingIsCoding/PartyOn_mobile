package edu.sjsu.cmpe.partyon.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.config.AppData;
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
        mPartyListAdapter = new PartyListAdapter(partyList);
        mPartyListRecyclerView.setAdapter(mPartyListAdapter);
    }
    private void prepareData() {
        partyList = new ArrayList<Party>();
        Party p1 = new Party();
        p1.setName("Friday Party");
        Party p2 = new Party();
        p2.setName("Friday Party1");
        Party p3 = new Party();
        p3.setName("Friday Party2");

        ParseQuery query = ParseQuery.getQuery(AppData.OBJ_NAME_PARTY);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.v(TAG,"the parties size from server:" + objects.size());
                    for(Object o : objects){
                        Log.v(TAG,((Party)o).getName());
                        partyList.add((Party)o);
                        mPartyListAdapter.notifyDataSetChanged();
                    }
                } else {
                    // something went wrong
                }
            }
        });

        partyList.add(p1);
        partyList.add(p2);
        partyList.add(p3);
        partyList.add(p1);
        partyList.add(p2);
        partyList.add(p3);
        partyList.add(p1);
        partyList.add(p2);
    }


    private class PartyItemViewHolder extends RecyclerView.ViewHolder{
        public TextView mTitleTextView;
        private TextView mPartyNameView;
        public PartyItemViewHolder(View itemView){
            super(itemView);
            mPartyNameView = (TextView)itemView.findViewById(R.id.party_item_name);
            //mTitleTextView = (TextView)itemView;
        }
        public void bindParty(Party p){
            mPartyNameView.setText(p.getName());
        }
    }



    private class PartyListAdapter extends RecyclerView.Adapter<PartyItemViewHolder>{
        private List<Party> partyList;

        public PartyListAdapter(List<Party> list){
            Log.v(TAG, "the list's size is "+ list.size());

            partyList = list;
        }

        @Override
        public PartyItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.item_party_list,parent,false);
            return new PartyItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PartyItemViewHolder holder, int position) {
            Party party = partyList.get(position);
            holder.bindParty(party);
        }

        @Override
        public int getItemCount() {
            return partyList.size();
        }
    }
}
