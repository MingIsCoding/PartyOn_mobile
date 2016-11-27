package edu.sjsu.cmpe.partyon.holder;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.activities.NewPartyActivity;
import edu.sjsu.cmpe.partyon.activities.PostDetailActivity;
import edu.sjsu.cmpe.partyon.config.AppData;
import edu.sjsu.cmpe.partyon.entities.Like;
import edu.sjsu.cmpe.partyon.entities.Post;
import edu.sjsu.cmpe.partyon.entities.Reply;
import edu.sjsu.cmpe.partyon.entities.User;

/**
 * Created by Ming on 10/19/16.
 */

public class PostItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private final static String TAG = "PostItemViewHolder";
    private Post mPost;
    private ImageView mAuthorProfilePic;
    private TextView mAuthorNameView;
    private TextView mContentTxtView;
    private EditText mCommentEditText;
    private TextView mLikeView;
    private ImageButton mCommentBtn;
    private ImageButton mLikeBtn;
    private Button mFollowBtn;
    private Context mContext;
    private LinearLayout mCommentLayout;
    private RecyclerView.Adapter mAdapter;
    private boolean isLiked = false;
    private final static int LIKE_NAMES_LIMIT = 3;
    private boolean isFollowed =false;

    public PostItemViewHolder(View v) {
        super(v);
        mAuthorNameView = (TextView)v.findViewById(R.id.author_username_view);
        mContentTxtView = (TextView)v.findViewById(R.id.content_txt_view);
        mCommentEditText = (EditText)v.findViewById(R.id.post_list_add_comment_field);
        mCommentBtn = (ImageButton)v.findViewById(R.id.post_comment_add_btn);
        mCommentLayout = (LinearLayout)v.findViewById(R.id.post_comment_list_layout);
        mLikeBtn = (ImageButton)v.findViewById(R.id.post_like_btn);
        mLikeView = (TextView) v.findViewById(R.id.post_like_txtView);
        mFollowBtn = (Button)v.findViewById(R.id.follow_btn);
        mAuthorProfilePic = (ImageView)v.findViewById(R.id.author_profile_pic);
        //mCommentEditText.setOnClickListener(this);
        mCommentBtn.setOnClickListener(this);
        mLikeBtn.setOnClickListener(this);
        mFollowBtn.setOnClickListener(this);
    }
    public void bindPost(Post post, Context cont, RecyclerView.Adapter adapter){
        this.mPost = post;
        this.mContext = cont;
        this.mAdapter = adapter;
        try {
            mAuthorNameView.setText(
                    post.getAuthor().fetchIfNeeded().getUsername());//post.getAuthor().getUsername()
            mContentTxtView.setText(post.getTextContent());
        }catch (ParseException e){
            e.printStackTrace();
        }
        Picasso.with(mContext).load(post.getAuthor().getProfilePicSmall()).into(mAuthorProfilePic);
        //follow
        try {
            if(!AppData.getUser().getObjectId().equals(mPost.getAuthor().fetchIfNeeded().getObjectId())
                    && AppData.getUser().getFollows() != null){
                for(int i = 0; !isFollowed && i < AppData.getUser().getFollows().size(); i++){
                    //try {
                        if(((ParseObject)ParseUser.getCurrentUser().getList("follows").get(i)
                        ).getObjectId().equals(mPost.getAuthor().getObjectId())){
                            isFollowed = true;
                        }
                    /*} catch (ParseException e) {
                        e.printStackTrace();
                    }*/
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(AppData.getUser().getObjectId().equals(mPost.getAuthor().getObjectId())){
            isFollowed = true;
        }
        if(!isFollowed){
            mFollowBtn.setVisibility(View.VISIBLE);
        }else{
            mFollowBtn.setVisibility(View.INVISIBLE);
        }

        List<Like> likes = mPost.getLikes();
        if(likes != null){
            /*ParseQuery<Like> likeQuery = ParseQuery.getQuery(AppData.OBJ_NAME_LIKE);
            likeQuery.whereEqualTo("author",
                    ParseObject.createWithoutData(AppData.getUser().getObjectId()));
            //if it is liked by current user
            List<ParseObject> likesAsPO = mPost.getLikesAsPObject();
            for(ParseObject p : likesAsPO){
                System.out.println("like id");
                if(p.getObjectId().equals(AppData.getUser().getObjectId())){
                    System.out.println("liked before");
                }
            }*/
            //organize names
            Log.d(TAG,"content:" + mPost.getTextContent() + ", likes:" + likes.size());
            StringBuilder namesSb = new StringBuilder();
            for(int i = 0; i< LIKE_NAMES_LIMIT && i<likes.size(); i++){
                try {
                    Like l = likes.get(i).fetchIfNeeded();
                    Log.d(TAG,"like author id:"+l.getSender().getObjectId());
                    if(!isLiked && l.getSender().getObjectId().equals(AppData.getUser().getObjectId())){
                        isLiked = true;
                    }
                    if(i<LIKE_NAMES_LIMIT){
                        namesSb.append(l.getSender().fetchIfNeeded().getUsername()+", ");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            String names = namesSb.substring(0,namesSb.length()-2)+" ";
            if(likes.size() > LIKE_NAMES_LIMIT){
                names += "...";
            }
            mLikeView.setText(mContext.getString(R.string.post_like_format,
                    likes.size(),names));
            if(isLiked){
                mLikeBtn.setBackground(mContext.getDrawable(R.drawable.ic_favorite_black_24dp));
            }
        }
        List<Reply> replies = mPost.getReplies();
        if(replies != null) {
            mCommentLayout.removeAllViewsInLayout();
            Log.d(TAG,"replies size:"+replies.size());
            for (int i = 0; i < replies.size(); i++) {
                TextView rv = new TextView(cont);
                //r.getAuthor().fetchIfNeeded().getUsername()+":"+
                try {
                    rv.setText(Html.fromHtml("<b>"+((Reply) replies.get(i).fetchIfNeeded()).
                            getAuthor().fetchIfNeeded().getUsername()+": </b>")+
                            ((Reply) replies.get(i).fetchIfNeeded()).getContent());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d(TAG,"content:"+mPost.getTextContent());
                mCommentLayout.addView(rv,mCommentLayout.getChildCount());
            }
            //mCommentLayout.invalidate();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.post_like_btn && !isLiked){
            /*if(mPost.getLikes() != null){
                mLikeView.setText(mContext.getString(R.string.post_like_format,AppData.getUser().getUsername()+","+
                        mLikeView.getText().toString());
            }*/
            final Like like = new Like();
            like.setPost(mPost);
            try {
                like.setReceiver((User)mPost.getAuthor().fetchIfNeeded());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            like.setSender(AppData.getUser());
            like.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    mPost.addLike(like);
                    mPost.saveInBackground();
                    mAdapter.notifyDataSetChanged();
                }
            });
            mLikeBtn.setBackground(mContext.getDrawable(R.drawable.ic_favorite_black_24dp));
        }else if(v.getId() == R.id.post_comment_add_btn){
            String content = mCommentEditText.getText().toString();
            if(content != null && !content.equals("")){
                final Reply r = new Reply();
                r.setAuthor(AppData.getUser());
                r.setContent(content);
                mPost.addReply(r);
                r.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        mPost.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                mAdapter.notifyDataSetChanged();
                                mCommentEditText.setText("");
                                Toast.makeText(mContext,
                                        "commented successfully.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                //mCommentLayout.();
            }
        }else if(v.getId() == R.id.follow_btn){
            AppData.getUser().addFollow(mPost.getAuthor());
            AppData.getUser().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Toast.makeText(mContext,
                            "Start to follow "+ mPost.getAuthor().getUsername(), Toast.LENGTH_SHORT).show();
                    mFollowBtn.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}
