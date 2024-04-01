package com.sp.android_studio_project;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    EditText username, password, email, mobile, address, friend;
    Button btnSignUp, btnBack;
    ProgressBar mProgressBar;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
    private ImageView selectImage;
    Uri mImageUri;
    TextView selectImageText;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private StorageReference mStorageRef;

    private StorageTask mUploadTask;
    private UserController upload;
    private boolean getUser;
    private boolean getUser_Pts;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.mobile);
        address = (EditText) findViewById(R.id.address);
        friend = (EditText) findViewById(R.id.friend);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnBack = (Button) findViewById(R.id.btnBack);

        selectImage = (ImageView) findViewById(R.id.selectImage);
        selectImageText = (TextView) findViewById(R.id.selectImageText);

        mProgressBar = (ProgressBar) findViewById(R.id.reg_progress_bar);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            RegistrationActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION
                    );
                } else {
                    imageSelect();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String em = email.getText().toString();
                String num = mobile.getText().toString();
                String addr = address.getText().toString();
                String frd = friend.getText().toString();

                int pts = 10;

                if (selectImage.getDrawable() == null) {
                    selectImageText.setError("Please select an image!");
                    selectImageText.requestFocus();
                    return;
                }

                if (user.isEmpty()) {
                    username.setError("Please enter username!");
                    username.requestFocus();
                    return;
                }

                if (pass.isEmpty()) {
                    password.setError("Please enter password!");
                    password.requestFocus();
                    return;
                }

                if (pass.length() < 6) {
                    password.setError("Min password length should be 6 characters!");
                    password.requestFocus();
                    return;
                }

                if (em.trim().isEmpty()) {
                    email.setError("Please enter email!");
                    email.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(em).matches()) {
                    email.setError("Please provide valid email!");
                    email.requestFocus();
                    return;
                }

                if (num.trim().isEmpty()) {
                    mobile.setError("Please enter mobile!");
                    mobile.requestFocus();
                    return;
                }

                if (addr.trim().isEmpty()) {
                    address.setError("Please enter address!");
                    address.requestFocus();
                    return;
                }

                if (frd.trim().equals("")) { //frd.trim().equals("")
                    frd = "NIL";
                    pts = 0;
                } else if (!frd.trim().equals("") && !Patterns.EMAIL_ADDRESS.matcher(frd).matches()) {
                    friend.setError("Please provide valid email!");
                    friend.requestFocus();
                    return;
                }

                String finalFrd = frd;
                int points = pts;

                auth.fetchSignInMethodsForEmail(em)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                getUser = !task.getResult().getSignInMethods().isEmpty();

                                if (getUser) { // input given is an old user
                                    email.setError("The email address is already in use by another account.\nPlease Sign-In if you are an existing user!");
                                    email.requestFocus();
                                }
                            }
                        });

                //!finalFrd.equals("NIL") && (mUploadTask != null && mUploadTask.isComplete())
                // && ((mUploadTask != null && mUploadTask.isInProgress()) || (mUploadTask != null && mUploadTask.isComplete()))

                if (!finalFrd.equals("NIL")) {
                    auth.fetchSignInMethodsForEmail(frd)
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    getUser_Pts = !task.getResult().getSignInMethods().isEmpty();
                                    if (!getUser_Pts) { //input given is a new user
                                        friend.setError("The friend email address is invalid. Please enter a registered account!");
                                        friend.requestFocus();
                                        return;
                                    } else if (getUser_Pts) { //input given is an old user //&& !finalFrd.equals("NIL")
                                        db.collection("Users")
                                                .whereEqualTo("email", finalFrd)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                            int invitePts = 20;
                                                            int inviteUserPts = 0;
                                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                                int documentPoints = Math.toIntExact(documentSnapshot.getLong("points"));
                                                                int inviteUser = documentPoints;
                                                                inviteUser += invitePts;
                                                                inviteUserPts = inviteUser;

                                                                /*documentPoints = "";
                                                                documentPoints+=;*/
                                                            }
                                                            Map<String, Object> Update = new HashMap<>();
                                                            Update.put("points", inviteUserPts);
                                                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                                            String documentID = documentSnapshot.getId();
                                                            db.collection("Users")
                                                                    .document(documentID)
                                                                    .update(Update) //.update(Update) //.set(Update, SetOptions.merge()) //.set(Update)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Log.d("TAG", "Document successfully updated!");
                                                                            //Log.d(TAG, "Document successfully updated!");
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Log.w("TAG", "Error updating document", e);
                                                                            //Log.w(TAG, "Error updating document", e);
                                                                        }
                                                                    });
                                                        } else {
                                                            Log.d("TAG", "Error getting documents: ", task.getException());
                                                            //Log.d(TAG, "Error getting documents: ", task.getException());
                                                        }
                                                    }
                                                });

                                        auth.fetchSignInMethodsForEmail(em)
                                                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                                        getUser = !task.getResult().getSignInMethods().isEmpty();
                                                        if (!getUser) { // input given is a new user
                                                            if ((mUploadTask != null && mUploadTask.isInProgress()) || (mUploadTask != null && mUploadTask.isComplete())) {
                                                                Toast.makeText(RegistrationActivity.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                btnSignUp.setEnabled(false);
                                                                uploadFile();
                                                            }
                                                        }
                                                    }
                                                });

                                        auth.createUserWithEmailAndPassword(em, pass)
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            Map<String, Object> User = new HashMap<>();
                                                            User.put("username", user);
                                                            User.put("password", pass);
                                                            User.put("email", em);
                                                            User.put("mobile", num);
                                                            User.put("address", addr);
                                                            User.put("friend", finalFrd);
                                                            User.put("image", upload.getmImageUrl());
                                                            User.put("points", points);

                                                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                                            db.collection("Users")
                                                                    .document(uid)
                                                                    .set(User)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Toast.makeText(RegistrationActivity.this, "Sign-Up Successful!", Toast.LENGTH_SHORT).show();
                                                                            Intent intent = new Intent(getApplicationContext(), nav_headerP2.class);
                                                                            startActivity(intent);
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(RegistrationActivity.this, "Sign-Up failed! Try again!", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                });

                                    }
                                }
                            });
                }


                if (finalFrd.equals("NIL")) {
                    auth.fetchSignInMethodsForEmail(em)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                getUser = !task.getResult().getSignInMethods().isEmpty();

                                if (!getUser) { // input given is a new user
                                    if ((mUploadTask != null && mUploadTask.isInProgress()) || (mUploadTask != null && mUploadTask.isComplete())) {
                                        Toast.makeText(RegistrationActivity.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
                                    } else {
                                        btnSignUp.setEnabled(false);
                                        uploadFile();
                                    }
                                }
                            }
                        });

                    auth.createUserWithEmailAndPassword(em, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Map<String, Object> User = new HashMap<>();
                                    User.put("username", user);
                                    User.put("password", pass);
                                    User.put("email", em);
                                    User.put("mobile", num);
                                    User.put("address", addr);
                                    User.put("friend", finalFrd);
                                    User.put("image", upload.getmImageUrl());
                                    User.put("points", points);
                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    db.collection("Users")
                                            .document(uid)
                                            .set(User)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(RegistrationActivity.this, "Sign-Up Successful!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), nav_headerP2.class);
                                                    startActivity(intent);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(RegistrationActivity.this, "Sign-Up failed! Try again!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });

    }

    private void imageSelect() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imageSelect();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            if (mImageUri != null) {
                try {
                    Picasso.with(this).load(mImageUri).into(selectImage);
                } catch (Exception exception) {
                    Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 2000);

                            //upload = new UserController(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());

                            StorageReference reference = taskSnapshot.getMetadata().getReference();
                            Task<Uri> downloadUrl = reference.getDownloadUrl();
                            downloadUrl.addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    upload = new UserController(task.getResult().toString());
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot tasksnapshot) {
                            double progress = (100.0 * tasksnapshot.getBytesTransferred() / tasksnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }


    }
}