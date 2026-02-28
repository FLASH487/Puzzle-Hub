package com.example.puzzlehub.fragment;

/**
 * MemoryHardFragment - Hard difficulty for Memory Match (6×6 grid = 18 pairs).
 *
 * This Fragment extends BaseMemoryFragment and specifies:
 * - Grid size: 6 (creates a 6×6 board with 36 cards = 18 pairs)
 * - Difficulty name: "HARD"
 *
 * Same logic as MemoryEasyFragment but with a larger board.
 */
public class MemoryHardFragment extends BaseMemoryFragment {
    public static MemoryHardFragment newInstance() {
        return new MemoryHardFragment();
    }

    @Override
    protected int getGridSize() {
        return 6;  // 6×6 grid
    }

    @Override
    protected String getDifficulty() {
        return "HARD";
    }
}
