package edu.sjsu.cmpe.partyon.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.adapter.imageTestAdapter;

public class ImageTest extends Activity {

    private ParseFile testImage;
    private ParseObject testObject;
    private List<ParseObject> mImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_row);
        testQuery();
        ImageView mImagePreview = (ImageView) findViewById(R.id.imageTestView);
        ListView mList = (ListView) findViewById(R.id.singleList);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String message = "touched the item number "+position+" "+id;
                Toast.makeText(ImageTest.this, message, Toast.LENGTH_LONG).show();
            }
        });
        //ParseQuery<ParseObject> query = ParseQuery.getQuery("User_Photos");


//        ParseQuery query = ParseQuery.getQuery("User_Photos");
//        query.getInBackground(new GetCallback<ParseObject>(){
//            public void done(ParseObject object, ParseException e){
//
//            }
//        });
    }

    public void testQuery(){
        ParseQuery<ParseObject> query= new ParseQuery<>("User_Photos");


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> images, ParseException e) {
                if(e==null){
                    //ImageView imageShow = (ImageView) findViewById(R.id.imageTestView);
                   // ListView list= (ListView) findViewById(R.id.imageTestList);
                    ListView list = (ListView) findViewById(R.id.singleList);
                    imageTestAdapter adapter = new imageTestAdapter(ImageTest.this, images);
                    //setListAdapter(adapter);
                    list.setAdapter(adapter);

                    //Toast.makeText(NewPictureActivity.this, images.size()+"", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ImageTest.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
