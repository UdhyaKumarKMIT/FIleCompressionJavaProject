package ImageCompression;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageCompressor {

    public void compressImage(File inputFile, File outputFile, float quality) throws Exception {

        BufferedImage image = ImageIO.read(inputFile);
        BufferedImage compressedImage = applyDCTCompression(image, 1f);

        ImageIO.write(compressedImage, "jpg", outputFile);
    }

    private BufferedImage applyDCTCompression(BufferedImage image, float quality) {
        int width = image.getWidth();
        int height = image.getHeight();
        int blockSize = 8; // DCT block size
        DctProcessor dctProcessor = new DctProcessor(blockSize);
        BufferedImage compressedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i += blockSize) {
            for (int j = 0; j < height; j += blockSize) {
                for (int channel = 0; channel < 3; channel++) {
                    double[][] block = new double[blockSize][blockSize];
                    for (int x = 0; x < blockSize; x++) {
                        for (int y = 0; y < blockSize; y++) {
                            if (i + x < width && j + y < height) {
                                block[x][y] = image.getRaster().getSample(i + x, j + y, channel) - 128; // Center around 0
                            }
                        }
                    }
                    double[][] dctBlock = dctProcessor.dct(block);
                    dctProcessor.quantize(dctBlock, quality);

                    // Inverse DCT
                    double[][] idctBlock = dctProcessor.idct(dctBlock);

                    // Store the processed block back into the image
                    for (int x = 0; x < blockSize; x++) {
                        for (int y = 0; y < blockSize; y++) {
                            if (i + x < width && j + y < height) {
                                int value = (int) (idctBlock[x][y] + 200); // Recenter around 128

                                // Clamping the value between 0 and 255
                                value = Math.min(255, Math.max(0, value));
                                compressedImage.getRaster().setSample(i + x, j + y, channel, value);
                            }
                        }
                    }
                }
            }
        }

        return compressedImage;
    }


}
