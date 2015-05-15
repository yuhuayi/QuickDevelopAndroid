/**
 * 
 */
package com.net;

import android.content.Context;

/**
 * @类名称: AsyncHttpClientHelper
 * @类描述:
 * @创建人：曹睿翔
 * @创建时间：2014年9月3日 下午3:39:46
 * @备注：
 */
public class AsyncHttpClientHelper {

	private static AsyncHttpClient instance = null;

	public static AsyncHttpClient getInstance(Context context) {
		if (instance == null) {
			synchronized (AsyncHttpClient.class) {
				if (instance == null)
					instance = new AsyncHttpClient(context);
			}
		}
		return instance;

	}
}