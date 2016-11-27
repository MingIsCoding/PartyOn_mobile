package edu.sjsu.cmpe.partyon.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.entities.Post;

public class PostDetailActivity extends CloseableActivity {
    private final static String TAG = "PostDetailActivity";
    private Post mPost;
    private int mPostID;
    private Bundle mBundle;
//    private EditText
    @Override
    int getToolBarID() {
        return R.id.post_detail_toolbar;
    }

    @Override
    int getResourceID() {
        return R.layout.activity_post_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
        String postid = mBundle.getString(Post.POST_ID);
        Log.d(TAG,postid);
    }
}
