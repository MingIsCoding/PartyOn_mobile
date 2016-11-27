package edu.sjsu.cmpe.partyon.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.adapter.imageTestAdapter;

import static android.R.attr.data;

public class NewPictureActivity extends AppCompatActivity {
    // Using permission crashes the app, right now using the default camera app, no permission added
    // only uses-feature is added, need to find a better way -nav
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final String TAG ="New Picture Activity";
    private Bitmap image;
    private Intent intent;
    private Uri imageName = null;
    private String imagePath;
    private ImageView clickedPhoto;
    private int IMAGE_MAX_SIZE = 200 * 200;
    private File file;
    private Bitmap bit;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this will call the intent first then jump to the setcontentview
        setContentView(R.layout.activity_new_picture);
        //setContentView(R.layout.activity_image_test);
           //String imageStorage = Environment.getExternalStorageState();
           if(!hasCamera()){
               //set something for disabling the camera(not implemented yet) - nav
           }else{
               /// to get a unique file name for the image
               String timeStamp= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
               String imageFileName= timeStamp+".jpg";
               //get the absolute path for the image
               File imageStorage= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
               imagePath = imageStorage.getAbsolutePath()+"/"+imageFileName;
               //Assign it to a file
               File imageFileBeforeCompress = new File(imagePath);

               //Long length = imageFileBeforeCompress.length();

              //Compress the file
               //compressBitmap(imageFileBeforeCompress);
               //Get the URI
               imageName = Uri.fromFile(imageFileBeforeCompress);
//               file = new File(imageName.getPath());
//               Long length = file.length();
//               Log.d(TAG, "The length of file is "+ length);
               // Launch the intent for camera
               intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               //For full resolution image using the PUT_EXTRA
               intent.putExtra(MediaStore.EXTRA_OUTPUT, imageName);
               startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

           }
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_IMAGE_CAPTURE&& resultCode==RESULT_OK){
            Toast.makeText(this,imagePath,Toast.LENGTH_LONG).show();
            try{
               // image = (Bitmap) data.getExtras().get("data");

                //Get the file from the Uri and pass for passing it to the compress function
                file = new File(imageName.getPath());
                //test for image size before and after
                Long length = file.length();
                Log.d(TAG, "The length of file is before compress is  "+ length);
                //call the compress function here
                bit = compressBitmap(file);
                int lengthBitmap = bit.getAllocationByteCount();
                Log.d(TAG, "The length of bitmap after compress is  "+ lengthBitmap);
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageName);
//
// compressBitmap(image);
                //Bitmap bitmap = decodeStream
                //Long length = image.
            }catch(IOException e){
                e.printStackTrace();
            }

            if(image!=null){
                //Set the image in the imageview on the activity

                clickedPhoto = (ImageView) findViewById(R.id.clickedPhoto);
                clickedPhoto.setImageBitmap(image);
                FloatingActionButton clickPicture =(FloatingActionButton) findViewById(R.id.uploadPictureButton);
                clickPicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //compressBitmap(image);
                        uploadImage();
                        Intent in = new Intent(NewPictureActivity.this, ImageTest.class);
                        startActivity(in);
                    }
                });

            }
        }


    }

    //check for the user's camera, returns true or false
    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    private void uploadImage(){
        //initialize
        ParseObject imageObject = null;
        ParseFile imageFile = null;
        imageObject = new ParseObject("User_Photos");
        imageObject.put("name", "image");

        //Check if the image has some value
        if(image==null || imageName==null){
            Log.d("Error", "Check for the code, image is empty");
        }

        //Convert the bitmap into bytes
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //compress the bitmap, save as PNG(lossless)
        bit.compress(Bitmap.CompressFormat.PNG, 100, stream);
        imageFile = new ParseFile("image.png", stream.toByteArray());

        //compressBitmap(imageFile);
        try {
            imageFile.save();
            imageObject.put("Photo", imageFile);
            imageObject.save();
            Toast.makeText(this, "File Uploaded", Toast.LENGTH_LONG).show();
            //String imageUrl = imageObject.getUrl();
            String objectId = imageObject.getObjectId();
            Log.d(TAG, "Object ID is " + objectId);
        }catch (ParseException e){
            e.printStackTrace();
        }

    }


//    //To compress(Pixels) it is actually increasing the file size
//    public Bitmap compressBitmap(File file){
//        Bitmap bitmap = null;
//        FileInputStream fileInputStream;
//        // decoding for the image size
//        BitmapFactory.Options option = new BitmapFactory.Options();
//        option.inJustDecodeBounds = true;
//
//
//        try{
//            fileInputStream = new FileInputStream(file);
//            BitmapFactory.decodeStream(fileInputStream, null, option);
//            Toast.makeText(this, "test compress" , Toast.LENGTH_LONG).show();
//           //FileInputStream.close();
//            try{
//                fileInputStream.close();
//            }catch(IOException e){
//                e.printStackTrace();
//            }
//        }catch(FileNotFoundException e){
//            e.printStackTrace();
//        }
//
//        int scale = 1;
//        if(option.outHeight> IMAGE_MAX_SIZE || option.outWidth > IMAGE_MAX_SIZE){
//            scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE)/(double) Math.max(option.outHeight,option.outWidth))/Math.log(0.5));
//        }
//        // Decoding with inSampleSize
//        BitmapFactory.Options option2 = new BitmapFactory.Options();
//        option2.inSampleSize = scale;
//
//        try{
//            fileInputStream = new FileInputStream(file);
//            bitmap = BitmapFactory.decodeStream(fileInputStream, null, option2);
//
//            try{
//                fileInputStream.close();
//            }catch(FileNotFoundException e){
//                e.printStackTrace();
//            }
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//
//        return bitmap;
//    }

    // to compress
    private Bitmap compressBitmap(File file){
        try{
            BitmapFactory.Options option_1 = new BitmapFactory.Options();
            option_1.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file),null,option_1);

            //The Size to scale to
            final int REQUIRED_SIZE = 220;

            //find the correct scale value, should be a power of 2
            int scale = 1;

            while(option_1.outWidth/scale/2>=REQUIRED_SIZE &&
                    option_1.outHeight/scale/2 >= REQUIRED_SIZE){
                scale *=2;
            }

            //Decoding with inSAmpleSize
            BitmapFactory.Options option_2 = new BitmapFactory.Options();
            option_2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(file),null, option_2);

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    public void testQuery(){
        ParseQuery<ParseObject> query= new ParseQuery<>("User_Photos");


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> images, ParseException e) {
                if(e==null){
                    //ImageView imageShow = (ImageView) findViewById(R.id.imageTestView);
//                    ListView list= (ListView) findViewById(R.id.imageTestList);
//                    imageTestAdapter adapter = new imageTestAdapter(NewPictureActivity.this, images);
//                    list.setAdapter(adapter);

                    Toast.makeText(NewPictureActivity.this, images.size()+"", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(NewPictureActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
//    public void testQuery(){
//        ParseQuery<ParseObject> query= new ParseQuery<>("User_Photos");
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> images, ParseException e) {
//                if(e==null){
//                    imageTestAdapter adapter = new imageTestAdapter(NewPictureActivity.this, images);
//
//                    //Toast.makeText(NewPictureActivity.this, images.size()+"", Toast.LENGTH_LONG).show();
//                }else{
//                    Toast.makeText(NewPictureActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }



}
