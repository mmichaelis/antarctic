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

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.URLResource;
import org.arctic.text.Strings;


/**
 * Given a resource collection such as a fileset this task will set a property
 * to the file found via fileset. In case there are more than one file an
 * exception is thrown.
 * <p>
 * The main idea of this task is that you are sure that the fileset will only
 * contain one file but you are not sure of the exact name of this file because
 * the name might contain version information, dates, etc. which may vary.
 * <p>
 * The attributes for this task are:
 * <ul>
 * <li>property &ndash; the property to set</li>
 * <li>type &ndash; file, dir (or directory) or all; what will be a valid result
 * </li>
 * </ul>
 * <p>
 * If no matching file is found (especially when the resource collection is
 * empty) the property won't be set. No error is raised.
 * </p>
 * 
 * @author Mark Michaelis
 */
public class FindFile extends Task {
    /**
     * The name of the property which shall contain the resulting file.
     */
    private String property = null;
    /**
     * Resource Collections to scan for a matching file.
     */
    private final AbstractCollection<ResourceCollection> rcs = new Vector<ResourceCollection>();

    /**
     * Constructor.
     */
    public FindFile() {
        // nothing
    }

    /**
     * @return the property
     */
    public final String getProperty() {
        return property;
    }

    /**
     * @param property
     *            the property to set
     */
    public final void setProperty(final String property) {
        this.property = property;
    }

    /**
     * Add a collection of files to scan.
     * 
     * @param res
     *            a resource collection to scan.
     */
    public final void add(final ResourceCollection res) {
        rcs.add(res);
    }

    /**
     * Add a set of files to copy.
     * 
     * @param set
     *            a set of files to copy.
     */
    public final void addFileset(final FileSet set) {
        add(set);
    }

    /**
     * Perform the findfile operation.
     * 
     * @throws BuildException
     *             if an error occurs.
     */
    @SuppressWarnings("unchecked")
    @Override
    public final void execute() throws BuildException {
        // make sure we don't have an illegal set of options
        validate();

        String result = null;

        for (final ResourceCollection rc : rcs) {
            final Iterator<Resource> resources = rc.iterator();
            boolean fail = false;
            while (resources.hasNext()) {
                final Resource resource = resources.next();
                if (!resource.isExists()) {
                    continue;
                }
                if (result != null) {
                    // delayed failure for easier debugging of the resource
                    // pattern.
                    fail = true;
                }
                if (resource instanceof FileResource) {
                    final FileResource fileResource = (FileResource) resource;
                    result = fileResource.getFile().getAbsolutePath();
                }
                else if (resource instanceof URLResource) {
                    final URLResource urlResource = (URLResource) resource;
                    result = urlResource.getURL().toExternalForm();
                }
                else {
                    result = resource.getName();
                }
                log("Found resource \"" + result + "\" of type " + resource.getClass().getName() + ".",
                        Project.MSG_DEBUG);
                if (fail) {
                    throw new BuildException("Ambiguous resource pattern: Matches more than one resource. "
                            + "Run in debug mode for more details.");
                }
            }
        }
        getProject().setNewProperty(property, result);
        log(property + " := " + result, Project.MSG_DEBUG);
    }

    /**
     * Ensure we have a consistent and legal set of attributes.
     * 
     * @throws BuildException
     *             if an error occurs.
     */
    protected final void validate() throws BuildException {
        if (Strings.getInstance().isEmpty(getProperty())) {
            throw new BuildException("Please specify a property to set the filename to.");
        }
        if (rcs.isEmpty()) {
            throw new BuildException("Specify at least one resource collection.");
        }
    }

}
