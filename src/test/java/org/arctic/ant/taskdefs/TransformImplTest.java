/*
 * Copyright (C) 2009 Mark Michaelis <thragor_at_gmx.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arctic.ant.taskdefs;

import java.io.File;
import java.net.URL;
import java.util.Locale;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.Project;
import org.arctic.ant.taskdefs.AbstractTransformBase;
import org.arctic.ant.taskdefs.TransformImpl;

public class TransformImplTest extends BuildFileTest {
    private static final String BUILD_FILE = "build-transform.xml";
    private String transformUmlauts;
    private String transformSimple;
    private String transformUpperUmlautsExpected;
    private String transformUpperSimpleExpected;
    private String transformLowerSimpleExpected;
    private TransformImplInvalidTransformerMock invalidTransformMock;

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
        transformSimple = getProject().getProperty("transform.simple");
        transformUpperSimpleExpected = transformSimple.toUpperCase(Locale.getDefault());
        transformLowerSimpleExpected = transformSimple.toLowerCase(Locale.getDefault());
        transformUmlauts = getProject().getProperty("transform.umlauts");
        transformUpperUmlautsExpected = transformUmlauts.toUpperCase(Locale.getDefault());
        invalidTransformMock = new TransformImplInvalidTransformerMock();
    }

    public void testSimpleIdentity() {
        executeTarget("transform-simple-identity");
        final String result = getProject().getProperty("transform.simple.result");
        assertNotNull("Property \"transform.simple.result\" not set.", result);
        assertEquals("Transform (identity) should have converted all lowercase characters to uppercase.",
                transformSimple, result);
    }

    public void testSimpleLower() {
        executeTarget("transform-simple-lower");
        final String result = getProject().getProperty("transform.simple.result");
        assertNotNull("Property \"transform.simple.result\" not set.", result);
        assertEquals("Transform (lower) should have converted all lowercase characters to uppercase.",
                transformLowerSimpleExpected, result);
    }

    public void testSimpleUpper() {
        executeTarget("transform-simple-upper");
        final String result = getProject().getProperty("transform.simple.result");
        assertNotNull("Property \"transform.simple.result\" not set.", result);
        assertEquals("Transform (upper) should have converted all lowercase characters to uppercase.",
                transformUpperSimpleExpected, result);
    }

    public void testUmlauts() {
        executeTarget("transform-umlauts");
        final String result = getProject().getProperty("transform.umlauts.result");
        assertNotNull("Property \"transform.umlauts.result\" not set.", result);
        assertEquals(
                "Transform (upper) should have converted all lowercase characters to uppercase including the umlauts.",
                transformUpperUmlautsExpected, result);
    }

    public void testPropertysetNodot() {
        executeTarget("transform-propertyset-nodot");
        final String resultUmlauts = getProject().getProperty("result.transform.umlauts");
        final String resultSimple = getProject().getProperty("result.transform.simple");
        assertNotNull("Property \"result.transform.umlauts\" not set.", resultUmlauts);
        assertNotNull("Property \"result.transform.simple\" not set.", resultSimple);
        assertEquals(
                "Upper should have correctly converted the string to uppercase - respecting the locale.",
                transformUpperUmlautsExpected, resultUmlauts);
        assertEquals("Transform (upper) should have correctly converted the string to uppercase.",
                transformUpperSimpleExpected, resultSimple);
    }

    public void testEmptyValue() {
        executeTarget("transform-empty-value");
        final String result = getProject().getProperty("transform.empty");
        assertNotNull("Property \"transform.empty\" not set.", result);
        assertEquals("Transform should not have modified the input string.", "", result);
    }

    public void testMissingType() {
        try {
            executeTarget("transform-missing-type");
            fail("An exception should have been thrown because no transformer type was specified.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.", "Type not specified.", e.getMessage());
        }
    }

    public void testEmptyType() {
        try {
            executeTarget("transform-empty-type");
            fail("An exception should have been thrown because no transformer type was specified.");
        }
        catch (BuildException e) {
            assertEquals("Message is invalid.", "Type not specified.", e.getMessage());
        }
    }

    public void testInvalidType() {
        try {
            executeTarget("transform-invalid-type");
            fail("An exception should have been thrown because no transformer type was specified.");
        }
        catch (BuildException e) {
            final String msg = e.getMessage();
            final String expectedStartsWith = "Type \"invalid\" not supported. Please choose:";
            assertTrue("Message invalid. Expected that it starts with \"" + expectedStartsWith
                    + "\" but is: \"" + msg + "\".", msg.startsWith(expectedStartsWith));
        }
    }

    public void testTransformerInstantiationException() {
        invalidTransformMock.enableInstantiationException();
        invalidTransformMock.setType("identity");
        try {
            invalidTransformMock.execute();
            fail("An exception should have been thrown that the transformer could not be instantiated.");
        }
        catch (BuildException e) {
            assertEquals("The message does not match the expected string.",
                    "Unable to instantiate transformer of type \"identity\".", e.getMessage());
            assertEquals("Exception cause should be instance of InstantiationException.",
                    InstantiationException.class, e.getCause().getClass());
        }
    }

    public void testTransformerIllegalAccessException() {
        invalidTransformMock.enableIllegalAccessException();
        invalidTransformMock.setType("identity");
        try {
            invalidTransformMock.execute();
            fail("An exception should have been thrown that the transformer could not be instantiated.");
        }
        catch (BuildException e) {
            assertEquals("The message does not match the expected string.",
                    "Unable to access transformer of type \"identity\".", e.getMessage());
            assertEquals("Exception cause should be instance of IllegalAccessException.",
                    IllegalAccessException.class, e.getCause().getClass());
        }
    }

    private class TransformImplInvalidTransformerMock extends TransformImpl {

        final InstantiationException ie = new InstantiationException("IE");
        final IllegalAccessException iae = new IllegalAccessException("IAE");

        private boolean throwInstantiationException = true;
        private boolean throwIllegalAccessException = false;

        public void enableInstantiationException() {
            throwInstantiationException = true;
            throwIllegalAccessException = false;
        }

        public void enableIllegalAccessException() {
            throwInstantiationException = false;
            throwIllegalAccessException = true;
        }

        @Override
        protected AbstractTransformBase getTransformer(String key) throws InstantiationException,
                IllegalAccessException {
            if (throwInstantiationException) {
                throw ie;
            }
            if (throwIllegalAccessException) {
                throw iae;
            }
            return null;
        }

    }
}
