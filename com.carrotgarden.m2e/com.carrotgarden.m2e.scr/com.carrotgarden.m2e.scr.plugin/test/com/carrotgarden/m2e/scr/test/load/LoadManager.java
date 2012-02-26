package com.carrotgarden.m2e.scr.test.load;

import java.security.Permission;

public class LoadManager extends SecurityManager {

	private boolean isOn;

	public void set(final boolean on) {
		isOn = on;
	}

	@Override
	public void checkPermission(final Permission perm) {
		check(perm);
	}

	@Override
	public void checkPermission(final Permission perm, final Object context) {
		check(perm);
	}

	private void check(final Permission perm) {

		if (isOn) {

			throw new SecurityException("Permission denied");

		}

	}

}
