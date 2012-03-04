/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.m2e.scr.prop;

public class Props {
	//

	/** carrot maven plugin configuration properties */

	/** String; */
	public static final String PROP_GOAL_CLEAN = "clean";

	/** String; */
	public static final String PROP_GOAL_GENERATE = "generate";

	//

	/** carrot maven plugin configuration properties */

	/** Map <String, String> */
	public static final String PROP_ECLIPSE_SETTINGS = "eclipseSettings";

	/** Set <String> */
	public static final String PROP_EXCLUDED_SERVICES = "excludedServices";

	/** String; "OSGI-INF/service-component" */
	public static final String PROP_TARGET_DIRCTORY = "targetDirectorySCR";

	/** String; "xml" */
	public static final String PROP_OUTPUT_EXTENSION = "outputExtensionSCR";

	/** boolean; "true" */
	public static final String PROP_PROCESS_COMPILE = "isProcessMainClasses";

	/** boolean; "false" */
	public static final String PROP_PROCESS_TESTING = "isProcessTestClasses";

	//

	/** key names in PROP_ECLIPSE_SETTINGS */

	/** boolean */
	public static final String LOG_EXCLUDED_SERVICES = "logExcludedServices";

	/** boolean */
	public static final String LOG_COMPONENT_DESCRIPTORS = "logComponentDescriptors";

	/** boolean */
	public static final String LOG_INVOCATION_DETAILS = "logInvocationDetails";

	/** boolean */
	public static final String LOG_BUILD_TIMES = "logBuildTimes";

	/** boolean */
	public static final String LOG_ERROR_TRACES = "logErrorTraces";

	//

}
