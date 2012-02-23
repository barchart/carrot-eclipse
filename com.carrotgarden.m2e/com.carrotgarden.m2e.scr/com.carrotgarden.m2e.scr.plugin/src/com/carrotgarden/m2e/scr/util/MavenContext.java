package com.carrotgarden.m2e.scr.util;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.project.MavenProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MavenContext {

	private static final Logger log = LoggerFactory
			.getLogger(MavenContext.class);

	private final MavenSession session;

	private final MojoExecution execution;

	public MavenContext(final MavenSession session,
			final MojoExecution execution) {

		this.session = session.clone();

		this.execution = execution;

	}

	public MavenSession getSession() {
		return session;
	}

	public MojoExecution getExecution() {
		return execution;
	}

	public String getKey() {

		final MavenProject project = session.getCurrentProject();

		final String projId = project.getId();

		final String execId = execution.toString();

		final String key = projId + "/" + execId;

		return key;

	}

	// private volatile IProgressMonitor monitor;

	public void execute(final IProgressMonitor monitor) {

		// this.monitor = monitor;

		log.info("### EXECUTE @ CONTEXT : {}", getKey());

		final IMaven maven = MavenPlugin.getMaven();

		maven.execute(session, execution, monitor);

	}

	public void cancel() {

		log.info("### CANCEL  @ CONTEXT : {}", getKey());

		// final IProgressMonitor monitor = this.monitor;
		// if (monitor != null) {
		// monitor.setCanceled(true);
		// }

	}

}
