package com.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;

@SuppressLint({ "SimpleDateFormat", "UseValueOf" })
public class JsonUtils {

	public static String getAttFormJson(JSONObject bean, String att) {
		String value = null;
		if (bean != null && bean.has(att)) {
			try {
				value = bean.getString(att);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	/**
	 * JSONObject对象转JavaBean
	 * @return 转换结果（异常情况下返回null）
	 */
	@SuppressLint("DefaultLocale")
	@SuppressWarnings("rawtypes")
	public static Object jsonToBean(JSONObject json, Class<?> cls) {
		Object obj = null;

		try {
			obj = cls.newInstance();

			// 取出Bean里面的所有方法
			Method[] methods = cls.getMethods();
			for (int i = 0; i < methods.length; i++) {
				// 取出方法名
				String methodName = methods[i].getName();
				// 取出方法的类型
				Class[] clss = methods[i].getParameterTypes();
				if (clss.length != 1) {
					continue;
				}

				// 若是方法名不是以set开始的则退出本次循环
				if (methodName.indexOf("set") < 0) {
					continue;
				}

				// 类型
				String type = clss[0].getSimpleName();

				String key = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);

				// 如果map里有该key
				if (json.has(key) && json.get(key) != null) {
					setValue(type, json.get(key), methods[i], obj);
				}
			}
		} catch (Exception ex) {
			LogUtil.info(JsonUtils.class, "JSONObject转Bean失败-->" + ex.getMessage());
		}

		return obj;
	}

	/**
	 * 根据key从JSONObject对象中取得对应值
	 * 
	 * @param json
	 * @param key
	 * @return
	 * @throws org.json.JSONException
	 */
	public static String getString(JSONObject json, String key) throws JSONException {
		String retVal = null;
		if (json.isNull(key)) {
			retVal = "";
		} else {
			retVal = json.getString(key);
		}
		return retVal;
	}

	/**
	 * 给JavaBean的每个属性设值
	 * 
	 * @param type
	 * @param value
	 * @param method
	 * @param bean
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws java.lang.reflect.InvocationTargetException
	 * @throws java.text.ParseException
	 */
	private static void setValue(String type, Object value, Method method, Object bean) {
		if (value != null && !"".equals(value)) {
			try {
				if ("String".equals(type)) {
					method.invoke(bean, new Object[] { value });
				} else if ("int".equals(type) || "Integer".equals(type)) {
					method.invoke(bean, new Object[] { new Integer("" + value) });
				} else if ("double".equals(type) || "Double".equals(type)) {
					method.invoke(bean, new Object[] { new Double("" + value) });
				} else if ("float".equals(type) || "Float".equals(type)) {
					method.invoke(bean, new Object[] { new Float("" + value) });
				} else if ("long".equals(type) || "Long".equals(type)) {
					method.invoke(bean, new Object[] { new Long("" + value) });
				} else if ("int".equals(type) || "Integer".equals(type)) {
					method.invoke(bean, new Object[] { new Integer("" + value) });
				} else if ("boolean".equals(type) || "Boolean".equals(type)) {
					method.invoke(bean, new Object[] { Boolean.valueOf("" + value) });
				} else if ("BigDecimal".equals(type)) {
					method.invoke(bean, new Object[] { new BigDecimal("" + value) });
				} else if ("Date".equals(type)) {
					Class<?> dateType = method.getParameterTypes()[0];
					if ("java.util.Date".equals(dateType.getName())) {
						java.util.Date date = null;
						if ("String".equals(value.getClass().getSimpleName())) {
							String time = String.valueOf(value);
							String format = null;
							if (time.indexOf(":") > 0) {
								if (time.indexOf(":") == time.lastIndexOf(":")) {
									format = "yyyy-MM-dd H:mm";
								} else {
									format = "yyyy-MM-dd H:mm:ss";
								}
							} else {
								format = "yyyy-MM-dd";
							}
							SimpleDateFormat sf = new SimpleDateFormat();
							sf.applyPattern(format);
							date = sf.parse(time);
						} else {
							date = (java.util.Date) value;
						}

						if (date != null) {
							method.invoke(bean, new Object[] { date });
						}
					} else if ("java.sql.Date".equals(dateType.getName())) {
						Date date = null;
						if ("String".equals(value.getClass().getSimpleName())) {
							String time = String.valueOf(value);
							String format = null;
							if (time.indexOf(":") > 0) {
								if (time.indexOf(":") == time.lastIndexOf(":")) {
									format = "yyyy-MM-dd H:mm";
								} else {
									format = "yyyy-MM-dd H:mm:ss";
								}
							} else {
								format = "yyyy-MM-dd";
							}
							SimpleDateFormat sf = new SimpleDateFormat();
							sf.applyPattern(format);
							date = new Date(sf.parse(time).getTime());
						} else {
							date = (Date) value;
						}

						if (date != null) {
							method.invoke(bean, new Object[] { date });
						}
					}
				} else if ("Timestamp".equals(type)) {
					Timestamp timestamp = null;
					if ("String".equals(value.getClass().getSimpleName())) {
						String time = String.valueOf(value);
						String format = null;
						if (time.indexOf(":") > 0) {
							if (time.indexOf(":") == time.lastIndexOf(":")) {
								format = "yyyy-MM-dd H:mm";
							} else {
								format = "yyyy-MM-dd H:mm:ss";
							}
						} else {
							format = "yyyy-MM-dd";
						}
						SimpleDateFormat sf = new SimpleDateFormat();
						sf.applyPattern(format);
						timestamp = new Timestamp(sf.parse(time).getTime());
					} else {
						timestamp = (Timestamp) value;
					}

					if (timestamp != null) {
						method.invoke(bean, new Object[] { timestamp });
					}
				} else if ("byte[]".equals(type)) {
					method.invoke(bean, new Object[] { new String("" + value).getBytes() });
				}
			} catch (Exception ex) {
				LogUtil.info(JsonUtils.class, "JSONObject赋值给JavaBean失败");
			}
		}
	}

	/**
	 * 将Model转换成JSONObject
	 */
	public static JSONObject coverModelToJSONObject(Object o) throws Exception {
		JSONObject json = new JSONObject();
		Class<? extends Object> clazz = o.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field f = fields[i];
			json.put(f.getName(), invokeMethod(clazz, f.getName(), o));
		}
		return json;
	}

	/**
	 * 将list转换成JSONArray
	 */
	public static JSONArray coverModelToJSONArray(List<?> list) throws Exception {
		JSONArray array = null;
		if (list.isEmpty()) {
			return array;
		}
		array = new JSONArray();
		for (Object o : list) {
			array.put(coverModelToJSONObject(o));
		}
		return array;
	}

	@SuppressLint("DefaultLocale")
	private static Object invokeMethod(Class<? extends Object> c, String fieldName, Object o) {
		String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		Method method = null;
		try {
			method = c.getMethod("get" + methodName);
			return method.invoke(o);
		} catch (Exception e) {
			LogUtil.info(JsonUtils.class, e.getMessage());
			return "";
		}
	}

	public static void saveJsontoLocal(Context context, String jsonData, String fileName) {
		if (TextUtils.isEmpty(jsonData)) {
			return;
		}
		String filePath = FileUtils.getDiskCacheDir(context, "jsonCache").getAbsolutePath().concat(File.separator)
				.concat(FileUtils.md5(fileName));
		File file = new File(filePath);
		File parentFile = file.getParentFile().getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
		File sub_parentFile = file.getParentFile();
		if (!sub_parentFile.exists()) {
			sub_parentFile.mkdirs();
		}
		try {
			file.createNewFile();
			BufferedReader reader = new BufferedReader(new StringReader(jsonData));
			FileOutputStream outStream = new FileOutputStream(filePath, true);
			String lineContent = null;
			while ((lineContent = reader.readLine()) != null) {
				outStream.write(lineContent.getBytes());
			}
			outStream.flush();
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String readJsonFromLocal(Context context, String fileName) {
		// String jsonStr = null;
		String jsonData = null;
		String filePath = FileUtils.getDiskCacheDir(context, "jsonCache").getAbsolutePath().concat(File.separator)
				.concat(FileUtils.md5(fileName));
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(512);
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(filePath);
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, len);
			}
			byte[] data = outputStream.toByteArray();
			outputStream.close();
			inputStream.close();
			jsonData = new String(data);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonData;

	}

}
