package edu.sjsu.cmpe.partyon.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.List;

import edu.sjsu.cmpe.partyon.R;

public class TrademarkActivity extends AppCompatActivity {

//    private Integer images[] = {R.drawable.trade_2,R.drawable.trade_3, R.drawable.trade_4, R.drawable.trade_5,R.drawable.trade_6};
//    private int size=images.length;
    private List<ParseObject> mImages;
    final static String TAG="Trademark Activity ---";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trademark);
        testQuery();
        //addImagesToGallery();


    }
//    private void addImagesToGallery(){
//        LinearLayout imageGallery = (LinearLayout) findViewById(R.id.trademarkLinearLayout);
//        testQuery();
//
////        for (Integer image:images){
////            imageGallery.addView(getImageView(image));
////        }
//        for(int i=0; i<size; i++){
//            ImageView imageView = new ImageView(getApplicationContext());
//            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//            lParams.setMargins(0,0,10,0);
//            imageView.setId(i);
//            imageView.setLayoutParams(lParams);
//            imageView.setImageResource(images[i]);
//
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    int number = getNumber(v);
//                    Toast.makeText(TrademarkActivity.this, "touched an Item"+number, Toast.LENGTH_LONG).show();
//                }
//            });
//            imageGallery.addView(imageView);
//        }
//    }

    private int getNumber(View view){
        return view.getId();
    }

    public void testQuery(){
        ParseQuery<ParseObject> query= new ParseQuery<>("User_Photos");


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> images, ParseException e) {
                if(e==null){
                    LinearLayout imageGallery = (LinearLayout) findViewById(R.id.trademarkLinearLayout);

                    for(ParseObject object:images){
                        //ParseFile file = new ParseFile();
                        ParseFile file = object.getParseFile("Photo");
                        String path= file.getUrl();
                        //String newPath = path.toString();
                        Log.d(TAG,path);
                        //setTheImage(path);
                        ImageView imageView = new ImageView(getApplicationContext());
                        // Set the layout params (width and height)
                        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(150, 150);
                        // set margings, left top right bottom
                        lParams.setMargins(0,0,10,0);
                        imageView.setLayoutParams(lParams);
                        //Add the view before adding picasso, doesn't show otherwise
                        imageGallery.addView(imageView);
                        Picasso.with(TrademarkActivity.this).load(path).into(imageView, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                Log.d(TAG,"onSuccess");
                            }

                            @Override
                            public void onError() {
                                Log.d(TAG,"Error ocurred");
                            }
                        });
                        //imageGallery.addView(getImageView(path));
                    }
                }else{
                    Toast.makeText(TrademarkActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

//    private View getImageView(String path){
//        ImageView imageView = new ImageView(getApplicationContext());
//        // Set the layout params
//        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//        // set margings, left top right bottom
//        lParams.setMargins(0,0,10,0);
//        imageView.setLayoutParams(lParams);
//
//        Picasso.with(TrademarkActivity.this).load(path).into(imageView, new com.squareup.picasso.Callback() {
//            @Override
//            public void onSuccess() {
//                Log.d(TAG,"onSuccess");
//            }
//
//            @Override
//            public void onError() {
//                Log.d(TAG,"Error ocurred");
//            }
//        });
//        //Set resources for images since getting from drawable
//
////        imageView.setImageResource(image);
////        imageView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Toast.makeText(TrademarkActivity.this, "touched an Item", Toast.LENGTH_LONG).show();
////            }
////        });
//        return  imageView;
//    }
}
