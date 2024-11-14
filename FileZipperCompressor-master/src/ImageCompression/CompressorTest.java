package ImageCompression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

class CompressorTest {

    private compressor compressor;
    private File validImageFile;
    private File compressedImageFile;

    @BeforeEach
    void setUp() {
        compressor = new compressor();

        validImageFile = new File("C:\\Users\\udhya\\Downloads\\mouse.jpg"); // Input image file
        compressedImageFile = new File("C:\\Users\\udhya\\Downloads\\compressed_mouse.jpg"); // Output compressed image file
    }

    @Test
    void testCompressImageValidFile() {

        try {

            assertTrue(validImageFile.exists(), "The test image file should exist");


            compressor.compressImage(validImageFile, compressedImageFile, 0.5f); // 50% quality


            assertTrue(compressedImageFile.exists(), "The compressed file should be created");

              assertTrue(compressedImageFile.length() < validImageFile.length(), "The compressed file should be smaller than the original file");
        } catch (IOException e) {
            fail("IOException occurred during compression: " + e.getMessage());
        }
    }

    @Test
    void testCompressImageInvalidFile() {
        File invalidImageFile = new File("src/test/resources/non_existent_image.jpg");

        assertThrows(IOException.class, () -> {
            compressor.compressImage(invalidImageFile, compressedImageFile, 0.5f);
        }, "An IOException should be thrown when trying to compress a non-existent file");
    }

    @Test
    void testCompressImageWithNullFile() {

        assertThrows(IllegalArgumentException.class, () -> {
            compressor.compressImage(null, compressedImageFile, 0.5f);
        }, "An IllegalArgumentException should be thrown when input file is null");
    }

}
