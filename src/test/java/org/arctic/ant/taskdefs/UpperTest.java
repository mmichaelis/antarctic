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

import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.Project;

public class UpperTest extends BuildFileTest {
    private static final String BUILD_FILE = "build-upper.xml";
    private String upperUmlauts;
    private String upperSimple;
    private String upperUmlautsExpected;
    private String upperSimpleExpected;

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
        upperSimple = getProject().getProperty("upper.simple");
        upperSimpleExpected = upperSimple.toUpperCase(Locale.getDefault());
        upperUmlauts = getProject().getProperty("upper.umlauts");
        upperUmlautsExpected = upperUmlauts.toUpperCase(Locale.getDefault());
    }

    public void testSimple() {
        executeTarget("upper-simple");
        final String result = getProject().getProperty("upper.simple.result");
        assertNotNull("Property \"upper.simple.result\" not set.", result);
        assertEquals("Upper should have converted all lowercase characters to uppercase.",
                upperSimpleExpected, result);
    }

    public void testUmlauts() {
        executeTarget("upper-umlauts");
        final String result = getProject().getProperty("upper.umlauts.result");
        assertNotNull("Property \"upper.umlauts.result\" not set.", result);
        assertEquals(
                "Upper should have converted all lowercase characters to uppercase including the umlauts.",
                upperUmlautsExpected, result);
    }

    public void testPropertysetNodot() {
        executeTarget("upper-propertyset-nodot");
        final String resultUmlauts = getProject().getProperty("result.upper.umlauts");
        final String resultSimple = getProject().getProperty("result.upper.simple");
        assertNotNull("Property \"result.upper.umlauts\" not set.", resultUmlauts);
        assertNotNull("Property \"result.upper.simple\" not set.", resultSimple);
        assertEquals(
                "Upper should have correctly converted the string to uppercase - respecting the locale.",
                upperUmlautsExpected, resultUmlauts);
        assertEquals("Upper should have correctly converted the string to uppercase.", upperSimpleExpected,
                resultSimple);
    }

    public void testEmptyValue() {
        executeTarget("upper-empty-value");
        final String result = getProject().getProperty("upper.empty");
        assertNotNull("Property \"upper.empty\" not set.", result);
        assertEquals("Upper should not have modified the input string.", "", result);
    }

}
