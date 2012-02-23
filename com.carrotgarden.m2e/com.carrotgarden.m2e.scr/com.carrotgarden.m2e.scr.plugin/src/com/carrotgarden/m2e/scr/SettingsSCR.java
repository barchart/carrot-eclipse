package com.carrotgarden.m2e.scr;

import java.util.HashMap;
import java.util.Map;

public class SettingsSCR {

	private final Map<String, String> settings;

	public SettingsSCR(final Map<String, String> settings) {
		this.settings = //
		settings == null ? new HashMap<String, String>() : settings;
	}

	public boolean isLogExcludedServices() {
		return Boolean.parseBoolean(settings
				.get(PropsSCR.LOG_EXCLUDED_SERVICES));
	}

	public boolean isLogComponentDescriptors() {
		return Boolean.parseBoolean(settings
				.get(PropsSCR.LOG_COMPONENT_DESCRIPTORS));
	}

	public boolean isLogInvocationDetails() {
		return Boolean.parseBoolean(settings
				.get(PropsSCR.LOG_INVOCATION_DETAILS));
	}

	public boolean isLogBuildTimes() {
		return Boolean.parseBoolean(settings.get(PropsSCR.LOG_BUILD_TIMES));
	}

	public boolean isLogErrorTraces() {
		return Boolean.parseBoolean(settings.get(PropsSCR.LOG_ERROR_TRACES));
	}

}
