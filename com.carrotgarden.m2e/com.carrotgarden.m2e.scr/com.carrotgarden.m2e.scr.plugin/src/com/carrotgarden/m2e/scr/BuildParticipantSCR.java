/**
 * Copyright (C) 2010-2012 Andrei Pozolotin <Andrei.Pozolotin@gmail.com>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.carrotgarden.m2e.scr;

import static com.carrotgarden.m2e.scr.BuildParticipant.ClassesSelector.*;
import static com.carrotgarden.m2e.scr.prop.Props.*;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.plugin.MojoExecution;
import org.codehaus.plexus.util.Scanner;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.m2e.scr.prop.Settings;
import com.carrotgarden.m2e.scr.util.FileUtil;
import com.carrotgarden.m2e.scr.util.MojoUtil;
import com.carrotgarden.osgi.anno.scr.make.Maker;

/**
 * provides m2e connector for carrot-maven-scr-plugin
 */
public class BuildParticipantSCR extends BuildParticipant {

	private static final Logger log = LoggerFactory
			.getLogger(BuildParticipantSCR.class);

	/** return null to tell m2e to skip extra actions */
	private final static Set<IProject> NOTHING = null;

	public BuildParticipantSCR(final MojoExecution execution) {
		super(execution, true, true);
	}

	@Override
	public Set<IProject> build(final int kind, final IProgressMonitor monitor)
			throws Exception {

		boolean isLogErrorTraces = true;

		try {

			isLogErrorTraces = getSettings().isLogErrorTraces();

			final BuildType type = BuildType.fromKind(kind);

			log.info("#########################################################################################");
			log.info("### " + PluginSCR.getHeader() + " : " + type.comment);

			switch (type.action) {
			case DO_FULL:
				buildClean(type, monitor);
				buildGenerate(type, monitor);
				break;
			case DO_INCR:
				buildGenerate(type, monitor);
				break;
			default:
				break;
			}

			log.info("#########################################################################################");

			return NOTHING;

		} catch (final Throwable e) {

			final StackTraceElement[] trace = e.getStackTrace();
			if (trace != null && isLogErrorTraces) {
				log.warn("### error trace");
				for (final StackTraceElement entry : trace) {
					log.warn("### {}", entry);
				}
			}

			final String message = e.getMessage();

			/** print it m2e colsole log */
			log.error("### message = '{}'", message);

			/** print it eclipse error log */
			PluginSCR.logError(message, e);

			/** let m2e handle error */
			throw new Exception(e);

		}

	}

	protected Set<IProject> buildClean(final BuildType type,
			final IProgressMonitor monitor) throws Exception {

		log.info("### clean");

		final File outputDirectorySCR = getOutputDirectorySCR();

		//

		log.info("### delete " + outputDirectorySCR);

		final boolean isDelete = FileUtil.deleteDir(outputDirectorySCR);

		if (!isDelete) {
			log.warn("### delete failed");
		}

		//

		log.info("### create " + outputDirectorySCR);

		final boolean isCreate = outputDirectorySCR.mkdirs();

		if (!isCreate) {
			log.warn("### create failed");
		}

		return NOTHING;

	}

	protected Set<IProject> buildGenerate(final BuildType type,
			final IProgressMonitor monitor) throws Throwable {

		switch (type.action) {
		case DO_FULL:

			/** invoke maven plugin */

			super.build(type.kind, monitor);

			break;

		case DO_INCR:

			/** process locally */

			if (getPropertyBoolean(PROP_PROCESS_COMPILE)) {
				buildGenerate(COMPILE, type, monitor);
			}
			if (getPropertyBoolean(PROP_PROCESS_TESTING)) {
				buildGenerate(TESTING, type, monitor);
			}

			break;

		default:

			log.error("unexpected invocation type = " + type.comment);

			break;

		}

		return NOTHING;

	}

	protected BuildResult buildGenerate(final ClassesSelector selector,
			final BuildType type, final IProgressMonitor monitor)
			throws Throwable {

		final Settings settings = getSettings();

		final List<String> sourceRoots = selector.getSourceRoots(this);

		if (settings.isLogInvocationDetails()) {
			log.info("### selector = {}", selector);
			log.info("### sourceRoots= {}", sourceRoots);
		}

		if (!MojoUtil.isValid(sourceRoots)) {
			return BuildResult.SKIP;
		}

		final Set<String> excludedServices = getPropertySet(PROP_EXCLUDED_SERVICES);
		if (settings.isLogExcludedServices()) {
			log.info("### excludedServices = {}", excludedServices);
		}

		final File outputDirectorySCR = getOutputDirectorySCR();
		if (settings.isLogInvocationDetails()) {
			log.info("### outputDirectorySCR = {}", outputDirectorySCR);
		}

		final String outputExtensionSCR = getPropertyString(PROP_OUTPUT_EXTENSION);
		if (settings.isLogInvocationDetails()) {
			log.info("### outputExtensionSCR = {}", outputExtensionSCR);
		}

		final long timeStart = System.nanoTime();

		int allclassesCounter = 0;
		int descriptorCounter = 0;

		/** descriptor generator */
		final Maker maker = new Maker(excludedServices);

		/** scope: compile vs testing */
		final ClassLoader loader = selector.getClassLoader(this);

		/** iterate over source root folders */
		for (final String rootPath : sourceRoots) {

			if (!MojoUtil.isValid(rootPath)) {
				continue;
			}

			if (settings.isLogInvocationDetails()) {
				log.info("### sourceRoot = {}", rootPath);
			}

			final File rootDir = new File(rootPath);

			final Scanner scanner = getBuildContext().newScanner(rootDir);

			scanner.scan();

			final String[] includedFiles = scanner.getIncludedFiles();

			if (!MojoUtil.isValid(includedFiles)) {
				continue;
			}

			/** iterate over changed source files */
			for (final String included : includedFiles) {

				if (monitor.isCanceled()) {
					return BuildResult.CANCEL;
				}

				// final File file = new File(rootDir, included);
				// final String text = FileUtil.readTextFile(file);

				/** com/example/impl/Component.java */
				final String relative = included;

				/** com/example/impl/Component */
				final int dotIndex = relative.indexOf(".");
				final String sourcePath = relative.substring(0, dotIndex);

				/** com.example.impl.Component */
				final String sourceName = sourcePath.replace("/", ".");

				if (settings.isLogInvocationDetails()) {
					log.info("### name = {}", sourceName);
				}

				/** produce component descriptor */
				final String text = maker.make(loader, sourceName);

				final boolean isComponent = (text != null);

				if (isComponent) {

					/** com.example.impl.Component.xml */
					final String descriptorFilename = sourceName + "."
							+ outputExtensionSCR;

					final File file = new File(outputDirectorySCR,
							descriptorFilename);

					FileUtil.writeTextFile(file, text);

					if (settings.isLogComponentDescriptors()) {
						log.info("### descriptor file = \n{}", file);
						log.info("### descriptor text = \n{}", text);
					}

					descriptorCounter++;

				}

				allclassesCounter++;

			}

		}

		final long timeFinish = System.nanoTime();

		if (settings.isLogBuildTimes()) {
			final long timeDiff = timeFinish - timeStart;
			final long timeRate = descriptorCounter == 0 ? 0 : timeDiff
					/ descriptorCounter;
			log.info("### total class count = {}", allclassesCounter);
			log.info("### component class count = {}", descriptorCounter);
			log.info("### millis total = {}", timeDiff / 1000000);
			log.info("### millis per component  = {}", timeRate / 1000000);
		}

		return BuildResult.NORMAL;

	}

	private Settings settings;

	protected Settings getSettings() throws Exception {

		if (settings == null) {
			final Map<String, String> eclipseSettings = //
			getPropertyMap(PROP_ECLIPSE_SETTINGS);
			settings = new Settings(eclipseSettings);
		}

		return settings;

	}

	protected static final String COMPONENT_PACKAGE = "org.osgi.service.component.annotations";
	protected static final String COMPONENT_ANNOTATION = "@Component";

	protected boolean hasComponent(final String text) {

		if (text == null || text.length() == 0) {
			return false;
		}

		if (text.contains(COMPONENT_ANNOTATION)
				&& text.contains(COMPONENT_PACKAGE)) {
			return true;
		}

		return false;

	}

	/**
	 * .../target/classes/OSGI-INF/service-component
	 * 
	 * @throws Exception
	 */
	protected File getOutputDirectorySCR() throws Exception {

		final String targetDirectory = getPropertyString(PROP_TARGET_DIRCTORY);

		final File outputMainClasses = COMPILE.getOutputDirectory(this);

		final File outputDirectorySCR = new File(outputMainClasses,
				targetDirectory);

		return outputDirectorySCR;

	}

}
