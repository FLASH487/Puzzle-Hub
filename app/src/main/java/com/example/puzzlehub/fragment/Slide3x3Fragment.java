package com.example.puzzlehub.fragment;

/**
 * Slide3x3Fragment - 3×3 Sliding Number Puzzle (8 tiles + 1 empty).
 *
 * Extends BaseSlidingFragment with grid size 3.
 * All puzzle logic (moving tiles, checking solution) is in the base class.
 */
public class Slide3x3Fragment extends BaseSlidingFragment {
    public static Slide3x3Fragment newInstance() {
        return new Slide3x3Fragment();
    }

    @Override
    protected int getGridSize() {
        return 3;  // 3×3 grid
    }

    @Override
    protected String getDifficulty() {
        return "3x3";
    }
}
