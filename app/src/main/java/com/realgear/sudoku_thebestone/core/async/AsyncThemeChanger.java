package com.realgear.sudoku_thebestone.core.async;

import android.os.AsyncTask;

import com.realgear.sudoku_thebestone.theme.Theme;
import com.realgear.sudoku_thebestone.view.sudokuboard.CellView;

public class AsyncThemeChanger extends AsyncTask<Theme, Integer, Boolean> {

    private final CellView[][] mCells;

    public AsyncThemeChanger(CellView[][] cellViews) {
        this.mCells = cellViews;
    }

    @Override
    protected Boolean doInBackground(Theme... themes) {
        Theme theme = themes[0];

        if (theme  == null) return false;
        if (mCells == null) return false;

        int x = mCells.length;
        int y = mCells[0].length;

        int count = x * y;
        int index = 0;

        for(CellView[] c : mCells) {
            for(CellView cell : c) {
                if(cell == null)
                    continue;

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        cell.setTheme(theme);
                    }
                });
                t.start();

                index++;

                try {
                    onProgressUpdate(index);
                    Thread.sleep(100);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }
}
