package com.sp.android_studio_project;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;


public class SortingGameActivity extends AppCompatActivity {

    private LinearLayout op1, op2, op3, op4, l1, l2;
    private TextView t1, t2, t3, t4, dr;
    private ImageView bin1, bin2, i1, i2, i3, i4;
    private Button b1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<SortingGameData> list;
    FirebaseAuth mAuth;
    String currentUid;
    int points = 1;
    int results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting_game);

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();
        results = 0;

        //views to drag
        op1 = findViewById(R.id.option_1);
        op2 = findViewById(R.id.option_2);
        op3 = findViewById(R.id.option_3);
        op4 = findViewById(R.id.option_4);

        //views to drop onto
        bin1 = findViewById(R.id.bin1);
        bin2 = findViewById(R.id.bin2);

        //link linearlayout
        l1 = findViewById(R.id.linear1);
        l2 = findViewById(R.id.linear2);

        //link textviews
        t1 = findViewById(R.id.type);
        t2 = findViewById(R.id.type2);
        t3 = findViewById(R.id.type3);
        t4 = findViewById(R.id.type4);
        dr = findViewById(R.id.displayresult1);

        //link imageview
        i1 = findViewById(R.id.i1);
        i2 = findViewById(R.id.i2);
        i3 = findViewById(R.id.i3);
        i4 = findViewById(R.id.i4);

        //link button
        b1 = findViewById(R.id.b1);
        //set button onclick
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserPoints(results);
                l1.setVisibility(View.GONE);
                dr.setText("Congratulations! You've gotten " + results + " points!");
                l2.setVisibility(View.VISIBLE);

            }
        });

        //linking items to touchListener
        op1.setOnTouchListener(touchListener);
        op2.setOnTouchListener(touchListener);
        op3.setOnTouchListener(touchListener);
        op4.setOnTouchListener(touchListener);

        bin1.setOnDragListener(BindragListener);
        bin2.setOnDragListener(RecyclebinDragListener);

        list = new ArrayList<>();

        //get data
        getDataFromFirestore();



        //
    }

    OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            ClipData data = ClipData.newPlainText("", "");
            DragShadowBuilder shadowBuilder = new DragShadowBuilder(view);
            view.startDragAndDrop(data, shadowBuilder, view, 0);
            return true;
        }
    };

    OnDragListener BindragListener = new OnDragListener(){

        @Override
        public boolean onDrag(View view, DragEvent event) {
            int dragEvent = event.getAction();
            String type;

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    final View v = (View) event.getLocalState();
                    switch(v.getId()){
                        case R.id.option_1:
                            type = t1.getText().toString();
                            checkansTrash(type);
                            op1.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.option_2:
                            type = t2.getText().toString();
                            checkansTrash(type);
                            op2.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.option_3:
                            type = t3.getText().toString();
                            checkansTrash(type);
                            op3.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.option_4:
                            type = t4.getText().toString();
                            checkansTrash(type);
                            op4.setVisibility(View.INVISIBLE);
                            break;
                    }
                    break;
            }
            return true;
        }
    };

    OnDragListener RecyclebinDragListener = new OnDragListener() {
        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            int dragEvent1 = dragEvent.getAction();
            String type1;

            switch (dragEvent1) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    final View v = (View) dragEvent.getLocalState();
                    switch(v.getId()){
                        case R.id.option_1:
                            type1 = t1.getText().toString();
                            checkansRecyclables(type1);
                            op1.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.option_2:
                            type1 = t2.getText().toString();
                            checkansRecyclables(type1);
                            op2.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.option_3:
                            type1 = t3.getText().toString();
                            checkansRecyclables(type1);
                            op3.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.option_4:
                            type1 = t4.getText().toString();
                            checkansRecyclables(type1);
                            op4.setVisibility(View.INVISIBLE);
                            break;
                    }
                    break;
            }
            return true;
        }
    };

    public void getDataFromFirestore(){
        CollectionReference collectionReference = db.collection("sortinggame");
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                SortingGameData data = document.toObject(SortingGameData.class);
                                list.add(data);
                            }
                            Collections.shuffle(list);
                            for(int i = 0; i < 4; i++){
                                SortingGameData sortingdata = list.get(i);
                                String image = sortingdata.getImage();
                                String type = sortingdata.getType();

                                switch(i){
                                    case 0:
                                        Picasso.with(SortingGameActivity.this).load(image).fit().centerInside().into(i1);
                                        t1.setText(type);
                                        break;
                                    case 1:
                                        Picasso.with(SortingGameActivity.this).load(image).fit().centerInside().into(i2);
                                        t2.setText(type);
                                        break;
                                    case 2:
                                        Picasso.with(SortingGameActivity.this).load(image).fit().centerInside().into(i3);
                                        t3.setText(type);
                                        break;
                                    case 3:
                                        Picasso.with(SortingGameActivity.this).load(image).fit().centerInside().into(i4);
                                        t4.setText(type);
                                        break;
                                }

                            }
                        } else {
                            Log.d("Firebase", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getUserPoints(int resultpts){
        db.collection("Users").document(currentUid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            int pts = Math.toIntExact(task.getResult().getLong("points"));
                            int updatedpts = resultpts + pts;
                            updatepts(updatedpts);

                        } else {
                            Toast.makeText(SortingGameActivity.this,"Search fail",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void updatepts(int pts){
        DocumentReference documentReference = db.collection("Users").document(currentUid);

        documentReference.update("points",pts)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SortingGameActivity.this, "Points updated!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SortingGameActivity.this, "Error updating user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkansTrash(String type){
        String typeofmaterial = type;
        if(typeofmaterial.equals("trash")){
            Toast.makeText(SortingGameActivity.this, "correct!", Toast.LENGTH_SHORT).show();
            results += points;
        } else {
            Toast.makeText(SortingGameActivity.this, "wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkansRecyclables(String type){
        String typeofmaterial = type;
        if(typeofmaterial.equals("recyclables")){
            Toast.makeText(SortingGameActivity.this, "correct!", Toast.LENGTH_SHORT).show();
            results += points;
        } else {
            Toast.makeText(SortingGameActivity.this, "wrong!", Toast.LENGTH_SHORT).show();
        }
    }

}