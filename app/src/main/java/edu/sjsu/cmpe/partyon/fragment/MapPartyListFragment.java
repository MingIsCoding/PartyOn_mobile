package edu.sjsu.cmpe.partyon.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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


public class MapPartyListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "MapPartyListFragment";
    private List<Party> mResultList;
    private RecyclerView mResultListView;
    private PartyListAdapter mListAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public MapPartyListFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapPartyListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapPartyListFragment newInstance(String param1, String param2) {
        MapPartyListFragment fragment = new MapPartyListFragment();
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
        Log.d(TAG,"onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map_party_list, container, false);
        mResultListView = (RecyclerView) v.findViewById(R.id.recycler_list_party_result_map);
        mResultListView.setLayoutManager(new LinearLayoutManager(getContext()));
        //mResultListView.setAdapter(new SimpleAdapter());
        //prepareData();

        //testData();
        Log.d(TAG,"onCreateView");
        return v;
    }

    private void testData(){

        //mResultList = new ArrayList<>();
        //mListAdapter = new PartyListAdapter(getActivity(), mResultList);

        for(int i = 0; i < 10 ; i ++){
            Party p = new Party();
            p.setName("party_"+i);
            p.setDescription("new party");
            //mResultList.add(p);
        }

        //if(mResultListView.getAdapter() == null){
            mResultListView.setAdapter(this.mListAdapter);
        //}

        mListAdapter.notifyDataSetChanged();
    }


    private void prepareData() {
       // mResultList = new ArrayList<Party>();
        ParseQuery query = ParseQuery.getQuery(App.OBJ_NAME_PARTY);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Party>() {
            @Override
            public void done(List<Party> objects, ParseException e) {
                if (e == null) {
                    Log.v(TAG,"the parties size from server:" + objects.size());
                    for(Party p : objects){
                        Log.v(TAG,p.getName());
                        //mResultList.add(p);
                        mListAdapter.notifyDataSetChanged();
                    }
                } else {
                    // something went wrong
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"attached");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"onDetach");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG,"MapPartyListFragment");
    }


    public void updateResultList(List<Party> _resultList) {
        if(this.mResultList == null){
            mResultList = new ArrayList<>();
        }else{
            mResultList.clear();
            for(Party p : _resultList){
                mResultList.add(p);
            }
        }
        //this.mResultList. = _resultList;

        /*if(_resultList.size() == 0){
            Log.d(TAG,"clean data");
            mResultList.clear();
            mResultListView.clearAnimation();
        }*/

        if(mListAdapter == null){
            Log.d(TAG,"initializing");
            mListAdapter = new PartyListAdapter(getActivity(), mResultList);
            mResultListView.setAdapter(mListAdapter);
        }
        Log.d(TAG,"current setmResultList:"+ mResultList.size());

        mListAdapter.notifyDataSetChanged();
    }
}
