package com.example.harpigle.happybirthday.Messages;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harpigle.happybirthday.R;

import java.util.ArrayList;

public class MessagesListAdapter extends RecyclerView.Adapter<MessagesListAdapter.ViewHolder> {

    private ArrayList<String> messages;

    private int mExpandedPosition = -1;

    public interface MessagesListItemClickListener {
        void onClickListener(@NonNull View view, String message);
    }

    private MessagesListItemClickListener listener;

    public MessagesListAdapter(ArrayList<String> messages, MessagesListItemClickListener listener) {
        this.listener = listener;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_messages_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.counter.setText(String.valueOf(i + 1));
        viewHolder.message.setText(messages.get(i));

        // Implement expand and collapse animation
        final boolean isExpanded = i == mExpandedPosition;
        if (isExpanded) {
            viewHolder.wholeMessage.setVisibility(View.VISIBLE);
            viewHolder.wholeMessage.setText(messages.get(i));
        } else
            viewHolder.wholeMessage.setVisibility(View.GONE);
        viewHolder.expandBtn.setVisibility(isExpanded ? View.INVISIBLE : View.VISIBLE);
        viewHolder.collapseBtn.setVisibility(isExpanded ? View.VISIBLE : View.INVISIBLE);
        viewHolder.message.setActivated(isExpanded);
        viewHolder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : i;
                notifyItemChanged(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView counter;
        private TextView message;
        private ImageView expandBtn;
        private ImageView collapseBtn;
        private ImageView etcBtn;
        private TextView wholeMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            counter = itemView.findViewById(R.id.message_count_messages_list);
            message = itemView.findViewById(R.id.messages_content_messages_list);
            expandBtn = itemView.findViewById(R.id.expand_message_messages_list);
            collapseBtn = itemView.findViewById(R.id.collapse_message_messages_list);
            etcBtn = itemView.findViewById(R.id.etc_messages_list);
            wholeMessage = itemView.findViewById(R.id.whole_message_messages_list);

            etcBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClickListener(etcBtn, messages.get(getAdapterPosition()));
        }
    }
}
