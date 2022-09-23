package com.realgear.sudoku_thebestone.core;

import android.content.Context;
import android.util.Log;


import com.realgear.sudoku_thebestone.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoadSudokuGrid {
    public static final String TAG = LoadSudokuGrid.class.getSimpleName();

    private Context mContext;
    private List<int[][]> mPuzzles;

    public LoadSudokuGrid(Context context) {
        this.mContext = context;

        mPuzzles = new ArrayList<>();
        String data = getPuzzles();

        String[] puzzles = data.split("-");

        if(puzzles == null) {
            Log.e(TAG, "Puzzle Not Split -");
            return;
        }

        if (puzzles.length == 0) {
            Log.e(TAG, "Puzzle Not Split -");
            return;
        }

        for(String puzzle : puzzles) {
            int[][] grid = new int[25][25];
            String[] rows = puzzle.split("\\|");

            for(int x = 0; x < rows.length; x++) {
                String[] cols = rows[x].split(",");

                Log.e(TAG, "Row Length : " + cols.length);
                Log.e(TAG, rows[x]);

                for(int y = 0; y < cols.length; y++) {
                    try {
                        int value = Integer.parseInt(cols[y]);
                        //Log.e(TAG, "Position : {" + x + "}{" + y +"}" );
                        grid[x][y] = value;
                    }
                    catch (NumberFormatException e) {
                        //Log.e(TAG, "Error Parsing Val {"+cols[y]+"}");
                        grid[x][y] = 0;

                    }
                }
            }
            mPuzzles.add(grid);
        }

        if(mPuzzles.size() > 0) {
            int[][] grid = mPuzzles.get(0);

            String result = "";
            for(int x = 0; x < 25; x++) {
                for(int y = 0; y < 25; y++) {
                    result += grid[x][y] + ",";
                }
                result += "\n";
            }

            Log.e(TAG, result);
        }

    }

    public String getPuzzles() {
        InputStream is = mContext.getResources().openRawResource(R.raw.puzzle);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        List<String> lines = new ArrayList<>();
        String strLine;
        try {
            while ((strLine = reader.readLine()) != null) {
                lines.add(strLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < lines.size(); i++) {
            if((i + 1) != lines.size()) {
                sb.append(lines.get(i) + "\n");
            }
            else {
                sb.append(lines.get(i));
            }
        }

        return sb.toString();
    }

    public List<int[][]> getPuzzlesArray() {
        return this.mPuzzles;
    }
}
