package com.example.puzzlehub.fragment;

public class MemoryHardFragment extends BaseMemoryFragment {
    public static MemoryHardFragment newInstance() {
        return new MemoryHardFragment();
    }

    @Override
    protected int getGridSize() {
        return 6;
    }

    @Override
    protected String getDifficulty() {
        return "HARD";
    }
}
