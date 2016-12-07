package edu.sjsu.cmpe.partyon.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.entities.Post;
import edu.sjsu.cmpe.partyon.entities.User;

public class NewPostActivity extends CloseableActivity {
    private String mPostTxt;
    private EditText mPostTxtView;
    private ImageView mImageView;
    private Post post;
    private Bitmap bitmap;
    private ParseFile imageFile;
    private ProgressBar spinner;
    private static String TAG = "POST THINGY";

    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_new_post);
        //setupToolBar();

        initView();
    }

    private void initView(){
        mPostTxtView = (EditText)findViewById(R.id.post_content_txt);
        mImageView = (ImageView) findViewById(R.id.post_img_view);
        spinner = (ProgressBar) findViewById(R.id.progress_bar_post);
        spinner.setVisibility(View.GONE);

        Toast.makeText(this, "new Post View", Toast.LENGTH_LONG).show();
        //Get the passed on value from the camera and watermark(name is thumbImage)
        byte[] passedByte = getIntent().getByteArrayExtra("thumbImage");
        bitmap = BitmapFactory.decodeByteArray(passedByte, 0, passedByte.length);
        mImageView.setImageBitmap(bitmap);

    }
    private void savePost(){
        post = new Post();
        post.setTextContent(mPostTxtView.getText().toString());
        post.setAuthor((User)User.getCurrentUser());
        //Added Content Navdeep
        uploadImage();
        //End Added Content
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    e.printStackTrace();
                }else {

                    Intent intent = new Intent(NewPostActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_new_post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ProgressDialog progressDialog = ProgressDialog.show(NewPostActivity.this, "", "Please wait");
        progressDialog.show();
        int id = item.getItemId();
        if(id == R.id.action_postMenu){
            savePost();
        }
        return super.onOptionsItemSelected(item);
    }
    private void uploadImage(){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //compress the bitmap, save as PNG(lossless) 100 is compression for quality, 0 is compression for size
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        imageFile = new ParseFile("image.png", stream.toByteArray());

        //compressBitmap(imageFile);
        try {
            imageFile.save();
            post.put("Photo", imageFile);
            post.save();
            Toast.makeText(this, "File Uploaded", Toast.LENGTH_LONG).show();
            //String imageUrl = imageObject.getUrl();
            String objectId = post.getObjectId();
            Log.d(TAG, "Object ID is " + objectId);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    @Override
    int getToolBarID() {
        return R.id.new_post_toolbar;
    }

    @Override
    int getResourceID() {
        return R.layout.activity_new_post;
    }
}
