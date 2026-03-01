package com.example.puzzlehub.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.puzzlehub.MemoryMatchActivity;
import com.example.puzzlehub.R;
import com.example.puzzlehub.ResultActivity;
import com.example.puzzlehub.adapter.MemoryCardAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BaseMemoryFragment - Base Fragment for the Memory Match Cards game.
 *
 * WHAT IS A FRAGMENT?
 * A Fragment is a reusable piece of UI that lives inside an Activity.
 * Think of it as a "mini-Activity" that can be swapped in and out.
 * This Fragment handles the game board - the Activity handles the timer and moves display.
 *
 * FRAGMENT LIFECYCLE:
 * - onCreateView(): Inflates (loads) the XML layout for this fragment
 * - onViewCreated(): Called after the view is ready, sets up the RecyclerView
 *
 * FRAGMENT AS REUSABLE UI:
 * This is an abstract class - MemoryEasyFragment and MemoryHardFragment extend it.
 * Each subclass only needs to specify the grid size and difficulty name.
 * The game logic (flipping, matching, winning) is shared in this base class.
 *
 * RECYCLERVIEW WITH GRIDLAYOUTMANAGER:
 * The game board uses a RecyclerView with GridLayoutManager to display cards in a grid.
 * GridLayoutManager arranges items in rows and columns (4×4 or 6×6).
 *
 * HOW WIN LOGIC WORKS (MEMORY MATCH):
 * 1. Player taps a card → it flips face up
 * 2. Player taps a second card → it also flips face up
 * 3. If both cards have the same image → they are "matched" and stay face up
 * 4. If they don't match → both flip back face down after 1 second delay
 * 5. Input is locked during the 1-second comparison delay
 * 6. When all pairs are matched → player wins and goes to ResultActivity
 */
public abstract class BaseMemoryFragment extends Fragment {
    private MemoryCardAdapter adapter;
    private int firstCard = -1;      // Position of first flipped card (-1 = none selected)
    private int secondCard = -1;     // Position of second flipped card
    private boolean isComparing = false;  // Lock input during comparison
    private int moves = 0;           // Move counter
    private int matchedPairs = 0;    // Number of pairs found
    private int totalPairs;          // Total pairs needed to win
    private final Handler handler = new Handler(Looper.getMainLooper());

    // Subclasses must implement these to define difficulty
    protected abstract int getGridSize();       // 4 for Easy, 6 for Hard
    protected abstract String getDifficulty();  // "EASY" or "HARD"

    // Array of drawable resources for card front images
    private static final int[] CARD_FRONTS = {
            R.drawable.card_front_1, R.drawable.card_front_2, R.drawable.card_front_3,
            R.drawable.card_front_4, R.drawable.card_front_5, R.drawable.card_front_6,
            R.drawable.card_front_7, R.drawable.card_front_8, R.drawable.card_front_9,
            R.drawable.card_front_10, R.drawable.card_front_11, R.drawable.card_front_12,
            R.drawable.card_front_13, R.drawable.card_front_14, R.drawable.card_front_15,
            R.drawable.card_front_16, R.drawable.card_front_17, R.drawable.card_front_18
    };

    /**
     * FRAGMENT LIFECYCLE - onCreateView:
     * Called when the Fragment needs to create its UI.
     * Inflates the XML layout (fragment_memory_board.xml) and returns the root view.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_memory_board, container, false);
    }

    /**
     * FRAGMENT LIFECYCLE - onViewCreated:
     * Called after the view is created. Sets up the RecyclerView and game logic.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvBoard = view.findViewById(R.id.rvBoard);
        int gridSize = getGridSize();
        totalPairs = (gridSize * gridSize) / 2;

        // Create pairs of cards (each card appears twice)
        List<Integer> cards = new ArrayList<>();
        for (int i = 0; i < totalPairs; i++) {
            cards.add(CARD_FRONTS[i]);
            cards.add(CARD_FRONTS[i]);
        }
        // Shuffle the cards randomly
        Collections.shuffle(cards);

        // Set up the RecyclerView with a GridLayoutManager
        // GridLayoutManager(context, spanCount) creates a grid with 'spanCount' columns
        adapter = new MemoryCardAdapter(cards);
        rvBoard.setLayoutManager(new GridLayoutManager(requireContext(), gridSize));
        rvBoard.setAdapter(adapter);

        // Set the click listener for card taps
        adapter.setOnCardClickListener(this::onCardClicked);
    }

    /**
     * Called when a card is tapped.
     * Handles the flip logic and matching comparison.
     */
    private void onCardClicked(int position) {
        // Ignore clicks while comparing two cards
        if (isComparing) return;
        // Ignore clicks on already flipped or matched cards
        if (adapter.isFlipped(position) || adapter.isMatched(position)) return;

        // Flip the card face up
        adapter.flipCard(position, true);

        if (firstCard == -1) {
            // First card selected - remember its position
            firstCard = position;
        } else {
            // Second card selected - compare the pair
            secondCard = position;
            moves++;

            // Update the Activity's move counter and start timer on first move
            if (getActivity() instanceof MemoryMatchActivity) {
                ((MemoryMatchActivity) getActivity()).updateMoves(moves);
                if (moves == 1) {
                    ((MemoryMatchActivity) getActivity()).startTimer();
                }
            }

            // Lock input during comparison
            isComparing = true;

            if (adapter.getCardFront(firstCard) == adapter.getCardFront(secondCard)) {
                // MATCH FOUND: Mark both cards as matched
                adapter.setMatched(firstCard);
                adapter.setMatched(secondCard);
                matchedPairs++;
                resetSelection();
                isComparing = false;

                // WIN DETECTION: Check if all pairs have been found
                if (matchedPairs == totalPairs) {
                    onGameWon();
                }
            } else {
                // NO MATCH: Flip both cards back after 1 second delay
                // Handler.postDelayed() runs the code after the specified delay
                handler.postDelayed(() -> {
                    adapter.flipCard(firstCard, false);
                    adapter.flipCard(secondCard, false);
                    resetSelection();
                    isComparing = false;
                }, 1000);
            }
        }
    }

    /** Resets the first/second card selection */
    private void resetSelection() {
        firstCard = -1;
        secondCard = -1;
    }

    /**
     * Called when all pairs are matched (player wins).
     * Navigates to ResultActivity with game data via Intent extras.
     */
    private void onGameWon() {
        if (getActivity() instanceof MemoryMatchActivity) {
            int timeSeconds = ((MemoryMatchActivity) getActivity()).stopTimer();
            // EXPLICIT INTENT with EXTRAS: Pass game results to ResultActivity
            Intent intent = new Intent(requireContext(), ResultActivity.class);
            intent.putExtra("GAME_TYPE", "MEMORY");
            intent.putExtra("DIFFICULTY", getDifficulty());
            intent.putExtra("MOVES", moves);
            intent.putExtra("TIME_SECONDS", timeSeconds);
            startActivity(intent);
            requireActivity().finish();
        }
    }

    /** Resets the game board for a new game */
    public void resetGame() {
        if (adapter != null) {
            // Cancel any pending flip-back callbacks to avoid crashes after reset
            handler.removeCallbacksAndMessages(null);
            firstCard = -1;
            secondCard = -1;
            isComparing = false;
            moves = 0;
            matchedPairs = 0;

            // Create a fresh shuffled deck of cards
            RecyclerView rvBoard = requireView().findViewById(R.id.rvBoard);
            int gridSize = getGridSize();
            totalPairs = (gridSize * gridSize) / 2;

            List<Integer> cards = new ArrayList<>();
            for (int i = 0; i < totalPairs; i++) {
                cards.add(CARD_FRONTS[i]);
                cards.add(CARD_FRONTS[i]);
            }
            Collections.shuffle(cards);

            adapter = new MemoryCardAdapter(cards);
            rvBoard.setAdapter(adapter);
            adapter.setOnCardClickListener(this::onCardClicked);
        }
    }
}
