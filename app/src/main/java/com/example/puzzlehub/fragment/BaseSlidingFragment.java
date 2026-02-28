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

/**
 * BaseSlidingFragment - Base Fragment for the Sliding Number Puzzle game.
 *
 * FRAGMENT AS REUSABLE UI:
 * This is an abstract base class. Slide3x3Fragment and Slide4x4Fragment extend it.
 * Each subclass only specifies the grid size - all game logic is shared here.
 *
 * RECYCLERVIEW WITH GRIDLAYOUTMANAGER:
 * The puzzle board uses a RecyclerView with GridLayoutManager.
 * Each tile is an item in the grid. Value 0 represents the empty space.
 *
 * HOW WIN LOGIC WORKS (SLIDING PUZZLE):
 * 1. Board starts shuffled by applying random valid moves from the solved state
 *    (this guarantees the puzzle is always solvable)
 * 2. Player taps a tile adjacent to the empty space → tile slides into empty space
 * 3. Board is "solved" when tiles are in order: 1, 2, 3, ..., N, 0 (empty last)
 * 4. Only tiles directly above, below, left, or right of the empty space can move
 * 5. When solved → player wins and navigates to ResultActivity
 */
public abstract class BaseSlidingFragment extends Fragment {
    private SlideTileAdapter adapter;
    private List<Integer> board;    // Board represented as a flat list (0 = empty tile)
    private int gridSize;           // 3 for 3×3, 4 for 4×4
    private int moves = 0;
    private int emptyPos;           // Position of the empty tile (value 0)

    // Subclasses must implement these
    protected abstract int getGridSize();       // 3 or 4
    protected abstract String getDifficulty();  // "3x3" or "4x4"

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

        // Initialize the board in solved state, then shuffle it
        initBoard();
        shuffleBoard();

        // Set up RecyclerView with GridLayoutManager
        adapter = new SlideTileAdapter(board);
        rvBoard.setLayoutManager(new GridLayoutManager(requireContext(), gridSize));
        rvBoard.setAdapter(adapter);

        adapter.setOnTileClickListener(this::onTileClicked);
    }

    /**
     * Initializes the board in the solved state.
     * For a 3×3 board: [1, 2, 3, 4, 5, 6, 7, 8, 0]
     * The 0 represents the empty space (always starts at the last position).
     */
    private void initBoard() {
        board = new ArrayList<>();
        int total = gridSize * gridSize;
        for (int i = 1; i < total; i++) {
            board.add(i);
        }
        board.add(0);  // Empty tile at the end
        emptyPos = total - 1;
    }

    /**
     * Shuffles the board by applying random valid moves from the solved state.
     * This guarantees the puzzle is always solvable (unlike random shuffling
     * which could create an unsolvable configuration).
     */
    private void shuffleBoard() {
        Random random = new Random();
        int shuffleMoves = gridSize == 3 ? 100 : 200;
        for (int i = 0; i < shuffleMoves; i++) {
            // Get all valid neighbors of the empty tile
            List<Integer> neighbors = getNeighbors(emptyPos);
            // Pick a random neighbor and swap it with the empty tile
            int randomNeighbor = neighbors.get(random.nextInt(neighbors.size()));
            Collections.swap(board, emptyPos, randomNeighbor);
            emptyPos = randomNeighbor;
        }
    }

    /**
     * Gets the positions of all tiles adjacent to the given position.
     * A tile is adjacent if it's directly above, below, left, or right.
     */
    private List<Integer> getNeighbors(int pos) {
        List<Integer> neighbors = new ArrayList<>();
        int row = pos / gridSize;
        int col = pos % gridSize;
        if (row > 0) neighbors.add(pos - gridSize);        // Above
        if (row < gridSize - 1) neighbors.add(pos + gridSize);  // Below
        if (col > 0) neighbors.add(pos - 1);                // Left
        if (col < gridSize - 1) neighbors.add(pos + 1);     // Right
        return neighbors;
    }

    /**
     * Called when a tile is tapped.
     * Only moves the tile if it's adjacent to the empty space.
     */
    private void onTileClicked(int position) {
        // Only allow moving tiles adjacent to the empty space
        if (!isAdjacentToEmpty(position)) return;

        // Swap the tapped tile with the empty space
        Collections.swap(board, position, emptyPos);
        emptyPos = position;
        moves++;

        // Update the Activity's move counter and start timer on first move
        if (getActivity() instanceof SlidingPuzzleActivity) {
            ((SlidingPuzzleActivity) getActivity()).updateMoves(moves);
            if (moves == 1) {
                ((SlidingPuzzleActivity) getActivity()).startTimer();
            }
        }

        // Refresh the display
        adapter.updateTiles(board);

        // WIN DETECTION: Check if the puzzle is solved
        if (isSolved()) {
            onGameWon();
        }
    }

    /** Checks if the given position is adjacent to the empty tile */
    private boolean isAdjacentToEmpty(int position) {
        List<Integer> neighbors = getNeighbors(emptyPos);
        return neighbors.contains(position);
    }

    /**
     * Checks if the board is in the solved state.
     * Solved means: [1, 2, 3, ..., N-1, 0] where 0 is the last tile.
     */
    private boolean isSolved() {
        int total = gridSize * gridSize;
        for (int i = 0; i < total - 1; i++) {
            if (board.get(i) != i + 1) return false;
        }
        return board.get(total - 1) == 0;
    }

    /**
     * Called when the puzzle is solved (player wins).
     * Navigates to ResultActivity with game data.
     */
    private void onGameWon() {
        if (getActivity() instanceof SlidingPuzzleActivity) {
            int timeSeconds = ((SlidingPuzzleActivity) getActivity()).stopTimer();
            // EXPLICIT INTENT with EXTRAS: Pass game results to ResultActivity
            Intent intent = new Intent(requireContext(), ResultActivity.class);
            intent.putExtra("GAME_TYPE", "SLIDE");
            intent.putExtra("DIFFICULTY", getDifficulty());
            intent.putExtra("MOVES", moves);
            intent.putExtra("TIME_SECONDS", timeSeconds);
            startActivity(intent);
            requireActivity().finish();
        }
    }

    /** Resets the game board for a new game */
    public void resetGame() {
        moves = 0;
        initBoard();
        shuffleBoard();
        if (adapter != null) {
            adapter.updateTiles(board);
        }
    }
}
