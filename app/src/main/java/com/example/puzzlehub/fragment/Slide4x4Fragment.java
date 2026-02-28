package com.example.puzzlehub.fragment;

/**
 * Slide4x4Fragment - 4×4 Sliding Number Puzzle (15 tiles + 1 empty).
 *
 * Extends BaseSlidingFragment with grid size 4.
 * More challenging than 3×3 due to more tiles.
 */
public class Slide4x4Fragment extends BaseSlidingFragment {
    public static Slide4x4Fragment newInstance() {
        return new Slide4x4Fragment();
    }

    @Override
    protected int getGridSize() {
        return 4;  // 4×4 grid
    }

    @Override
    protected String getDifficulty() {
        return "4x4";
    }
}
