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

import java.util.Enumeration;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.PropertySet;
import org.arctic.text.Strings;

/**
 * Base class for all active transformers (unlike wrappers like
 * {@link TransformImpl}).
 * 
 * @author Mark Michaelis
 */
public abstract class AbstractTransform extends AbstractTransformBase implements Transform {
    /**
     * The separator which will be appended automatically to the prefix if there
     * is no such separator. This allows to set the prefix either as "prefix."
     * or as "prefix" and will both result in "prefix.name".
     */
    private static final String DEFAULT_PREFIX_SEPARATOR = ".";

    /**
     * Does the actual transformation of the given value. For property sets it
     * will be called one by one for each property.
     * 
     * @param value
     *            the value to transform
     * @return the transformed value
     * @throws BuildException
     *             if an error occurs during the transformation
     */
    protected abstract String transform(final String value) throws BuildException;

    /**
     * Executes the command for transforming multiple properties.
     * 
     * @throws BuildException
     *             in case of errors
     */
    @SuppressWarnings("unchecked")
    private void executeMultiple() throws BuildException {
        final int prefixLength = getPrefix().length();
        for (final PropertySet ps : getPropertySets()) {
            final Properties props = ps.getProperties();
            final Enumeration<String> propertyNames = (Enumeration<String>) props.propertyNames();
            while (propertyNames.hasMoreElements()) {
                final String propertyName = propertyNames.nextElement();
                final String propertyValue = props.getProperty(propertyName);
                final StringBuffer newPropertyNameBuffer = new StringBuffer(prefixLength
                        + propertyName.length() + 1);
                // Append parts to new name. If Prefix does not end with a dot,
                // add it as separator.
                newPropertyNameBuffer.append(getPrefix());
                final boolean trailingPrefixSeparatorMissing = !getPrefix()
                        .endsWith(DEFAULT_PREFIX_SEPARATOR);
                if (trailingPrefixSeparatorMissing) {
                    newPropertyNameBuffer.append(DEFAULT_PREFIX_SEPARATOR);
                }
                newPropertyNameBuffer.append(propertyName);
                final String newPropertyValue = transform(propertyValue);
                getProject().setNewProperty(newPropertyNameBuffer.toString(), newPropertyValue);
            }
        }
    }

    /**
     * Executes the command for a single property to transform.
     * 
     * @throws BuildException
     *             in case of an error
     */
    private void executeSingle() throws BuildException {
        final String newPropertyValue = transform(getValue());
        getProject().setNewProperty(getProperty(), newPropertyValue);
    }

    /**
     * Execute this task. Validation will be done before.
     * 
     * @throws BuildException
     *             in case of an error
     */
    @Override
    public void execute() throws BuildException {
        validate();
        if (isMultiple()) {
            executeMultiple();
        }
        else {
            executeSingle();
        }
    }

    /**
     * Validation for multiple transformation.
     * 
     * @throws BuildException
     *             if attributes are wrong or missing for multiple
     *             transformation.
     */
    protected void validateMultiple() throws BuildException {
        if (!Strings.getInstance().isEmpty(getProperty())) {
            throw new BuildException(
                    "Invalid attribute combination. Attribute \"property\" must not be set.", getLocation());
        }
        if (!Strings.getInstance().isEmpty(getValue())) {
            throw new BuildException("Invalid attribute combination. Attribute \"value\" must not be set.",
                    getLocation());
        }
        if (Strings.getInstance().isEmpty(getPrefix())) {
            throw new BuildException("You must set a prefix to transform multiple properties.", getLocation());
        }
    }

    /**
     * Validation for single transformation.
     * 
     * @throws BuildException
     *             if attributes are wrong or missing for single transformation.
     */
    protected void validateSingle() throws BuildException {
        if (!Strings.getInstance().isEmpty(getPrefix())) {
            throw new BuildException("Invalid attribute combination. Attribute \"prefix\" must not be set.",
                    getLocation());
        }
        if (Strings.getInstance().isEmpty(getProperty())) {
            throw new BuildException("You must set a property name as target for the transformation.",
                    getLocation());
        }
        if (getValue() == null) {
            throw new BuildException("You must set a value to transform.", getLocation());
        }
        if (!getPropertySets().isEmpty()) {
            throw new BuildException(
                    "Invalid attribute combination. Embedded propertyset does not match attributes set.",
                    getLocation());
        }
    }

    /**
     * Validates that the required attributes have been set.
     * 
     * @throws BuildException
     *             If attributes are missing or wrong.
     */
    protected void validate() throws BuildException {
        if (isMultiple()) {
            log("Mode set to multiple transformation.", Project.MSG_DEBUG);
            validateMultiple();
        }
        else {
            log("Mode set to single transformation.", Project.MSG_DEBUG);
            validateSingle();
        }
    }

}
