package com.sp.android_studio_project;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 2;
    ImageView pic;
    EditText name,password,email,mobile,address;
    ImageButton save;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String imageString;
    String currentUid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        
        pic = view.findViewById(R.id.pic);
        name = view.findViewById(R.id.name);
        password = view.findViewById(R.id.password);
        email = view.findViewById(R.id.email);
        mobile = view.findViewById(R.id.mobile);
        address = view.findViewById(R.id.address);
        save = view.findViewById(R.id.save);

        getData();

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateImage();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

        return view;
    }

    private void getData(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String fireEmail = user.getEmail();
        db = FirebaseFirestore.getInstance();
        db.collection("Users")
                //.whereEqualTo("email",SharePreferenceUtil.getData(getContext(),"email"))
                .whereEqualTo("email", fireEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot result = task.getResult();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : result) {
                            Map<String, Object> data = queryDocumentSnapshot.getData();
                            Log.e("TAG", "onComplete: "+ data);
                            imageString = (String) data.get("image");
                            Picasso.with(getContext()).load(imageString).into(pic);
                            name.setText(data.get("username").toString());
                            password.setText(data.get("password").toString());
                            email.setText(data.get("email").toString());
                            mobile.setText(data.get("mobile").toString());
                            address.setText(data.get("address").toString());

                        }
                    }
                });

    }

    public void updateData(){
        user = mAuth.getCurrentUser();
        String fireEmail = user.getEmail();
        db = FirebaseFirestore.getInstance();

        //String newName =
        Map<String, Object> updates = new HashMap<>();
        updates.put("username", name.getText().toString());
        updates.put("password", password.getText().toString());
        updates.put("email", email.getText().toString());
        updates.put("mobile", mobile.getText().toString());
        updates.put("address", address.getText().toString());
        //updates.put("image", upload.getmImageUrl());

        db.collection("Users")
                .whereEqualTo("email", fireEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        QuerySnapshot result = task.getResult();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : result) {
                            String documentId = queryDocumentSnapshot.getId();
                            db.collection("Users").document(documentId)
                                    .update(updates)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("TAG", "User information updated successfully");
                                            Toast.makeText(getContext(), "Information updated successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("TAG", "Error updating user information", e);
                                            Toast.makeText(getContext(), "Information updated fail", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
    }

    /*private void updateImageUrlInFirestore(Uri imageUri) {
        // Get a reference to the user's document in Firestore
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users").document(currentUid);

        // Update the image URL field in the user's document
        userRef.update("image", imageUri.toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Update the UI with the new image URL
                        Picasso.with(getContext()).load(imageUri).into(pic);
                    }
                });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }*/


    public void updateImage(){
        // Reference to the image in Firebase Storage
        StorageReference imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageString);

        // Delete the existing image from Firebase Storage
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Upload the new image to Firebase Storage
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult.launch(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> startActivityForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri selectedImageUri = data.getData();
                        StorageReference newImageRef = FirebaseStorage.getInstance().getReference().child("uploads/" + selectedImageUri.getLastPathSegment());
                        UploadTask uploadTask = newImageRef.putFile(selectedImageUri);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get the new image URL from Firebase Storage
                                newImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Update the document in Firestore with the new URL
                                        currentUid = mAuth.getCurrentUser().getUid();
                                        Map<String, Object> update = new HashMap<>();
                                        update.put("image", uri.toString());
                                        db.collection("Users").document(currentUid)
                                                .update(update)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // Image update successful
                                                        Picasso.with(getContext()).load(selectedImageUri).into(pic);
                                                        Toast.makeText(getContext(), "image changed", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                });
                            }
                        });
                    }
                }
    });

}