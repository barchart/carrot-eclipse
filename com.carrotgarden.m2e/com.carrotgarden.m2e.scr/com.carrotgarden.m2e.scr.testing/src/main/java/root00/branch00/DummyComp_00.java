/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package root00.branch00;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Property;
import org.osgi.service.component.annotations.Reference;

@Component
public class DummyComp_00 {//

	@SuppressWarnings("unused")
	private String name;

	@Property
	static final String PROP = "prop value"; //

	//

	@SuppressWarnings("unused")
	private Runnable task;

	@Reference
	protected void bind(final Runnable task, final Map<?, ?> map) {

	}

	protected void unbind(final Runnable task, final Map<?, ?> map) {

	}

}
