package com.example.harpigle.happybirthday.Persons;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public interface PersonsListActivityItemClickListener {
    void onClickListener(
            @NonNull View view,
            @NonNull String name,
            @NonNull String date,
            @NonNull String time,
            @NonNull String phoneNumber
    );
}