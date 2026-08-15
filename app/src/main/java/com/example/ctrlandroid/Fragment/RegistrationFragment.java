package com.example.ctrlandroid.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.example.ctrlandroid.R;

public class RegistrationFragment extends Fragment {

    private ImageView imageView;
    private Button btnPickImage , btnCreateCamp;
    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        imageView = view.findViewById(R.id.imageview);
        btnPickImage = view.findViewById(R.id.btnPickImage);
        btnCreateCamp = view.findViewById(R.id.btnCreateCamp);

        btnCreateCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Camp Created Successfully", Toast.LENGTH_SHORT).show();
            }
        });


        // Register gallery picker
        resultRegister();

        btnPickImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            resultLauncher.launch(intent);
        });

        return view;
    }

    private void resultRegister() {
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        imageView.setImageURI(uri);

                        // Show toast confirmation
                        Toast.makeText(getContext(), "Image selected successfully!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "No image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
