package com.realgear.sudoku_thebestone.view;

import android.animation.ArgbEvaluator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.otaliastudios.zoom.ZoomLayout;
import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.activities.Gameplay;
import com.realgear.sudoku_thebestone.core.SudokuTypes;
import com.realgear.sudoku_thebestone.core.TimeHandler;
import com.realgear.sudoku_thebestone.data.SettingsData;
import com.realgear.sudoku_thebestone.theme.Theme;
import com.realgear.sudoku_thebestone.theme.Themeable;
import com.realgear.sudoku_thebestone.theme.Themes;
import com.realgear.sudoku_thebestone.utils.Action;
import com.realgear.sudoku_thebestone.utils.Time;
import com.realgear.sudoku_thebestone.view.sudokuboard.SudokuBoardView;
import com.realgear.sudoku_thebestone.view.sudokuparams.ParamActionView;
import com.realgear.sudoku_thebestone.view.sudokuparams.SudokuParamsView;

public class GameplayView extends Themeable {
    private Theme mCurrentTheme;

    private final Gameplay  mActivity;
    public SudokuBoardView  mSudokuBoardView;
    public SudokuParamsView mSudokuParamsView;

    private FloatingActionButton mBackBtn;
    private TextView            mLevelInfo;
    private TextView            mTime;

    private View mHeader_layout;
    private View mThemeSelector;
    private View mZoomIn_layout;
    private View mParams_layout;

    private View mFinish_layout;
    private TextView mCheerUp;
    private TextView mCurrentLevel;
    private TextView mCurrentSudokuGrid;
    private TextView mCurrentDifficulty;
    private TextView mCurrentSudokuType;
    private FrameLayout mFrameLayout_nextLevel;
    private Button mBtn_NextLevel;

    private ParamActionView mParam_Btn_NextLevel;

    private View mLoadingScreen_layout;
    private CircularProgressIndicator mProgressBar;
    private TextView mProgress_textView;

    private boolean mIsDarkTheme;

    private TimeHandler mTimeManager;

    private SettingsData mSettings;

    public GameplayView(Gameplay gameplay) {
        this.mActivity = gameplay;

        mSettings = new SettingsData(mActivity);

        mHeader_layout = findViewById(R.id.header_layout);
        mThemeSelector = findViewById(R.id.layout_themeMenu);
        mZoomIn_layout = findViewById(R.id.sudokuBoard_ZoomLayout);
        mParams_layout = findViewById(R.id.layout_params);

        mFinish_layout          = findViewById(R.id.finished_relativeLayout);
        mCheerUp                = (TextView) findViewById(R.id.textView_cheerUp);
        mCurrentLevel           = (TextView)findViewById(R.id.textView_currentLevel);
        mCurrentDifficulty      = (TextView)findViewById(R.id.textView_currentDifficulty);
        mCurrentSudokuGrid      = (TextView)findViewById(R.id.textView_currentGrid);
        mCurrentSudokuType      = (TextView)findViewById(R.id.textView_currentSudokuType);
        mFrameLayout_nextLevel  = (FrameLayout)findViewById(R.id.frame_next_level);
        mBtn_NextLevel          = (Button)findViewById(R.id.btn_next_level);

        onTouchListener(mBtn_NextLevel,
                () -> {
                    mFrameLayout_nextLevel.setBackgroundTintList(ColorStateList.valueOf(getTheme().getPrimaryColor()));
                    mBtn_NextLevel.setTextColor(Color.WHITE);
                },
                () -> {
                    mFrameLayout_nextLevel.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0, 0, 0,0)));
                    mBtn_NextLevel.setTextColor(getTheme().getPrimaryColor());
                });

        addListener(mBtn_NextLevel, this::onNextLevelClicked);


        mLoadingScreen_layout   = (RelativeLayout)findViewById(R.id.loadingScreen_relativeLayout);
        mProgressBar            = (CircularProgressIndicator)findViewById(R.id.grid_progressbar);
        mProgress_textView      = (TextView)findViewById(R.id.textView_gridProgress);

        mFinish_layout.setAlpha(0.0f);


        mHeader_layout.setAlpha(0.0f);
        mThemeSelector.setAlpha(0.0f);
        mZoomIn_layout.setAlpha(0.0f);
        mParams_layout.setAlpha(0.0f);

        //mActivity.getWindow().getDecorView().getRootView().setAlpha(0.0f);

        mBackBtn        = (FloatingActionButton)findViewById(R.id.back_btn);
        mLevelInfo      = (TextView)findViewById(R.id.textView_level_info);
        mTime           = (TextView)findViewById(R.id.time_textView);

        mSudokuBoardView = new SudokuBoardView(
                (ZoomLayout)findViewById(R.id.sudokuBoard_ZoomLayout),
                (GridLayout)findViewById(R.id.sudokuBoard_GridLayout),
                mActivity,
                this
        );
        initTimeManager();
        mIsDarkTheme = false;
    }

    private void onNextLevelClicked() {
        mActivity.startNextLevel();
    }

    private void initTimeManager() {
        if(!mSettings.getShowTime()) {
            mTime.setVisibility(View.GONE);
        }
        else {
            mTime.setVisibility(View.VISIBLE);
        }

        mTimeManager = new TimeHandler();
        mTimeManager.setTimer(new Time() {
            @Override
            public void onTick(int second) {
                mTime.setText(mTimeManager.getTime(second));
                mActivity.setTime(second);
            }
        });
    }

    public void setTime(int time) {
        mTimeManager.setPrevTime(time);
    }

    public void createBoardView() {
        mSudokuBoardView.createBoard(mActivity.getSudoku());

        mSudokuParamsView = new SudokuParamsView(mSudokuBoardView, mActivity.getSudoku(), mActivity);

        initViews();
    }

    public void initViews() {
        this.mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });

        mLevelInfo.setText("Level : "+ mActivity.mCurrentLevel + " - " + mActivity.mGameMode.name());
    }

    public void setDayNightTheme(boolean isDarkMode) {
        if(isDarkMode) {

            if(getTheme().getOrdinal() == Themes.FOREST_GREEN_WHITE.ordinal() || getTheme().getOrdinal() == Themes.GREY_WHITE.ordinal()) {
                return;
            }

            int black_bg = ContextCompat.getColor(mActivity, R.color.black_grey);
            int white_bg = getTheme().getBackgroundColor();

            ValueAnimator valueAnimator = getValueAnimator(white_bg, black_bg);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mActivity.getWindow().getDecorView().getRootView().setBackgroundColor((int) valueAnimator.getAnimatedValue());
                    //mActivity.getWindow().setStatusBarColor((int) valueAnimator.getAnimatedValue());
                    setStatusBarColor((int) valueAnimator.getAnimatedValue(), false);
                }
            });
            valueAnimator.start();


            mIsDarkTheme = true;
            Log.e("MainActivityView", "Dark Mode Set");
        }
        else {
            int black_bg = ContextCompat.getColor(mActivity, R.color.black_grey);
            int white_bg = Color.WHITE;

            ValueAnimator valueAnimator = getValueAnimator(black_bg, white_bg);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mActivity.getWindow().getDecorView().getRootView().setBackgroundColor((int) valueAnimator.getAnimatedValue());
                    //mActivity.getWindow().setStatusBarColor();
                    setStatusBarColor((int) valueAnimator.getAnimatedValue(), true);
                }
            });
            valueAnimator.start();

            //mActivity.getWindow().getDecorView().
            mIsDarkTheme = false;
            Log.e("MainActivityView", "Light Mode Set");
        }
    }

    private void setStatusBarColor(Integer color, boolean darkStatusBarTint) {
        Activity activity = mActivity;
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(color);
        int flag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(darkStatusBarTint ? flag : 0);
    }

    private ValueAnimator getValueAnimator(int from, int to) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), from, to);
        colorAnimation.setDuration(1000);
        return colorAnimation;
    }

    private ValueAnimator getFloatAnimator(float from, float to) {
        ValueAnimator floatAnimation = ValueAnimator.ofObject(new TypeEvaluator<Float>() {
            @Override
            public Float evaluate(float fraction, Float startValue, Float endValue) {
                return (startValue + (endValue - startValue) * fraction);
            }
        }, from, to);
        floatAnimation.setDuration(3000);
        return floatAnimation;
    }

    public void showFinish() {
        mCurrentLevel.setText("Level : " + mActivity.mCurrentLevel);
        int gridSize = mActivity.mSudoku.getBoardSquareSize();
        mCurrentSudokuGrid.setText("Sudoku Grid : " + ((mActivity.mSudokuType == SudokuTypes.SAMURAI) ? mActivity.mGridType.name() : gridSize + " x " + gridSize));
        mCurrentDifficulty.setText("Difficulty : " + mActivity.mGameMode.name());
        mCurrentSudokuType.setText("Sudoku Type : " + mActivity.mSudokuType.name());

        if(!mActivity.hasNextLevel()) {
            mBtn_NextLevel.setText("Close");
            mBtn_NextLevel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActivity.setIsCompleted();
                    mActivity.finish();
                }
            });
        }

        startFading(mFinish_layout, 0.0f, 1.0f, 350, true);

        mTimeManager.stop();
    }

    public void hideFinish() {
        startFading(mFinish_layout, 1.0f, 0.0f, 350, true);
    }

    public void fadeOut() {
        ValueAnimator val = getFloatAnimator(1.0f, 0.0f);
        val.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float val = (float)valueAnimator.getAnimatedValue();
                mHeader_layout.setAlpha(val);
                mThemeSelector.setAlpha(val);
                mZoomIn_layout.setAlpha(val);
                mParams_layout.setAlpha(val);
            }
        });
        val.start();

        mLoadingScreen_layout.post(new Runnable() {
            @Override
            public void run() {
                startFading(mLoadingScreen_layout, 0.0f, 1.0f, 350, false);
            }
        });
    }

    public void fadeIn() {
        ValueAnimator val = getFloatAnimator(0.0f, 1.0f);
        val.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float val = (float)valueAnimator.getAnimatedValue();
                mHeader_layout.setAlpha(val);
                mThemeSelector.setAlpha(val);
                mZoomIn_layout.setAlpha(val);
                mParams_layout.setAlpha(val);
            }
        });
        val.start();
    }

    public View findViewById(int id) {
        return this.mActivity.findViewById(id);
    }

    public void updateCount(int value) {
        mSudokuParamsView.updateCount(value);
        mActivity.saveCurrentBoard();
    }

    @SuppressLint("SetTextI18n")
    public void updateProgressbar(int value, int max) {
        mProgressBar.post(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setProgress(value, false);
                mProgressBar.setMax(max);
            }
        });

        mProgress_textView.setText("" + value);
    }

    public void onGridLoaded() {
        ZoomLayout zoomLayout = (ZoomLayout)mZoomIn_layout;
        mZoomIn_layout.post(new Runnable() {
            @Override
            public void run() {
                //Looper.prepare();
                zoomLayout.zoomTo(zoomLayout.getMinZoom(), true);
                //Looper.loop();
            }
        });

        mLoadingScreen_layout.post(new Runnable() {
            @Override
            public void run() {
                startFading(mLoadingScreen_layout, 1.0f, 0.0f, 500, false);
            }
        });

        mTimeManager.start();
    }

    public void resetTime() {
        mTimeManager.reset();
    }

    public Gameplay getActivity() {
        return mActivity;
    }

    //public Gameplay getGameplayActivity() { return mActivity; }

    @Override
    public Theme getTheme() {
        return this.mCurrentTheme;
    }

    @Override
    public void setTheme(Theme theme) {
        this.mCurrentTheme = theme;
        updateTheme();
    }

    @Override
    public void updateTheme() {
        if(!mIsDarkTheme) {
            mActivity.getWindow().getDecorView().getRootView().setBackgroundColor(getTheme().getBackgroundColor());
            mActivity.getWindow().setStatusBarColor(getTheme().getBackgroundColor());

            if(getTheme().getBackgroundColor() == Color.WHITE) {
                setStatusBarColor(getTheme().getBackgroundColor(), true);
            }
            else {
                setStatusBarColor(getTheme().getBackgroundColor(), false);

            }
        }

        mLevelInfo.setTextColor(getTheme().getSecondaryColor());
        mTime.setTextColor(getTheme().getSecondaryColor());

        mSudokuBoardView.setTheme(mCurrentTheme);
        mSudokuParamsView.setTheme(mCurrentTheme);
    }

    public void onDestroy() {
        mTimeManager.stop();
        mTimeManager = null;
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

    @SuppressLint("ClickableViewAccessibility")
    private void onTouchListener(Button button, Action onTouchDownAction, Action onTouchUpAction) {
        if(button == null) {
            return;
        }

        button.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_HOVER_ENTER:
                            onTouchDownAction.call();
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                        case MotionEvent.ACTION_HOVER_EXIT:
                            onTouchUpAction.call();
                            break;

                        default:
                            return false;

                    }
                }
                catch (Exception e) {

                }
                return false;
            }
        });
    }

}
