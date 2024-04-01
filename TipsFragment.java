package com.sp.android_studio_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TipsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tips, container, false);
        ImageButton button1 = (ImageButton) view.findViewById(R.id.paper);
        ImageButton button2 = (ImageButton) view.findViewById(R.id.electronic);
        ImageButton button3 = (ImageButton) view.findViewById(R.id.metal);
        ImageButton button4 = (ImageButton) view.findViewById(R.id.glass);
        ImageButton button5 = (ImageButton) view.findViewById(R.id.household);
        ImageButton button6 = (ImageButton) view.findViewById(R.id.garden);
        ImageButton button7 = (ImageButton) view.findViewById(R.id.plastic);
        ImageButton button8 = (ImageButton) view.findViewById(R.id.hazardous);
        ImageButton button9 = (ImageButton) view.findViewById(R.id.others);

        ImageView i = view.findViewById(R.id.pic);
        TableLayout tableview = view.findViewById(R.id.as);



        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setImageResource(R.drawable.pic1);
                tableview.setVisibility(View.GONE);

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setImageResource(R.drawable.pic2);
                tableview.setVisibility(View.GONE);

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setImageResource(R.drawable.pic3);
                tableview.setVisibility(View.GONE);

            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setImageResource(R.drawable.pic4);
                tableview.setVisibility(View.GONE);

            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setImageResource(R.drawable.pic5);
                tableview.setVisibility(View.GONE);

            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setImageResource(R.drawable.pic6);
                tableview.setVisibility(View.GONE);

            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setImageResource(R.drawable.pic7);
                tableview.setVisibility(View.GONE);

            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setImageResource(R.drawable.pic8);
                tableview.setVisibility(View.GONE);

            }
        });

        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.setImageResource(R.drawable.pic9);
                tableview.setVisibility(View.GONE);

            }
        });

        return view;

    }


}