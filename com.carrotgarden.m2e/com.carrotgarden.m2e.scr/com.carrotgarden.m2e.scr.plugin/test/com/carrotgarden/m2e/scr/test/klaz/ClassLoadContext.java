/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.m2e.scr.test.klaz;

public class ClassLoadContext {

	public final Class getCallerClass() {
		return m_caller;
	}

	ClassLoadContext(final Class caller) {
		m_caller = caller;
	}

	private final Class m_caller;

} // End of class