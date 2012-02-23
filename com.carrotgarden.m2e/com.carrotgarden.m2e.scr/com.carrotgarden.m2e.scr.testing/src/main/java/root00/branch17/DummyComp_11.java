package root00.branch17;

import java.util.concurrent.Executor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Property;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

// should ignore runnable

@Component(property={"first-name=carrot", "last-name=garden"})
public class DummyComp_11 implements Cloneable, Runnable {

	@Property
	static final String HELLO= "hello";

	@Property
	static final String HELLO_AGAIN= "hello-again";

	@Reference(name = "1133-1")
	void bind1(final Executor executor) {
	}

	void unbind1(final Executor executor) {
	}

	@Reference(name = "1133-2")
	void bind2(final Executor executor) {
	}

	void unbind2(final Executor executor) {
	}

	@Reference(name = "1133-3")
	void bind3(final Executor executor) {
	}

	void unbind3(final Executor executor) {
	}

	@Reference(name = "1133-4")
	void bind4(final Executor executor) {
	}

	void unbind4(final Executor executor) {
	}

	@Reference(name = "1133-5")
	void bind(final Executor executor) {
	}

	void unbind(final Executor executor) {
	}

	@Reference(name = "1133-add/remove")
	void addExec(final Executor executor) {
	}

	void removeExec(final Executor executor) {
	}

	@Reference(name = "113311", policy=ReferencePolicy.DYNAMIC, cardinality=ReferenceCardinality.MULTIPLE)
	void bind(final Runnable tasker) {
	}

	void unbind(final Runnable tasker) {
	}

	////////////////
	
	@Override
	public void run() {
	}

}
