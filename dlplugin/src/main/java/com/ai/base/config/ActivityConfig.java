package com.ai.base.config;

import android.app.Activity;
import android.content.SharedPreferences;

import com.ai.base.AIActivityCollector;

/**
 * Created by wuyoujian on 17/5/1.
 */

public class ActivityConfig {

    private String kSharedPreferencesKey_AlreadyGesturePWD = "kSharedPreferencesKey_DeviceId";
    private long kDurTime = 60*1000;
    private static ActivityConfig instance;
    // 保存最后一次调用onPause()的系统时间戳
    private long lockTime = 0;

    public void setDurTime(long kDurTime) {
        this.kDurTime = kDurTime;
    }

    public void setLockTime(long lockTime) {
        this.lockTime = lockTime;
    }

    public long getLockTime() {
        return lockTime;
    }

    public void setAlreadyGesturePassword() {
        // 从本地读取
        SharedPreferences sharedPreferences= AIActivityCollector.getInstance().rootActivity().getSharedPreferences("ActivityConfig",
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String key = kSharedPreferencesKey_AlreadyGesturePWD;
        // 保存到本地
        editor.putBoolean(key,true);
        editor.apply();
    }

    public boolean isAlreadyGesturePassword() {
        SharedPreferences sharedPreferences= AIActivityCollector.getInstance().rootActivity().getSharedPreferences("ActivityConfig",
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象
        boolean isAlready = sharedPreferences.getBoolean(kSharedPreferencesKey_AlreadyGesturePWD,false);

        return isAlready;
    }

    public void clearAlreadyGesturePassword() {
        SharedPreferences sharedPreferences= AIActivityCollector.getInstance().rootActivity().getSharedPreferences("ActivityConfig",
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String key = kSharedPreferencesKey_AlreadyGesturePWD;
        editor.putBoolean(key,false);
    }


    public static ActivityConfig getInstance() {
        if (instance == null) {
            synchronized (ActivityConfig.class) {
                instance = new ActivityConfig();
            }
        }
        return instance;
    }

    public void saveLockTime() {
        lockTime = System.currentTimeMillis();
    }

    public boolean isShowGesturePasswordActivity() {
        long durTime = System.currentTimeMillis() - lockTime;

        // 需要结合是否有设置手势密码
        if (durTime <= kDurTime || !isAlreadyGesturePassword()) {
            return false;
        }

        return true;
    }
}
