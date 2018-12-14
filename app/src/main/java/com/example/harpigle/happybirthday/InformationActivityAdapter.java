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
        viewHolder.name.setText(information.get(i)[0]);
        if (i == 0)
            viewHolder.divider.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return information.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View divider;
        private TextView name;
        private ImageView etc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            divider = itemView.findViewById(R.id.divider_item);
            name = itemView.findViewById(R.id.person_name_item);
            etc = itemView.findViewById(R.id.etc_item);

            etc.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String[] item = information.get(getAdapterPosition());
            listener.onClickListener(etc, item[0], item[1], item[2], item[3]);
        }
    }
}