package com.meijia.utils;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

/**
 * json处理
 *
 */
public class JsonUtil {
	/**
	 * map转json
	 *
	 * @param map
	 * @return
	 */
	public static JsonObject mapTojson(Map map) {
		JsonObject j = new JsonObject();
		j.addProperty(new Gson().toJson(map), true);
		return j;
	}

	/**
	 * string转json
	 *
	 * @param str
	 * @return
	 */
	public static JsonObject stringTojson(String str) {
		JsonObject j = new JsonObject();
		j.addProperty(str, true);
		return j;
	}

	/**
	 * json转map
	 *
	 * @param str
	 * @return
	 */
	public static Map<String, Object> stringToMap(String str) {
		GsonBuilder gb = new GsonBuilder();
		Gson g = gb.create();
		Map<String, Object> map = g.fromJson(str,
				new TypeToken<Map<String, Object>>() {
				}.getType());
		return map;
	}

	/**
	 * list转json
	 *
	 * @param list
	 * @return
	 */
	public static <T> String listTojson(List<T> list) {
		GsonBuilder gb = new GsonBuilder();
		Gson g = gb.create();
		return g.toJson(list);
	}

	public static String objectTojson(Object o) {
		GsonBuilder gb = new GsonBuilder();
		Gson g = gb.create();
		return g.toJson(o);
	}

	/**
	 * json转List<T>
	 *
	 * @param str
	 * @return
	 */
	public static <T> List<T> convert(String str) {
		GsonBuilder gb = new GsonBuilder();
		Gson g = gb.create();
		List<T> map = g.fromJson(str, new TypeToken<List<T>>() {
		}.getType());
		return map;
	}
}
