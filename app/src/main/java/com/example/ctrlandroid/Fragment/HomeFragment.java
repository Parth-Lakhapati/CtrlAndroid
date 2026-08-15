package com.example.ctrlandroid.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.ctrlandroid.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton fabAdd = view.findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(v -> {
            // Try to open ChatGPT app
            Intent launchIntent = requireActivity().getPackageManager()
                    .getLaunchIntentForPackage("com.openai.chatgpt");
            if (launchIntent != null) {
                startActivity(launchIntent);
            } else {
                // Fallback: open ChatGPT in browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://chat.openai.com/"));
                startActivity(browserIntent);
            }
        });

        return view;
    }
}
