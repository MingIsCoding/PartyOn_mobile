package edu.sjsu.cmpe.partyon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.sjsu.cmpe.partyon.R;
import edu.sjsu.cmpe.partyon.activities.MyPostsActivity;
import edu.sjsu.cmpe.partyon.entities.Post;
import edu.sjsu.cmpe.partyon.holder.PostItemViewHolder;

/**
 * Created by navdeep on 12/6/2016.
 */

public class MyPostsAdapter extends RecyclerView.Adapter<PostItemViewHolder> {
    private Context mContext;
    private List<Post> mList;


    public MyPostsAdapter(Context context, List<Post> postList){
        this.mContext = context;
        this.mList = postList;
    }
    @Override
    public PostItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.item_post_list,parent,false);

        return new PostItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostItemViewHolder holder, int position) {
        Post post = mList.get(position);
        holder.bindPost(post,mContext,this);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
