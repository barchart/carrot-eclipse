package com.carrotgarden.m2e.scr;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecution;
import org.codehaus.plexus.util.Scanner;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.m2e.scr.util.FileUtil;
import com.carrotgarden.m2e.scr.util.MojoUtil;
import com.carrotgarden.osgi.anno.scr.make.Maker;

/**  */
public class BuildParticipantSCR extends MojoExecutionBuildParticipant {

	private static final Logger log = LoggerFactory
			.getLogger(BuildParticipantSCR.class);

	/** return null to tell m2e to skip mojo invocation */
	private final static Set<IProject> NOTHING = null;

	public BuildParticipantSCR(final MojoExecution execution) {
		super(execution, true);
	}

	/**
	 * @see <a href=
	 *      "https://github.com/sonatype/sisu-build-api/tree/master/src/main/java/org/sonatype/plexus/build/incremental"
	 *      />
	 */

	@Override
	public Set<IProject> build(final int kind, final IProgressMonitor monitor)
			throws Exception {

		final MojoExecution execution = getMojoExecution();

		final String goal = execution.getGoal();

		if ("clean".equals(goal)) {
			return buildClean(kind, monitor);
		}

		if ("generate".equals(goal)) {
			return buildGenerate(kind, monitor);
		}

		log.error("unsupported goal = " + goal);

		return NOTHING;

	}

	protected Set<IProject> buildClean(final int kind,
			final IProgressMonitor monitor) throws Exception {

		final File outputMainClasses = ClassesSelector.COMPILE
				.getOutputDirectory(this);

		final String targetDirectory = MavenPlugin.getMaven()
				.getMojoParameterValue(getSession(), getMojoExecution(),
						PropsSCR.PROP_TARGET_DIRCTORY, String.class);

		final File outputDirectorySCR = new File(outputMainClasses,
				targetDirectory);

		//

		log.info("### delete " + outputDirectorySCR);

		final boolean isDelete = FileUtil.deleteDir(outputDirectorySCR);

		if (!isDelete) {
			log.error("### delete failed");
		}

		//

		log.info("### create " + outputDirectorySCR);

		final boolean isCreate = outputDirectorySCR.mkdirs();

		if (!isCreate) {
			log.error("### create failed");
		}

		return NOTHING;

	}

	protected Set<IProject> buildGenerate(final int kind,
			final IProgressMonitor monitor) throws Exception {

		boolean isLogErrorTraces = false;

		try {

			final IMaven maven = MavenPlugin.getMaven();
			final MavenSession session = getSession();
			final MojoExecution execution = getMojoExecution();

			@SuppressWarnings("unchecked")
			final Map<String, String> eclipseSettings = maven
					.getMojoParameterValue(session, execution,
							PropsSCR.PROP_ECLIPSE_SETTINGS, Map.class);

			final Boolean isProcessMainClasses = maven.getMojoParameterValue(
					session, execution, PropsSCR.PROP_PROCESS_COMPILE,
					Boolean.class);

			final Boolean isProcessTestClasses = maven.getMojoParameterValue(
					session, execution, PropsSCR.PROP_PROCESS_TESTING,
					Boolean.class);

			//

			final SettingsSCR settings = new SettingsSCR(eclipseSettings);

			isLogErrorTraces = settings.isLogErrorTraces();

			if (getBuildContext().isIncremental()) {

				if (settings.isLogInvocationDetails()) {
					log.info("################# m2e carrot-maven-scr-plugin ################# ");
					log.info("### incremental generate");
				}

				if (isProcessMainClasses) {
					buildGenerate(settings, ClassesSelector.COMPILE, kind,
							monitor);
				}

				if (isProcessTestClasses) {
					buildGenerate(settings, ClassesSelector.TESTING, kind,
							monitor);
				}

				return NOTHING;

			} else {

				log.info("################# m2e carrot-maven-scr-plugin ################# ");
				log.info("###");
				log.info("### batch clean");
				final Set<IProject> resultClean = buildClean(kind, monitor);
				log.info("###");
				log.info("### batch generate");
				final Set<IProject> resultBuild = super.build(kind, monitor);

				return resultBuild;

			}

		} catch (final Exception e) {

			final StackTraceElement[] trace = e.getStackTrace();
			if (trace != null && isLogErrorTraces) {
				log.warn("### exception");
				for (final StackTraceElement entry : trace) {
					log.warn("### {}", entry);
				}
			}

			log.error("### message = '{}'", e.getMessage());

			throw e;

		}

	}

	protected Set<IProject> buildGenerate(final SettingsSCR settings,
			final ClassesSelector selector, final int kind,
			final IProgressMonitor monitor) throws Exception {

		final IMaven maven = MavenPlugin.getMaven();
		final MavenSession session = getSession();
		final MojoExecution execution = getMojoExecution();

		@SuppressWarnings("unchecked")
		final Set<String> excludedServices = maven.getMojoParameterValue(
				session, execution, PropsSCR.PROP_EXCLUDED_SERVICES, Set.class);

		final String outputExtension = maven.getMojoParameterValue(session,
				execution, PropsSCR.PROP_OUTPUT_EXTENSION, String.class);

		final String targetDirectory = maven.getMojoParameterValue(session,
				execution, PropsSCR.PROP_TARGET_DIRCTORY, String.class);

		final File outputMainClasses = ClassesSelector.COMPILE
				.getOutputDirectory(this);

		/** .../target/classes/OSGI-INF/service-component */
		final File outputDirectorySCR = new File(outputMainClasses,
				targetDirectory);

		final List<String> sourceRoots = selector.getSourceRoots(this);

		final boolean isValidSourceRoots = MojoUtil.isValid(sourceRoots);

		if (settings.isLogInvocationDetails()) {
			log.info("### selector={}", selector);
			log.info("### sourceRoots={}", sourceRoots);
			log.info("### targetDirectory={}", targetDirectory);
			if (!isValidSourceRoots) {
				log.info("### skip invocation : no valid source roots");
			}
		}

		if (!isValidSourceRoots) {
			return NOTHING;
		}

		if (settings.isLogExcludedServices()) {
			log.info("### excludedServices={}", excludedServices);
		}

		final long timeStart = System.nanoTime();

		int descriptorCounter = 0;

		/** descriptor generator */
		final Maker maker = new Maker(excludedServices);

		/** compile vs testing */
		final ClassLoader loader = selector.getClassLoader(this);

		/** iterate over source root folders */
		for (final String rootPath : sourceRoots) {

			if (!MojoUtil.isValid(rootPath)) {
				continue;
			}

			if (settings.isLogInvocationDetails()) {
				log.info("### sourceRoot={}", rootPath);
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
					log.warn("### monitor is cancelled; terminating");
					return NOTHING;
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

				final Class<?> sourceKlaz = Class.forName(sourceName, true,
						loader);

				if (settings.isLogInvocationDetails()) {
					log.info("### name : {}", sourceKlaz.getName());
				}

				/** produce component descriptor */
				final String text = maker.make(sourceKlaz);

				if (text == null) {

					continue;

				} else {

					/** com.example.impl.Component.xml */
					final String name = sourceKlaz.getName() + "."
							+ outputExtension;

					final File file = new File(outputDirectorySCR, name);

					FileUtil.writeTextFile(file, text);

					if (settings.isLogComponentDescriptors()) {
						log.info("### file={}", file);
						log.info("### descriptor : \n{}", text);
					}

					descriptorCounter++;

				}

			}

		}

		final long timeFinish = System.nanoTime();

		if (settings.isLogBuildTimes()) {
			final long timeDiff = timeFinish - timeStart;
			final long timeRate = descriptorCounter == 0 ? 0 : timeDiff
					/ descriptorCounter;
			log.info("### nanos total = {}", timeDiff);
			log.info("### nanos per class  = {}", timeRate);
		}

		return NOTHING;

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

	protected enum ClassesSelector {

		COMPILE() {

			@Override
			public List<String> getSourceRoots(final BuildParticipantSCR builder) {
				return builder.getSession().getCurrentProject()
						.getCompileSourceRoots();
			}

			@Override
			public List<String> getClasspathElements(
					final BuildParticipantSCR builder)
					throws DependencyResolutionRequiredException {
				return builder.getSession().getCurrentProject()
						.getCompileClasspathElements();
			}

			@Override
			public File getOutputDirectory(final BuildParticipantSCR builder) {
				final IWorkspaceRoot root = ResourcesPlugin.getWorkspace()
						.getRoot();
				final IFolder outputLocation = root.getFolder(builder
						.getMavenProjectFacade().getOutputLocation());
				return outputLocation.getLocation().toFile();
			}

		},

		TESTING() {

			@Override
			public List<String> getSourceRoots(final BuildParticipantSCR builder) {
				return builder.getSession().getCurrentProject()
						.getTestCompileSourceRoots();
			}

			@Override
			public List<String> getClasspathElements(
					final BuildParticipantSCR builder)
					throws DependencyResolutionRequiredException {
				return builder.getSession().getCurrentProject()
						.getTestClasspathElements();
			}

			@Override
			public File getOutputDirectory(final BuildParticipantSCR builder) {
				final IWorkspaceRoot root = ResourcesPlugin.getWorkspace()
						.getRoot();
				final IFolder outputLocation = root.getFolder(builder
						.getMavenProjectFacade().getTestOutputLocation());
				return outputLocation.getLocation().toFile();
			}

		},

		;

		/** absolute file path */
		public abstract List<String> getSourceRoots(BuildParticipantSCR builder);

		/** absolute file path */
		public abstract List<String> getClasspathElements(
				BuildParticipantSCR builder)
				throws DependencyResolutionRequiredException;

		/** absolute file path */
		public abstract File getOutputDirectory(BuildParticipantSCR builder);

		//

		public ClassLoader getClassLoader(final BuildParticipantSCR builder)
				throws Exception {

			final List<String> pathList = getClasspathElements(builder);

			final URL[] entryUrlArray = new URL[pathList.size()];

			int index = 0;
			for (final String path : pathList) {
				final URL entryURL = new File(path).toURI().toURL();
				log.debug("\t### found class path entry = " + entryURL);
				entryUrlArray[index++] = entryURL;
			}

			/** m2e class loader */
			final ClassLoader TCCL = Thread.currentThread()
					.getContextClassLoader();

			/** class path loader for a selector scope */
			final URLClassLoader loader = new URLClassLoader(entryUrlArray,
					TCCL);

			return loader;

		}

	}

}
