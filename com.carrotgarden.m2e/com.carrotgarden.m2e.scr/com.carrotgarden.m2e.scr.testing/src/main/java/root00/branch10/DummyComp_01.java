/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package root00.branch10;

import java.util.concurrent.Executor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

//
// should ignore runnable
//
@Component
public class DummyComp_01 implements Cloneable, Runnable {

	//////////////
	/////////////////

	@Reference(name = "1133-3311")
	void bind(final Executor executor) {
	}

	void unbind(final Executor executor) {
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
	
	//
	
	// ///////////////////

}

//
// /////////
////
