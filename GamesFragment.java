package com.sp.android_studio_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GamesFragment extends Fragment {

    private LinearLayout Rq;
    private LinearLayout As;
    private LinearLayout Sw;
    private TextView points;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String currentUid;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        Rq = (LinearLayout) view.findViewById(R.id.RQ);
        As = (LinearLayout) view.findViewById(R.id.AS);
        Sw = (LinearLayout) view.findViewById(R.id.Sort);
        points = view.findViewById(R.id.points);

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        showpoint();

        Rq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RecycleQuizFragment()).addToBackStack(null).commit();
            }
        });

        As.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ArrangeStepsActivity.class);
                startActivity(intent);
            }
        });

        Sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SortingGameActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }

    public void showpoint(){
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(currentUid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()) {
                            String pts = Long.toString(task.getResult().getLong("points"));
                            points.setText(pts);
                        } else {
                            Toast.makeText(getContext(),"Search fail",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}