package edu.sjsu.cmpe.partyon.adapter;

import android.widget.BaseAdapter;

/**
 * Created by Ming on 11/25/16.
 */

public abstract class BaseSwipListAdapter extends BaseAdapter {

    public boolean getSwipEnableByPosition(int position){
        return true;
    }
}
