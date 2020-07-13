package com.example.instaclone;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instaclone.fragments.ProfileFragment;
import com.parse.ParseUser;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private Context context;
    private List<Comment> comments;
    private Post post;

    public CommentsAdapter(Context context, List<Comment> posts, Post post) {
        this.context = context;
        this.comments = posts;
        this.post = post;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comments, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);

    }

    @Override
    public int getItemCount() {
        return comments.size();
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

        public void bind(final Comment comment) {
            String username = comment.getCommenter().getUsername();
            String sourceString = "<b>" + username + "</b> " + comment.getMessage();
            tvCommentsContent.setText(Html.fromHtml(sourceString));
            tvCommentsContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment newProfFrag = new ProfileFragment(comment.getCommenter());
                    ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, newProfFrag).addToBackStack(null).commit();
                }
            });
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
