package com.example.puzzlehub.fragment;

import android.os.Bundle;

public class MemoryEasyFragment extends BaseMemoryFragment {
    public static MemoryEasyFragment newInstance() {
        return new MemoryEasyFragment();
    }

    @Override
    protected int getGridSize() {
        return 4;
    }

    @Override
    protected String getDifficulty() {
        return "EASY";
    }
}
