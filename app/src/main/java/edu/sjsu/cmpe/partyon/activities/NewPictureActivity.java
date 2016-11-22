package edu.sjsu.cmpe.partyon.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.sjsu.cmpe.partyon.R;

public class NewPictureActivity extends AppCompatActivity {
    // Using permission crashes the app, right now using the default camera app, no permission added
    // only uses-feature is added, need to find a better way -nav
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String TAG ="New Picture Activity";
    ImageView photoClicked;
    private URI mImageUri;
    File image;
    private String imagePath="";
    private String imageFileName="";
    private File mImage;
    private  Bitmap photo;
    private Uri outputImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this will call the intent first then jump to the setcontentview
        setContentView(R.layout.activity_new_picture);

        FloatingActionButton clickPicture =(FloatingActionButton) findViewById(R.id.clickPictureButton);
        //photoClicked = (ImageView) findViewById(R.id.photoClicked);

        //check if the user has a camera, if not, then disable button

        if(!hasCamera()){
            clickPicture.setEnabled(false);
        }else{
            // to make a unique file name using the timestamp
            //make sure to allow permissions for storage, changed since version 6.0
            String timeStamp= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            imageFileName= timeStamp+".jpg";
            File imageStorage= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            imagePath = imageStorage.getAbsolutePath()+"/"+imageFileName;
            File image = new File(imagePath);
            // using the Uri to get the full sized image(put it in PutExtra)
            outputImageUri = Uri.fromFile(image);


           Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputImageUri);
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

        // to upload the picture to parse server
        ParseObject mPhoto= null;
        ParseFile mFile = null;



        mPhoto = new ParseObject("User_Photos");

        //checking if the bitmap has some values
        //mImage = new File(imagePath);
        // need to get the decoded file for bitmap compress to work
       // Bitmap parsePhoto = BitmapFactory.decodeFile(mImage.getAbsolutePath());

        if(photo == null || outputImageUri == null){
            Toast.makeText(this, "Problem with picture",Toast.LENGTH_LONG).show();
            return;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //PNG is lossless, save as that, 100 is highest quality
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        //byte[] upload= stream.toByteArray();
        mFile = new ParseFile("something.png", stream.toByteArray());

        mFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(NewPictureActivity.this, "photo uploaded", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(NewPictureActivity.this, "Error Uploading", Toast.LENGTH_LONG).show();
                }
            }
        });

        try {
            mPhoto.put("Photo", mFile);
            mPhoto.save();
        }catch (ParseException e){
            e.printStackTrace();
        }


    }
    // To return the image captured, do whatever with it
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check for errors
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode== RESULT_OK){
//            // Get the photo(data)
//            Bundle extras = data.getExtras();
//            //get the photo info, convert into bitmap, save in photos
//            Bitmap photo =(Bitmap) extras.get("data");
//            //set the bitmap in the imageview
//            photoClicked.setImageBitmap(photo);
            Toast.makeText(this, imageFileName,Toast.LENGTH_LONG).show();

            mImage = new File(imagePath);

            if(mImage.exists()){
                photo= BitmapFactory.decodeFile(mImage.getAbsolutePath());
                photoClicked = (ImageView) findViewById(R.id.photoClicked);
                photoClicked.setImageBitmap(photo);

               // photoClicked.setImageURI(outputImageUri);
            }
            //check if the image folder exists
//            File imageStorage = new File(Environment.getExternalStorageDirectory()+File.separator+"imagesFolder");
//            if(!imageStorage.exists()){
//                imageStorage.mkdirs();
//                Log.d(TAG, "Folder created at "+ imageStorage.toString());
//            }

        }
            }
}
