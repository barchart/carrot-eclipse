package com.carrotgarden.m2e.scr;

import java.io.File;

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.MavenProjectChangedEvent;
import org.eclipse.m2e.core.project.configurator.AbstractBuildParticipant;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.m2e.jdt.AbstractJavaProjectConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * m2e will LOAD this class when project is first built / open
 * 
 * m2e will INIT this class when project on: clean, full build, incremental
 * build;
 */
public class ProjectConfiguratorSCR extends AbstractJavaProjectConfigurator {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(ProjectConfiguratorSCR.class);

	static {
		// log.info("######### getBuildParticipant LOAD");
	}

	{
		// log.info("######### getBuildParticipant INIT");
	}

	@Override
	public AbstractBuildParticipant getBuildParticipant(
			final IMavenProjectFacade projectFacade,
			final MojoExecution execution,
			final IPluginExecutionMetadata executionMetadata) {

		// log.info("######### getBuildParticipant {} {}", //
		// projectFacade, execution);

		// final MavenProject project = projectFacade.getMavenProject();

		return new BuildParticipantSCR(execution);

	}

	/** this configuration does not generate java source files */
	@Override
	protected File[] getSourceFolders(
			final ProjectConfigurationRequest request,
			final MojoExecution mojoExecution) throws CoreException {

		// final List<String> sourceRoots = request.getMavenProject()
		// .getCompileSourceRoots();

		return new File[] {};

	}

	@Override
	protected String getOutputFolderParameterName() {

		throw new UnsupportedOperationException("non use");

	}

	protected boolean hasChangedDependencies(
			final MavenProjectChangedEvent event) {
		return MavenProjectChangedEvent.KIND_CHANGED == event.getKind() && //
				MavenProjectChangedEvent.FLAG_DEPENDENCIES == event.getFlags();
	}

	// @Override
	// public void mavenProjectChanged(final MavenProjectChangedEvent event,
	// final IProgressMonitor monitor) throws CoreException {
	//
	// log.info("######### mavenProjectChanged kind={} flags={}",
	// event.getKind(), event.getFlags());
	//
	// final IMavenProjectFacade project = event.getMavenProject();
	//
	// if (hasChangedDependencies(event)) {
	// log.info("######### mavenProjectChanged YES");
	// } else {
	// log.info("######### mavenProjectChanged NON");
	// }
	//
	// final IPath[] main = project.getCompileSourceLocations();
	//
	// for (final IPath path : main) {
	// log.info("### path=" + path);
	// }
	//
	// }

}
