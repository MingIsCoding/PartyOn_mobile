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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

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
        //setContentView(R.layout.activity_new_picture);
        //trademarkQuery();
        //setContentView(R.layout.activity_image_test);
           //String imageStorage = Environment.getExternalStorageState();
           if(!hasCamera()){
               //set something for disabling the camera(not implemented yet) - Navdeep
           }else{
               /// to get a unique file name for the image
               String timeStamp= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
               String imageFileName= timeStamp+".jpg";
               //get the absolute path for the image
               File imageStorage= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
               imagePath = imageStorage.getAbsolutePath()+"/"+imageFileName;
               //Assign it to a file
               File imageFileBeforeCompress = new File(imagePath);

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
        setContentView(R.layout.activity_new_picture);
        super.onActivityResult(requestCode, resultCode, data);
        trademarkQuery();

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

                       }catch(IOException e){
                e.printStackTrace();
            }

            if(image!=null){
                //Set the image in the imageview on the activity

                clickedPhoto = (ImageView) findViewById(R.id.clickedPhoto);
                clickedPhoto.setImageBitmap(image);
//                FloatingActionButton clickPicture =(FloatingActionButton) findViewById(R.id.uploadPictureButton);
//                clickPicture.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //compressBitmap(image);
//                        uploadImage();
//                        Intent in = new Intent(NewPictureActivity.this, ImageTest.class);
//                        startActivity(in);
//                    }
//                });

            }
        }


    }

    //check for the user's camera, returns true or false
    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

//    private void uploadImage(){
//        //initialize
//        ParseObject imageObject = null;
//        ParseFile imageFile = null;
//        imageObject = new ParseObject("User_Photos");
//        imageObject.put("name", "image");
//
//        //Check if the image has some value
//        if(image==null || imageName==null){
//            Log.d("Error", "Check for the code, image is empty");
//        }
//
//        //Convert the bitmap into bytes
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        //compress the bitmap, save as PNG(lossless) 100 is compression for quality, 0 is compression for size
//        bit.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        imageFile = new ParseFile("image.png", stream.toByteArray());
//
//        //compressBitmap(imageFile);
//        try {
//            imageFile.save();
//            imageObject.put("Photo", imageFile);
//            imageObject.save();
//            Toast.makeText(this, "File Uploaded", Toast.LENGTH_LONG).show();
//            //String imageUrl = imageObject.getUrl();
//            String objectId = imageObject.getObjectId();
//            Log.d(TAG, "Object ID is " + objectId);
//        }catch (ParseException e){
//            e.printStackTrace();
//        }
//
//    }


    // to compress
    private Bitmap compressBitmap(File file){
        try{
            BitmapFactory.Options option_1 = new BitmapFactory.Options();
            option_1.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file),null,option_1);

            //The Size to scale to
            final int REQUIRED_SIZE = 270;

            //find the correct scale value, should be a power of 2
            int scale = 1;

            while(option_1.outWidth/scale/2>=REQUIRED_SIZE &&
                    option_1.outHeight/scale/2 >= REQUIRED_SIZE){
                scale *=2;
            }

            //Second method
            //        int scale = 1;
//        if(option.outHeight> IMAGE_MAX_SIZE || option.outWidth > IMAGE_MAX_SIZE){
//            scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE)/(double) Math.max(option.outHeight,option.outWidth))/Math.log(0.5));
//        }

            //Decoding with inSAmpleSize
            BitmapFactory.Options option_2 = new BitmapFactory.Options();
            option_2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(file),null, option_2);

        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }
    // To display the horizontal scroll view on bottom of preview for trademarks
    public void trademarkQuery(){
        ParseQuery<ParseObject> query= new ParseQuery<>("Trademark");


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> images, ParseException e) {
                if(e==null){
                    LinearLayout imageGallery = (LinearLayout) findViewById(R.id.trademarkLinearLayout_1);

                    for(ParseObject object:images){
                        //ParseFile file = new ParseFile();
                        ParseFile file = object.getParseFile("image");
                        String path= file.getUrl();
                        //String newPath = path.toString();
                        Log.d(TAG,path);
                        //setTheImage(path);
                        ImageView imageView = new ImageView(getApplicationContext());
                        // Set the layout params (width and height, in that order)
                        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(300, 350);
                        // set margings, left top right bottom
                        lParams.setMargins(0,0,5,0);
                        imageView.setLayoutParams(lParams);
                        //Add the view before adding picasso, doesn't show otherwise
                        imageGallery.addView(imageView);
                        Picasso.with(NewPictureActivity.this).load(path).into(imageView, new com.squareup.picasso.Callback() {
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_picture, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId=item.getItemId();//= R.id.action_uploadPictureMenu;
        if(itemId ==R.id.action_uploadPictureMenu){
            Log.d(TAG, "the item ID is "+itemId);
            bit = compressBitmap(file);
            int lengthBitmap = bit.getAllocationByteCount();
            //Convert the bitmap into bytes
            Toast.makeText(this, ""+lengthBitmap, Toast.LENGTH_LONG).show();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //compress the bitmap, save as PNG(lossless) 100 is compression for quality, 0 is compression for size
            bit.compress(Bitmap.CompressFormat.PNG, 100, stream);   //stream is passed to the bit for compress algorithm
            byte[] byteArrayToPass = stream.toByteArray();

            Intent intent = new Intent(this, NewPostActivity.class);
            intent.putExtra("thumbImage", byteArrayToPass);
            startActivity(intent);
//        uploadImage();
//        Intent in = new Intent(NewPictureActivity.this, ImageTest.class);
//        startActivity(in);
        }


        return super.onOptionsItemSelected(item);
    }
}
