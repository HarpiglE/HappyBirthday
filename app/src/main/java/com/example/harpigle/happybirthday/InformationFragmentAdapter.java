package com.example.harpigle.happybirthday;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class InformationFragmentAdapter
        extends RecyclerView.Adapter<InformationFragmentAdapter.ViewHolder> {

    private ArrayList<String[]> information = new ArrayList<>();

    public InformationFragmentAdapter(ArrayList<String[]> information) {
        this.information = information;
    }

    @NonNull
    @Override
    public InformationFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_information, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.counter.setText(String.valueOf(i + 1));
        viewHolder.name.setText(information.get(i)[0]);
        viewHolder.date.setText(information.get(i)[1]);
    }

    @Override
    public int getItemCount() {
        return information.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView counter;
        private TextView name;
        private TextView date;
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            counter = itemView.findViewById(R.id.number_fragment);
            name = itemView.findViewById(R.id.name_fragment);
            date = itemView.findViewById(R.id.date_fragment);
            time = itemView.findViewById(R.id.time_fragment);
        }
    }
}
