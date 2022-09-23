package com.realgear.sudoku_thebestone.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.view.PlayStoreView;

public class PlayStore extends AppCompatActivity {

    private PlayStoreView mActivityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_store);

        this.mActivityView = new PlayStoreView(this);
    }
}