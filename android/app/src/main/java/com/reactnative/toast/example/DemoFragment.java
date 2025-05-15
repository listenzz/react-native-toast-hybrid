package com.reactnative.toast.example;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.reactnative.hybridnavigation.HybridFragment;
import com.reactnative.toast.Toast;

public class DemoFragment extends HybridFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_demo, container, false);

        root.findViewById(R.id.loading).setOnClickListener(v -> {
            Toast toast = Toast.loading(requireActivity(), "Downloading...");
            root.postDelayed(() -> {
                toast
                        .text("Download done!")
                        .hideDelayDefault();

                root.postDelayed(() -> {
                    toast.loading("New Task...");

                    root.postDelayed(() -> {
                        toast.done("New Task is done.").hideDelayDefault();

                    }, 1000);

                }, 3000);


            }, 1000);
        });

        root.findViewById(R.id.text).setOnClickListener(v -> {
            Toast.text(requireActivity(), "Hello Native!");
        });

        root.findViewById(R.id.info).setOnClickListener(v -> {
            Toast.info(requireActivity(), "A long long message to tell you.");
        });

        root.findViewById(R.id.done).setOnClickListener(v -> {
            Toast.done(requireActivity(), "Work is done!");
        });

        root.findViewById(R.id.error).setOnClickListener(v -> {
            Toast.error(requireActivity(), "Maybe somthing is wrong!");
        });


        return root;
    }


}
