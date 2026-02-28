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

/**
 * MemoryCardAdapter - RecyclerView Adapter for the Memory Match Cards game.
 *
 * HOW RECYCLERVIEW ADAPTER WORKS:
 * A RecyclerView.Adapter connects data (the list of cards) to the RecyclerView UI.
 * It has three main methods:
 * 1. onCreateViewHolder() - Creates a new view (card layout) when needed
 * 2. onBindViewHolder() - Fills a view with data (shows card front or back)
 * 3. getItemCount() - Returns how many items are in the list
 *
 * The RecyclerView "recycles" views - when a card scrolls off screen,
 * its view is reused for a new card. This saves memory and is efficient.
 *
 * CARD STATES:
 * Each card can be in one of three states:
 * - Face down (showing card_back drawable)
 * - Flipped (showing card_front drawable)
 * - Matched (showing card_front with green background and reduced opacity)
 */
public class MemoryCardAdapter extends RecyclerView.Adapter<MemoryCardAdapter.CardViewHolder> {
    private final List<Integer> cardFronts;  // List of drawable resource IDs for card fronts
    private final boolean[] flipped;          // Tracks which cards are flipped face up
    private final boolean[] matched;          // Tracks which cards have been matched

    // Listener interface for card click events (callback pattern)
    private OnCardClickListener listener;

    /** Interface for handling card click events */
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

    /** Flips a card face up or face down */
    public void flipCard(int position, boolean show) {
        flipped[position] = show;
        notifyItemChanged(position);  // Tells RecyclerView to redraw this item
    }

    /** Marks a card as matched (it stays face up with green background) */
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

    /** Resets all cards to face down and unmatched */
    public void resetAll() {
        for (int i = 0; i < flipped.length; i++) {
            flipped[i] = false;
            matched[i] = false;
        }
        notifyDataSetChanged();  // Tells RecyclerView to redraw all items
    }

    /**
     * onCreateViewHolder - Called when RecyclerView needs a new view.
     * Inflates the item_memory_card.xml layout and wraps it in a ViewHolder.
     */
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_memory_card, parent, false);
        return new CardViewHolder(view);
    }

    /**
     * onBindViewHolder - Called to display data for a specific position.
     * Shows the correct card state (matched, flipped, or face down).
     */
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        if (matched[position]) {
            // Card is matched: show front with soft green background
            holder.ivCard.setImageResource(cardFronts.get(position));
            holder.cardView.setCardBackgroundColor(0xFFA5D6A7);  // Soft green
            holder.cardView.setAlpha(0.7f);
        } else if (flipped[position]) {
            // Card is flipped: show front with white background
            holder.ivCard.setImageResource(cardFronts.get(position));
            holder.cardView.setCardBackgroundColor(0xFFFFFFFF);
            holder.cardView.setAlpha(1f);
        } else {
            // Card is face down: show card back
            holder.ivCard.setImageResource(R.drawable.card_back);
            holder.cardView.setCardBackgroundColor(0xFFFFFFFF);
            holder.cardView.setAlpha(1f);
        }

        // Only allow clicking on cards that are not flipped and not matched
        holder.itemView.setOnClickListener(v -> {
            if (listener != null && !flipped[position] && !matched[position]) {
                listener.onCardClick(position);
            }
        });
    }

    /** Returns the total number of cards */
    @Override
    public int getItemCount() {
        return cardFronts.size();
    }

    /**
     * ViewHolder - Holds references to views in item_memory_card.xml.
     * This avoids calling findViewById() every time a card is displayed.
     */
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
