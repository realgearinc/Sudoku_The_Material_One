package com.realgear.sudoku_thebestone.view;

import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.realgear.sudoku_thebestone.R;
import com.realgear.sudoku_thebestone.activities.About;
import com.realgear.sudoku_thebestone.utils.Action;

public class AboutView {
    private About mActivity;

    private FloatingActionButton mBtn_Back;

    public AboutView(About activity) {
        this.mActivity = activity;

        initButtons();
    }

    private void initButtons() {
        mBtn_Back = (FloatingActionButton)findViewById(R.id.back_btn);

        addListener(mBtn_Back, () -> onClickBackBtn());
    }

    private void onClickBackBtn() {
        this.mActivity.finish();
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
}
