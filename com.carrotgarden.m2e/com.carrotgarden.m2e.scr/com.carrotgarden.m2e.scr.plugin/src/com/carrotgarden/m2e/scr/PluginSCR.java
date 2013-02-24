/**
 * Copyright (C) 2010-2013 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.m2e.scr;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;

public class PluginSCR extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.carrotgarden.m2e.scr.plugin";

	private static PluginSCR PLUGIN;

	public static PluginSCR getPlugin() {
		return PLUGIN;
	}

	public static String getHeader() {

		final Version version = getPlugin().getBundle().getVersion();

		final String header = PluginSCR.PLUGIN_ID + "-" + version;

		return header;

	}

	public PluginSCR() {

	}

	@Override
	public void start(final BundleContext context) throws Exception {

		super.start(context);

		PLUGIN = this;

	}

	@Override
	public void stop(final BundleContext context) throws Exception {

		PLUGIN = null;

		super.stop(context);

	}

	public static void logInfo(final String message) {

		final IStatus status = new Status(IStatus.INFO, PluginSCR.PLUGIN_ID, 0,
				message, null);

		getPlugin().getLog().log(status);

	}

	public static void logError(final String message, final Throwable exception) {

		final IStatus status = new Status(IStatus.ERROR, PluginSCR.PLUGIN_ID,
				0, message, exception);

		getPlugin().getLog().log(status);

	}

}
