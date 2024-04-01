package com.sp.android_studio_project;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ExploreFragment_Progress extends Fragment {

    BottomNavigationView bottomNavigationView;
    Button btnBackProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_progress, container, false);

        ((DrawerLocker)getActivity()).setDrawerLocked(true);

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.INVISIBLE);

        btnBackProgress = view.findViewById(R.id.btnBackProgress);
        btnBackProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExploreFragment backFrag = new ExploreFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, backFrag, "findBackFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        VideoView videoView = view.findViewById(R.id.videoProgress_view);
        String videoPath = "android.resource://" + getContext().getPackageName() + "/" + R.raw.video;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(getContext());
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((DrawerLocker)getActivity()).setDrawerLocked(false);

        bottomNavigationView.setVisibility(View.VISIBLE);
    }

}