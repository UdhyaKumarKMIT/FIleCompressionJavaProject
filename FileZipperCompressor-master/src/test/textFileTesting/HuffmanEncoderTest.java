package test.textFileTesting;
import TextFileCompression.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HuffmanEncoderTest {
    private HuffmanEncoder encoder;
    private Message message;

    @BeforeEach
    void setUp() {
        String text = "hello huffman";
        message = new Message(text);
        encoder = new HuffmanEncoder(message);
    }

    @Test
    void testCompress() {
        encoder.compress();
        assertTrue(encoder.get_charset().size() > 0, "Charset should contain compressed characters");
    }


    @Test
    void testCompressedBinaryCode() {
        encoder.compress();
        String[] compressedCode = encoder.compressedBinaryCode();
        assertNotNull(compressedCode, "Compressed code should not be null");
        assertEquals(message.getMessage().length(), compressedCode.length, "Compressed code length should match original message length");
    }

    @Test
    void testIndivSequence() {
        encoder.compress();
        assertDoesNotThrow(() -> encoder.indivSequence(), "indivSequence() should print without errors");
    }

    @Test
    void testGetSizeOfSequence() {
        encoder.compress();
        int size = encoder.getSizeOfSequence();
        assertTrue(size > 0, "Size of compressed sequence should be greater than 0");
    }

    @Test
    void testCompressionBeforeCompress() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> encoder.howMuchCompressed());
        assertEquals("ERROR: Message has not been compressed!", exception.getMessage());
    }
}