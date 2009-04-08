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
