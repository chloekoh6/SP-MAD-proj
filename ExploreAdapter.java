package com.sp.android_studio_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.MyViewHolder> {

    private final Explore_RecyclerViewInterface exploreRecyclerViewInterface;

    Context context;
    ArrayList<ExploreModel> exploreModels;

    public ExploreAdapter(Context context, ArrayList<ExploreModel> exploreModels,
                          Explore_RecyclerViewInterface exploreRecyclerViewInterface) {
        this.context = context;
        this.exploreModels = exploreModels;
        this.exploreRecyclerViewInterface = exploreRecyclerViewInterface;
    }

    @NonNull
    @Override
    public ExploreAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_explore_cell, parent, false);
        return new ExploreAdapter.MyViewHolder(view, exploreRecyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreAdapter.MyViewHolder holder, int position1) {
        int position = position1;
        holder.setIsRecyclable(false);

        ExploreModel currentItem = exploreModels.get(position);

        holder.tvTitle.setText(currentItem.getExploreTitle());
        holder.tvDesc.setText(currentItem.getExploreDesc());
        holder.imageView.setImageResource(currentItem.getExploreImage());
    }

    @Override
    public int getItemCount() {
        return exploreModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTitle, tvDesc;

        public MyViewHolder(@NonNull View itemView, Explore_RecyclerViewInterface exploreRecyclerViewInterface) {
            super(itemView);

            imageView = itemView.findViewById(R.id.exploreImage);
            tvTitle = itemView.findViewById(R.id.exploreTitle);
            tvDesc = itemView.findViewById(R.id.exploreDesc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(exploreRecyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if(pos!=RecyclerView.NO_POSITION) {
                            exploreRecyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }

    public void filterList(ArrayList<ExploreModel> filteredList) {
        exploreModels = filteredList;
        notifyDataSetChanged();
    }

}
