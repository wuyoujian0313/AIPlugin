package com.ai.base.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.ailk.common.data.IData;

public class Utility {
	
	/**
	 * encode
	 * @param str
	 * @param String
	 */
	public static String encode(String str) {
		byte[] bsrc = str.getBytes();
		int len = bsrc.length;
		byte[] bdest = new byte[len];
		for (int i=len-1; i>=0; i--) {
			bdest[len-i-1] = (byte) (bsrc[i]+1);
		}
		return new String(bdest);
	}

	/**
	 * error
	 * @param message
	 */
	public static void error(String message) {
		throw new RuntimeException(message);
	}
	
	/**
	 * error
	 * @param throwable
	 * @param throwable
	 */
	public static void error(Throwable throwable) {
		throw new RuntimeException(throwable);
	}
	
	/**
	 * error
	 * @param message
	 * @param throwable
	 * @param throwable
	 */
	public static void error(String message, Throwable throwable) {
		throw new RuntimeException(message, throwable);
	}
	
	/**
	 * get bottom exception
	 * @param exception
	 * @return Throwable
	 */
	public static Throwable getBottomException(Throwable exception) {
		if (exception == null) return null;
		if (exception.getCause() != null) {
			exception = exception.getCause();
			return getBottomException(exception);
		}
		return exception;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 *
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

}