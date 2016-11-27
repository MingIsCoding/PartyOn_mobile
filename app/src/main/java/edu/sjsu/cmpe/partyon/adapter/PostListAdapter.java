package edu.sjsu.cmpe.partyon.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.entities.Post;
import edu.sjsu.cmpe.partyon.holder.PostItemViewHolder;

/**
 * Created by Ming on 10/19/16.
 */

public class PostListAdapter extends RecyclerView.Adapter<PostItemViewHolder>{
    private static final String TAG = "PostListAdapter";
    private FragmentActivity mContent;
    private List<Post> postList;

    public PostListAdapter(FragmentActivity mContent, List<Post> postList) {
        this.mContent = mContent;
        this.postList = postList;
    }

    @Override
    public PostItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContent);
        View view = layoutInflater.inflate(R.layout.item_post_list,parent,false);
        return new PostItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostItemViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.bindPost(post,mContent, this);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
