package com.realgear.sudoku_thebestone.core;

import android.os.CountDownTimer;

import com.realgear.sudoku_thebestone.utils.Time;

public class TimeHandler {
    private final long duration = (((((365 * 12) * 30) * 24) * 60) * 60) * 1000;

    private abstract class CountUpTimer extends CountDownTimer {
        private static final long INTERVAL_MS = 1000;
        private final long duration = (((((365 * 12) * 30) * 24) * 60) * 60) * 1000;

        protected CountUpTimer(long durationMS)
        {
            super(durationMS, INTERVAL_MS);
        }

        public abstract void onTick(int second);

        @Override
        public void onTick(long l) {
            int second = (int) ((duration - l) / 1000);
            onTick(second);
        }

        @Override
        public void onFinish() {
            onTick(duration / 1000);
        }

        public void onStop() {
            CountDownTimer timer = this;
            timer.cancel();
        }
    }

    private CountUpTimer mCountUpTimer;
    private Time mTimer;

    private boolean stopped = false;
    private boolean running = false;
    int prevTime = 0;
    int cursTime = 0;

    public void setTimer(Time timer)
    {
        mTimer = timer;
        mCountUpTimer = new CountUpTimer(duration) {
            @Override
            public void onTick(int second) {
                if(running && !stopped) {
                    int s = (prevTime + second);
                    mTimer.onTick(s);
                }
            }
        };
    }

    public void stop() {
        this.stopped = true;
    }

    public void start() {
        this.running = true;
        this.stopped = false;
        this.mCountUpTimer.start();
    }

    public void reset() {
        mCountUpTimer.onStop();
        prevTime = 0;
        mCountUpTimer = new CountUpTimer(duration) {
            @Override
            public void onTick(int second) {
                if(running && !stopped) {
                    int s = (prevTime + second);
                    mTimer.onTick(s);
                }
            }
        };
    }

    public void setPrevTime(int value) {
        this.prevTime = value;
    }

    public String getTime(int seconds)
    {
        int m = seconds / 60;
        int h = m / 60;
        int d = h / 24;


        int min     = seconds / 60;
        int hours   = min / 60;
        int days    = hours / 24;

        int curMin      = (min      - (hours    * 60));
        int curHours    = (hours    - (days     * 24));
        int curSeconds  = seconds   - ((curMin  * 60) + ((curHours * 60) * 60) + (((days * 24) * 60) * 60));

        String time = "";
        time += ((days     > 0) ? ""    + days      + "D " : "")    +
                ((curHours > 0) ? ""    + curHours  + "H " : "")    +
                ((curMin   > 0) ? ""    + curMin    + "M " : "")    + "" + curSeconds + "S";

        return time;
    }
}
