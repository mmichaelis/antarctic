package org.arctic.ant.taskdefs;

import java.io.File;
import java.net.URL;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.Project;

public class IdentityTest extends BuildFileTest {
    private static final String BUILD_FILE = "build-identity.xml";
    private String identityUmlauts;
    private String identitySimple;

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
        identitySimple = getProject().getProperty("identity.simple");
        identityUmlauts = getProject().getProperty("identity.umlauts");

    }

    public void testSimple() {
        executeTarget("identity-simple");
        final String result = getProject().getProperty("identity.simple.result");
        assertNotNull("Property \"identity.simple.result\" not set.", result);
        assertEquals("Identity should not have modified the input string.", identitySimple, result);
    }

    public void testUmlauts() {
        executeTarget("identity-umlauts");
        final String result = getProject().getProperty("identity.umlauts.result");
        assertNotNull("Property \"identity.umlauts.result\" not set.", result);
        assertEquals("Identity should not have modified the input string.", identityUmlauts, result);
    }

    public void testPropertysetNodot() {
        executeTarget("identity-propertyset-nodot");
        final String resultUmlauts = getProject().getProperty("result.identity.umlauts");
        final String resultSimple = getProject().getProperty("result.identity.simple");
        assertNotNull("Property \"result.identity.umlauts\" not set.", resultUmlauts);
        assertNotNull("Property \"result.identity.simple\" not set.", resultSimple);
        assertEquals("Identity should not have modified the input string.", identityUmlauts, resultUmlauts);
        assertEquals("Identity should not have modified the input string.", identitySimple, resultSimple);
    }

    public void testInvalidAttributes1() {
        try {
            executeTarget("identity-invalid-attributes-1");
            fail("An exception should have been thrown because of illegal arguments.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.",
                    "Invalid attribute combination. Attribute \"prefix\" must not be set.", e.getMessage());
        }
    }

    public void testInvalidAttributes2() {
        try {
            executeTarget("identity-invalid-attributes-2");
            fail("An exception should have been thrown because of illegal arguments.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.",
                    "Invalid attribute combination. Attribute \"value\" must not be set.", e.getMessage());
        }
    }

    public void testInvalidAttributes3() {
        try {
            executeTarget("identity-invalid-attributes-3");
            fail("An exception should have been thrown because of illegal arguments.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.",
                    "Invalid attribute combination. Attribute \"prefix\" must not be set.", e.getMessage());
        }
    }

    public void testInvalidAttributes4() {
        try {
            executeTarget("identity-invalid-attributes-4");
            fail("An exception should have been thrown because of illegal arguments.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.",
                    "Invalid attribute combination. Attribute \"prefix\" must not be set.", e.getMessage());
        }
    }

    public void testInvalidAttributes5() {
        try {
            executeTarget("identity-invalid-attributes-5");
            fail("An exception should have been thrown because of illegal arguments.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.",
                    "Invalid attribute combination. Embedded propertyset does not match attributes set.", e
                            .getMessage());
        }
    }

    public void testInvalidAttributes6() {
        try {
            executeTarget("identity-invalid-attributes-6");
            fail("An exception should have been thrown because of illegal arguments.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.",
                    "Invalid attribute combination. Attribute \"value\" must not be set.", e.getMessage());
        }
    }

    public void testInvalidAttributes7() {
        try {
            executeTarget("identity-invalid-attributes-7");
            fail("An exception should have been thrown because of illegal arguments.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.", "You must set a value to transform.", e.getMessage());
        }
    }

    public void testInvalidAttributes8() {
        try {
            executeTarget("identity-invalid-attributes-8");
            fail("An exception should have been thrown because of illegal arguments.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.", "You must set a prefix to transform multiple properties.", e
                    .getMessage());
        }
    }

    public void testInvalidAttributes9() {
        try {
            executeTarget("identity-invalid-attributes-9");
            fail("An exception should have been thrown because of illegal arguments.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.",
                    "Invalid attribute combination. Attribute \"value\" must not be set.", e.getMessage());
        }
    }

    public void testInvalidAttributes10() {
        try {
            executeTarget("identity-invalid-attributes-10");
            fail("An exception should have been thrown because of illegal arguments.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.", "You must set a value to transform.", e.getMessage());
        }
    }

    public void testInvalidAttributes11() {
        try {
            executeTarget("identity-invalid-attributes-11");
            fail("An exception should have been thrown because of illegal arguments.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.",
                    "Invalid attribute combination. Attribute \"prefix\" must not be set.", e.getMessage());
        }
    }

    public void testInvalidAttributes12() {
        try {
            executeTarget("identity-invalid-attributes-12");
            fail("An exception should have been thrown because of illegal arguments.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.",
                    "Invalid attribute combination. Attribute \"value\" must not be set.", e.getMessage());
        }
    }

    public void testInvalidAttributes13() {
        try {
            executeTarget("identity-invalid-attributes-13");
            fail("An exception should have been thrown because of illegal arguments.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.",
                    "Invalid attribute combination. Embedded propertyset does not match attributes set.", e
                            .getMessage());
        }
    }

    public void testInvalidAttributes14() {
        try {
            executeTarget("identity-invalid-attributes-14");
            fail("An exception should have been thrown because of illegal arguments.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.",
                    "Invalid attribute combination. Attribute \"value\" must not be set.", e.getMessage());
        }
    }

    public void testInvalidAttributes15() {
        try {
            executeTarget("identity-invalid-attributes-15");
            fail("An exception should have been thrown because of illegal arguments.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.",
                    "Invalid attribute combination. Embedded propertyset does not match attributes set.", e
                            .getMessage());
        }
    }

    public void testInvalidAttributes16() {
        try {
            executeTarget("identity-invalid-attributes-16");
            fail("An exception should have been thrown because of illegal arguments.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.", "You must set a prefix to transform multiple properties.", e
                    .getMessage());
        }
    }

    public void testInvalidAttributes17() {
        try {
            executeTarget("identity-invalid-attributes-17");
            fail("An exception should have been thrown because of illegal arguments.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.",
                    "Invalid attribute combination. Attribute \"value\" must not be set.", e.getMessage());
        }
    }

    public void testEmptyValue() {
        executeTarget("identity-empty-value");
        final String result = getProject().getProperty("identity.empty");
        assertNotNull("Property \"identity.empty\" not set.", result);
        assertEquals("Identity should not have modified the input string.", "", result);
    }

}
