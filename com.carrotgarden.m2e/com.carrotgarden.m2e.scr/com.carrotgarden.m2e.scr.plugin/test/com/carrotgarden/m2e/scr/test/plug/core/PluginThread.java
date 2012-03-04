/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.m2e.scr.test.plug.core;

class PluginThread extends Thread {

	PluginThread(final Runnable target) {
		super(target);
	}

	@Override
	public void run() {

		final SecurityManager managerOld = System.getSecurityManager();

		final PluginSecurityManager managerNew = new PluginSecurityManager();

		System.setSecurityManager(managerNew);

		managerNew.enableSandbox();

		super.run();

		managerNew.disableSandbox();

		System.setSecurityManager(managerOld);

	}

}
