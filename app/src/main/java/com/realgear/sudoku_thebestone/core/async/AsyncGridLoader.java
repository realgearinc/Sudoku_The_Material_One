package com.realgear.sudoku_thebestone.core.async;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.FrameLayout;

import com.realgear.sudoku_thebestone.core.Cell;
import com.realgear.sudoku_thebestone.view.sudokuboard.CellView;
import com.realgear.sudoku_thebestone.view.sudokuboard.SudokuBoardView;


//480 x 800 hdpi
public class AsyncGridLoader extends AsyncTask<Cell[][], Integer, Object> {

    private final Context mContext;
    private final SudokuBoardView mSudokBoardView;
    private int length;

    public AsyncGridLoader(Context mContext, SudokuBoardView mSudokBoardView) {
        this.mContext = mContext;
        this.mSudokBoardView = mSudokBoardView;
    }

    @Override
    protected String doInBackground(Cell[][]... cells) {
        Cell[][] cells1 = cells[0];
        length = mSudokBoardView.getMaxLimitX() * mSudokBoardView.getMaxLimitY();
        int index = 0;

        for(int x = 0; x < mSudokBoardView.getMaxLimitX(); x++) {
            for(int y = 0; y < mSudokBoardView.getMaxLimitY(); y++) {
                Cell cell = cells1[x][y];
                if(!cell.isActive() && !cell.isExtra()) {
                    FrameLayout v = new FrameLayout(mContext);
                    v.setVisibility(View.INVISIBLE);
                    mSudokBoardView.addView(v);
                }
                else if(!cell.isActive() && cell.isExtra()) {

                    CellView cellView = new CellView(x, y, mContext);
                    cellView.setCellRule(cell.getCellRule());
                    mSudokBoardView.addView(cellView);
                }
                else {
                    CellView cellView = new CellView(
                            cell.getX(),
                            cell.getY(),
                            cell.getValue(),
                            mContext,
                            cell.isFixed());

                    cellView.setCellRule(cell.getCellRule());

                    if(!cell.isFixed()) {
                        cellView.getButton().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mSudokBoardView.onClickCellView(cell.getX(), cell.getY());
                            }
                        });

                        cellView.getButton().setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                mSudokBoardView.onLongClickCellView(cell.getX(), cell.getY());
                                return true;
                            }
                        });

                        cellView.getHintsFrame().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mSudokBoardView.onClickCellView(cell.getX(), cell.getY());
                            }
                        });
                    }
                    else {
                        cellView.getButton().post(new Runnable() {
                            @Override
                            public void run() {
                                cellView.getButton().setEnabled(false);
                            }
                        });
                    }


                    cellView.initHintsLayout(mSudokBoardView.getSize());
                    mSudokBoardView.addView(cellView);
                }

                try {
                    index++;
                    onProgressUpdate(index);
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return "Completed";
    }

    @Override
    protected void onPostExecute(Object o) {
        mSudokBoardView.getGameplayView().onGridLoaded();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int value = values[0];

        int perc = (int)(((float)value / (float)length)  * 100);

        mSudokBoardView.getGameplayView().updateProgressbar(perc, 100);
    }

}
