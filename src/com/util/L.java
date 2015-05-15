package com.util;

import android.util.Log;
import com.zhengbang.TeMe.BuildConfig;


/**
 * Copyright © 2015 amanbo.All rights reserved.  
 * @description 自动以调用者SimpleName生成TAG，并且对已签名apk自动屏蔽所有日志（已测）。
 * 				使用方法：<pre>L.d("log message ...")</pre>
 * @date: 2015-3-28 上午11:30:31
 * @author itdora
 * @email kiddingwu@163.com
 */
public class L {
	private final static int NO_LOG = -1;
	private final static int LOG_V = 1;
	private final static int LOG_D = 2;
	private final static int LOG_I = 3;
	private final static int LOG_W = 4;
	private final static int LOG_E = 5;
	
	private static int logLevel;
	
	//对签名apk自动屏蔽日志
	static{
		logLevel = BuildConfig.DEBUG ? LOG_E : NO_LOG;
	}
	
	public static void v(String msg) {
		if (logLevel >= LOG_V) {
			Log.v(generateTAG(msg), msg);
		}
	}
	
	public static void d(String msg) {
		if (logLevel >= LOG_D) {
			Log.d(generateTAG(msg), msg);
		}
	}
	
	public static void i(String msg) {
		if (logLevel >= LOG_I) {
			Log.i(generateTAG(msg), msg);
		}
	}
	
	public static void w(String msg) {
		if (logLevel >= LOG_W) {
			Log.w(generateTAG(msg), msg);
		}
	}
	
	public static void e(String msg) {
		if (logLevel >= LOG_E) {
			Log.e(generateTAG(msg), msg);
		}
	}
	
	private static String generateTAG(String msg) {
		StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[4];
		String fullClassName = stackTrace.getClassName();
		//以下代码纯属装b用。
		if (null == msg ) {
			int lineNumber = stackTrace.getLineNumber();
			throw new IllegalArgumentException(fullClassName + "第 " + lineNumber +" 行方法参数不能为null！" );
		}
		return fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
	}
}
