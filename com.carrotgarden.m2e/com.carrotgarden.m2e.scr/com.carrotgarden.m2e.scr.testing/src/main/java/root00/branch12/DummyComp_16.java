package root00.branch12;

import java.util.concurrent.Executor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Property;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/** should ignore runnable */ 
@Component(//////
factory = "factory-16", property = {
		"first-name=carrot", "last-name=garden" } //
)
public class DummyComp_16 implements Cloneable, Runnable {

	//////
	
	@Property
	static final String HELLO = "hello";

	@Property
	static final String HELLO_AGAIN = "hello-again";

	@Property
	static final String THANK_YOU = "thank-yours";

	@Property
	static final String PROP_01 = "property with index 1";
	@Property
	static final String PROP_02 = "property with index 2";
	@Property
	static final String PROP_03 = "property with index 3";
	@Property
	static final String PROP_04 = "property with index 4";
	@Property
	static final String PROP_05 = "property with index 5";

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
	void bind5(final Executor executor) {
	}

	void unbind5(final Executor executor) {
	}

	@Reference(name = "1133-6")
	void bind6(final Executor executor) {
	}

	void unbind6(final Executor executor) {
	}

	@Reference(name = "1133-add/remove")
	void addExec(final Executor executor) {
	}

	void removeExec(final Executor executor) {
	}

	@Reference(name = "113311", policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
	void bind(final Runnable tasker) {
	}

	void unbind(final Runnable tasker) {
	}

	// ////////////

	@Override
	public void run() {
	}

}
/////////////
////////////
//////////////////
