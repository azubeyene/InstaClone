package com.example.instaclone;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private Context context;
    private List<Comment>posts;

    public CommentsAdapter(Context context, List<Comment> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comments, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = posts.get(position);
        holder.bind(comment);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivMyCommentPic;
        private TextView tvCommentsContent;
        private TextView tvCommentsRelTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMyCommentPic = itemView.findViewById(R.id.ivMyCommentPic);
            tvCommentsContent = itemView.findViewById(R.id.tvCommentsContent);
            tvCommentsRelTime = itemView.findViewById(R.id.tvCommentsRelTime);
        }

        public void bind(Comment comment) {
            String username = comment.getCommenter().getUsername();
            String sourceString = "<b>" + username + "</b> " + comment.getMessage();
            tvCommentsContent.setText(Html.fromHtml(sourceString));
            //post.getCreatedAt.getTime
            String relativeDate = DateUtils.getRelativeTimeSpanString(comment.getCreatedAt().getTime(),
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            tvCommentsRelTime.setText(relativeDate);

            if (comment.getCommenter().getParseFile("profilePhoto")!=null){
                //Set this pic as profile pic on main comment
                Glide.with(context).load(comment.getCommenter().getParseFile("profilePhoto").getUrl()).into(ivMyCommentPic);
            } else {
                ivMyCommentPic.setImageResource(R.drawable.ic_person_black_24dp);
            }

        }
    }
}
