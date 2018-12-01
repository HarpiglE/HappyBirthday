package com.example.harpigle.happybirthday;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
            String[] valuesList = new String[3];

            try {
                /* Get encoded name, date and time respectively;
                   decode them and store in the valuesList
                 */
                valuesList[0] = decodeString(sharedPrefsList.get(i).get(0).toString());
                valuesList[1] = decodeString(sharedPrefsList.get(i).get(1).toString());
                valuesList[2] = decodeString(sharedPrefsList.get(i).get(2).toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapterList.add(valuesList);
        }

        return adapterList;
    }

    String decodeString(String string) {
        String decodedString = "";
        try {
            decodedString = URLDecoder.decode(string, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodedString;
    }
}
