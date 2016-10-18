package edu.sjsu.cmpe.partyon.activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

import edu.sjsu.cmpe.partyon.R;

/**
 * Created by navdeep on 10/16/2016.
 */
public class StatusAdapter extends ArrayAdapter<ParseObject>{
    protected Context mContext;      //gets the context
    protected List<ParseObject> mStatus;    //list of parse objects,status in list

    public StatusAdapter(Context context, List<ParseObject> status){
        super(context, R.layout.post_list_custom_layout, status);   //status is the values in the list
        mContext = context;     //getters and setters.
        mStatus= status;
    }

    @Override       //using this method to inflate each row
    public View getView(final int position, View view, ViewGroup parent){
        ViewHolder holder;      //initialize the viewholder

        //if no items to display then create the view

        if(view == null ){
            view = LayoutInflater.from(mContext).inflate(       //inflate the layoutt
                    R.layout.post_list_custom_layout, null
            );

            holder = new ViewHolder();      //initialize the holder
            holder.usernameTextView = (TextView) view.findViewById(R.id.postUsername);
            holder.statusTextView = (TextView) view.findViewById(R.id.postStatus);

            //after conversion set the tag to holder as variable
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        ParseObject status = mStatus.get(position);     // for the row that has been clicked

        //username
        String username = status.getString("user"); // whatever the column name is in DB
        holder.usernameTextView.setText(username);  //set to whatever you want to display

        //Status

        String comment = status.getString("status");
        holder.statusTextView.setText(comment);     //set text to whatever you stored in the string

        return view;
    }

    public static class ViewHolder {    //using the view holder pattern
        TextView usernameTextView;      //I can use ImageView for images
        TextView statusTextView;
    }
}
