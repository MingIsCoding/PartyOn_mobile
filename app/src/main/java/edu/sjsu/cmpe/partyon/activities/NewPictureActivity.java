package edu.sjsu.cmpe.partyon.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import edu.sjsu.cmpe.partyon.R;

public class NewPictureActivity extends AppCompatActivity {
    // Using permission crashes the app, right now using the default camera app, no permission added
    // only uses-feature is added, need to find a better way -nav
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String TAG ="New Picture Activity";
    ImageView photoClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this will call the intent first then jump to the setcontentview
        setContentView(R.layout.activity_new_picture);

        FloatingActionButton clickPicture =(FloatingActionButton) findViewById(R.id.clickPictureButton);
        photoClicked = (ImageView) findViewById(R.id.photoClicked);

        //check if the user has a camera, if not, then disable button

        if(!hasCamera()){
            clickPicture.setEnabled(false);
        }else{
           Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //Take picture and pass results(the image) to onActivityResult
            //Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //check for the user's camera, returns true or false
    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    // Method for launching the Camera
    public void launchCamera(View view){
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        //Take picture and pass results(the image) to onActivityResult
//        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        Toast.makeText(this, "This button works",Toast.LENGTH_LONG).show();
    }
    // To return the image captured, do whatever with it
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for errors
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode== RESULT_OK){
            // Get the photo(data)
            Bundle extras = data.getExtras();
            //get the photo info, convert into bitmap, save in photos
            Bitmap photo =(Bitmap) extras.get("data");
            //set the bitmap in the imageview
            photoClicked.setImageBitmap(photo);
            //check if the image folder exists
//            File imageStorage = new File(Environment.getExternalStorageDirectory()+File.separator+"imagesFolder");
//            if(!imageStorage.exists()){
//                imageStorage.mkdirs();
//                Log.d(TAG, "Folder created at "+ imageStorage.toString());
//            }

        }
            }
}
