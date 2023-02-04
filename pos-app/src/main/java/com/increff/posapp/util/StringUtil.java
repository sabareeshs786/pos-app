package com.increff.posapp.util;

import com.increff.posapp.service.ApiException;

public class StringUtil {

	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static String toLowerCase(String s) {
		return s == null ? null : s.trim().toLowerCase();
	}

	public static void checkValid(String s) throws ApiException {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < Integer.MAX_VALUE && i < s.length(); i++) {
			try {
				sb.append(s.charAt(i));
			} catch (Throwable e) {
				throw new ApiException("Maximum limit of characters exceeded");
			}
		}
	}
}
