package com.example.tripsystem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.TaskViewHolder> {

    private ArrayList<TripTask> tasks;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public Adapter(ArrayList<TripTask> tasks) {
        this.tasks = tasks;
    }

    public void updateList(ArrayList<TripTask> newList) {
        this.tasks = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manage_item, parent, false);
        return new TaskViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TripTask current = tasks.get(position);

        holder.tvTitle.setText(current.getTitle());
        holder.tvDate.setText(current.getDate());
        holder.tvType.setText(current.getType());
        holder.tvBudget.setText(current.getBudget() + " JD");

        String priority = current.isImportant() ? "Premium Trip" : "Regular Trip";
        String payment = current.isPaid() ? "Included" : "Pay on Arrival";

        holder.tvStatus.setText(priority + " • " + payment);

        // image in card
        if (current.getCardImageResId() != 0) {
            holder.imgPlace.setImageResource(current.getCardImageResId());
        } else {
            holder.imgPlace.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDate, tvType, tvBudget, tvStatus;
        ImageView imgPlace;

        public TaskViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvType = itemView.findViewById(R.id.tvType);
            tvBudget = itemView.findViewById(R.id.tvBudget);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            imgPlace = itemView.findViewById(R.id.imgPlace);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}