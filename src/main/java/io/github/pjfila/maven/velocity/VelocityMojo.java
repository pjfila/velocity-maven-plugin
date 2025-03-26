package io.github.pjfila.maven.velocity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

import lombok.Setter;
import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.runtime.RuntimeConstants;
import org.codehaus.plexus.util.FileUtils;

/**
 * @author www.slide.se
 * @author javamonkey79 - Shaun Elliott
 */
@Mojo(name = "velocity", defaultPhase = LifecyclePhase.NONE)
public class VelocityMojo extends AbstractMojo {
	
	/**
	 * The maven project.
	 */
	@Parameter(property = "project", readonly = true)
	@Setter
	private MavenProject project;
	
	/**
	 * The character encoding scheme to be applied when filtering resources. Must not be null.
	 */
	@Parameter(property = "encoding", defaultValue = "${project.build.sourceEncoding}")
	@Setter
	private String encoding;

	/**
	 * Base directory to look for resources.
	 */
	@Parameter(property = "resourceLoaderPath", defaultValue = "${project.build.absolutePath}")
	@Setter
	private List<String> resourceLoaderPath;
	
	/**
	 * Location of the file.
	 */
	@Parameter(property = "outputDirectory", required = true)
	@Setter
	private File outputDirectory;
	
	/**
	 * Template files. The files to apply velocity on.
	 */
	@Parameter(property = "templateFiles", required = true)
	@Setter
	private FileSet templateFiles;
	
	/**
	 * Template values
	 */
	@Parameter(property = "templateValues")
	@Setter
	private Properties templateValues;

	/**
	 * Extra properties to be set to VelocityEngine
	 */
	@Parameter(property = "extraProperties")
	@Setter
	private Map<String, String> extraProperties;

	/**
	 * Set this parameter if you want the plugin to remove an unwanted extension when saving result.<br>
	 * For example {@code "foo.xml.vtl"} will become {@code "foo.xml"} if removeExtension = {@code '.vtl'}.<br>
	 * Null and empty means no substitution.
	 */
	@Parameter(property = "removeExtension")
	@Setter
	private String removeExtension;

	/**
	 * Optional, remove prefix from template file, similar to {@link #removeExtension}
	 * Null and empty means no substitution.
	 */
	@Parameter(property = "removePrefix")
	@Setter
	private String removePrefix;

	/**
	 * Velocity engine instance.
	 */
	private VelocityEngine velocity;
	
	public void execute() throws MojoExecutionException {
		
		getLog().info("Executing Velocity");
		try {
			velocity = new VelocityEngine();
			velocity.setProperty(VelocityEngine.RUNTIME_LOG_INSTANCE, new LogHandler(this));

			velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
			velocity.setProperty("file.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.FileResourceLoader");

			// Provide all directories as a single comma-separated path
			String fileResourceLoaderPath = resourceLoaderPath.stream()
					.collect(Collectors.joining(","));
			velocity.setProperty("file.resource.loader.path", fileResourceLoaderPath);

			// Configure encodings
			velocity.setProperty("input.encoding", getEncoding());
			velocity.setProperty("output.encoding", getEncoding());

			if (extraProperties != null) {
				for (Map.Entry<String, String> extraProperty : extraProperties.entrySet()) {
					velocity.setProperty(extraProperty.getKey(), extraProperty.getValue());
				}
			}

			velocity.init();
			
			List<?> fileNames = expandFileSet();
			if (fileNames == null) {
				getLog().warn("Empty fileset");
			} else {
				getLog().debug("Translating files");
				Iterator<?> i = fileNames.iterator();
				while (i.hasNext()) {
					String templateFile = (String) i.next();
					translateFile(templateFiles.getDirectory(), templateFile);
				}
			}
		} catch (ResourceNotFoundException e) {
			throw new MojoExecutionException("Resource not found", e);
		} catch (VelocityException e) {
			getLog().info(e.getMessage());
		} catch (MojoExecutionException e) {
			throw e;
		} catch (IOException e) {
			throw new MojoExecutionException("Failed to save result", e);
		} catch (Exception e) {
			getLog().error(e);
			throw new MojoExecutionException("Unexpected", e);
		}
	}
	
	private void addPropertiesToContext(VelocityContext context, Properties prop) {
		getLog().debug("Exporting properties to context: " + prop);
		if (prop == null) {
			return;
		}
		Enumeration<?> propEnumeration = prop.propertyNames();
		while (propEnumeration.hasMoreElements()) {
			String key = (String) propEnumeration.nextElement();
			String value = prop.getProperty(key);
			getLog().debug(key + "=" + value);
			context.put(key, value);
		}
	}
	
	private List<?> expandFileSet() throws IOException, MojoExecutionException {
		String baseDir = project.getBasedir().getAbsolutePath();
		String templateFilesDirectory = templateFiles.getDirectory();
		if (templateFilesDirectory != null) {
			baseDir += File.separator + templateFilesDirectory;
		}
		File baseDirFile = new File(baseDir);

		getLog().debug("baseDir: " + baseDirFile.getAbsolutePath());
		String includes = list2CvsString(templateFiles.getIncludes());
		getLog().debug("includes: " + includes);
		String excludes = list2CvsString(templateFiles.getExcludes());
		getLog().debug("excludes: " + excludes);
		if (!baseDirFile.exists()) {
			throw new MojoExecutionException("Base directory does not exists: " + baseDirFile.getAbsolutePath());
		}
		return FileUtils.getFileNames(baseDirFile, includes, excludes, false);
	}
	
	private String list2CvsString(List<?> patterns) {
		String delim = "";
		StringBuffer buf = new StringBuffer();
		if (patterns != null) {
			Iterator<?> i = patterns.iterator();
			while (i.hasNext()) {
				buf.append(delim).append(i.next());
				delim = ", ";
			}
		}
		return buf.toString();
	}
	
	private void translateFile(String basedir, String templateFile)
			throws VelocityException, MojoExecutionException, IOException
	
	{
		String outputFilename = getOutputFilename(templateFile);
		getLog().debug("(IN) " + templateFile + " -> (OUT) " + outputFilename);
		VelocityContext context = new VelocityContext();
		addPropertiesToContext(context, templateValues);
		context.put("project", project);
		StringWriter sw = new StringWriter();
		Template template = velocity.getTemplate(templateFile);
		template.merge(context, sw);

		File outputFile = new File(outputDirectory, outputFilename);
		File dir = outputFile.getParentFile();
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new MojoExecutionException("Failed to create outputDirectory");
			}
		}

		try (FileOutputStream os = new FileOutputStream(outputFile)) {
			os.write(sw.toString().getBytes(getEncoding()));
		}
		getLog().info("Write file " + outputFile);
	}

	private String getOutputFilename(String templateFilename) {
		File templateFile = new File(templateFilename);
		String basename = templateFile.getName();

		if (!isBlank(removeExtension) && basename.endsWith(removeExtension)) {
			if (basename.equals(removeExtension)) {
				getLog().warn("removeExtension equals filename will not remove it (" + basename + ")");
			} else {
				basename = basename.substring(0, basename.length() - removeExtension.length());
			}
		}
		if (!isBlank(removePrefix) && basename.startsWith(removePrefix)) {
			if (basename.equals(removePrefix)) {
				getLog().warn("removePrefix equals basename will not remove it (" + basename + ")");
			} else {
				basename = basename.substring(removePrefix.length());
			}
		}

		return new File(templateFile.getParentFile(), basename).toString();
	}

	private String getEncoding() {
		return encoding == null ? "UTF-8" : encoding;
	}

	private boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
	
}
