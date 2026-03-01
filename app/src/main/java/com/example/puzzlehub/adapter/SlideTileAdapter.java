package com.example.puzzlehub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.puzzlehub.R;

import java.util.List;

/**
 * SlideTileAdapter - RecyclerView Adapter for the Sliding Number Puzzle.
 *
 * HOW THIS ADAPTER WORKS:
 * - Each tile is an item in the RecyclerView grid
 * - The tile with value 0 is the "empty" space (displayed as blank)
 * - All other tiles show their number and are clickable
 * - When the data changes (after a move), updateTiles() refreshes the display
 *
 * Uses GridLayoutManager (set in the Fragment) to display tiles in a grid.
 */
public class SlideTileAdapter extends RecyclerView.Adapter<SlideTileAdapter.TileViewHolder> {
    private List<Integer> tiles;  // List of tile values (0 = empty)
    private OnTileClickListener listener;

    /** Interface for handling tile click events */
    public interface OnTileClickListener {
        void onTileClick(int position);
    }

    public SlideTileAdapter(List<Integer> tiles) {
        this.tiles = tiles;
    }

    public void setOnTileClickListener(OnTileClickListener listener) {
        this.listener = listener;
    }

    /** Updates the tile data and refreshes the display */
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
            // Empty tile: show blank with transparent background
            holder.tvTile.setText("");
            holder.tvTile.setBackgroundColor(0x00000000);
        } else {
            // Numbered tile: show the number with colored background
            holder.tvTile.setText(String.valueOf(value));
            holder.tvTile.setBackgroundResource(R.drawable.rounded_button);
        }

        // Only numbered tiles (not empty) are clickable
        holder.itemView.setOnClickListener(v -> {
            if (listener != null && value != 0) {
                listener.onTileClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tiles.size();
    }

    /** ViewHolder holds the TextView for each tile */
    static class TileViewHolder extends RecyclerView.ViewHolder {
        TextView tvTile;

        TileViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTile = itemView.findViewById(R.id.tvTile);
        }
    }
}
