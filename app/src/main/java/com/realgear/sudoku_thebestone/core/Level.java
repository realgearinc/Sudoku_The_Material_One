package com.realgear.sudoku_thebestone.core;

import com.realgear.sudoku_thebestone.core.enums.LevelEnums;

import java.io.Serializable;
import java.util.HashMap;

public class Level implements Serializable {

    public HashMap<Integer, String> mData;

    public Level() {

    }

    public Object getData(LevelEnums enums) {
        if(mData == null){
            mData = new HashMap<>();
        }

        if(mData.containsKey(enums.ordinal())) {
            if(enums == LevelEnums.Time) {
                return getTime(Integer.valueOf(mData.get(enums.ordinal())));
            }
            else
                return mData.get(enums.ordinal());
        }
        else {
            return enums.name();
        }
    }

    public void putData(LevelEnums key, String value) {
        if(mData == null){
            mData = new HashMap<>();
        }

        if(mData.containsKey(key.ordinal())) {
            mData.replace(key.ordinal(), value);
        }
        else {
            mData.put(key.ordinal(), value);
        }
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
