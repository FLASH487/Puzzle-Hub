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

public abstract class BaseMemoryFragment extends Fragment {
    private MemoryCardAdapter adapter;
    private int firstCard = -1;
    private int secondCard = -1;
    private boolean isComparing = false;
    private int moves = 0;
    private int matchedPairs = 0;
    private int totalPairs;
    private final Handler handler = new Handler(Looper.getMainLooper());

    protected abstract int getGridSize();
    protected abstract String getDifficulty();

    private static final int[] CARD_FRONTS = {
            R.drawable.card_front_1, R.drawable.card_front_2, R.drawable.card_front_3,
            R.drawable.card_front_4, R.drawable.card_front_5, R.drawable.card_front_6,
            R.drawable.card_front_7, R.drawable.card_front_8, R.drawable.card_front_9,
            R.drawable.card_front_10, R.drawable.card_front_11, R.drawable.card_front_12,
            R.drawable.card_front_13, R.drawable.card_front_14, R.drawable.card_front_15,
            R.drawable.card_front_16, R.drawable.card_front_17, R.drawable.card_front_18
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_memory_board, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvBoard = view.findViewById(R.id.rvBoard);
        int gridSize = getGridSize();
        totalPairs = (gridSize * gridSize) / 2;

        List<Integer> cards = new ArrayList<>();
        for (int i = 0; i < totalPairs; i++) {
            cards.add(CARD_FRONTS[i]);
            cards.add(CARD_FRONTS[i]);
        }
        Collections.shuffle(cards);

        adapter = new MemoryCardAdapter(cards);
        rvBoard.setLayoutManager(new GridLayoutManager(requireContext(), gridSize));
        rvBoard.setAdapter(adapter);

        adapter.setOnCardClickListener(this::onCardClicked);
    }

    private void onCardClicked(int position) {
        if (isComparing) return;
        if (adapter.isFlipped(position) || adapter.isMatched(position)) return;

        adapter.flipCard(position, true);

        if (firstCard == -1) {
            firstCard = position;
        } else {
            secondCard = position;
            moves++;
            if (getActivity() instanceof MemoryMatchActivity) {
                ((MemoryMatchActivity) getActivity()).updateMoves(moves);
                if (moves == 1) {
                    ((MemoryMatchActivity) getActivity()).startTimer();
                }
            }

            isComparing = true;
            if (adapter.getCardFront(firstCard) == adapter.getCardFront(secondCard)) {
                adapter.setMatched(firstCard);
                adapter.setMatched(secondCard);
                matchedPairs++;
                resetSelection();
                isComparing = false;

                if (matchedPairs == totalPairs) {
                    onGameWon();
                }
            } else {
                handler.postDelayed(() -> {
                    adapter.flipCard(firstCard, false);
                    adapter.flipCard(secondCard, false);
                    resetSelection();
                    isComparing = false;
                }, 1000);
            }
        }
    }

    private void resetSelection() {
        firstCard = -1;
        secondCard = -1;
    }

    private void onGameWon() {
        if (getActivity() instanceof MemoryMatchActivity) {
            int timeSeconds = ((MemoryMatchActivity) getActivity()).stopTimer();
            Intent intent = new Intent(requireContext(), ResultActivity.class);
            intent.putExtra("GAME_TYPE", "MEMORY");
            intent.putExtra("DIFFICULTY", getDifficulty());
            intent.putExtra("MOVES", moves);
            intent.putExtra("TIME_SECONDS", timeSeconds);
            startActivity(intent);
            requireActivity().finish();
        }
    }

    public void resetGame() {
        if (adapter != null) {
            firstCard = -1;
            secondCard = -1;
            isComparing = false;
            moves = 0;
            matchedPairs = 0;

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
