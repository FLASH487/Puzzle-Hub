package com.example.puzzlehub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.puzzlehub.R;

import java.util.List;

public class MemoryCardAdapter extends RecyclerView.Adapter<MemoryCardAdapter.CardViewHolder> {
    private final List<Integer> cardFronts;
    private final boolean[] flipped;
    private final boolean[] matched;
    private OnCardClickListener listener;

    public interface OnCardClickListener {
        void onCardClick(int position);
    }

    public MemoryCardAdapter(List<Integer> cardFronts) {
        this.cardFronts = cardFronts;
        this.flipped = new boolean[cardFronts.size()];
        this.matched = new boolean[cardFronts.size()];
    }

    public void setOnCardClickListener(OnCardClickListener listener) {
        this.listener = listener;
    }

    public void flipCard(int position, boolean show) {
        flipped[position] = show;
        notifyItemChanged(position);
    }

    public void setMatched(int position) {
        matched[position] = true;
        notifyItemChanged(position);
    }

    public boolean isFlipped(int position) {
        return flipped[position];
    }

    public boolean isMatched(int position) {
        return matched[position];
    }

    public int getCardFront(int position) {
        return cardFronts.get(position);
    }

    public void resetAll() {
        for (int i = 0; i < flipped.length; i++) {
            flipped[i] = false;
            matched[i] = false;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_memory_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        if (matched[position]) {
            holder.ivCard.setImageResource(cardFronts.get(position));
            holder.cardView.setCardBackgroundColor(0xFF66BB6A);
            holder.cardView.setAlpha(0.7f);
        } else if (flipped[position]) {
            holder.ivCard.setImageResource(cardFronts.get(position));
            holder.cardView.setCardBackgroundColor(0xFFFFFFFF);
            holder.cardView.setAlpha(1f);
        } else {
            holder.ivCard.setImageResource(R.drawable.card_back);
            holder.cardView.setCardBackgroundColor(0xFFFFFFFF);
            holder.cardView.setAlpha(1f);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null && !flipped[position] && !matched[position]) {
                listener.onCardClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardFronts.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCard;
        CardView cardView;

        CardViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCard = itemView.findViewById(R.id.ivCard);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
