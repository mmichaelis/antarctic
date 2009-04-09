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
package org.arctic.text;

public class Strings {
    private static Strings instance;

    private Strings() {
        // nothing to do
    }

    public static Strings getInstance() {
        if (instance == null) {
            instance = new Strings();
        }
        return instance;
    }

    /**
     * <p>
     * Checks if a string is empty ("") or null.
     * </p>
     * 
     * @param str
     *            the string to check
     * @return <code>true</code> if the string is empty or null
     */
    public boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

}
