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

import java.util.HashMap;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.PropertySet;
import org.arctic.text.Strings;

/**
 * A generic ant task to access all transformation tasks.
 * 
 * @author Mark Michaelis
 * @see AbstractTransform
 */
public class TransformImpl extends AbstractTransformBase {
    /**
     * The map for accessing the different transformation tasks via keys.
     */
    private static Map<String, Class<? extends AbstractTransform>> transformers =
            new HashMap<String, Class<? extends AbstractTransform>>();
    /**
     * The type of this transformer. Should be equal to a key available in
     * {@link #transformers}.
     */
    private String type = null;

    /**
     * Constructor which also registers the different transformers.
     */
    public TransformImpl() {
        registerTransformer("upper", Upper.class);
        registerTransformer("lower", Lower.class);
        registerTransformer("identity", Identity.class);
    }

    /**
     * Register one transformer.
     * 
     * @param id
     *            the id which will map to the type-attribute later
     * @param transform
     *            the class to instantiate for transformation
     */
    public static void registerTransformer(final String id, final Class<? extends AbstractTransform> transform) {
        transformers.put(id, transform);
    }

    /**
     * Returns the available transformers.
     * 
     * @return the transformers
     */
    private Map<String, Class<? extends AbstractTransform>> getTransformers() {
        return transformers;
    }

    /**
     * Check if a transformer with the given key is available.
     * 
     * @param key
     *            the key/id of the transformer
     * @return true if there is such a transformer, false if not
     */
    private boolean hasTransformer(final String key) {
        return getTransformers().containsKey(key);
    }

    /**
     * Get all transformer keys.
     * 
     * @return all available transformers keys
     */
    private Iterable<String> getTransformerKeys() {
        return getTransformers().keySet();
    }

    /**
     * Gets the class of the transformer which matches the id/key.
     * 
     * @param key
     *            the key/id of the transformer which got registered
     * @return a matching transformer class or null if there is no transformer
     *         registered for the given key
     */
    private Class<? extends AbstractTransform> getTransformerClass(final String key) {
        return getTransformers().get(key);
    }

    /**
     * Creates a new instance of the transformer for the given key.
     * 
     * @param key
     *            the key/id of the transformer
     * @return an instantiated transformer
     * @throws InstantiationException
     *             in case the instantiation of the transformer failed
     * @throws IllegalAccessException
     *             in case the instantiation of the transformer failed
     */
    protected AbstractTransformBase getTransformer(final String key) throws InstantiationException,
            IllegalAccessException {
        return getTransformerClass(key).newInstance();
    }

    /**
     * @return Transformer ids as comma separated list, so it may be used
     *         e.&nbsp;g. in error messages.
     */
    private String getTransformerIds() {
        final StringBuffer result = new StringBuffer();
        for (final String key : getTransformerKeys()) {
            if (result.length() == 0) {
                result.append(key);
            }
            else {
                result.append(", ").append(key);
            }
        }
        return result.toString();
    }

    /**
     * Applies important settings to the newly created transformer as for
     * example to which project it belongs.
     * 
     * @param transformer
     *            the transformer instance to adopt
     */
    private void prepareTransformer(final AbstractTransformBase transformer) {
        transformer.setPrefix(getPrefix());
        transformer.setProperty(getProperty());
        transformer.setValue(getValue());
        for (final PropertySet ps : getPropertySets()) {
            transformer.addPropertyset(ps);
        }
        transformer.setLocation(getLocation());
        transformer.setOwningTarget(getOwningTarget());
        transformer.setProject(getProject());
        transformer.setTaskName(getTaskName());
        transformer.setTaskType(getTaskType());
        transformer.setRuntimeConfigurableWrapper(getRuntimeConfigurableWrapper());
    }

    /**
     * @return the type of the transformer
     */
    public final String getType() {
        return type;
    }

    /**
     * @param type
     *            the type of the transformer to instantiate
     */
    public final void setType(final String type) {
        this.type = type;
    }

    /** {@inheritDoc} */
    @Override
    public void execute() throws BuildException {
        validate();
        AbstractTransformBase transformer;
        final String transformerType = getType();
        try {
            transformer = getTransformer(transformerType);
        }
        catch (final InstantiationException e) {
            throw new BuildException(
                    "Unable to instantiate transformer of type \"" + transformerType + "\".", e,
                    getLocation());
        }
        catch (final IllegalAccessException e) {
            throw new BuildException("Unable to access transformer of type \"" + transformerType + "\".", e,
                    getLocation());
        }
        prepareTransformer(transformer);
        transformer.execute();
    }

    /**
     * Validates that the required attributes have been set.
     * 
     * @throws BuildException
     *             If attributes are missing or wrong.
     */
    protected void validate() throws BuildException {
        final String transformerType = getType();
        if (Strings.getInstance().isEmpty(transformerType)) {
            throw new BuildException("Type not specified.", getLocation());
        }
        if (!hasTransformer(transformerType)) {
            throw new BuildException("Type \"" + transformerType + "\" not supported. Please choose: "
                    + getTransformerIds(), getLocation());
        }
    }

}
