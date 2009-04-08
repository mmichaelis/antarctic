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

import java.util.Vector;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.PropertySet;
import org.arctic.text.Strings;


/**
 * Base for all transformers.
 * 
 * @author Mark Michaelis
 * @see AbstractTransform
 * @see TransformImpl
 */
public abstract class AbstractTransformBase extends Task {

    /**
     * The property name which will receive the transformed value.
     */
    private String property = null;
    /**
     * The value to transform.
     */
    private String value = null;
    /**
     * Prefix to place in front of properties if using these tasks with
     * propertysets rather than with single properties.
     */
    private String prefix = null;
    /**
     * Sets of properties to transform.
     */
    private final Vector<PropertySet> propertySets = new Vector<PropertySet>();

    /**
     * @return Prefix to place in front of properties
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * @param prefix
     *            Prefix to place in front of properties
     */
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    /**
     * @return the property which will receive the transformed value
     */
    public final String getProperty() {
        return property;
    }

    /**
     * @param property
     *            the property which will receive the transformed value
     */
    public final void setProperty(final String property) {
        this.property = property;
    }

    /**
     * @return the propertySets
     */
    protected final Vector<PropertySet> getPropertySets() {
        return propertySets;
    }

    /**
     * @return the value to be transformed
     */
    public final String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to be transformed
     */
    public final void setValue(final String value) {
        this.value = value;
    }

    /**
     * A set of properties to transform.
     * 
     * @param ps
     *            the property set to transform
     */
    public void addPropertyset(final PropertySet ps) {
        propertySets.addElement(ps);
    }

    /**
     * Decision rule to decide if this is a multiple or single transformation.
     * 
     * @return true, if it is a multiple transformation; false otherwise
     */
    protected boolean isMultiple() {
        return Strings.getInstance().isEmpty(getProperty());
    }

}
