package com.sp.android_studio_project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ShopFragment extends Fragment {

    ArrayList<ShopData> reusableitems;
    ArrayList<ShopData> vouchers;
    ShopAdapter adapter;
    ShopAdapter adapter2;
    RecyclerView Itemsrv;
    RecyclerView Voucherrv;

    //testing recyclerview with onCreate
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reusableitems = new ArrayList<>();
        vouchers = new ArrayList<>();
    }

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        Itemsrv = view.findViewById(R.id.reusableitems);
        Voucherrv = view.findViewById(R.id.vouchers);
        adapter = new ShopAdapter(this.getContext(),reusableitems);
        adapter2 = new ShopAdapter(this.getContext(),vouchers);
        Itemsrv.setAdapter(adapter);
        Voucherrv.setAdapter(adapter2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        // create another layout manager for vouchers rv bcuz the rv can't reuse the other layout manager
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        Itemsrv.setLayoutManager(layoutManager);
        Voucherrv.setLayoutManager(layoutManager2);
        getReusuableItemData();
        getVoucherItemData();

        return view;
    }

    private void getReusuableItemData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("shop").document("reusuableitems")
                .collection("items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                // Create a ShopData object with the data
                                ShopData item = document.toObject(ShopData.class);
                                item.setType("item");
                                item.setId(document.getId());

                                // Add the ShopData object to the ArrayList
                                reusableitems.add(item);

                            }
                            adapter.notifyDataSetChanged();

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void getVoucherItemData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("shop").document("vouchers")
                .collection("vouchers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                // Create a ShopData object with the data
                                ShopData item = document.toObject(ShopData.class);
                                item.setType("voucher");
                                item.setId(document.getId());

                                // Add the ShopData object to the ArrayList
                                vouchers.add(item);
                            }
                            adapter2.notifyDataSetChanged();

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopAdapterViewHolder> {

        private ArrayList<ShopData> shopitems;
        private Context context;

        public ShopAdapter(Context context, ArrayList<ShopData> shopitems/*, OnItemClickListener mlistener*/) {
            this.context = context;
            this.shopitems = shopitems;
        }

        @NonNull
        @Override
        public ShopAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate Layout
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_items_layout, parent, false);
            return new ShopAdapterViewHolder(view, context, shopitems);
        }

        @Override
        public void onBindViewHolder(@NonNull ShopAdapterViewHolder holder, int position) {

            ShopData Item = shopitems.get(position);

            String image = Item.getImage();
            String item = Item.getItem();
            String type = Item.getType();
            String id = Item.getId();
            int points = Item.getPoints();
            int quantity = Item.getQuantity();
            String pointsString = Integer.toString(points);

            RedeemShopItemFragment fragment = new RedeemShopItemFragment(image,item,points,quantity,type,id);


            holder.points.setText(pointsString);
            Picasso.with(context).load(image).fit().centerInside().into(holder.shopItem);

            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    fragment.show(getParentFragmentManager(), "dialog");
                }
            });
        }

        @Override
        public int getItemCount() {
            // this method returns the size of recyclerview
            return shopitems.size();
        }

        // View Holder Class to handle Recycler View.
        public class ShopAdapterViewHolder extends RecyclerView.ViewHolder{

            private TextView points;
            private ImageView shopItem;

            Context context;
            ArrayList<ShopData> data;

            public ShopAdapterViewHolder(@NonNull View itemView, Context context, ArrayList<ShopData> data) {
                super(itemView);
                shopItem = itemView.findViewById(R.id.shop_item);
                points = itemView.findViewById(R.id.points);
                this.data = data;
                this.context = context;

            }

        }

    }

}