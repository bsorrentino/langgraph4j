package dev.langchain4j.image_to_diagram;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Objects;

public class ImageLoader {

    public static String loadImageAsBase64(String resourcePath) throws IOException {
        try (InputStream inputStream = ImageLoader.class.getClassLoader().getResourceAsStream(resourcePath) ) {
            return loadImageAsBase64(inputStream);
        }
    }

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