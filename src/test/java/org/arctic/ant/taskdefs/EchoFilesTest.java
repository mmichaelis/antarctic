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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.arctic.ant.taskdefs.EchoFiles;

public class EchoFilesTest extends BuildFileTest {
    private static final String ERROR_MSG = "Lorem Ipsum Dolor Sit Amet";
    private static final int READ_BUFFER_SIZE = 1024;
    private static final String BUILD_FILE = "build-echofiles.xml";
    private File outFile;

    public EchoFilesTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        final URL buildFileResource = getClass().getResource(BUILD_FILE);
        final File buildFileResourceFile = new File(buildFileResource.toURI());
        configureProject(buildFileResourceFile.getAbsolutePath(), Project.MSG_DEBUG);
        outFile = File.createTempFile("EchoFilesTest", ".out");
        getProject().setNewProperty("outfile", outFile.getAbsolutePath());
        outFile.deleteOnExit();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        executeTarget("clean");
        outFile.delete();
    }

    public void testTypeInvalidString() {
        final String invalidType = "invalidString";
        EchoFiles echofiles = new EchoFiles();
        echofiles.setType(invalidType);
        try {
            echofiles.execute();
            fail("An exception should have been thrown that the type set is invalid.");
        }
        catch (Exception e) {
            // exception expected
            String expected = "Type \"" + invalidType + "\" is invalid. Choose either \"xml\" or \"text\".";
            assertEquals("Wrong exception message.", expected, e.getMessage());
        }
    }

    public void testTypeInvalidStringBuildFile() {
        final String invalidType = "invalidString";
        try {
            executeTarget("echofiles-type-invalid-string");
            fail("An exception should have been thrown that the type set is invalid.");
        }
        catch (Exception e) {
            // exception expected
            String expected = "Type \"" + invalidType + "\" is invalid. Choose either \"xml\" or \"text\".";
            assertEquals("Wrong exception message.", expected, e.getMessage());
        }
    }

    public void testGetFile() {
        EchoFiles echofiles = new EchoFiles();
        echofiles.setFile(outFile.getAbsolutePath());
        assertEquals("The retrieved file via getFile() should be the same as the one set before.", outFile
                .getAbsolutePath(), echofiles.getFile());
    }

    public void testGetType() {
        EchoFiles echofiles = new EchoFiles();
        echofiles.setType("xml");
        assertEquals("The retrieved type via getType() should be the same as the one set before.", "xml",
                echofiles.getType());
        echofiles.setType("text");
        assertEquals("The retrieved type via getType() should be the same as the one set before.", "text",
                echofiles.getType());
    }

    public void testTypeNull() {
        final String invalidType = null;
        EchoFiles echofiles = new EchoFiles();
        echofiles.setType(invalidType);
        try {
            echofiles.execute();
            fail("An exception should have been thrown that the type set is invalid.");
        }
        catch (Exception e) {
            // exception expected
            String expected = "Type \"" + String.valueOf(invalidType)
                    + "\" is invalid. Choose either \"xml\" or \"text\".";
            assertEquals("Wrong exception message.", expected, e.getMessage());
        }
    }

    public void testMissingResourceCollection() {
        EchoFiles echofiles = new EchoFiles();
        try {
            echofiles.execute();
            fail("An exception should have been thrown that no resource collections where specified.");
        }
        catch (Exception e) {
            // exception expected
            String expected = "Specify at least one resource collection.";
            assertEquals("Wrong exception message.", expected, e.getMessage());
        }
    }

    public void testMissingResourceCollectionBuildFile() {
        try {
            executeTarget("echofiles-missing-resource-collection");
            fail("An exception should have been thrown that no resource collections where specified.");
        }
        catch (Exception e) {
            // exception expected
            String expected = "Specify at least one resource collection.";
            assertEquals("Wrong exception message.", expected, e.getMessage());
        }
    }

    public void testSelfTextStdout() {
        executeTarget("echofiles-self-text-stdout");
        assertOutputContaining(BUILD_FILE);
    }

    public void testSelfXmlStdout() {
        executeTarget("echofiles-self-xml-stdout");
        assertOutputContaining(BUILD_FILE + "</resource></resources>");
        assertOutputContaining("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><resources><resource>");
    }

    public void testSelfZipTextStdout() {
        executeTarget("echofiles-self-zip-text-stdout");
        assertOutputContaining(BUILD_FILE);
    }

    public void testNoneTextFile() {
        executeTarget("echofiles-none-text-file");
        assertTrue("The file \"" + outFile.getAbsolutePath() + " should have been created.", outFile.exists());
        assertEquals("The file \"" + outFile.getAbsolutePath() + " should have size 0.", 0, outFile.length());
    }

    public void testNotExists() {
        executeTarget("echofiles-not-exists-text-stdout");
        assertTrue("The file \"" + outFile.getAbsolutePath() + " should have been created.", outFile.exists());
        assertEquals("The file \"" + outFile.getAbsolutePath() + " should have size 0.", 0, outFile.length());
    }

    private static String readTextFile(final File file) throws IOException {
        final StringBuffer sb = new StringBuffer(READ_BUFFER_SIZE);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                "UTF-8"));

        char[] chars = new char[1024];
        while (reader.read(chars) > -1) {
            sb.append(String.valueOf(chars));
        }

        reader.close();

        return sb.toString();
    }

    private void assertStringContains(final String expected, final String actual) {
        if (expected == null || actual == null) {
            assertEquals(expected, actual);
            return;
        }
        assertTrue("Expected file to contain \"" + expected + "\" but got: " + actual, actual
                .contains(expected));
    }

    public void testMultipleXmlFile() throws IOException {
        executeTarget("echofiles-multiple-xml-file");
        assertTrue("The file \"" + outFile.getAbsolutePath() + " should have been created.", outFile.exists());
        assertTrue("The size of \"" + outFile.getAbsolutePath() + " should be greater than 0.", 0 < outFile
                .length());
        final String fileContent = readTextFile(outFile);
        assertStringContains(BUILD_FILE + "</resource>", fileContent);
        assertStringContains(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><resources><resource>",
                fileContent);
        assertStringContains("</resource><resource>", fileContent);
    }

    public void testGetWriterThrowsException() {
        final String errorMessage = ERROR_MSG;
        EchoFiles echofiles = new EchoFiles() {

            /*
             * (non-Javadoc)
             * @see org.arctic.ant.taskdefs.EchoFiles#getWriter()
             */
            @Override
            protected Writer getWriter() throws IOException {
                throw new UnsupportedEncodingException(errorMessage);
            }

        };
        echofiles.addFileset((FileSet) getProject().getReference("fileset-self"));
        try {
            echofiles.execute();
            fail("A build exception should have been thrown because the writer caused a problem.");
        }
        catch (BuildException e) {
            assertTrue("Error message should start with \"Error while opening\" but is \"" + e.getMessage()
                    + "\"", e.getMessage().startsWith("Error while opening"));
            assertEquals("Cause of exception should be of the type which got thrown.",
                    UnsupportedEncodingException.class, e.getCause().getClass());
            assertEquals("Exception-cause should have correct message.", errorMessage, e.getCause()
                    .getMessage());
        }
    }

    public void testWriteThrowsExceptionOnWrite() {
        EchoFiles echofiles = new EchoFilesWriteThrowsExceptionOnWrite();
        echofiles.addFileset((FileSet) getProject().getReference("fileset-self"));
        try {
            echofiles.execute();
            fail("A build exception should have been thrown because the writer caused a problem.");
        }
        catch (BuildException e) {
            assertTrue("Error message should start with \"Error while writing\" but is \"" + e.getMessage()
                    + "\"", e.getMessage().startsWith("Error while writing"));
            assertEquals("Cause of exception should be of the type which got thrown.", IOException.class, e
                    .getCause().getClass());
            assertEquals("Exception-cause should have correct message.", ERROR_MSG, e.getCause().getMessage());
        }
    }

    public void testWriteThrowsExceptionOnWriteClose() {
        EchoFiles echofiles = new EchoFilesWriteThrowsExceptionOnCloseAndWrite();
        echofiles.addFileset((FileSet) getProject().getReference("fileset-self"));
        try {
            echofiles.execute();
            fail("A build exception should have been thrown because the writer caused a problem.");
        }
        catch (BuildException e) {
            assertTrue("Error message should start with \"Error while writing\" but is \"" + e.getMessage()
                    + "\"", e.getMessage().startsWith("Error while writing"));
            assertEquals("Cause of exception should be of the type which got thrown.", IOException.class, e
                    .getCause().getClass());
            assertEquals("Exception-cause should have correct message.", ERROR_MSG, e.getCause().getMessage());
        }
    }

    public void testWriteThrowsExceptionOnClose() {
        EchoFiles echofiles = new EchoFilesWriteThrowsExceptionOnWrite();
        echofiles.addFileset((FileSet) getProject().getReference("fileset-self"));
        try {
            echofiles.execute();
            fail("A build exception should have been thrown because the writer caused a problem.");
        }
        catch (BuildException e) {
            assertTrue("Error message should start with \"Error while writing\" but is \"" + e.getMessage()
                    + "\"", e.getMessage().startsWith("Error while writing"));
            assertEquals("Cause of exception should be of the type which got thrown.", IOException.class, e
                    .getCause().getClass());
            assertEquals("Exception-cause should have correct message.", ERROR_MSG, e.getCause().getMessage());
        }
    }

    private class EchoFilesWriteThrowsExceptionOnWrite extends EchoFiles {
        /*
         * (non-Javadoc)
         * @see org.arctic.ant.taskdefs.EchoFiles#getWriter()
         */
        @Override
        protected Writer getWriter() throws IOException {
            return new FileWriter("testWriteThrowsExceptionOnWrite.txt") {
                @Override
                public void write(String str) throws IOException {
                    throw new IOException(ERROR_MSG);
                }
            };
        }
    }

    private class EchoFilesWriteThrowsExceptionOnCloseAndWrite extends EchoFilesWriteThrowsExceptionOnWrite {
        /*
         * (non-Javadoc)
         * @see org.arctic.ant.taskdefs.EchoFiles#getWriter()
         */
        @Override
        protected Writer getWriter() throws IOException {
            return new FileWriter("testWriteThrowsExceptionOnWrite.txt") {
                @Override
                public void close() throws IOException {
                    throw new IOException(ERROR_MSG);
                }
            };
        }
    }
}
