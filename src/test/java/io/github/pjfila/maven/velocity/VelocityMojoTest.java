package io.github.pjfila.maven.velocity;

import org.junit.jupiter.api.Test;

public class VelocityMojoTest extends BaseVelocityMojoTest {

    @Test
    public void testMinimalProject() throws Exception {
        // minimal features project
        runMojoForProject("src/test/resources/project-minimal");
        assertFileContent("target/template.vm", "HEADER\nBODY");
        assertFileContent("target/sub/template.vm", "HEADER\nBODY");
        assertFileContent("target/directive.if.empty_check.vm", "HEADER\nEMPTY VAR IS FALSE");

        assertFileNotExists("target/include/header.vm");
    }

    @Test
    public void testFullProject() throws Exception {
        // all features
        runMojoForProject("src/test/resources/project-full");
        assertFileContent("target/template", "HEADER\nEXTRA\nALPHA\nBETA");
        assertFileContent("target/sub/template", "HEADER\nEXTRA\nBODY ALPHA");
        assertFileContent("target/removeprefix", "HEADER\nEXTRA\nPROCESS ME!");
        assertFileContent("target/directive.if.empty_check", "HEADER\nEXTRA\nEMPTY VAR IS TRUE");
        assertFileContent("target/too-short/.vm", "HEADER\nDO NOT RENAME ME");
        assertFileContent("target/too-short/tmpl.", "HEADER\nDO NOT RENAME ME");

        assertFileNotExists("target/include/header.vm");
        assertFileNotExists("target/template.vm");
        assertFileNotExists("target/sub/template.vm");
        assertFileNotExists("target/tmpl.removeprefix.vm");
    }

}