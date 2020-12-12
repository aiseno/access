package com.github.aiseno.access.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author admin
 */
public class JSONKit {
	
	private static final Gson GSON = new GsonBuilder().create();

	public static <T> T o2t(Object object,Class<T> classs) {
		return GSON.fromJson(o2s(object), classs);
	}
	
	public static String o2s(Object object) {
		if(object == null || "".equals(object.toString())) {
		   return "";
		}
		return GSON.toJson(object);
	}
}
