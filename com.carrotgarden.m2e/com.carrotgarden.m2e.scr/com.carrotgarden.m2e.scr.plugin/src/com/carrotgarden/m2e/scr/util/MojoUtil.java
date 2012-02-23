package com.carrotgarden.m2e.scr.util;

import java.util.List;

public class MojoUtil {

	public static boolean isValid(final String text) {
		return text != null && text.length() > 0;
	}

	public static boolean isValid(final List<?> list) {
		return list != null && list.size() > 0;
	}

	public static boolean isValid(final Object[] array) {
		return array != null && array.length > 0;
	}

}
