package com.reactnative.toast.example;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.navigationhybrid.HybridFragment;
import com.reactnative.toast.Toast;

public class DemoFragment extends HybridFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_demo, container, false);

        root.findViewById(R.id.loading).setOnClickListener(v -> {
            Toast toast = Toast.loading(requireActivity(), "正在下载...");
            root.postDelayed(() -> {
                toast
                        .text("资料已经下载完成")
                        .hideDelayDefault();

                root.postDelayed(() -> {
                    toast.loading("新的任务...");

                    root.postDelayed(() -> {
                        toast.done("新任务已完成").hideDelayDefault();

                    }, 1000);

                }, 3000);


            }, 1000);
        });

        root.findViewById(R.id.text).setOnClickListener(v -> {
            Toast.text(requireActivity(), "Hello Native!");
        });

        root.findViewById(R.id.info).setOnClickListener(v -> {
            Toast.info(requireActivity(), "一条好消息，一条坏消息，你要先听哪一条？");
        });

        root.findViewById(R.id.done).setOnClickListener(v -> {
            Toast.done(requireActivity(), "工作完成，提前下班");
        });

        root.findViewById(R.id.error).setOnClickListener(v -> {
            Toast.error(requireActivity(), "哈，你又写 BUG 了！");
        });


        return root;
    }


}
