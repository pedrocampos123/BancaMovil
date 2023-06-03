package com.example.bancamovil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bancamovil.R;

import java.util.List;

public class ShortsAdapter extends RecyclerView.Adapter<ShortsAdapter.ShortViewHolder> {

    private List<ShortItem> shortItemList;

    public ShortsAdapter(List<ShortItem> shortItemList) {
        this.shortItemList = shortItemList;
    }

    @NonNull
    @Override
    public ShortViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_short, parent, false);
        return new ShortViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortViewHolder holder, int position) {
        ShortItem shortItem = shortItemList.get(position);
        holder.titleTextView.setText(shortItem.getTitle());
        holder.descriptionTextView.setText(shortItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return shortItemList.size();
    }

    static class ShortViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;

        ShortViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
