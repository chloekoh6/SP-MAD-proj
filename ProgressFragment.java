package com.sp.android_studio_project;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sp.android_studio_project.ml.ModelUnquant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProgressFragment extends Fragment {

    private TextView result, point, received;
    private ImageView imageView;
    private Button picture;
    private Button picture1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DocumentReference documentReference;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    int imageSize = 224;
    private String currentUid;
    private ArrayList<LatLng> locationArrayList;

    private Calendar calendar = Calendar.getInstance();
    private int year = calendar.get(Calendar.YEAR);
    private int month = calendar.get(Calendar.MONTH);
    private int day = calendar.get(Calendar.DAY_OF_MONTH);
    private String todayString = year + "" + month + "" + day;

    private GPSTracker gpsTracker;

    //recycling bin locations
    LatLng b1 = new LatLng(1.446735, 103.792589);
    LatLng b2 = new LatLng(1.432924, 103.847409);
    LatLng b3 = new LatLng(1.398602, 103.826810);
    LatLng b4 = new LatLng(1.380411, 103.758832);
    LatLng b5 = new LatLng(1.457635, 103.827840);
    LatLng b6 = new LatLng(1.301812, 103.819257);
    LatLng b7 = new LatLng(1.276756, 103.813764);
    LatLng b8 = new LatLng(1.284666, 103.829578);
    LatLng b9 = new LatLng(1.293980, 103.802844);
    LatLng b10 = new LatLng(1.303883, 103.828019);
    LatLng b11 = new LatLng(1.358863, 103.985038);
    LatLng b12 = new LatLng(1.370876, 103.932853);
    LatLng b13 = new LatLng(1.321794, 103.928046);
    LatLng b14 = new LatLng(1.321451, 103.898864);
    LatLng b15 = new LatLng(1.357146, 103.947959);
    LatLng b16 = new LatLng(1.326256, 103.747115);
    LatLng b17 = new LatLng(1.335866, 103.706260);
    LatLng b18 = new LatLng(1.317454, 103.712561);
    LatLng b19 = new LatLng(1.316791, 103.673474);
    LatLng b20 = new LatLng(1.359351, 103.683774);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        result = view.findViewById(R.id.result);
        point = view.findViewById(R.id.points);
        received = view.findViewById(R.id.received);
        imageView = view.findViewById(R.id.imageView);
        picture = view.findViewById(R.id.button);
        //picture1 = view.findViewById(R.id.button1);
        swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();
        view.findViewById(R.id.button).setEnabled(TestDate(todayString));
        point();
        locationArrayList = new ArrayList<>();
        gpsTracker=new GPSTracker(getContext());

        locationArrayList.add(b1);
        locationArrayList.add(b2);
        locationArrayList.add(b3);
        locationArrayList.add(b4);
        locationArrayList.add(b5);
        locationArrayList.add(b6);
        locationArrayList.add(b7);
        locationArrayList.add(b8);
        locationArrayList.add(b9);
        locationArrayList.add(b10);
        locationArrayList.add(b11);
        locationArrayList.add(b12);
        locationArrayList.add(b13);
        locationArrayList.add(b14);
        locationArrayList.add(b15);
        locationArrayList.add(b16);
        locationArrayList.add(b17);
        locationArrayList.add(b18);
        locationArrayList.add(b19);
        locationArrayList.add(b20);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                point();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("You only have a total of 3 scans per day. Are you sure you want to scan now?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (gpsTracker.canGetLocation()) {
                                    double latitude = gpsTracker.getLatitude();
                                    double longitude = gpsTracker.getLongitude();
                                    LatLng currentLocation = new LatLng(latitude, longitude);
                                    for (LatLng locationArrayList : locationArrayList) {
                                        if (locationArrayList.equals(currentLocation)) {
                                            view.findViewById(R.id.button).setEnabled(TestDate(todayString));
                                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                                // Launch camera if we have permission
                                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                takeImageActivity.launch(cameraIntent);
                                                //startActivityForResult(cameraIntent, 1);
                                            } else {
                                                //Request camera permission if we don't have it.
                                                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                                            }
                                        } else {
                                            Toast.makeText(getContext(),"You need to be near a recycling bin",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Do nothing
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        /*picture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                selectImageActivity.launch(intent);
            }
        });*/

        return view;
    }

    public void point(){
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(currentUid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            String pts = Long.toString(task.getResult().getLong("points"));
                            point.setText(pts);
                        } else {
                            Toast.makeText(getContext(),"Search fail",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean TestDate(String todayString) {
        final SharedPreferences scanChecks =  this.getActivity().getSharedPreferences("SCANCHECK",0);
        boolean check = true;
        if(scanChecks.getString("date","").equals(todayString)){
            if(scanChecks.getInt("count",0)<3){
                scanChecks.edit().putInt("count",scanChecks.getInt("count",0)+1).apply();
                check = true;
            }else{
                check = false;
            }
        }else{
            scanChecks.edit().putString("date",todayString).apply();
            scanChecks.edit().putInt("count",1).apply();
        }
        return check;
    }

    public void classifyImage(Bitmap image){

        try {
            ModelUnquant model = ModelUnquant.newInstance(getContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            int pixel = 0;
            for(int i = 0; i < imageSize; i++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"cardboard","glass","metal","paper","plastic","unknown"};
            result.setText(classes[maxPos]);

            if (classes[maxPos] == "metal"){
                update(2);
                received.setText("2");
            } else if (classes[maxPos] == "paper"){
                update(5);
                received.setText("5");
            } else if (classes[maxPos] == "plastic"){
                update(8);
                received.setText("8");
            } else if (classes[maxPos] == "glass"){
                update(10);
                received.setText("10");
            } else if (classes[maxPos] == "cardboard"){
                update(12);
                received.setText("12");
            } else {
                received.setText("0");
            }

            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    public void update(int pt) {

        int newPts;
        CharSequence oldPts;

        documentReference = db.collection("Users").document(currentUid);
        oldPts = point.getText();

        newPts = Integer.parseInt(oldPts.toString()) + pt;

        // Update the field
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("points", newPts);

        documentReference.update(updateData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "User data updated successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error updating user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    ActivityResultLauncher<Intent> takeImageActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }
        }
    });

    /*ActivityResultLauncher<Intent> selectImageActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri selectedImageUri = data.getData();
                        try {
                            Bitmap selectedImageBM = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                            selectedImageBM = Bitmap.createScaledBitmap(selectedImageBM, imageSize, imageSize, false);
                            imageView.setImageBitmap(selectedImageBM);
                            classifyImage(selectedImageBM);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );*/


}