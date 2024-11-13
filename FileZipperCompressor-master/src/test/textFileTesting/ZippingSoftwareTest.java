package test.textFileTesting;

import FileZippingUnzipping.ZippingSoftware;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ZippingSoftwareTest {

    private ZippingSoftware zippingSoftware;
    private Path tempDir;
    private Path zipFilePath;

    @BeforeEach
    void setUp() throws IOException {
        zippingSoftware = new ZippingSoftware();
        tempDir = Files.createTempDirectory("testDir");
        zipFilePath = Files.createTempFile("test", ".zip");

        Files.createFile(tempDir.resolve("file1.txt"));
        Files.createFile(tempDir.resolve("file2.txt"));
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up temporary files and directories
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(File::delete);
        Files.deleteIfExists(zipFilePath);
    }
    @Test
    void testZipFolder() {
        // Delete the existing zip file if it already exists to avoid any conflicts
        if (Files.exists(zipFilePath)) {
            zipFilePath.toFile().delete();
        }

        // Zip the folder and verify
        assertDoesNotThrow(() -> zippingSoftware.zipFolder(tempDir.toString(), zipFilePath.toString()));

        File zipFile = zipFilePath.toFile();
        assertTrue(zipFile.exists(), "Zip file should exist after zipping");
        assertTrue(zipFile.length() > 0, "Zip file should not be empty");
    }

    @Test
    void testUnzipFile() throws IOException {
        // Delete the zip file if it already exists
        if (Files.exists(zipFilePath)) {
            Files.delete(zipFilePath);
        }

        // Zip the folder
        zippingSoftware.zipFolder(tempDir.toString(), zipFilePath.toString());

        // Create a temporary directory for unzipping
        Path unzipDir = Files.createTempDirectory("unzippedDir");

        // Ensure the unzipping process does not throw an exception
        assertDoesNotThrow(() -> zippingSoftware.unzipFile(zipFilePath.toString(), unzipDir.toString()));

        // Verify that files were unzipped successfully, even if they already exist
        assertTrue(Files.exists(unzipDir.resolve("file1.txt")), "file1.txt should exist after unzipping");
        assertTrue(Files.exists(unzipDir.resolve("file2.txt")), "file2.txt should exist after unzipping");

        // Clean up the unzipped directory files
        Files.walk(unzipDir)
                .map(Path::toFile)
                .forEach(File::delete);

        // Delete the unzipped directory itself
        Files.deleteIfExists(unzipDir);
    }


    @Test
    void testZipFolderThrowsIOExceptionForInvalidPath() {
        assertThrows(IOException.class, () -> zippingSoftware.zipFolder("invalid/path", zipFilePath.toString()));
    }

    @Test
    void testUnzipFileThrowsIOExceptionForInvalidZipFilePath() {
        assertThrows(IOException.class, () -> zippingSoftware.unzipFile("invalid.zip", tempDir.toString()));
    }


}
