package com.carrotgarden.m2e.scr.test.plug.core;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import com.carrotgarden.m2e.scr.test.plug.api.Plugin;

public class PluginTest {

	static final String NAME = "com.carrotgarden.m2e.scr.plug.plugin.PluginProvider";

	public static void pluginTest(final String pathToJar) {

		try {

			final File file = new File(pathToJar);

			final URL url = file.toURI().toURL();

			final URLClassLoader loader = new URLClassLoader(
					new java.net.URL[] { url });

			final Class<?> klaz = Class.forName(NAME, false, loader);

			final Plugin plugin = (Plugin) klaz.newInstance();

			final PluginThread thread = new PluginThread(new Runnable() {
				@Override
				public void run() {
					plugin.run();
				}
			});

			thread.start();

		} catch (final Exception ex) {

			ex.printStackTrace();

		}

	}

}
