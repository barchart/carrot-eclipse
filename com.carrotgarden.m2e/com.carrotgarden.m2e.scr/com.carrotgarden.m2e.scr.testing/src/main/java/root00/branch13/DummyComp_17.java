package root00.branch13;

import java.util.concurrent.Executor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Property;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/** should ignore runnable */
@Component(//
factory = "factory-17", property = { "first-name=carrot", "last-name=garden" } //
)
public class DummyComp_17 implements Cloneable, Runnable {

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
	@Property
	static final String PROP_07 = "property with index 7";
	@Property
	static final String PROP_08 = "property with index 8";
	@Property
	static final String PROP_09 = "property with index 9";
	@Property
	static final String PROP_10 = "property with index 10";
	@Property
	static final String PROP_11 = "property with index 11";

	//

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

	@Reference(name = "113311-a", policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.AT_LEAST_ONE)
	void set1(final Runnable tasker) {
	}

	void unset1(final Runnable tasker) {
	}

	@Reference(name = "113311-b", //
	policy = ReferencePolicy.DYNAMIC,//
	cardinality = ReferenceCardinality.MULTIPLE,//
	target = "&(first-name=carrot)(last-name=garden)(name=11*)")
	void set2(final Runnable tasker) {
	}

	void unset2(final Runnable tasker) {
	}

	// ////////////

	@Override
	public void run() {
	}

}
