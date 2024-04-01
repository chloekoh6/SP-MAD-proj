package com.sp.android_studio_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecycleQuizFragment extends Fragment {

    private Button nextbutton;
    private RadioGroup rg;
    private LinearLayout l1;
    private LinearLayout l2;
    private TextView question;
    private TextView points;
    private TextView displayresults;
    private RadioButton a1;
    private RadioButton a2;
    private RadioButton a3;
    private RadioButton a4;
    private boolean check;
    private int pts;
    private int resultpts = 0;
    private String correctans;
    private String currentUid;

    private ArrayList<RecycleQuizQuestionsData> quizDataList;
    private FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //qns = new ArrayList<>();
        //getLevels();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycle_quiz, container, false);

        nextbutton = view.findViewById(R.id.next_button);
        question = view.findViewById(R.id.question);
        points = view.findViewById(R.id.resultpts);
        displayresults = view.findViewById(R.id.displayresult);
        rg = view.findViewById(R.id.radiogroup);
        l1 = view.findViewById(R.id.linearlayout1);
        l2 = view.findViewById(R.id.linearlayout2);
        a1 = view.findViewById(R.id.a1);
        a2 = view.findViewById(R.id.a2);
        a3 = view.findViewById(R.id.a3);
        a4 = view.findViewById(R.id.a4);

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();
        retrieveQuizData();

        return view;
    }

    private boolean checkAnswer(RadioGroup radioGroup, String correctAnswer) {

        String answer;
        switch(radioGroup.getCheckedRadioButtonId()){
            case R.id.a1:
                answer = a1.getText().toString();
                break;
            case R.id.a2:
                answer = a2.getText().toString();
                break;
            case R.id.a3:
                answer = a3.getText().toString();
                break;
            case R.id.a4:
                answer = a4.getText().toString();
                break;
            default:
                answer = "";
                break;
        }
        if (answer.equals(correctAnswer)) {
            return true;
        } else {
            return false;
        }
    }


    private void retrieveQuizData() {
        db = FirebaseFirestore.getInstance();
        quizDataList = new ArrayList<>();
        CollectionReference quizzesRef = db.collection("quizzes");

        quizzesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    final int[] i = {1};
                    for (DocumentSnapshot document : task.getResult()) {
                        RecycleQuizQuestionsData quizData = document.toObject(RecycleQuizQuestionsData.class);
                        quizDataList.add(quizData);
                    }

                    Collections.shuffle(quizDataList);
                    RecycleQuizQuestionsData qn = quizDataList.get(0);
                    List<String> options = qn.getAnswers();

                    question.setText(qn.getQuestion());
                    a1.setText(options.get(0));
                    a2.setText(options.get(1));
                    a3.setText(options.get(2));
                    a4.setText(options.get(3));
                    correctans = qn.getCorrectans();
                    pts = qn.getQpoints();

                    nextbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(i[0] <= 4){
                                check = checkAnswer(rg, correctans);
                                rg.clearCheck();
                                if(check == true){
                                    resultpts = resultpts + pts;
                                    String sResultpts = "Points: " + resultpts;
                                    points.setText(sResultpts);
                                } else {}

                                RecycleQuizQuestionsData qn = quizDataList.get(i[0]);
                                List<String> options = qn.getAnswers();

                                question.setText(qn.getQuestion());
                                a1.setText(options.get(0));
                                a2.setText(options.get(1));
                                a3.setText(options.get(2));
                                a4.setText(options.get(3));
                                correctans = qn.getCorrectans();
                                pts = qn.getQpoints();

                                i[0]++;
                                if(i[0]>4){
                                    nextbutton.setText("Get Result");
                                }
                            } else{
                                l1.setVisibility(View.GONE);
                                check = checkAnswer(rg, correctans);
                                if(check == true){
                                    resultpts = resultpts + pts;
                                    String sResultpts = "Points: " + resultpts;
                                } else {}
                                displayresults.setText("Congratulations! You've gotten " + resultpts + " points!");
                                l2.setVisibility(View.VISIBLE);
                                getUserPoints(resultpts);

                            }

                        }
                    });

                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
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
                            Toast.makeText(getContext(),"Search fail",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getContext(), "Points updated!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error updating user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}