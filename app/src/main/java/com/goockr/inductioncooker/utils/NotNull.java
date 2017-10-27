package com.goockr.inductioncooker.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class NotNull {


	private static String naN;

	public static boolean isNotNull(Integer i) {
		if (null != i && 0 != i) {
			return true;
		}
		return false;
	}

	public static boolean isNotNull(Double d) {
		if (null != d && 0 != d) {
			return true;
		}
		return false;
	}

	public static boolean isNotNull(Object object) {
		if (null != object && !"".equals(object)) {
			return true;
		}
		return false;
	}

	public static boolean isNotNull(List<?> t) {
		if (null != t && t.size() > 0) {
			return true;
		}
		return false;
	}

	public static boolean isNotNull(Map<?, ?> t) {
		if (null != t && t.size() > 0) {
			return true;
		}
		return false;
	}

	public static boolean isNotNull(Object[] objects) {
		if (null != objects && objects.length > 0) {
			return true;
		}
		return false;
	}

	public static boolean isNotNull(JSONArray jsonArray) {
		if (null != jsonArray && jsonArray.length() > 0) {
			return true;
		}
		return false;
	}

	public static boolean isNotNull(JSONObject jsonObject) {
		if (null != jsonObject && !"".equals(jsonObject)) {
			return true;
		}
		return false;
	}

	public static boolean isNotNullAndNaN(Object object) {
		naN = "NaN";
		if (isNotNull(object) && !TextUtils.equals(object.toString(), naN)) {
			return true;
		}
		return false;
	}

}
