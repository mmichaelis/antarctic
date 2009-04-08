package org.arctic.ant.taskdefs;

import java.io.File;
import java.net.URL;
import java.util.Locale;

import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.Project;

public class LowerTest extends BuildFileTest {
    private static final String BUILD_FILE = "build-lower.xml";
    private String lowerUmlauts;
    private String lowerSimple;
    private String lowerUmlautsExpected;
    private String lowerSimpleExpected;

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final URL buildFileResource = getClass().getResource(BUILD_FILE);
        final File buildFileResourceFile = new File(buildFileResource.toURI());
        configureProject(buildFileResourceFile.getAbsolutePath(), Project.MSG_DEBUG);
        lowerSimple = getProject().getProperty("lower.simple");
        lowerSimpleExpected = lowerSimple.toLowerCase(Locale.getDefault());
        lowerUmlauts = getProject().getProperty("lower.umlauts");
        lowerUmlautsExpected = lowerUmlauts.toLowerCase(Locale.getDefault());
    }

    public void testSimple() {
        executeTarget("lower-simple");
        final String result = getProject().getProperty("lower.simple.result");
        assertNotNull("Property \"lower.simple.result\" not set.", result);
        assertEquals("Lower should have converted all uppercase characters to lowercase.",
                lowerSimpleExpected, result);
    }

    public void testUmlauts() {
        executeTarget("lower-umlauts");
        final String result = getProject().getProperty("lower.umlauts.result");
        assertNotNull("Property \"lower.umlauts.result\" not set.", result);
        assertEquals(
                "Lower should have converted all uppercase characters to lowercase including the umlauts.",
                lowerUmlautsExpected, result);
    }

    public void testPropertysetNodot() {
        executeTarget("lower-propertyset-nodot");
        final String resultUmlauts = getProject().getProperty("result.lower.umlauts");
        final String resultSimple = getProject().getProperty("result.lower.simple");
        assertNotNull("Property \"result.lower.umlauts\" not set.", resultUmlauts);
        assertNotNull("Property \"result.lower.simple\" not set.", resultSimple);
        assertEquals(
                "Lower should have correctly converted the string to lowercase - respecting the locale.",
                lowerUmlautsExpected, resultUmlauts);
        assertEquals("Lower should have correctly converted the string to lowercase.", lowerSimpleExpected,
                resultSimple);
    }

    public void testEmptyValue() {
        executeTarget("lower-empty-value");
        final String result = getProject().getProperty("lower.empty");
        assertNotNull("Property \"lower.empty\" not set.", result);
        assertEquals("Lower should not have modified the input string.", "", result);
    }

}
