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

/**
 * Interface for transformers for property values. A transformer will take one
 * property value and transform it e.&nbsp;g. to uppercase. It can also handle
 * propertysets which will be transformed by adding a prefix to the orignal
 * properties.
 * 
 * @author Mark Michaelis
 */
public interface Transform {
    /**
     * @return the target property to store the transformed value to
     */
    String getProperty();

    /**
     * @return the value to transform
     */
    String getValue();

    /**
     * @return the prefix to use in case this task is called with a nested
     *         propertyset
     */
    String getPrefix();
}
