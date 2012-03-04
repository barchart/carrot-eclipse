/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package root00.branch07;

import java.util.concurrent.Executor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Property;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

// should ignore runnable

@Component
public class DummyComp_06 implements Cloneable, Runnable {

	@Property
	static final String VALUE= "hello";

	@Reference(name = "1133")
	void bind(final Executor executor) {
		//
	}

	void unbind(final Executor executor) {
		//
	}

	@Reference(name = "113311", policy=ReferencePolicy.DYNAMIC, cardinality=ReferenceCardinality.MULTIPLE)
	void bind(final Runnable tasker) {
		//
	}

	void unbind(final Runnable tasker) {
	}

	//
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
	}

}
