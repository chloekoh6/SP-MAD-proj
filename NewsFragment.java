package com.sp.android_studio_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsFragment extends Fragment {
    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    ArrayList<NewsModel> newsModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        recyclerView = view.findViewById(R.id.news_view);
        newsModel = new ArrayList<>();
        newsAdapter = new NewsAdapter(getContext(),newsModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(newsAdapter);
        getModels();
        return view;
    }

    private void getModels(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("news");
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NewsModel news = document.toObject(NewsModel.class);
                                newsModel.add(news);
                            }
                            newsAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("Firebase", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.viewHolder> {
        Context context;
        ArrayList<NewsModel> newsModel;

        public NewsAdapter (Context context, ArrayList<NewsModel> newsModel){
            this.context = context;
            this.newsModel = newsModel;
        }

        @NonNull
        @Override
        public NewsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.row_news, parent, false);
            return new NewsAdapter.viewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NewsAdapter.viewHolder holder, int position) {
            holder.title.setText(newsModel.get(position).getTitle());
            holder.url.setText(newsModel.get(position).getUrl());
            Picasso.with(context).load((newsModel.get(position)).getImage()).fit().centerInside().into(holder.imageView);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    Intent intent = new Intent(context, NewsWebActivity.class);
                    intent.putExtra("URL_NAME", newsModel.get(position).getUrl());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return newsModel.size();
        }

        public class viewHolder extends RecyclerView.ViewHolder{

            ImageView imageView;
            TextView title;
            TextView url;
            CardView cardView;

            public viewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.news_image);
                title = itemView.findViewById(R.id.news_title);
                url = itemView.findViewById(R.id.news_url);
                cardView = itemView.findViewById(R.id.cardView);
            }
        }
    }
}