package org.github.sebamutuku.utils;

import java.io.File;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class FileAttachmentTest {
    /**
     * Method under test:
     * {@link FileAttachment#createPDFFileFromBase64String(String, String, String)}
     */
    @Test
    public void testCreatePDFFileFromBase64String() {
        // Arrange and Act
        File actualCreatePDFFileFromBase64StringResult = FileAttachment.createPDFFileFromBase64String("foo.pdf",
                "Not all who wander are lost", "testpassword");

        // Assert
        assertEquals("foo.pdf", actualCreatePDFFileFromBase64StringResult.getName());
        assertFalse(actualCreatePDFFileFromBase64StringResult.isAbsolute());
    }

}
