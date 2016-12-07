package edu.sjsu.cmpe.partyon.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.adapter.MyPostsAdapter;
import edu.sjsu.cmpe.partyon.entities.Post;
import edu.sjsu.cmpe.partyon.entities.User;

public class MyPostsActivity extends CloseableActivity {
    private List<Post> postList;
    private MyPostsAdapter mMyAdapter;

    @Override
    int getToolBarID() {
        return R.id.posts_toolbar;
    }

    @Override
    int getResourceID() {
        return R.layout.activity_my_posts;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postList = new ArrayList<>();
        RecyclerView mMyPostsRecyclerView =(RecyclerView) findViewById(R.id.my_posts_recyclerView);
        mMyPostsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMyAdapter = new MyPostsAdapter(this, postList);
        mMyPostsRecyclerView.setAdapter(mMyAdapter);
        fetchData();

        //Because content view is already set by the closeable activity
        //setContentView(R.layout.activity_my_posts);
    }

    private void fetchData(){

        ParseQuery<Post> query = new ParseQuery<Post>("Post");
        //User user = new User();
        Post post = new Post();
        //final User author = post.getAuthor();
        final String user = User.getCurrentUser().getObjectId();

        query.orderByDescending("createdAt");
        //query.whereEqualTo("author", User.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
//                Log.d(TAG,"post got:"+posts.size());
                if(e == null){
                    for(Post p : posts){
                        ParseObject author = p.getParseObject("author");
                        String intendedAuthor = author.getObjectId();
                        Log.d("Before comparison====="+User.getCurrentUser().getObjectId(),"====>"+p.getAuthor());
//                       if (p.getAuthor().equals(User.getCurrentUser().getObjectId())
                        if(intendedAuthor.equals(user)){
                            Log.d("After comparison====="+user,"====>"+intendedAuthor);
                            postList.add(p);
                        }
                    }
                    mMyAdapter.notifyDataSetChanged();
                }else{
                    e.printStackTrace();
                    //Problem detected
                }
            }
        });
    }
}
