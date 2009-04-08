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

import java.util.Locale;

import org.apache.tools.ant.BuildException;

/**
 * Transforms property values to lowercase.
 * <p>
 * <strong>Usage:</strong>
 * 
 * <pre>
 * &lt;lower property=&quot;target&quot; value=&quot;lorem&quot;/&gt;
 * &lt;lower prefix=&quot;pre&quot;&gt;
 *   &lt;propertyset id=&quot;properties-starting-with-foo&quot;&gt;
 *     &lt;propertyref prefix=&quot;foo&quot;/&gt;
 *     &lt;mapper type=&quot;glob&quot; from=&quot;foo*&quot; to=&quot;bar*&quot;/&gt;
 *   &lt;/propertyset&gt;
 * &lt;/upper&gt;
 * </pre>
 * 
 * @author Mark Michaelis
 */
public class Lower extends AbstractTransform {

    /**
     * Transforms value to lowercase.
     * 
     * @param value
     *            the value to transform
     * @return the value transformed to lowercase
     * @throws BuildException
     *             in case of errors during transformation
     */
    @Override
    protected String transform(final String value) throws BuildException {
        return value.toLowerCase(Locale.getDefault());
    }

}
