package org.arctic.ant.taskdefs;

import junit.framework.TestCase;

import org.apache.tools.ant.BuildException;
import org.arctic.ant.taskdefs.AbstractTransform;

public class AbstractTransformTest extends TestCase {

    private AbstractTransformImpl transformMock;

    /*
     * (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        transformMock = new AbstractTransformImpl();
    }

    public void testValidateMultipleMissingPrefix() {
        transformMock.setMultiple(true);
        try {
            transformMock.execute();
            fail("An exception should have been thrown that attributes are missing.");
        }
        catch (BuildException e) {
            assertEquals("The message does not match the expected string.",
                    "You must set a prefix to transform multiple properties.", e.getMessage());
        }
    }

    public void testValidateMultipleInvalidPropertyAttribute() {
        transformMock.setMultiple(true);
        transformMock.setProperty("abc");
        try {
            transformMock.execute();
            fail("An exception should have been thrown that attributes are missing.");
        }
        catch (BuildException e) {
            assertEquals("The message does not match the expected string.",
                    "Invalid attribute combination. Attribute \"property\" must not be set.", e.getMessage());
        }
    }

    public void testValidateMultipleInvalidValueAttribute() {
        transformMock.setMultiple(true);
        transformMock.setValue("abc");
        try {
            transformMock.execute();
            fail("An exception should have been thrown that attributes are missing.");
        }
        catch (BuildException e) {
            assertEquals("The message does not match the expected string.",
                    "Invalid attribute combination. Attribute \"value\" must not be set.", e.getMessage());
        }
    }

    public void testValidateSingleMissingProperty() {
        transformMock.setMultiple(false);
        try {
            transformMock.execute();
            fail("An exception should have been thrown that attributes are missing.");
        }
        catch (BuildException e) {
            assertEquals("The message does not match the expected string.",
                    "You must set a property name as target for the transformation.", e.getMessage());
        }
    }

    public void testValidateSingleMissingValue() {
        transformMock.setMultiple(false);
        transformMock.setProperty("abc");
        try {
            transformMock.execute();
            fail("An exception should have been thrown that attributes are missing.");
        }
        catch (BuildException e) {
            assertEquals("The message does not match the expected string.",
                    "You must set a value to transform.", e.getMessage());
        }
    }

    public void testValidateSingleInvalidPrefixAttribute() {
        transformMock.setMultiple(false);
        transformMock.setProperty("abc");
        transformMock.setValue("abc");
        transformMock.setPrefix("abc");
        try {
            transformMock.execute();
            fail("An exception should have been thrown that attributes are missing.");
        }
        catch (BuildException e) {
            assertEquals("The message does not match the expected string.",
                    "Invalid attribute combination. Attribute \"prefix\" must not be set.", e.getMessage());
        }
    }

    private class AbstractTransformImpl extends AbstractTransform {

        private boolean isMultiple = false;

        @Override
        protected String transform(String value) throws BuildException {
            return null;
        }

        @Override
        public final boolean isMultiple() {
            return isMultiple;
        }

        public final void setMultiple(boolean isMultiple) {
            this.isMultiple = isMultiple;
        }

    }
}
