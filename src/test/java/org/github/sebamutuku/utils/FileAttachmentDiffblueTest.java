package org.github.sebamutuku.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


import java.io.File;
import org.junit.Test;

public class FileAttachmentDiffblueTest {
    /**
     * Method under test:
     * {@link FileAttachment#createPDFFileFromBase64String(String, String, String)}
     */
    @Test
    public void testCreatePDFFileFromBase64String() {
        // Arrange and Act
        File actualCreatePDFFileFromBase64StringResult = FileAttachment.createPDFFileFromBase64String("foo.txt",
                "Not all who wander are lost", "iloveyou");

        // Assert
        assertEquals("foo.txt", actualCreatePDFFileFromBase64StringResult.getName());
        assertFalse(actualCreatePDFFileFromBase64StringResult.isAbsolute());
    }

    /**
     * Method under test:
     * {@link FileAttachment#createPDFFileFromBase64String(String, String, String)}
     */
    @Test
    public void testCreatePDFFileFromBase64String2() {
        // Arrange and Act
        File actualCreatePDFFileFromBase64StringResult = FileAttachment.createPDFFileFromBase64String("foo.txt",
                "Not all who wander are lost", null);

        // Assert
        assertEquals("foo.txt", actualCreatePDFFileFromBase64StringResult.getName());
        assertFalse(actualCreatePDFFileFromBase64StringResult.isAbsolute());
    }

    /**
     * Method under test:
     * {@link FileAttachment#createPDFFileFromBase64String(String, String, String)}
     */
    @Test
    public void testCreatePDFFileFromBase64String3() {
        // Arrange and Act
        File actualCreatePDFFileFromBase64StringResult = FileAttachment.createPDFFileFromBase64String("File Name",
                "Not all who wander are lost", "iloveyou");

        // Assert
        assertEquals("File Name", actualCreatePDFFileFromBase64StringResult.getName());
        assertFalse(actualCreatePDFFileFromBase64StringResult.isAbsolute());
    }

    /**
     * Method under test:
     * {@link FileAttachment#createPDFFileFromBase64String(String, String, String)}
     */
    @Test
    public void testCreatePDFFileFromBase64String4() {
        // Arrange and Act
        File actualCreatePDFFileFromBase64StringResult = FileAttachment.createPDFFileFromBase64String("42",
                "Not all who wander are lost", "iloveyou");

        // Assert
        assertEquals("42", actualCreatePDFFileFromBase64StringResult.getName());
        assertFalse(actualCreatePDFFileFromBase64StringResult.isAbsolute());
    }
}
