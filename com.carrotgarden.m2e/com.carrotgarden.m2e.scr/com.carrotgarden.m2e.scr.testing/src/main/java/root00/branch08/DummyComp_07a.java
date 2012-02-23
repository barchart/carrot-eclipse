package root00.branch08;

import java.util.concurrent.Executor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Property;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

//////////

// should ignore runnable

@Component
public class DummyComp_07a implements Cloneable, Runnable {

	@Property
	static final String VALUE= "hello";

	@Reference(name = "1133")
	void bind(final Executor executor) {
	}

	void unbind(final Executor executor) {
	}

	@Reference(name = "1133-more")
	void addExec(final Executor executor) {
	}

	void removeExec(final Executor executor) {
	}

	@Reference(name = "113311", policy=ReferencePolicy.DYNAMIC, cardinality=ReferenceCardinality.MULTIPLE)
	void bind(final Runnable tasker) {
	}

	void unbind(final Runnable tasker) {
	}

	//
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
	}

}

///////////////
