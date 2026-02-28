package com.example.puzzlehub.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.puzzlehub.R;
import com.example.puzzlehub.ResultActivity;
import com.example.puzzlehub.SlidingPuzzleActivity;
import com.example.puzzlehub.adapter.SlideTileAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class BaseSlidingFragment extends Fragment {
    private SlideTileAdapter adapter;
    private List<Integer> board;
    private int gridSize;
    private int moves = 0;
    private int emptyPos;

    protected abstract int getGridSize();
    protected abstract String getDifficulty();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sliding_board, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridSize = getGridSize();
        RecyclerView rvBoard = view.findViewById(R.id.rvBoard);

        initBoard();
        shuffleBoard();

        adapter = new SlideTileAdapter(board);
        rvBoard.setLayoutManager(new GridLayoutManager(requireContext(), gridSize));
        rvBoard.setAdapter(adapter);

        adapter.setOnTileClickListener(this::onTileClicked);
    }

    private void initBoard() {
        board = new ArrayList<>();
        int total = gridSize * gridSize;
        for (int i = 1; i < total; i++) {
            board.add(i);
        }
        board.add(0);
        emptyPos = total - 1;
    }

    private void shuffleBoard() {
        Random random = new Random();
        int shuffleMoves = gridSize == 3 ? 100 : 200;
        for (int i = 0; i < shuffleMoves; i++) {
            List<Integer> neighbors = getNeighbors(emptyPos);
            int randomNeighbor = neighbors.get(random.nextInt(neighbors.size()));
            Collections.swap(board, emptyPos, randomNeighbor);
            emptyPos = randomNeighbor;
        }
    }

    private List<Integer> getNeighbors(int pos) {
        List<Integer> neighbors = new ArrayList<>();
        int row = pos / gridSize;
        int col = pos % gridSize;
        if (row > 0) neighbors.add(pos - gridSize);
        if (row < gridSize - 1) neighbors.add(pos + gridSize);
        if (col > 0) neighbors.add(pos - 1);
        if (col < gridSize - 1) neighbors.add(pos + 1);
        return neighbors;
    }

    private void onTileClicked(int position) {
        if (!isAdjacentToEmpty(position)) return;

        Collections.swap(board, position, emptyPos);
        emptyPos = position;
        moves++;

        if (getActivity() instanceof SlidingPuzzleActivity) {
            ((SlidingPuzzleActivity) getActivity()).updateMoves(moves);
            if (moves == 1) {
                ((SlidingPuzzleActivity) getActivity()).startTimer();
            }
        }

        adapter.updateTiles(board);

        if (isSolved()) {
            onGameWon();
        }
    }

    private boolean isAdjacentToEmpty(int position) {
        List<Integer> neighbors = getNeighbors(emptyPos);
        return neighbors.contains(position);
    }

    private boolean isSolved() {
        int total = gridSize * gridSize;
        for (int i = 0; i < total - 1; i++) {
            if (board.get(i) != i + 1) return false;
        }
        return board.get(total - 1) == 0;
    }

    private void onGameWon() {
        if (getActivity() instanceof SlidingPuzzleActivity) {
            int timeSeconds = ((SlidingPuzzleActivity) getActivity()).stopTimer();
            Intent intent = new Intent(requireContext(), ResultActivity.class);
            intent.putExtra("GAME_TYPE", "SLIDE");
            intent.putExtra("DIFFICULTY", getDifficulty());
            intent.putExtra("MOVES", moves);
            intent.putExtra("TIME_SECONDS", timeSeconds);
            startActivity(intent);
            requireActivity().finish();
        }
    }

    public void resetGame() {
        moves = 0;
        initBoard();
        shuffleBoard();
        if (adapter != null) {
            adapter.updateTiles(board);
        }
    }
}
