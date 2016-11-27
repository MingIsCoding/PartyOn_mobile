package edu.sjsu.cmpe.partyon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import edu.sjsu.cmpe.partyon.R;

/**
 * Created by navdeep on 11/22/2016.
 */

public class imageTestAdapter extends ArrayAdapter<ParseObject> {
   // private static final android.R.attr R = ;
    private Context mContext;
    private List<ParseObject> mImages;

    //No return types since it is a constructor
    public imageTestAdapter(Context context, List<ParseObject> images){
        super(context, R.layout.activity_image_test, images);
        mContext = context;
        mImages = images;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        final ViewHolder holder;
        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.single_row,null);
            holder = new ViewHolder();
            holder.imageTestView = (ImageView) view.findViewById(R.id.imageViewSingle);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        ParseObject object = mImages.get(position);
//        String message = "touched the item number "+position+" "+object.getObjectId();
//        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

        Picasso.with(getContext().getApplicationContext()).load(object.getParseFile("Photo").getUrl()).fit().centerCrop().noFade().into(holder.imageTestView);
        return view;
    }

    public static class ViewHolder{
        ImageView imageTestView;
    }
}
