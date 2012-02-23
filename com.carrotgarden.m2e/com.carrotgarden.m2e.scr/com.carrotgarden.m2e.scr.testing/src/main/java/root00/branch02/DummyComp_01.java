package root00.branch02;

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
