package com.sp.android_studio_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FeedbackFragment extends Fragment {

    private WebView webViewFeedback;
    String urlFeedback = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        webViewFeedback = view.findViewById(R.id.feedback_web);
        webViewFeedback.getSettings().setJavaScriptEnabled(true); // enable javascript
        webViewFeedback.setWebViewClient(new WebViewClient()); // important to open url in your app
        webViewFeedback.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSfC7xemCdkfMHu5OR2brOCoHPi9tRdgOTs0CpHu6GLMMH13iA/viewform?usp=sf_link");

        return view;
    }
}