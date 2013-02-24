/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
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
