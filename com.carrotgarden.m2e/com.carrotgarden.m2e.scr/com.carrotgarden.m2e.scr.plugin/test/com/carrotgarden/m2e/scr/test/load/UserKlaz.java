package com.carrotgarden.m2e.scr.test.load;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UserKlaz {

	static void log(final String text) {
		System.out.println(text);
	}

	static void read(final String location) throws Exception {

		final URL oracle = new URL(location);

		final URLConnection connection = oracle.openConnection();

		final BufferedReader input = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));

		String line;
		while ((line = input.readLine()) != null) {
			log(line);
		}

		input.close();

	}

	static {

		log("load UserKlaz");

		try {
			read("http://carrot-garden.github.com/carrot-eclipse/repository");
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

}
