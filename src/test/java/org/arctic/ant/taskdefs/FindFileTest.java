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

import org.apache.tools.ant.BuildFileTest;
import org.arctic.ant.taskdefs.FindFile;

public class FindFileTest extends BuildFileTest {
    public FindFileTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        final URL buildFileResource = getClass().getResource("build-findfile.xml");
        final File buildFileResourceFile = new File(buildFileResource.toURI());
        configureProject(buildFileResourceFile.getAbsolutePath());
    }

    public void testMissingProperty() {
        FindFile find = new FindFile();
        try {
            find.execute();
            fail("An exception should have been thrown that the property name is not set.");
        }
        catch (Exception e) {
            // exception expected
            String expected = "Please specify a property to set the filename to.";
            assertEquals("Wrong exception message.", expected, e.getMessage());
        }
    }

    public void testMissingResourceCollection() {
        FindFile find = new FindFile();
        find.setProperty("lorem");
        try {
            find.execute();
            fail("An exception should have been thrown that no resource collections where specified.");
        }
        catch (Exception e) {
            // exception expected
            String expected = "Specify at least one resource collection.";
            assertEquals("Wrong exception message.", expected, e.getMessage());
        }
    }

    public void testMissingPropertyBuildFile() {
        try {
            executeTarget("findfile-missing-property");
            fail("An exception should have been thrown that the property name is not set.");
        }
        catch (Exception e) {
            // exception expected
            final String expected = "Please specify a property to set the filename to.";
            assertEquals("Wrong exception message.", expected, e.getMessage());
        }
    }

    public void testMissingResourceCollectionBuildFile() {
        try {
            executeTarget("findfile-missing-resource-collection");
            fail("An exception should have been thrown that no resource collections where specified.");
        }
        catch (Exception e) {
            // exception expected
            final String expected = "Specify at least one resource collection.";
            assertEquals("Wrong exception message.", expected, e.getMessage());
        }
    }

    public void testFilePresent() {
        executeTarget("findfile-self");
        final String result = getProject().getProperty("findfile.self");
        assertNotNull("Property not set.", result);
        assertTrue("Wrong file found.", result.endsWith("build-findfile.xml"));
    }

    public void testFileInZipPresent() {
        executeTarget("findfile-self-zip");
        final String result = getProject().getProperty("findfile.self.zip");
        assertNotNull("Property not set.", result);
        assertTrue("Wrong file found.", result.endsWith("build-findfile.xml"));
    }

    public void testMultipleFiles() {
        try {
            executeTarget("findfile-multiple");
        }
        catch (Exception e) {
            final String expected = "Ambiguous resource pattern: Matches more than one resource. Run in debug mode for more details.";
            assertEquals("Wrong exception message.", expected, e.getMessage());
        }
    }

    public void testNoFiles() {
        executeTarget("findfile-none");
        final String result = getProject().getProperty("findfile.none");
        assertNull("Property set.", result);
    }
}
