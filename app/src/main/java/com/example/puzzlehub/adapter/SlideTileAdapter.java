package com.example.puzzlehub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.puzzlehub.R;

import java.util.List;

public class SlideTileAdapter extends RecyclerView.Adapter<SlideTileAdapter.TileViewHolder> {
    private List<Integer> tiles;
    private OnTileClickListener listener;

    public interface OnTileClickListener {
        void onTileClick(int position);
    }

    public SlideTileAdapter(List<Integer> tiles) {
        this.tiles = tiles;
    }

    public void setOnTileClickListener(OnTileClickListener listener) {
        this.listener = listener;
    }

    public void updateTiles(List<Integer> tiles) {
        this.tiles = tiles;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slide_tile, parent, false);
        return new TileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TileViewHolder holder, int position) {
        int value = tiles.get(position);
        if (value == 0) {
            holder.tvTile.setText("");
            holder.tvTile.setBackgroundColor(0x00000000);
        } else {
            holder.tvTile.setText(String.valueOf(value));
            holder.tvTile.setBackgroundResource(R.drawable.rounded_button);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null && value != 0) {
                listener.onTileClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tiles.size();
    }

    static class TileViewHolder extends RecyclerView.ViewHolder {
        TextView tvTile;

        TileViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTile = itemView.findViewById(R.id.tvTile);
        }
    }
}
