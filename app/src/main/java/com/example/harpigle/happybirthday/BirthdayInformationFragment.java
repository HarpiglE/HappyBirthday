package com.example.harpigle.happybirthday;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

public class BirthdayInformationFragment extends Fragment {

    private RecyclerView recyclerView;
    private InformationFragmentAdapter adapter;

    public BirthdayInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_birthday_information,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view_fragment);

        ArrayList<String[]> information = extractInformation();

        adapter = new InformationFragmentAdapter(information);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false
        ));
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<String[]> extractInformation() {
        BirthDaySharedPref birthDaySharedPref = BirthDaySharedPref.getInstance(getContext());

        ArrayList<JSONArray> sharedPrefsList = birthDaySharedPref.getAll();
        ArrayList<String[]> adapterList = new ArrayList<>();

        // Get data from shared prefs and store to a array list that contains static string arrays
        for (int i = 0; i < sharedPrefsList.size(); i++) {
            String[] valuesList = new String[2];

            try {
                // Get encoded name, decode it and store to index 0 of the valuesList
                valuesList[0] = URLDecoder.decode(
                        sharedPrefsList.get(i).get(0).toString(),
                        "utf-8"
                );

                // Get date and store it at index 1 of the valuesList
                valuesList[1] = sharedPrefsList.get(i).get(1).toString();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            adapterList.add(valuesList);
        }

        return adapterList;
    }
}
