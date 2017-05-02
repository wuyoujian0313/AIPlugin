package com.ai.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

/**
 * Created by wuyoujian on 17/4/11.
 */

public class AICommon {

    public static String kSharedPreferencesKey_DeviceId = "kSharedPreferencesKey_DeviceId";
    public static String kSharedPreferencesKey_ZH_String_Suffix = "_kSharedPreferencesKey_ZH_String_Suffix";
    public static void backToHomePage() {
        // 返回到首页
        AIActivityCollector.getInstance().backToRootActivity();
    }


    public static String generateGUID(){
        UUID uuid = UUID.randomUUID();
        // 保存到本地
        String uuidString = uuid.toString();
        return uuidString;
    }

    public static String generateZH_string(String pluginId) {
        // 从本地读取
        SharedPreferences sharedPreferences= AIActivityCollector.getInstance().rootActivity().getSharedPreferences("AIBase",
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String Zh_stringKey = pluginId + kSharedPreferencesKey_ZH_String_Suffix;
        String zhString = sharedPreferences.getString(Zh_stringKey,null);
        if (zhString != null && zhString.length() > 0) {
            return zhString;
        }

        String s = "湖南亚信软件有限公司陕西移动CRM支撑团队";
        char[] cs = s.toCharArray();
        int length = s.length();

        Random random = new Random();
        String zh_string = new String();
        for(int i = 0; i < 10; i++) {
            int index = random.nextInt(length);
            zh_string += cs[index];
        }

        // 保存到本地
        editor.putString(Zh_stringKey,zh_string);
        editor.apply();

        return zh_string;
    }

    public static String generateDeviceId() {
        // 从本地读取,如果本地没有就自动生成一个
        // 从本地读取
        SharedPreferences sharedPreferences= AIActivityCollector.getInstance().rootActivity().getSharedPreferences("AIBase",
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String deviceId = sharedPreferences.getString(kSharedPreferencesKey_DeviceId,null);
        if (deviceId != null && deviceId.length() > 0) {
            return deviceId;
        }

        String uuidString = generateGUID();
        // 保存到本地
        editor.putString(kSharedPreferencesKey_DeviceId,uuidString);
        editor.apply();

        return uuidString;
    }

    public static String generateToken(String userName, String ZH_string, String deviceId, String pluginId) {
        // 对userName、ZH_string,deviceId、pluginId排序首字母,然后拼接起来
        // 最后MD5
        String[] arrays = new String[]{userName,deviceId,pluginId};
        Arrays.sort(arrays);
        String sumString = new String();
        for (String str:arrays) {
            sumString += str;
        }

        sumString += ZH_string;
        String token = md5(sumString);

        return token;
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
