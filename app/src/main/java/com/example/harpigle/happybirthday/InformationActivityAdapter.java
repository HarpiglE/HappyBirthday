package com.example.harpigle.happybirthday;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class InformationActivityAdapter
        extends RecyclerView.Adapter<InformationActivityAdapter.ViewHolder> {

    private ArrayList<String[]> information = new ArrayList<>();
    private InformationActivityItemClickListener listener;

    public InformationActivityAdapter(ArrayList<String[]> information,
                                      InformationActivityItemClickListener listener) {
        this.information = information;
        this.listener = listener;
    }

    @NonNull
    @Override
    public InformationActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_information_activity, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.counter.setText(String.valueOf(i + 1));
        viewHolder.name.setText(information.get(i)[0]);
        viewHolder.date.setText(information.get(i)[1]);
        viewHolder.time.setText(information.get(i)[2]);
    }

    @Override
    public int getItemCount() {
        return information.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView counter;
        private TextView name;
        private TextView date;
        private TextView time;

        private ImageView delete;
        private ImageView edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            counter = itemView.findViewById(R.id.number_information);
            name = itemView.findViewById(R.id.name_information);
            date = itemView.findViewById(R.id.date_information);
            time = itemView.findViewById(R.id.time_information);
            delete = itemView.findViewById(R.id.delete_information);
            edit = itemView.findViewById(R.id.edit_information);

            delete.setOnClickListener(this);
            edit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String[] item = information.get(getAdapterPosition());
            if (v.toString().contains("delete_information"))
                listener.onClickListener(item[0], null, null);
            else
                listener.onClickListener(item[0], item[1], item[2]);
        }
    }
}