import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.apache.commons.cli.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Util for generating QR codes.
 */
public class Generator {

    public static void main(String[] args) throws IOException, ParseException {
        Options options = new Options();
        options.addOption("dest", true, "Path to save generated image");
        options.addOption("content", false, "Content of the QR code");
        options.addOption("size", false, "Generated QR code dimension");
        options.addOption("windowSize", false, "Dimension of the window in the center of QR code");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        File destination = new File(cmd.getOptionValue("dest"));

        Generator generator = new Generator();
        generator.generate(
                cmd.getOptionValue("content", ""),
                Integer.valueOf(cmd.getOptionValue("size", "400")),
                Integer.valueOf(cmd.getOptionValue("windowSize", "0")),
                destination
        );
    }

    /**
     * Generate QR code
     * @param content String content of qr code
     * @param size Generated image dimension
     * @param windowSize Dimension of the window in the center of QR code
     * @param destination File where QR code image will be saved
     */
    public void generate(String content, int size, int windowSize, File destination) throws IOException {
        byte[] qrBytes = QRCode.from(content).to(ImageType.PNG)
                .withErrorCorrection(ErrorCorrectionLevel.H).withSize(size, size).stream().toByteArray();

        BufferedImage qr = ImageIO.read(new ByteArrayInputStream(qrBytes));

        Graphics graphics = qr.getGraphics();
        graphics.drawImage(new BufferedImage(windowSize, windowSize, BufferedImage.TYPE_INT_ARGB),
                (size - windowSize) / 2, (size - windowSize) / 2, null);

        ImageIO.write(qr, "png", destination);
    }

}
