package com.sp.android_studio_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ArrangeStepsActivity extends AppCompatActivity {

    private LinearLayout op1, op2, op3, op4, l0, l1;
    private TextView t1, t2, t3, t4;
    private TextView tv1, tv2, tv3, tv4, step1, step2, step3, step4, dr2;
    private TextView q;
    private Button b1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<ArrangeStepsData> list;
    FirebaseAuth mAuth;
    String currentUid;
    int points = 1;
    int results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange_steps);

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();
        results = 0;

        //views to drag
        op1 = findViewById(R.id.op1);
        op2 = findViewById(R.id.op2);
        op3 = findViewById(R.id.op3);
        op4 = findViewById(R.id.op4);

        //views to drop onto
        t1 = findViewById(R.id.s1);
        t2 = findViewById(R.id.s2);
        t3 = findViewById(R.id.s3);
        t4 = findViewById(R.id.s4);


        //link textviews
        tv1 = findViewById(R.id.textv1);
        tv2 = findViewById(R.id.textv2);
        tv3 = findViewById(R.id.textv3);
        tv4 = findViewById(R.id.textv4);

        step1 = findViewById(R.id.step);
        step2 = findViewById(R.id.step2);
        step3 = findViewById(R.id.step3);
        step4 = findViewById(R.id.step4);

        q = findViewById(R.id.material);

        dr2 = findViewById(R.id.displayresult2);

        //link linear layout
        l0 = findViewById(R.id.l0);
        l1 = findViewById(R.id.l1);

        //link button
        b1 = findViewById(R.id.button2);

        //set button onclick
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserPoints(results);
                l1.setVisibility(View.GONE);
                dr2.setText("Congratulations! You've gotten " + results + " points!");
                l0.setVisibility(View.VISIBLE);

            }
        });

        //linking items to touchListener
        op1.setOnTouchListener(touchListener);
        op2.setOnTouchListener(touchListener);
        op3.setOnTouchListener(touchListener);
        op4.setOnTouchListener(touchListener);

        //linking items to dragListener
        t1.setOnDragListener(dragListener);
        t2.setOnDragListener(dragListener2);
        t3.setOnDragListener(dragListener3);
        t4.setOnDragListener(dragListener4);

        list = new ArrayList<>();

        getDataFromFirestore();

    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDragAndDrop(data, shadowBuilder, view, 0);
            return true;
        }
    };

    View.OnDragListener dragListener = new View.OnDragListener() {

        @Override
        public boolean onDrag(View view, DragEvent event) {
            int dragEvent = event.getAction();
            String step;
            String text;

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    final View v = (View) event.getLocalState();
                    switch(v.getId()){
                        case R.id.op1:
                            step = step1.getText().toString();
                            checkansStep1(step);
                            text = tv1.getText().toString();
                            t1.setText(text);
                            op1.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.op2:
                            step = step2.getText().toString();
                            checkansStep1(step);
                            text = tv2.getText().toString();
                            t1.setText(text);
                            op2.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.op3:
                            step = step3.getText().toString();
                            checkansStep1(step);
                            text = tv3.getText().toString();
                            t1.setText(text);
                            op3.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.op4:
                            step = step4.getText().toString();
                            checkansStep1(step);
                            text = tv4.getText().toString();
                            t1.setText(text);
                            op4.setVisibility(View.INVISIBLE);
                            break;
                    }
                    break;
            }
            return true;
        }
    };

    View.OnDragListener dragListener2 = new View.OnDragListener() {

        @Override
        public boolean onDrag(View view, DragEvent event) {
            int dragEvent = event.getAction();
            String step;
            String text;

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    final View v = (View) event.getLocalState();
                    switch(v.getId()){
                        case R.id.op1:
                            step = step1.getText().toString();
                            checkansStep2(step);
                            text = tv1.getText().toString();
                            t2.setText(text);
                            op1.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.op2:
                            step = step2.getText().toString();
                            checkansStep2(step);
                            text = tv2.getText().toString();
                            t2.setText(text);
                            op2.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.op3:
                            step = step3.getText().toString();
                            checkansStep2(step);
                            text = tv3.getText().toString();
                            t2.setText(text);
                            op3.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.op4:
                            step = step4.getText().toString();
                            checkansStep2(step);
                            text = tv4.getText().toString();
                            t2.setText(text);
                            op4.setVisibility(View.INVISIBLE);
                            break;
                    }
                    break;
            }
            return true;
        }
    };

    View.OnDragListener dragListener3 = new View.OnDragListener() {

        @Override
        public boolean onDrag(View view, DragEvent event) {
            int dragEvent = event.getAction();
            String step;
            String text;

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    final View v = (View) event.getLocalState();
                    switch(v.getId()){
                        case R.id.op1:
                            step = step1.getText().toString();
                            checkansStep3(step);
                            text = tv1.getText().toString();
                            t3.setText(text);
                            op1.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.op2:
                            step = step2.getText().toString();
                            checkansStep3(step);
                            text = tv2.getText().toString();
                            t3.setText(text);
                            op2.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.op3:
                            step = step3.getText().toString();
                            checkansStep3(step);
                            text = tv3.getText().toString();
                            t3.setText(text);
                            op3.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.op4:
                            step = step4.getText().toString();
                            checkansStep3(step);
                            text = tv4.getText().toString();
                            t3.setText(text);
                            op4.setVisibility(View.INVISIBLE);
                            break;
                    }
                    break;
            }
            return true;
        }
    };

    View.OnDragListener dragListener4 = new View.OnDragListener() {

        @Override
        public boolean onDrag(View view, DragEvent event) {
            int dragEvent = event.getAction();
            String step;
            String text;

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    final View v = (View) event.getLocalState();
                    switch(v.getId()){
                        case R.id.op1:
                            step = step1.getText().toString();
                            checkansStep4(step);
                            text = tv1.getText().toString();
                            t4.setText(text);
                            op1.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.op2:
                            step = step2.getText().toString();
                            checkansStep4(step);
                            text = tv2.getText().toString();
                            t4.setText(text);
                            op2.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.op3:
                            step = step3.getText().toString();
                            checkansStep4(step);
                            text = tv3.getText().toString();
                            t4.setText(text);
                            op3.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.op4:
                            step = step4.getText().toString();
                            checkansStep4(step);
                            text = tv4.getText().toString();
                            t4.setText(text);
                            op4.setVisibility(View.INVISIBLE);
                            break;
                    }
                    break;
            }
            return true;
        }
    };

    public void getDataFromFirestore(){
        CollectionReference collectionReference = db.collection("arrangestepsgame");
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ArrangeStepsData data = document.toObject(ArrangeStepsData.class);
                                list.add(data);
                            }
                            Collections.shuffle(list);

                            ArrangeStepsData stepsData = list.get(0);
                            String question = stepsData.getQuestion();
                            String s1 = stepsData.getStep1();
                            String s2 = stepsData.getStep2();
                            String s3 = stepsData.getStep3();
                            String s4 = stepsData.getStep4();

                            q.setText(question);
                            tv1.setText(s2);
                            tv2.setText(s3);
                            tv3.setText(s4);
                            tv4.setText(s1);

                            step1.setText("step2");
                            step2.setText("step3");
                            step3.setText("step4");
                            step4.setText("step1");

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
                            Toast.makeText(ArrangeStepsActivity.this,"Search fail",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(ArrangeStepsActivity.this, "Points updated!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ArrangeStepsActivity.this, "Error updating user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean checkansStep1(String step){
        if(step.equals("step1")){
            Toast.makeText(ArrangeStepsActivity.this, "correct!", Toast.LENGTH_SHORT).show();
            results += points;
            return true;
        } else {
            Toast.makeText(ArrangeStepsActivity.this, "wrong!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean checkansStep2(String step){
        if(step.equals("step2")){
            Toast.makeText(ArrangeStepsActivity.this, "correct!", Toast.LENGTH_SHORT).show();
            results += points;
            return true;
        } else {
            Toast.makeText(ArrangeStepsActivity.this, "wrong!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean checkansStep3(String step){
        if(step.equals("step3")){
            Toast.makeText(ArrangeStepsActivity.this, "correct!", Toast.LENGTH_SHORT).show();
            results += points;
            return true;
        } else {
            Toast.makeText(ArrangeStepsActivity.this, "wrong!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean checkansStep4(String step){
        if(step.equals("step4")){
            Toast.makeText(ArrangeStepsActivity.this, "correct!", Toast.LENGTH_SHORT).show();
            results += points;
            return true;
        } else {
            Toast.makeText(ArrangeStepsActivity.this, "wrong!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}