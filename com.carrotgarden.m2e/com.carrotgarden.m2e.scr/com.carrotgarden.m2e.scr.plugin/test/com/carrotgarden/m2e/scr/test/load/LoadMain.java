package com.carrotgarden.m2e.scr.test.load;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;

public class LoadMain {

	static void log(final String text) {
		System.out.println(text);
	}

	final static String NAME = "com.carrotgarden.m2e.scr.test.load.UserKlaz";

	public static void main(final String[] args) throws Throwable {

		log("init");

		//

		final String path = "target/classes/";

		final File file = new File(path);

		final URL url = file.toURI().toURL();

		log("url=" + url);

		final URLClassLoader loader = new URLClassLoader(new URL[] { url });

		//

		final PermissionCollection perms = new Permissions();
		perms.add(new RuntimePermission("accessDeclaredMembers"));

		final AccessControlContext context = new AccessControlContext(
				new ProtectionDomain[] { new ProtectionDomain(null, perms) });

		final Class<?> klaz = AccessController.doPrivileged(
				new PrivilegedExceptionAction<Class<?>>() {
					@Override
					public Class<?> run() throws Exception {
						return Class.forName(NAME, true, loader);
					}
				}, context);

		log("klaz=" + klaz.getName());

		//

		log("done");

	}

}
