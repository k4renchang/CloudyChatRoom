package com.example.cloudychatroom.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cloudychatroom.R;
import com.example.cloudychatroom.Components.Message;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context context;
    private List<Message> messages;
    private DatabaseReference reference;

    public Adapter(Context context, List<Message> messages, DatabaseReference reference) {
        this.context = context;
        this.messages = messages;
        this.reference = reference;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Message message = messages.get(i);
        String toSet = message.getMessage() + ": " + message.getName();
        viewHolder.title.setText(toSet);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageButton delete;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.message_item_tv);
            delete = itemView.findViewById(R.id.delete_button);
            linearLayout = itemView.findViewById(R.id.message_item_ll);
            delete.setVisibility(View.VISIBLE);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reference.child(messages.get(getAdapterPosition()).getKey()).removeValue();
                }
            });
        }
    }
}

