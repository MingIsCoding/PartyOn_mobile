package edu.sjsu.cmpe.partyon.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.entities.Post;
import edu.sjsu.cmpe.partyon.entities.User;

/**
 * Created by Ming on 10/19/16.
 */

public class PostItemViewHolder extends RecyclerView.ViewHolder {
    private Post mPost;
    private TextView mAuthorNameView;
    private TextView mContentTxtView;

    public PostItemViewHolder(View v) {
        super(v);
        mAuthorNameView = (TextView)v.findViewById(R.id.author_username_view);
        mContentTxtView = (TextView)v.findViewById(R.id.content_txt_view);
    }
    public void bindPost(Post post){
        this.mPost = post;
        mAuthorNameView.setText(post.getAuthor().getUsername());
        mContentTxtView.setText(post.getTextContent());
    }
}
