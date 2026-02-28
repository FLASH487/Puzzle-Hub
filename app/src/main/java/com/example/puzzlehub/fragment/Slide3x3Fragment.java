package com.example.puzzlehub.fragment;

public class Slide3x3Fragment extends BaseSlidingFragment {
    public static Slide3x3Fragment newInstance() {
        return new Slide3x3Fragment();
    }

    @Override
    protected int getGridSize() {
        return 3;
    }

    @Override
    protected String getDifficulty() {
        return "3x3";
    }
}
