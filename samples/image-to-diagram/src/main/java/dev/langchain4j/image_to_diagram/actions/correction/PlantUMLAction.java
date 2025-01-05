package dev.langchain4j.image_to_diagram.actions.correction;

import net.sourceforge.plantuml.*;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;

/**
 * The `PlantUMLAction` class provides utility methods for validating PlantUML code.
 *
 * It includes nested classes and static methods to handle the validation of PlantUML code blocks,
 * extracting errors if any, and returning results in a `CompletableFuture`.
 */
public class PlantUMLAction {

    /**
     * Represents an error that occurred during the PlantUML code processing.
     *
     * This subclass extends `Exception` and includes additional information about the type of error.
     */
    public static class Error extends Exception {

        private final ErrorUmlType type;

        /**
         * Gets the type of the error.
         *
         * @return the type of the error
         */
        public ErrorUmlType getType() {
            return type;
        }

        /**
         * Constructs a new `Error` instance with the given message and error type.
         *
         * @param message the detail message
         * @param type    the type of the error
         */
        public Error(String message, ErrorUmlType type) {
            super(message);
            this.type = type;
        }

    }

    /**
     * Validates PlantUML code and returns a `CompletableFuture`.
     *
     * This method takes a string containing PlantUML code, processes it,
     * and completes the future exceptionally if an error is found.
     *
     * @param <T>   the type of the result
     * @param code  the PlantUML code to validate
     * @return a `CompletableFuture` that will contain the result or error
     */
    public static <T> CompletableFuture<T> validate(String code) {
        CompletableFuture<T> result = new CompletableFuture<>();

        SourceStringReader reader = new SourceStringReader(code);

        final List<BlockUml> blocks = reader.getBlocks();
        if (blocks.size() != 1) {
            result.completeExceptionally(new IllegalArgumentException("Invalid PlantUML code"));
            return result;
        }

        final Diagram system = blocks.get(0).getDiagram();

        if (system instanceof PSystemError errors) {
            ErrorUml err = errors.getFirstError();

            try (ByteArrayOutputStream png = new ByteArrayOutputStream()) {
                reader.outputImage(png, 0, new FileFormatOption(FileFormat.UTXT));

                result.completeExceptionally(new Error(png.toString(), err.getType()));

            } catch (IOException e) {
                result.completeExceptionally(e);
            }
        } else {
            result.complete(null);
        }

        return result;
    }

    /**
     * Validates PlantUML code with a single line error and returns a `CompletableFuture`.
     *
     * This method takes a string containing PlantUML code, processes it,
     * and completes the future exceptionally if an error is found, formatted as a single-line error message.
     *
     * @param <T>   the type of the result
     * @param code  the PlantUML code to validate
     * @return a `CompletableFuture` that will contain the result or error
     */
    public static <T> CompletableFuture<T> validateWithSingleLineError(String code) {
        CompletableFuture<T> result = new CompletableFuture<>();

        SourceStringReader reader = new SourceStringReader(code);

        final List<BlockUml> blocks = reader.getBlocks();
        if (blocks.size() != 1) {
            result.completeExceptionally(new IllegalArgumentException("Invalid PlantUML code"));
            return result;
        }

        final Diagram system = blocks.get(0).getDiagram();

        if (system instanceof PSystemError errors) {

            ErrorUml err = errors.getFirstError();
            String error = format("error '%s' at line %d : '%s'",
                    err.getType(),
                    err.getPosition(),
                    errors.getSource().getLine(err.getLineLocation()));

            result.completeExceptionally(new Error(error, err.getType()));
        } else {
            result.complete(null);
        }

        return result;
    }
}
