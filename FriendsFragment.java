package com.sp.android_studio_project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsFragment extends Fragment {
    private EditText searchFri;
    private ImageButton btnSearch;;
    private RecyclerView recyclerView;
    private List<FriendsModel> friendsModels = new ArrayList<>();
    private List<FriendsModel> friendsModel = new ArrayList<>();
    private FriendsAdapter friendsAdapter;
    String s;
    FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.friends_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        getModels();
        friendsAdapter = new FriendsAdapter(getContext(), friendsModels);
        recyclerView.setAdapter(friendsAdapter);

        btnSearch = view.findViewById(R.id.btn_search);
        searchFri = view.findViewById(R.id.search_fri);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchFri.getText().toString();
                searchFriends(searchText);
            }
        });


        return view;
    }

    private void getModels() {
        CollectionReference collectionReference = db.collection("Users");
        collectionReference.orderBy("points", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            for (QueryDocumentSnapshot queryDocumentSnapshot : result) {
                                FriendsModel friendsModel = queryDocumentSnapshot.toObject(FriendsModel.class);
                                Map<String, Object> data = queryDocumentSnapshot.getData();
                                String imageUrl = data.get("image").toString();
                                Log.e("TAG", "onComplete: " + data);
                                friendsModel.setImage(imageUrl);
                                friendsModels.add(friendsModel);
                            }
                            friendsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void searchFriends(String searchQuery) {
        friendsAdapter = new FriendsAdapter(getContext(), friendsModel);
        recyclerView.setAdapter(friendsAdapter);
        // Query the Firestore database for documents that match the search criteria
        db.collection("Users")
                .whereEqualTo("username", searchQuery)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            for (QueryDocumentSnapshot queryDocumentSnapshot : result) {
                                FriendsModel friendModel = queryDocumentSnapshot.toObject(FriendsModel.class);
                                Map<String, Object> data = queryDocumentSnapshot.getData();
                                String imageUrl = data.get("image").toString();
                                Log.e("TAG", "onComplete: " + data);
                                friendModel.setImage(imageUrl);
                                friendsModel.add(friendModel);
                            }
                        } else {
                            Toast.makeText(getContext(), "Search fail", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
