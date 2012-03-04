/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package root00.branch12;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Property;

@Component
public class DummyComp_00 {

	// //////////////////////

	private String name;

	@Property
	static final String PROP = "prop value";

}

//
