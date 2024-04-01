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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CampaignsFragment extends Fragment {

    ArrayList<CampaignData> list;
    RecyclerView campaign_rv;
    CampaignAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_campaigns, container, false);

        campaign_rv = view.findViewById(R.id.orgs);
        adapter = new CampaignAdapter(this.getContext(), list);
        campaign_rv.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        campaign_rv.setLayoutManager(layoutManager);
        getData();

        return view;
    }

    private void getData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("campaigns");
        collectionReference.orderBy("num", Query.Direction.ASCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CampaignData campaign = document.toObject(CampaignData.class);
                                list.add(campaign);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("Firebase", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.CampaignAdapterViewHolder> {

        private Context context;
        private ArrayList<CampaignData> arrayListCampaignItem;

        public CampaignAdapter(Context context, ArrayList<CampaignData> arrayListCampaignItem) {
            this.context = context;
            this.arrayListCampaignItem = arrayListCampaignItem;
        }

        @NonNull
        @Override
        public CampaignAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.campaign_layout, parent, false);
            return new CampaignAdapterViewHolder(view, context, arrayListCampaignItem );
        }

        @Override
        public void onBindViewHolder(@NonNull CampaignAdapter.CampaignAdapterViewHolder holder, int position) {
            CampaignData campaignItem = arrayListCampaignItem.get(position);
            String image = campaignItem.getImage();
            String name = campaignItem.getName();
            String desc = campaignItem.getDesc();
            String url = campaignItem.getUrl();
            String num = "0" + campaignItem.getNum();


            holder.name.setText(name);
            holder.desc.setText(desc);
            holder.num.setText(num);
            Picasso.with(context).load(image).fit().centerInside().into(holder.icon);

        }

        @Override
        public int getItemCount() {
            return arrayListCampaignItem.size();
        }

        public class CampaignAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView icon;
            private TextView name;
            private TextView desc;
            private TextView num;
            Context context;
            ArrayList<CampaignData> data;

            public CampaignAdapterViewHolder(@NonNull View itemView, Context context, ArrayList<CampaignData> data) {
                super(itemView);
                icon = itemView.findViewById(R.id.icon);
                name = itemView.findViewById(R.id.name);
                desc = itemView.findViewById(R.id.desc);
                num = itemView.findViewById(R.id.num);
                itemView.setOnClickListener(this);
                this.context = context;
                this.data = data;
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CampaignWebviewActivity.class);
                intent.putExtra("URL_NAME", data.get(getAdapterPosition()).getUrl());
                context.startActivity(intent);
            }
        }
    }
}