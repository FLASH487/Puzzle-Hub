package com.example.puzzlehub.fragment;

/**
 * MemoryEasyFragment - Easy difficulty for Memory Match (4×4 grid = 8 pairs).
 *
 * This Fragment extends BaseMemoryFragment and simply specifies:
 * - Grid size: 4 (creates a 4×4 board with 16 cards = 8 pairs)
 * - Difficulty name: "EASY" (sent to ResultActivity via Intent extras)
 *
 * All game logic (flipping, matching, winning) is inherited from BaseMemoryFragment.
 * This shows how Fragments can be reused - same logic, different configuration.
 */
public class MemoryEasyFragment extends BaseMemoryFragment {
    /** Factory method to create a new instance of this Fragment */
    public static MemoryEasyFragment newInstance() {
        return new MemoryEasyFragment();
    }

    @Override
    protected int getGridSize() {
        return 4;  // 4×4 grid
    }

    @Override
    protected String getDifficulty() {
        return "EASY";
    }
}
