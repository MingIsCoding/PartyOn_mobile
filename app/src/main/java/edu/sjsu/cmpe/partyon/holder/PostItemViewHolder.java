package edu.sjsu.cmpe.partyon.holder;

import android.content.Context;
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

import com.mikhaellopez.circularimageview.CircularImageView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.config.App;
import edu.sjsu.cmpe.partyon.entities.Like;
import edu.sjsu.cmpe.partyon.entities.Post;
import edu.sjsu.cmpe.partyon.entities.Reply;
import edu.sjsu.cmpe.partyon.entities.User;
import edu.sjsu.cmpe.partyon.utilities.BadgeTool;

/**
 * Created by Ming on 10/19/16.
 */

public class PostItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private final static String TAG = "PostItemViewHolder";
    private Post mPost;
    private ParseFile file;
    private CircularImageView mAuthorProfilePic;
    private ImageView mPostImageView;
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
        mAuthorProfilePic = (CircularImageView)v.findViewById(R.id.author_profile_pic);
        mPostImageView = (ImageView) v.findViewById(R.id.post_image_view);

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
            showPostImage();
//            file = post.getPostPhotos();
//            String Url = file.getUrl();
//            Picasso.with(mContext).load(Url).into(mPostImageView);
        }catch (ParseException e){
            e.printStackTrace();
        }
        mAuthorProfilePic.setBorderColor(BadgeTool.getLevelColor(App.getUser().getPoints()));
        Picasso.with(mContext).load(post.getAuthor().getProfilePicSmall()).into(mAuthorProfilePic);
        //follow
        try {
            if(!App.getUser().getObjectId().equals(mPost.getAuthor().fetchIfNeeded().getObjectId())
                    && App.getUser().getFollows() != null){
                for(int i = 0; !isFollowed && i < App.getUser().getFollows().size(); i++){
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
        if(App.getUser().getObjectId().equals(mPost.getAuthor().getObjectId())){
            isFollowed = true;
        }
        if(!isFollowed){
            mFollowBtn.setVisibility(View.VISIBLE);
        }else{
            mFollowBtn.setVisibility(View.INVISIBLE);
        }

        List<Like> likes = mPost.getLikes();
        if(likes != null){
            /*ParseQuery<Like> likeQuery = ParseQuery.getQuery(App.OBJ_NAME_LIKE);
            likeQuery.whereEqualTo("author",
                    ParseObject.createWithoutData(App.getUser().getObjectId()));
            //if it is liked by current user
            List<ParseObject> likesAsPO = mPost.getLikesAsPObject();
            for(ParseObject p : likesAsPO){
                System.out.println("like id");
                if(p.getObjectId().equals(App.getUser().getObjectId())){
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
                    if(!isLiked && l.getSender().getObjectId().equals(App.getUser().getObjectId())){
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
                mLikeView.setText(mContext.getString(R.string.post_like_format,App.getUser().getUsername()+","+
                        mLikeView.getText().toString());
            }*/
            final Like like = new Like();
            like.setPost(mPost);
            try {
                like.setReceiver((User)mPost.getAuthor().fetchIfNeeded());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            like.setSender(App.getUser());
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
                r.setAuthor(App.getUser());
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
            App.getUser().addFollow(mPost.getAuthor());
            App.getUser().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Toast.makeText(mContext,
                            "Start to follow "+ mPost.getAuthor().getUsername(), Toast.LENGTH_SHORT).show();
                    mFollowBtn.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
    private void showPostImage(){
            //String object = mPost.getObjectId();
            //Toast.makeText(mContext, "ObjectID"+object, Toast.LENGTH_LONG).show();
            file = (ParseFile) mPost.getParseFile("Photo");
            int targetWidth = mPostImageView.getWidth();
            int targetHeight = mPostImageView.getHeight();
            Log.d(TAG,"Width "+targetWidth+" Height "+targetHeight);

            String Url = file.getUrl();
            //Set the resize width to something bigger than screen size and
            //put the height to 0 to maintain the aspect ratio.
            Picasso.with(mContext).load(Url).resize(800,0).into(mPostImageView);
            //Next Line does almost the same thing.
            //Picasso.with(mContext).load(Url).resize(600,600).centerInside().into(mPostImageView);

//        ParseQuery<Post> query= new ParseQuery<>("Post");
//        query.findInBackground(new FindCallback<Post>() {
//            @Override
//            public void done(List<Post> image, ParseException e) {
//                if(e==null){
//                    file = mPost.getPostPhotos();
//                    String Url = file.getUrl();
//                    Picasso.with(mContext).load(Url).into(mPostImageView);
//
//                }else{
//                    Toast.makeText(mContext, "error retrieving Image", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }


}
