package com.carrotgarden.m2e.scr;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.plugin.MojoExecution;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @see <a href=
 *      "https://github.com/sonatype/sisu-build-api/tree/master/src/main/java/org/sonatype/plexus/build/incremental"
 *      />
 */
public class BuildParticipant extends MojoExecutionBuildParticipant {

	private static final Logger log = LoggerFactory
			.getLogger(BuildParticipant.class);

	public BuildParticipant(final MojoExecution execution,
			final boolean runOnIncremental, final boolean runOnConfiguration) {

		super(execution, runOnIncremental, runOnConfiguration);

	}

	//

	protected IMaven getMaven() {
		return MavenPlugin.getMaven();
	}

	//

	protected boolean getPropertyBoolean(final String name) throws Exception {
		return getParameterValue(name, Boolean.class);
	}

	protected String getPropertyString(final String name) throws Exception {
		return getParameterValue(name, String.class);
	}

	@SuppressWarnings("unchecked")
	protected List<String> getPropertyList(final String name) throws Exception {
		return getParameterValue(name, List.class);
	}

	@SuppressWarnings("unchecked")
	protected Set<String> getPropertySet(final String name) throws Exception {
		return getParameterValue(name, Set.class);
	}

	@SuppressWarnings("unchecked")
	protected Map<String, String> getPropertyMap(final String name)
			throws Exception {
		return getParameterValue(name, Map.class);
	}

	//

	/** does not work */
	@SuppressWarnings("unchecked")
	protected Map<String, String> getPropertyMapXXX(final String name)
			throws Exception {
		return getMaven().getMojoParameterValue(getSession(),
				getMojoExecution(), name, Map.class);
	}

	//

	protected enum BuildAction {
		DO_SKIP, //
		DO_FULL, //
		DO_INCR, //
	}

	protected enum BuildType {

		TYPE_PRECONFIGURE(PRECONFIGURE_BUILD, BuildAction.DO_SKIP), //

		TYPE_CLEAN_BUILD(CLEAN_BUILD, BuildAction.DO_FULL), //
		TYPE_FULL_BUILD(FULL_BUILD, BuildAction.DO_FULL), //

		TYPE_AUTO_BUILD(AUTO_BUILD, BuildAction.DO_INCR), //
		TYPE_INCREMENTAL(INCREMENTAL_BUILD, BuildAction.DO_INCR), //

		;

		public final int kind;
		public final String comment;
		public final BuildAction action;

		BuildType(final int kind, final BuildAction action) {
			this.kind = kind;
			this.action = action;
			this.comment = name() + "/" + action;
		}

		public static BuildType fromKind(final int kind) {
			for (final BuildType known : BuildType.values()) {
				if (known.kind == kind) {
					return known;
				}
			}
			return TYPE_CLEAN_BUILD;
		}

	}

	protected enum BuildResult {

		SKIP("no source file changes found; skipping build."), //
		NORMAL("build sucress; done."), //
		CANCEL("build cancelation requested; aboring."), //
		ERROR("error occurred during the build; terminating"), //

		;

		public final String comment;

		BuildResult(final String comment) {
			this.comment = comment;
		}

	}

	protected enum ClassesSelector {

		COMPILE() {

			@Override
			public List<String> getSourceRoots(final BuildParticipant builder) {
				return builder.getSession().getCurrentProject()
						.getCompileSourceRoots();
			}

			@Override
			public List<String> getClasspathElements(
					final BuildParticipant builder)
					throws DependencyResolutionRequiredException {
				return builder.getSession().getCurrentProject()
						.getCompileClasspathElements();
			}

			@Override
			public File getOutputDirectory(final BuildParticipant builder) {
				final IWorkspaceRoot root = ResourcesPlugin.getWorkspace()
						.getRoot();
				final IFolder outputLocation = root.getFolder(builder
						.getMavenProjectFacade().getOutputLocation());
				return outputLocation.getLocation().toFile();
			}

		},

		TESTING() {

			@Override
			public List<String> getSourceRoots(final BuildParticipant builder) {
				return builder.getSession().getCurrentProject()
						.getTestCompileSourceRoots();
			}

			@Override
			public List<String> getClasspathElements(
					final BuildParticipant builder)
					throws DependencyResolutionRequiredException {
				return builder.getSession().getCurrentProject()
						.getTestClasspathElements();
			}

			@Override
			public File getOutputDirectory(final BuildParticipant builder) {
				final IWorkspaceRoot root = ResourcesPlugin.getWorkspace()
						.getRoot();
				final IFolder outputLocation = root.getFolder(builder
						.getMavenProjectFacade().getTestOutputLocation());
				return outputLocation.getLocation().toFile();
			}

		},

		;

		/** absolute file path */
		public abstract List<String> getSourceRoots(BuildParticipant builder);

		/** absolute file path */
		public abstract List<String> getClasspathElements(
				BuildParticipant builder)
				throws DependencyResolutionRequiredException;

		/** absolute file path */
		public abstract File getOutputDirectory(BuildParticipant builder);

		//

		public ClassLoader getClassLoader(final BuildParticipant builder)
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

	//

	/**
	 * 
	 * this version of parameter lookup seems to work
	 * 
	 * {@link org.eclipse.m2e.core.project.configurator.AbstractProjectConfigurator}
	 * 
	 * */
	protected <T> T getParameterValue(final String parameter,
			final Class<T> asType) throws CoreException {

		final MavenSession session = getSession();
		final MojoExecution mojoExecution = getMojoExecution();

		//

		final Plugin plugin = mojoExecution.getPlugin();

		final PluginExecution execution = new PluginExecution();
		execution.setConfiguration(mojoExecution.getConfiguration());

		final String goal = mojoExecution.getGoal();

		//

		final T value = getMaven().getMojoParameterValue(parameter, asType,
				session, plugin, execution, goal);

		// log.info("@@@ value=" + value);

		return value;

	}

}
