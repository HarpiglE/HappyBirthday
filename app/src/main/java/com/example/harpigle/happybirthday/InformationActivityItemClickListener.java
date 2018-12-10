package com.example.harpigle.happybirthday;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface InformationActivityItemClickListener {
    void onClickListener(
            @NonNull String name,
            @Nullable String date,
            @Nullable String time,
            @Nullable String phoneNumber
    );
}