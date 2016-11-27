package edu.sjsu.cmpe.partyon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.utilities.ProfileItem;

/**
 * Created by navdeep on 11/5/2016.
 */

public class ProfileAdapter extends ArrayAdapter<ProfileItem> {

    protected Context mContext;
    protected List<ProfileItem> mItems;// = new ArrayList<Profile>();
    public ProfileAdapter(Context context, List<ProfileItem> item) {
        super(context, R.layout.item_personal_list,item);
            mContext = context;
            mItems = item;
    }

    //@NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    View view = convertView;
        //we need a view to work with(might be null)
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_personal_list, parent, false);
        }

       // get the clicked item
        final ProfileItem currentItem = mItems.get(position);

        //Put Items in the view
        ImageView imageView = (ImageView) view.findViewById(R.id.personal_info_btn_list_icon);
        imageView.setImageResource(currentItem.getIconID());

        //item details/name
        TextView textView = (TextView) view.findViewById(R.id.personal_info_btn_list_name);
        textView.setText(currentItem.getTitle());

        return view;
    }


}
