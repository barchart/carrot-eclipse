package com.carrotgarden.m2e.scr.test.plug.impl;

import com.carrotgarden.m2e.scr.test.plug.api.Plugin;

public class PluginProvider implements Plugin {

	@Override
	public void run() {

		// ClassNotFoundException with SecurityManager
		// new AnotherClassInTheSamePlugin();

		// permitted without a SecurityManager
		// doSomethingDangerous();

	}

	private void doSomethingDangerous() {
		// use your imagination

	}

}
