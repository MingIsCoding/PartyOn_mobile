package edu.sjsu.cmpe.partyon.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.entities.Profile;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by navdeep on 11/5/2016.
 */

public class ProfileAdapter extends ArrayAdapter<Profile> {

    protected Context mContext;
    protected List<Profile> mProfile;// = new ArrayList<Profile>();
    public ProfileAdapter(Context context, List<Profile> profile) {
        super(context, R.layout.personal_list_custom,profile);
    //getter setters
        mContext = context;
        mProfile = profile;
    }

    //@NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    View view = convertView;
        //we need a view to work with(might be null)
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.personal_list_custom, parent, false);
        }

       // get the clicked item
        final Profile currentItem = mProfile.get(position);

        //Put Items in the view
        ImageView imageView = (ImageView) view.findViewById(R.id.item_image);
        imageView.setImageResource(currentItem.getIconID());

        //item details/name
        TextView textView = (TextView) view.findViewById(R.id.item_detail);
        textView.setText(currentItem.getTitle());


//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Profile clickedItem = mProfile.get(position);
//                String message = "Clicked on " + position + " item " + clickedItem.getTitle();
//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();            }
//        });
                //registerClickCall();
        return view;
//        return super.getView(position, convertView, parent);
    }
    // To listen to the clicks
//    private void registerClickCall(){
//
//
//
//    }

}
