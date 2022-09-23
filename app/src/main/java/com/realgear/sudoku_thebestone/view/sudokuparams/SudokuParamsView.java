package com.realgear.sudoku_thebestone.view.sudokuparams;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.activities.Gameplay;
import com.realgear.sudoku_thebestone.core.ActionState;
import com.realgear.sudoku_thebestone.core.Sudoku;
import com.realgear.sudoku_thebestone.core.enums.HintsCreator;
import com.realgear.sudoku_thebestone.data.SettingsData;
import com.realgear.sudoku_thebestone.theme.Theme;
import com.realgear.sudoku_thebestone.theme.Themeable;
import com.realgear.sudoku_thebestone.utils.Action;
import com.realgear.sudoku_thebestone.view.ConfirmLayoutView;
import com.realgear.sudoku_thebestone.view.sudokuboard.SudokuBoardView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class SudokuParamsView extends Themeable {
    private static final String TAG = SudokuParamsView.class.getSimpleName();

    public Theme mTheme;

    //UI - Properties
    private final Sudoku            mSudoku;
    private final SudokuBoardView   mSudokuBoardView;
    private final Gameplay          mActivity;

    private final SettingsData mSettings;

    private final TreeMap<ParamActionEnum,ParamActionView> mParamsAction;
    private final TreeMap<Integer, ParamView> mParamViews;
    private final TreeMap<Integer, String> mConverter;

    private ActionState mCurrentActionState;

    public SudokuParamsView(SudokuBoardView sudokuBoardView, Sudoku sudoku, Gameplay mainActivity) {
        this.mSudoku            = sudoku;
        this.mSudokuBoardView   = sudokuBoardView;
        this.mActivity          = mainActivity;
        this.mSettings          = new SettingsData(mActivity);
        this.mParamViews        = new TreeMap<>();
        this.mParamsAction      = new TreeMap<>();
        this.mConverter         = getConverter();

        initParamsLayout();
        initParamsAction();
        initParamActionLayout();
        initParamActionOnClicks();

        mParamsAction.get(ParamActionEnum.HINTS).setVisibility(mSettings.getShowHint() ? View.VISIBLE : View.GONE);
        //mSettings.get
        //mParamsAction.get(mSettings.get)
    }

    private TreeMap<Integer, String> getConverter() {
        TreeMap<Integer, String> result = new TreeMap<>();

        result.put(10, "A");
        result.put(11, "B");
        result.put(12, "C");
        result.put(13, "D");
        result.put(14, "E");
        result.put(15, "F");
        result.put(16, "G");
        result.put(17, "H");
        result.put(18, "I");
        result.put(19, "J");
        result.put(20, "K");
        result.put(21, "L");
        result.put(22, "M");
        result.put(23, "N");
        result.put(24, "O");
        result.put(25, "P");

        return result;
    }

    private void initParamActionOnClicks() {
        addListener(mParamsAction.get(ParamActionEnum.UNDO).getButton(), () -> onClickUndo());
        addListener(mParamsAction.get(ParamActionEnum.ERASE).getButton(), () -> onClickErase());
        addListener(mParamsAction.get(ParamActionEnum.RESTART).getButton(), () -> onClickRestart());
        addListener(mParamsAction.get(ParamActionEnum.HINTS).getButton(), () -> onClickHints());
        addListener(mParamsAction.get(ParamActionEnum.CHECK).getButton(), () -> onClickCheck());
    }

    private void onClickCheck() {
        LayoutInflater inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View parent = mActivity.getWindow().getDecorView().getRootView();

        Action action = new Action() {
            @Override
            public void call() throws Exception {
                mActivity.mSudokuData.getBoardData().setChecksUsed();
                mSudokuBoardView.validateBoard();
            }
        };

        ConfirmLayoutView confirmLayoutView = new ConfirmLayoutView(
                inflater,
                parent,
                action,
                "Check",
                getTheme()
        );

        TextView info = new TextView(mActivity.getApplicationContext());
        info.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        info.setText("Are you sure you want to start a check your values ?");
        info.setTextSize(18);
        confirmLayoutView.addText(info);

        confirmLayoutView.show();
    }

    private void onClickHints() {
        if(mParamsAction.get(ParamActionEnum.HINTS).isChecked()) {
            mSudokuBoardView.setHints(HintsCreator.ADD);
            mActivity.mSudokuData.getBoardData().setHintsUsed();
        }
        else {
            mSudokuBoardView.setHints(HintsCreator.REMOVE);
        }
    }

    private void onClickRestart() {
        LayoutInflater inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View parent = mActivity.getWindow().getDecorView().getRootView();

        Action action = new Action() {
            @Override
            public void call() throws Exception {
                Log.e("CLV", "Yes Restart This Level");
                mActivity.mSudokuData.getBoardData().onClear();
            }
        };

        ConfirmLayoutView confirmLayoutView = new ConfirmLayoutView(
                inflater,
                parent,
                action,
                "Restart",
                getTheme()
        );

        TextView info = new TextView(mActivity.getApplicationContext());
        info.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        info.setText("Are you sure you want to restart this level ?");
        info.setTextSize(18);
        confirmLayoutView.addText(info);

        confirmLayoutView.show();
    }

    private void onClickErase() {
        if(mParamsAction.get(ParamActionEnum.ERASE).isChecked()) {

            mCurrentActionState = ActionState.REMOVE;
            mSudokuBoardView.setActionState(mCurrentActionState, 0);

            return;
        }
        else {
            for (ParamView paramView : mParamViews.values()) {
                if(paramView.isSelected()) {
                    mCurrentActionState = ActionState.ADD_HIGHLIGHT;
                    mSudokuBoardView.setActionState(mCurrentActionState, paramView.getValue());
                    return;
                }
            }

            mCurrentActionState = ActionState.ADD;
            mSudokuBoardView.setActionState(mCurrentActionState, 0);
        }
    }

    private void unselect(ParamActionEnum actionEnum) {
        ParamActionView paramView = mParamsAction.get(actionEnum);

        if(paramView.isChecked()) {
            paramView.unCheck();
        }
    }

    private void onClickUndo() {
        mSudokuBoardView.Undo();
    }

    private void initParamActionLayout() {
        int gridSize = mSudoku.getBoards().get(0).getSquareSize();
        LinearLayout paramsActionLayout = (LinearLayout)findViewById(R.id.layout_params_action);
        if(gridSize >= 25) {
            for(ParamActionView param : mParamsAction.values()) {
                param.setState(ParamEnum.ICON_ONLY);
            }
            int width = (int) (mActivity.getResources().getDimension(R.dimen.action_btn_width));

            ScrollView scrollView = (ScrollView) paramsActionLayout.getParent();
            if(scrollView != null) {
                scrollView.getLayoutParams().width = width;
                scrollView.requestLayout();
            }

            paramsActionLayout.getLayoutParams().width = width;
            paramsActionLayout.requestLayout();
        }
        else {
            for(ParamActionView param : mParamsAction.values()) {
                param.setState(ParamEnum.TEXT_ONLY);
            }
            int width = (int) (mActivity.getResources().getDimension(R.dimen.action_btn_extended_width));

            ScrollView scrollView = (ScrollView) paramsActionLayout.getParent();
            if(scrollView != null) {
                scrollView.getLayoutParams().width = width;
                scrollView.requestLayout();
            }

            paramsActionLayout.getLayoutParams().width = width;
            paramsActionLayout.requestLayout();
        }
    }

    private void initParamsLayout(){

        boolean showCount = mSettings.getHighlightRemaining();

        int params = mSudoku.getBoards().get(0).getSquareSize();
        int cIndex = 0;
        List<Integer> toRemove = new ArrayList<>();
        for(int i = 0; i < 25; i++) {
            int index = i + 1;
            cIndex++;
            if(cIndex > 25) {
                cIndex = index;
            }

            if(params <= 16) {
                switch (index) {
                    case 5:
                        cIndex = 6;
                        toRemove.add(5);
                        break;
                    case 9:
                        cIndex = 11;
                        toRemove.add(10);
                        break;
                    case 13:
                        cIndex = 16;
                        toRemove.add(15);
                }
            }

            String buttonID             = ("btn_number_" + cIndex);
            String textViewID           = ("btn_number_" + cIndex + "_count");
            String frameID              = ("frame_" + cIndex);
            String valueTextViewID      = "btn_number_" + cIndex + "_value";
            FloatingActionButton btn    = (FloatingActionButton)mActivity.findViewById(getIdByString(buttonID));

            //btn.setDefaultFocusHighlightEnabled(false);
            btn.setDefaultFocusHighlightEnabled(false);
            btn.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0, 0, 0, 0)));

            TextView textView = mActivity.findViewById(getIdByString(textViewID));
            TextView valTextView = mActivity.findViewById(getIdByString(valueTextViewID));

            //int count = (index <= params) ? mSudokuBoard.getLeftCount(index) : 0;
            int count = (index <= params) ? mSudoku.getLeftCount(index) : 0;

            if(index <= params) {

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OnClickParam(index);
                    }
                });

                btn.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        unselectParam(index);
                        return true;
                    }
                });
                valTextView.setText((mConverter.containsKey(index)) ? mConverter.get(index) : String.valueOf(index));
                FrameLayout frameLayout = mActivity.findViewById(getIdByString(frameID));
                ParamView paramView = new ParamView(btn, valTextView, textView, frameLayout, index, count);
                paramView.setCountVisibility(showCount ? View.VISIBLE : View.GONE);
                //mParams.add(new ParamView(btn, textView, frameLayout, index, count));
                mParamViews.put(index, paramView);
            }
            else {
                FrameLayout frameLayout = mActivity.findViewById(getIdByString(frameID));
                frameLayout.setVisibility(View.GONE);
            }

            for(int x: toRemove) {
                String tempId              = ("frame_" + x);
                FrameLayout frameLayout = mActivity.findViewById(getIdByString(tempId));
                frameLayout.setVisibility(View.GONE);
            }
        }
    }

    private void initParamsAction(){
        initParamAction(R.id.btn_undo,      R.id.frame_btn_undo,    R.id.btn_undo_icon,     ParamActionEnum.UNDO,    ParamActionTypeEnum.NORMAL);
        initParamAction(R.id.btn_erase,     R.id.frame_btn_erase,   R.id.btn_erase_icon,    ParamActionEnum.ERASE,   ParamActionTypeEnum.CHECK);
        initParamAction(R.id.btn_restart,   R.id.frame_btn_restart, R.id.btn_restart_icon,  ParamActionEnum.RESTART, ParamActionTypeEnum.NORMAL);
        initParamAction(R.id.btn_hints,     R.id.frame_btn_hints,   R.id.btn_hints_icon,    ParamActionEnum.HINTS,   ParamActionTypeEnum.CHECK);
        initParamAction(R.id.btn_check,     R.id.frame_btn_check,   R.id.btn_check_icon,    ParamActionEnum.CHECK,   ParamActionTypeEnum.NORMAL);
    }

    private void initParamAction(int buttonId, int frameId, int imageId, ParamActionEnum action, ParamActionTypeEnum type) {
        Button button = (Button) findViewById(buttonId);
        FrameLayout frame = (FrameLayout)findViewById(frameId);
        ImageView image = (ImageView)findViewById(imageId);

        mParamsAction.put(action, new ParamActionView(button, frame, image, action, type));
    }

    private int getIdByString(String id) {
        return mActivity.getResources().getIdentifier(id, "id", mActivity.getPackageName());
    }

    private void addListener(Object button, Action action) {
        if(button == null)
            return;

        try {
            FloatingActionButton btn = (FloatingActionButton)button;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        action.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            try {
                Button btn = (Button) button;
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            action.call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception x) {}
        }
    }

    public View findViewById(int id) {
        return mActivity.findViewById(id);
    }

    public void OnClickParam(int value) {
        Log.e(TAG, "Param has been clicked = " + value);

        if(mCurrentActionState == ActionState.REMOVE) {
            unselect(ParamActionEnum.ERASE);
        }

        if(mSettings.getHighlightSameDigits()) {
            mCurrentActionState = ActionState.ADD_HIGHLIGHT;
            mSudokuBoardView.setActionState(mCurrentActionState, value);
        }

        for (ParamView paramView : mParamViews.values()) {
            paramView.setIsSelected(paramView == mParamViews.get(value));
        }
    }

    public void unselectParam(int value) {
        if(!mParamViews.containsKey(value))
            return;

        if(mParamViews.get(value).isSelected()) {
            mSudokuBoardView.setActionState(ActionState.ADD, value);
            mParamViews.get(value).setIsSelected(false);
        }
    }

    public void updateCount(int value) {
        //int count = mSudokuBoard.getLeftCount(value);
        int count = mSudoku.getLeftCount(value);
        mParamViews.get(value).setCount(count);
    }

    @Override
    public Theme getTheme() {
        return this.mTheme;
    }

    @Override
    public void setTheme(Theme theme) {
        this.mTheme = theme;
        updateTheme();
    }

    @Override
    public void updateTheme() {
        //super.updateTheme();

        for(ParamView param : mParamViews.values()) {
            param.setTheme(getTheme());
        }
        for(ParamActionView param : mParamsAction.values()) {
            param.setTheme(getTheme());
        }
    }
}
