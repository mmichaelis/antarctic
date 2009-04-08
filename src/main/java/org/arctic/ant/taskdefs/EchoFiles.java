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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.URLResource;
import org.arctic.text.Strings;

import org.arctic.ant.jaxb.model.ObjectFactory;
import org.arctic.ant.jaxb.model.Resources;

/**
 * This task takes a fileset and outputs all files addressed by this fileset.
 * The output can be of format "text" which will output the files line by line
 * or format "xml" which will output the files found in XML format of the
 * following structure:
 * 
 * <pre>
 *   &lt;resources&gt;
 *     &lt;resource&gt;File 1&lt;/resource&gt;
 *     &lt;resource&gt;File 2&lt;/resource&gt;
 *   &lt;/resources&gt;
 * </pre>
 * 
 * The latter one might be used with XSL transformation to process all files
 * e.&nbsp;g. for merging them.
 * 
 * @author Mark Michaelis
 */
public class EchoFiles extends Task {
    /**
     * Line Separator to output text files.
     */
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    /**
     * (Standard-)Encoding for file outputs.
     */
    private static final String FILE_ENCODING = "UTF-8";
    /**
     * Description for default output target.
     */
    private static final String DEFAULT_FILE_DESC = "System.out";
    /**
     * Output type "text". Will print the files found line by line.
     */
    public static final String TYPE_TEXT = "text";
    /**
     * Output type "xml". Will print the files found in XML format.
     */
    public static final String TYPE_XML = "xml";

    /**
     * Where to write the output to. Defaults to {@link System#out}.
     */
    private String file = null;
    /**
     * Description of the file on Log-Output.
     */
    private String fileDescription = DEFAULT_FILE_DESC;
    /**
     * The type of output. Might be text or XML. text will output the files one
     * by line. Format XML will output the files embedded in XML tags.
     */
    private String type = TYPE_TEXT;
    /**
     * Collections of resources to echo.
     */
    private final AbstractCollection<ResourceCollection> rcs = new ArrayList<ResourceCollection>();

    /**
     * Constructor.
     */
    public EchoFiles() {
        // nothing to do
    }

    /**
     * Add a collection of files to echo.
     * 
     * @param res
     *            a resource collection to echo.
     */
    public final void add(final ResourceCollection res) {
        rcs.add(res);
    }

    /**
     * Add a set of files to echo.
     * 
     * @param set
     *            a set of files to echo.
     */
    public final void addFileset(final FileSet set) {
        add(set);
    }

    /**
     * Where to write the output to. Defaults to {@link System#out}.
     * 
     * @return the file; <code>null</code> for {@link System#out}
     */
    public final String getFile() {
        return file;
    }

    /**
     * Where to write the output to. For output to {@link System#out} set it to
     * <code>null</code>.
     * 
     * @param file
     *            the file to set
     */
    public final void setFile(final String file) {
        this.file = file;
    }

    /**
     * @return the output-type; either {@link #TYPE_TEXT} or {@link #TYPE_XML}.
     */
    public final String getType() {
        return type;
    }

    /**
     * Sets the output type to either {@link #TYPE_TEXT} OR {@link #TYPE_XML}.
     * 
     * @param type
     *            the type to set
     */
    public final void setType(final String type) {
        this.type = type;
    }

    /**
     * Evaluates the writer to use for output.
     * 
     * @return the writer to use for output
     * @throws IOException
     *             in case there occurred an error while creating the writer.
     */
    protected Writer getWriter() throws IOException {
        final Writer baseWriter;
        if (Strings.getInstance().isEmpty(getFile())) {
            baseWriter = new OutputStreamWriter(System.out, System.getProperty("file.encoding"));
            fileDescription = DEFAULT_FILE_DESC;
        }
        else {
            final File fileHandle = new File(getFile());
            baseWriter = new OutputStreamWriter(new FileOutputStream(fileHandle), FILE_ENCODING);
            fileDescription = fileHandle.getAbsolutePath();
        }
        return new BufferedWriter(baseWriter);
    }

    /**
     * Ensure we have a consistent and legal set of attributes.
     * 
     * @throws BuildException
     *             if an error occurs.
     */
    protected final void validate() throws BuildException {
        final boolean isValidType = TYPE_TEXT.equals(getType()) || TYPE_XML.equals(getType());
        if (!isValidType) {
            throw new BuildException("Type \"" + getType()
                    + "\" is invalid. Choose either \"xml\" or \"text\".");
        }
        if (rcs.isEmpty()) {
            throw new BuildException("Specify at least one resource collection.");
        }
    }

    /**
     * Writes the files found as text format.
     * 
     * @param resourceNames
     *            the files found to be written to the output
     * @throws BuildException
     *             in case an error occurs during writing - cause will be
     *             embedded into the exception.
     */
    private void writeText(final Iterable<String> resourceNames) throws BuildException {
        Writer writer;
        try {
            writer = getWriter();
        }
        catch (final Exception e) {
            throw new BuildException("Error while opening stream to \"" + fileDescription
                    + "\" for writing text output.", e);
        }

        try {
            for (final String resource : resourceNames) {
                writer.write(resource);
                writer.write(LINE_SEPARATOR);
            }
            writer.close();
        }
        catch (final Exception e) {
            try {
                writer.close();
            }
            catch (final IOException e1) {
                log("Ignoring error during closing writer bound to \"" + fileDescription + "\".", e1,
                        Project.MSG_DEBUG);
            }
            throw new BuildException("Error while writing text to \"" + fileDescription + "\".", e);
        }
    }

    /**
     * Writes the files found as XML format.
     * 
     * @param resourceNames
     *            the files found to be written to the output
     * @throws BuildException
     *             in case an error occurs during writing - cause will be
     *             embedded into the exception.
     */
    private void writeXml(final Iterable<String> resourceNames) throws BuildException {
        Writer writer;
        try {
            writer = getWriter();
        }
        catch (final Exception e) {
            throw new BuildException("Error while opening stream to \"" + fileDescription
                    + "\" for writing text output.", e);
        }

        final ObjectFactory jaxbFactory = new ObjectFactory();
        final Resources resources = jaxbFactory.createResources();
        final List<String> resource = resources.getResource();
        for (final String resourceName : resourceNames) {
            resource.add(resourceName);
        }
        try {
            final Marshaller marshaller = JAXBContext.newInstance("org.arctic.ant.jaxb.model")
                    .createMarshaller();
            marshaller.marshal(resources, writer);
            writer.close();
        }
        catch (final Exception e) {
            try {
                writer.close();
            }
            catch (final IOException e1) {
                log("Ignoring error during closing writer bound to \"" + fileDescription + "\".", e1,
                        Project.MSG_DEBUG);
            }
            throw new BuildException("Unable to marshal XML output.", e);
        }
    }

    /**
     * Perform the echofiles operation.
     * 
     * @throws BuildException
     *             if an error occurs.
     */
    @SuppressWarnings("unchecked")
    @Override
    public final void execute() throws BuildException {
        // make sure we don't have an illegal set of options
        validate();

        final Collection<String> result = new HashSet<String>();

        for (final ResourceCollection rc : rcs) {
            final Iterator<Resource> resources = rc.iterator();
            while (resources.hasNext()) {
                final Resource resource = resources.next();
                if (!resource.isExists()) {
                    continue;
                }
                final String resourceName;
                if (resource instanceof FileResource) {
                    final FileResource fileResource = (FileResource) resource;
                    resourceName = fileResource.getFile().getAbsolutePath();
                }
                else if (resource instanceof URLResource) {
                    final URLResource urlResource = (URLResource) resource;
                    resourceName = urlResource.getURL().toExternalForm();
                }
                else {
                    resourceName = resource.getName();
                }
                result.add(resourceName);
                log("Added resource \"" + resourceName + "\" of type " + resource.getClass().getName()
                        + " to result set.", Project.MSG_DEBUG);
            }
        }
        if (TYPE_TEXT.equals(getType())) {
            writeText(result);
        }
        else if (TYPE_XML.equals(getType())) {
            writeXml(result);
        }
    }
}
