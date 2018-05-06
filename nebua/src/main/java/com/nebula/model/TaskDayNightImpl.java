package com.nebula.model;

import android.content.SharedPreferences;

import com.nebula.model.entity.ResponseDayNightModel;
import com.nebula.take.DayNightHelper;

/**
 * Created by qydda on 2016/12/7.
 */

public class TaskDayNightImpl implements TaskDayNight {
    private DayNightHelper helper = null;

    public TaskDayNightImpl(SharedPreferences sp) {
        helper = new DayNightHelper(sp);
    }

    @Override
    public boolean setDayModel() {
        return helper.setMode(ResponseDayNightModel.DAY);
    }

    @Override
    public boolean setNightMode() {
        return helper.setMode(ResponseDayNightModel.NIGHT);
    }

    @Override
    public boolean isDay() {
        return helper.isDay();
    }

    @Override
    public boolean isNight() {
        return helper.isNight();
    }
}
