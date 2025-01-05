package dev.langchain4j.image_to_diagram;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Objects;

/**
 * The `ImageLoader` class provides utility methods for loading and converting images to Base64 format.
 */
public class ImageLoader {

    /**
     * Loads an image from a resource path as a Base64 encoded string.
     *
     * @param resourcePath the path of the image resource
     * @return the Base64 encoded string representing the image
     * @throws IOException if an I/O error occurs while reading the input stream
     */
    public static String loadImageAsBase64(String resourcePath) throws IOException {
        try (InputStream inputStream = ImageLoader.class.getClassLoader().getResourceAsStream(resourcePath) ) {
            return loadImageAsBase64(inputStream);
        }
    }

    /**
     * Loads an image from an input stream and returns its Base64 encoded string representation.
     *
     * @param inputStream the input stream containing the image data
     * @return the Base64 encoded string representing the image
     * @throws IOException if an I/O error occurs while reading the input stream
     */
    public static String loadImageAsBase64(InputStream inputStream) throws IOException {

        Objects.requireNonNull(inputStream);

        try( ByteArrayOutputStream buffer = new ByteArrayOutputStream() ) {
            int nRead;
            byte[] data = new byte[4 *1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();

            byte[] imageBytes = buffer.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        }
    }

}
