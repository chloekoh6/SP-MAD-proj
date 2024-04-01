package com.sp.android_studio_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.viewHolder> {
    Context context;
    List<FriendsModel> friendsModels;

    public FriendsAdapter(Context context, List<FriendsModel> friendsModels){
        this.context = context;
        this.friendsModels = friendsModels;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.row_friends, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.ranking.setText(String.valueOf(position + 1));
        holder.user.setText(friendsModels.get(position).getUsername());
        holder.points.setText(Integer.toString(friendsModels.get(position).getPoints()));
        Picasso.with(context).load(friendsModels.get(position).getImage()).fit().centerInside().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return friendsModels.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView user;
        TextView points;
        TextView ranking;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.user_image);
            user = itemView.findViewById(R.id.username);
            points = itemView.findViewById(R.id.user_points);
            ranking = itemView.findViewById(R.id.rank);
        }
    }
}

