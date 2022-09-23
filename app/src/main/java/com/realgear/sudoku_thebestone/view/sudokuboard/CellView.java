package com.realgear.sudoku_thebestone.view.sudokuboard;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.gridlayout.widget.GridLayout;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.core.CellRule;
import com.realgear.sudoku_thebestone.core.CellState;
import com.realgear.sudoku_thebestone.theme.Theme;
import com.realgear.sudoku_thebestone.theme.Themeable;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class CellView extends Themeable {
    private Theme mTheme;

    private final int x;
    private final int y;
    private final boolean isFixed;
    private final boolean isExtra;

    private int mValue;

    private CellState.Status mCellStatus;
    private CellRule mCellRule;

    private final Context       mContext;
    private final FrameLayout   mFrameLayout;
    //private final FrameLayout   mFrameLayout_Hints;
    private final ExtendedFloatingActionButton mExtendedButton;
    private final TreeMap<Integer, String> mConverter;

    private List<TextView> mHintsList;
    private TableLayout mHintsLayout;

    private boolean isHintsAdded;

    public CellView(int x, int y, Context ctx) {
        this.x = x;
        this.y = y;
        this.mContext = ctx;


        this.mFrameLayout = new FrameLayout(ctx);
        //this.mFrameLayout_Hints = null;
        this.mExtendedButton = null;
        this.mConverter = null;
        this.isFixed = false;
        this.isExtra = true;
    }

    public CellView(int x, int y, int value, Context ctx, boolean isFixed) {
        this.x = x;
        this.y = y;
        this.mContext = ctx;

        this.mFrameLayout = new FrameLayout(ctx);

        this.mExtendedButton = new ExtendedFloatingActionButton(ctx);

        setHintsLayout();

        mExtendedButton.setPadding(0, 0, 0, 0);

        mConverter = new TreeMap<>();
        mConverter.put(10, "A");
        mConverter.put(11, "B");
        mConverter.put(12, "C");
        mConverter.put(13, "D");
        mConverter.put(14, "E");
        mConverter.put(15, "F");
        mConverter.put(16, "G");
        mConverter.put(17, "H");
        mConverter.put(18, "I");
        mConverter.put(19, "J");
        mConverter.put(20, "K");
        mConverter.put(21, "L");
        mConverter.put(22, "M");
        mConverter.put(23, "N");
        mConverter.put(24, "O");
        mConverter.put(25, "P");


        this.mFrameLayout.addView(mExtendedButton, 50, 50);
        //this.mFrameLayout.addView(mExtendedButton, 50, 50);

        this.isFixed = isFixed;
        this.isExtra = false;

        setValue(value);
        this.mCellStatus = CellState.Status.NORMAL;
    }

    public void setHintsLayout() {
        mHintsLayout = new TableLayout(mContext);
        mHintsLayout.setStretchAllColumns(true);
        mHintsLayout.setShrinkAllColumns(true);
        mHintsLayout.setGravity(Gravity.CENTER);
        mHintsList = new ArrayList<>();
    }

    public void initHintsLayout(int size) {
        int index = 0;
        for(int y = 0; y < size; y++) {
            TableRow row = new TableRow(mContext);
            for(int x = 0; x < size; x++) {
                index++;
                String hint = "";

                if(this.x == 0 && this.y == 0) {
                    Log.e("CellView", "Hint Added : " + index);
                }


                if(mConverter.containsKey(index)) {
                    hint = mConverter.get(index);
                }
                else {
                    hint = String.valueOf(index);
                }

                TextView textView = new TextView(mContext);
                textView.setText(hint);
                float fontSize = 12;

                if(size == 3) {
                    fontSize = 8;
                }
                if(size == 4) {
                    fontSize = 6;
                }
                if(size == 5) {
                    fontSize = 4;
                }

                textView.setTextSize(fontSize);
                textView.setGravity(Gravity.CENTER);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                mHintsList.add(textView);
                row.addView(textView);
                textView.requestLayout();
            }
            mHintsLayout.addView(row);
        }
    }

    public void setCellStatus(CellState.Status cellStatus) {
        if(isExtra)
            return;

        if(mCellStatus == cellStatus)
            return;

        if(cellStatus == null)
            return;

        mCellStatus = cellStatus;
        updateTheme();
    }

    public void setValue(int value) {
        if(isExtra)
            return;

        if(isHintsAdded) {
            mFrameLayout.post(new Runnable() {
                @Override
                public void run() {
                    mFrameLayout.removeAllViews();
                    mFrameLayout.addView(mExtendedButton, 50, 50);
                    isHintsAdded = false;
                }
            });
            initButton();
        }

        this.mValue = value;

        this.mExtendedButton.post(new Runnable() {
            @Override
            public void run() {
                if(mExtendedButton.getVisibility() == View.INVISIBLE) {
                    mExtendedButton.setVisibility(View.VISIBLE);
                }

                if(value != 0) {
                    if(mConverter.containsKey(value)) {
                        mExtendedButton.setText(mConverter.get(value));
                    }
                    else {
                        mExtendedButton.setText(String.valueOf(value));
                    }

                }
                else {
                    mExtendedButton.setText("");
                }
            }
        });

        //this.mExtendedButton.setVisibility(View.INVISIBLE);
    }

    public void setCellRule(CellRule cellRule) {
        this.mCellRule = cellRule;
    }

    public ExtendedFloatingActionButton getButton() {
        if(isExtra)
            return null;

        return this.mExtendedButton;
    }

    public void initButton() {
        if(isExtra)
            return;

        mExtendedButton.post(new Runnable() {
            @Override
            public void run() {
                mExtendedButton.setPadding(0, 0, 0, 0);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    mExtendedButton.setOutlineAmbientShadowColor(Color.WHITE);
                    mExtendedButton.setOutlineSpotShadowColor(Color.WHITE);
                }
                mExtendedButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                mExtendedButton.setTextSize(20);
                mExtendedButton.setAllCaps(true);
                mExtendedButton.setGravity(Gravity.CENTER);

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mExtendedButton.getLayoutParams().width, mExtendedButton.getLayoutParams().height);
                params.width = mExtendedButton.getLayoutParams().width;
                params.height = mExtendedButton.getLayoutParams().height;
                params.gravity = Gravity.CENTER;
                params.rightMargin = 2;
                mExtendedButton.setLayoutParams(params);
            }
        });

        mFrameLayout.post(new Runnable() {
            @Override
            public void run() {
                GridLayout.LayoutParams frameParams = new GridLayout.LayoutParams(mFrameLayout.getLayoutParams());
                frameParams.setGravity(Gravity.CENTER);
                mFrameLayout.setLayoutParams(frameParams);
            }
        });
    }

    public void initStyle()
    {
        if(mExtendedButton != null) {
            mExtendedButton.setElevation(1);
            mExtendedButton.setOutlineProvider(null);
            mExtendedButton.setRippleColor(null);

            mFrameLayout.setForegroundGravity(View.TEXT_ALIGNMENT_CENTER);
            mFrameLayout.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = mFrameLayout.getLayoutParams().width;
            params.height = mFrameLayout.getLayoutParams().height;
            params.setGravity(Gravity.CENTER);
            mFrameLayout.setLayoutParams(params);
            mFrameLayout.setClipToOutline(true);
            initButton();
        }

        if(mCellRule == CellRule.LOWER_RIGHT) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_vh);
        }
        if(mCellRule == CellRule.CENTER_RIGHT ||
                mCellRule == CellRule.UPPER_RIGHT) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_h);
        }

        if(mCellRule == CellRule.LOWER_LEFT ||
            mCellRule == CellRule.CENTER_LOWER) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_v);
        }

        if(mCellRule == CellRule.ANYWHERE_CENTER ||
            mCellRule == CellRule.CENTER_LEFT ||
            mCellRule == CellRule.UPPER_LEFT ||
            mCellRule == CellRule.CENTER_UPPER
            ) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_secondary_lb);
        }

        if(mCellRule == CellRule.CENTER_RIGHT_END ||
            mCellRule == CellRule.UPPER_RIGHT_END) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_secondary_cure);
        }

        if(mCellRule == CellRule.LOWER_RIGHT_END) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_re);
        }

        if(mCellRule == CellRule.LOWER_LEFT_END ||
                mCellRule == CellRule.CENTER_LOWER_END) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_secondary_l);
        }

        if(mCellRule == CellRule.LOWER_LEFT_END) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_1x3);
        }

        if(mCellRule == CellRule.LOWER_RIGHT_END_BOTTOM) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_h_l);
        }

        if(mCellRule == CellRule.LOWER_RIGHT_END_BOTTOM) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_vh);
        }

        if(mCellRule == CellRule.LOWER_RIGHT_END_CORNER) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_secondary_lrec);
        }

        if(mCellRule == CellRule.FULL_BORDER) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_1x1);
        }

        if(mCellRule == CellRule.LOWER_LEFT_0) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_1x2);
        }

        if(mCellRule == CellRule.UPPER_CORNER_0) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_0x0);
        }

        if(mCellRule == CellRule.UPPER_0X1) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_0x1);
        }

        if(mCellRule == CellRule.UPPER_0X2) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_0x2);
        }

        if(mCellRule == CellRule.UPPER_0X3) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_0x3);
        }

        if(mCellRule == CellRule.CENTER_1X0) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_1x0);
        }

        if(mCellRule == CellRule.CENTER_2X0) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_2x0);
        }

        if(mCellRule == CellRule.CENTER_1X3) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_1x3);
        }

        if(mCellRule == CellRule.CENTER_2X3) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_2x3);
        }

        if(mCellRule == CellRule.LOWER_3X2) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_3x2);
        }

        if(mCellRule == CellRule.LOWER_3X3_L) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_3x3);
        }

        if(mCellRule == CellRule.CENTER_3X2_L) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_3x2_l);
        }

        if(mCellRule == CellRule.CENTER_2X3_L) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_2x3_l);
        }

        if(mCellRule == CellRule.UPPER_0X2_L) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_0x2_l);
        }

        if(mCellRule == CellRule.LOWER_2X0_L) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_2x0_l);
        }


        //Set Extras
        if(mCellRule == CellRule.EXTRA_0X0) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_2x3);
        }
        if(mCellRule == CellRule.EXTRA_1X0) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_2x0_extra);
        }

        if(mCellRule == CellRule.EXTRA_2X0) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_2x3);
        }

        if(mCellRule == CellRule.EXTRA_3X0) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_2x3);
        }

        if(mCellRule == CellRule.EXTRA_4X0) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_2x3);
        }

        if(mCellRule == CellRule.EXTRA_5X0) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_3x3);
        }

        if(mCellRule == CellRule.EXTRA_0X0_X) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_3x2);
        }

        if(mCellRule == CellRule.EXTRA_0X1_X) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_3x2);
        }

        if(mCellRule == CellRule.EXTRA_0X2_X) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_3x2);
        }

        if(mCellRule == CellRule.EXTRA_0X3_X) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_3x2);
        }

        if(mCellRule == CellRule.EXTRA_0X4_X) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_3x2);
        }

        if(mCellRule == CellRule.EXTRA_0X5_X) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_3x3);
        }

        if(mCellRule == CellRule.CORNER_8X8) {
            mFrameLayout.setBackgroundResource(R.drawable.grid_primary_8x8);
        }
    }

    public void setHints(List<Integer> hints) {
        if(mValue == 0) {

            if(hints.size() > 0) {
                mFrameLayout.post(new Runnable() {
                    @Override
                    public void run() {

                        for(TextView textView : mHintsList) {
                            textView.setVisibility(View.INVISIBLE);
                        }

                        for(int i = 0; i < hints.size(); i++) {
                            int index = (hints.get(i) - 1);

                            if(index < mHintsList.size()) {
                                mHintsList.get(index).setVisibility(View.VISIBLE);
                            }
                        }

                        //mFrameLayout_Hints.removeAllViews();
                        //mFrameLayout_Hints.addView(mHintsLayout);
                        mFrameLayout.removeAllViews();
                        mFrameLayout.addView(mHintsLayout);
                        mHintsLayout.requestLayout();
                        isHintsAdded = true;
                    }
                });
            }

            /*if(hints.size() > 0) {
                TextView text = new TextView(mContext);
                for(int i = 0; i < hints.size(); i++) {
                    if(mConverter.containsKey(hints.get(i))) {
                        text.setText(text.getText() + " " + mConverter.get(hints.get(i)));
                    }
                    else
                        text.setText(text.getText() + " " + String.valueOf(hints.get(i)));
                }
                text.setTextColor(mTheme.getSecondaryColor());
                mFrameLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mFrameLayout_Hints.removeAllViews();
                        mFrameLayout_Hints.addView(text);
                        mFrameLayout.removeAllViews();
                        mFrameLayout.addView(mFrameLayout_Hints, 50, 50);
                        isHintsAdded = true;
                    }
                });
            }*/
        }
    }

    public View getView() {
        return this.mFrameLayout;
    }

    public int getValue() {
        return mValue;
    }

    @Override
    public void updateTheme() {
        mTheme = getTheme();

        updateCellStyle();
    }

    public void updateCellStyle() {
        if(mTheme == null)
            return;

        LayerDrawable bgDrawable = (LayerDrawable) mFrameLayout.getBackground();
        if (bgDrawable != null) {
            GradientDrawable bgShape = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.shape_primary_color);
            if (bgShape != null) {
                bgShape.setColor(mTheme.getPrimaryColor());
            }

            GradientDrawable bgShape2 = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.shape_primary_color_sec);
            if (bgShape2 != null) {
                bgShape2.setColor(mTheme.getPrimaryColor());
            }

            GradientDrawable bgShape3 = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.shape_primary_color_3rd);
            if (bgShape3 != null) {
                bgShape3.setColor(mTheme.getPrimaryColor());
            }

            GradientDrawable bgShape4 = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.shape_primary_color_4th);
            if (bgShape4 != null) {
                bgShape4.setColor(mTheme.getPrimaryColor());
            }
        }

        if(!isFixed && !isExtra) {
            switch (mCellStatus) {
                case NORMAL:
                    mExtendedButton.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0, 0, 0, 0)));
                    mExtendedButton.setTextColor(mTheme.getPrimaryColor());
                    break;
                case HIGHLIGHTED:
                    Color c = Color.valueOf(mTheme.getPrimaryColor());
                    mExtendedButton.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0.5f, c.red(), c.green(), c.blue())));
                    mExtendedButton.setTextColor(mTheme.getPrimaryColor());
                    break;
                case SOLVED:
                    Color solvedColor = Color.valueOf(Color.MAGENTA);
                    mExtendedButton.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0.35f, solvedColor.red(), solvedColor.green(), solvedColor.blue())));
                    mExtendedButton.setTextColor(Color.WHITE);
                    break;
                case ERROR:
                    Color errorColor = Color.valueOf(Color.RED);
                    mExtendedButton.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0.35f, errorColor.red(), errorColor.green(), errorColor.blue())));
                    mExtendedButton.setTextColor(Color.WHITE);
                    break;
            }

            /*if (mCellStatus != CellState.Status.HIGHLIGHTED) {
                mExtendedButton.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0, 0, 0, 0)));
            } else {
                //Log.e("CellView", "Highlight cell");
                Color c = Color.valueOf(mTheme.getPrimaryColor());
                mExtendedButton.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0.5f, c.red(), c.green(), c.blue())));
            }*/

        }
        else if (!isExtra){
            switch (mCellStatus) {
                case HIGHLIGHTED:
                {
                    Color c = Color.valueOf(mTheme.getPrimaryColor());
                    mExtendedButton.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0.5f, c.red(), c.green(), c.blue())));
                    mExtendedButton.setTextColor(mTheme.getPrimaryColor());
                }
                break;
                default:
                {
                    mExtendedButton.setBackgroundTintList(ColorStateList.valueOf(mTheme.getSecondaryColor2()));
                    Color c = Color.valueOf(mTheme.getSecondaryColor2());
                    mExtendedButton.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0.3f, c.red(), c.green(), c.blue())));
                    mExtendedButton.setTextColor(mTheme.getSecondaryColor());
                }
                break;
            }

            /*if (mCellStatus != CellState.Status.HIGHLIGHTED) {
                //mExtendedButton.setBackgroundColor(Color.argb(0, 0, 0, 0));
                mExtendedButton.setBackgroundTintList(ColorStateList.valueOf(mTheme.getSecondaryColor2()));
                Color c = Color.valueOf(mTheme.getSecondaryColor2());
                mExtendedButton.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0.3f, c.red(), c.green(), c.blue())));
                mExtendedButton.setTextColor(mTheme.getSecondaryColor());
            } else {
                Color c = Color.valueOf(mTheme.getPrimaryColor());
                mExtendedButton.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0.5f, c.red(), c.green(), c.blue())));
                mExtendedButton.setTextColor(mTheme.getPrimaryColor());
            }*/
        }
    }

    public TableLayout getHintsFrame() {
        return this.mHintsLayout;
    }

    public int getY() {
        return this.y;
    }

    public int getX() {
        return this.x;
    }

    public void hideHints() {
        setValue(0);
    }
}
