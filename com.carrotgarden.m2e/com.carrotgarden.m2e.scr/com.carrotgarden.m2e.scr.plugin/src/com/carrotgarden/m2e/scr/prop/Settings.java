/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.m2e.scr.prop;

import java.util.HashMap;
import java.util.Map;

public class Settings {

	private final Map<String, String> settings;

	public Settings(final Map<String, String> settings) {
		this.settings = //
		settings == null ? new HashMap<String, String>() : settings;
	}

	public boolean isLogExcludedServices() {
		return Boolean.parseBoolean(settings
				.get(Props.LOG_EXCLUDED_SERVICES));
	}

	public boolean isLogComponentDescriptors() {
		return Boolean.parseBoolean(settings
				.get(Props.LOG_COMPONENT_DESCRIPTORS));
	}

	public boolean isLogInvocationDetails() {
		return Boolean.parseBoolean(settings
				.get(Props.LOG_INVOCATION_DETAILS));
	}

	public boolean isLogBuildTimes() {
		return Boolean.parseBoolean(settings.get(Props.LOG_BUILD_TIMES));
	}

	public boolean isLogErrorTraces() {
		return Boolean.parseBoolean(settings.get(Props.LOG_ERROR_TRACES));
	}

}
