package com.example.puzzlehub.fragment;

public class Slide4x4Fragment extends BaseSlidingFragment {
    public static Slide4x4Fragment newInstance() {
        return new Slide4x4Fragment();
    }

    @Override
    protected int getGridSize() {
        return 4;
    }

    @Override
    protected String getDifficulty() {
        return "4x4";
    }
}
