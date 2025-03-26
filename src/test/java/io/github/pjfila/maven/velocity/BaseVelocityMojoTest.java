package io.github.pjfila.maven.velocity;

import org.apache.commons.io.FileUtils;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.*;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * we just separate the boilerplate code here so that the test class(es) can focus on the important
 */
public abstract class BaseVelocityMojoTest extends AbstractMojoTestCase {

    /** {@inheritDoc} */
    @Override
    @BeforeEach
    public void setUp() throws Exception
    {
        // required
        super.setUp();
    }

    /** {@inheritDoc} */
    @Override
    @AfterEach
    public void tearDown() throws Exception
    {
        // required
        super.tearDown();
    }

    protected File activePomFile;

    protected VelocityMojo runMojoForProject(String baseDir) throws Exception {
        activePomFile = getProjectPom(baseDir);
        MojoExecution mojoExecution = newMojoExecution("velocity");
        MavenSession mavenSession = newMavenSession(activePomFile);
        VelocityMojo mojo = (VelocityMojo) lookupConfiguredMojo(mavenSession, mojoExecution);
        assertNotNull(mojo);
        mojo.execute();
        return mojo;
    }

    protected File getProjectPom(String baseDir) {
        File pomFile = new File(baseDir, "pom.xml");
        assertNotNull(pomFile);
        assertTrue(pomFile.exists());
        return pomFile;
    }

    protected MavenProject newMavenProject(File pomFile) throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try (FileReader fileReader = new FileReader(pomFile)) {
            Model model = reader.read(fileReader);
            model.setPomFile(pomFile);
            MavenProject mavenProject = new MavenProject(model);
            mavenProject.setFile(pomFile);
            return mavenProject;
        }
    }

    protected MavenSession newMavenSession(File pomFile) throws Exception {
        MavenProject project = newMavenProject(pomFile);
        return newMavenSession(project);
    }

    protected void assertFileNotExists(String filename) {
        File file = new File(activePomFile.getParentFile(), filename);
        if (file.exists()) {
            fail("File " + filename + " should not exist but does");
        }
    }

    protected void assertFileExists(String filename) {
        File file = new File(activePomFile.getParentFile(), filename);
        if (!file.exists()) {
            fail("File " + filename + " is expected to exist");
        }
    }

    protected void assertFileContent(String filename, String expectedContent) throws IOException {
        assertFileExists(filename);
        File file = new File(activePomFile.getParentFile(), filename);
        String actualContent = FileUtils.readFileToString(file);
        if (!expectedContent.equals(actualContent)) {
            StringBuilder sb = new StringBuilder();
            sb.append("File content is different from expected, file " + filename);
            sb.append("\nexpected:\n" + expectedContent + "\n");
            sb.append("\nactual:\n" + actualContent + "\n");
            fail(sb.toString());
        }
    }

}