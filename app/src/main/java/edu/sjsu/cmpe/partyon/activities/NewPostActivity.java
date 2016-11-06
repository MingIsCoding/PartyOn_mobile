package edu.sjsu.cmpe.partyon.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.entities.Post;
import edu.sjsu.cmpe.partyon.entities.User;

public class NewPostActivity extends CloseableActivity {
    private String mPostTxt;
    private EditText mPostTxtView;

    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_new_post);
        //setupToolBar();
        initView();
    }

    private void initView(){
        mPostTxtView = (EditText)findViewById(R.id.post_content_txt);
    }
    private void savePost(){
        Post post = new Post();
        post.setTextContent(mPostTxtView.getText().toString());
        post.setAuthor((User)User.getCurrentUser());
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    e.printStackTrace();
                }else {
                    Log.d("NewPost","saved");
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
        int id = item.getItemId();
        if(id == R.id.action_postMenu){
            savePost();
        }
        return super.onOptionsItemSelected(item);
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
