package com.realgear.sudoku_thebestone.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.view.SettingsView;

public class Settings extends AppCompatActivity {

    private SettingsView mSettingsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.mSettingsView = new SettingsView(this);
    }
}