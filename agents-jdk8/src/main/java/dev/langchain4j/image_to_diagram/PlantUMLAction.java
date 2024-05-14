package dev.langchain4j.image_to_diagram;

import net.sourceforge.plantuml.*;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;

public class PlantUMLAction {

    public static class Error extends Exception {

        private ErrorUmlType type;

        public Error(String message, ErrorUmlType type) {
            super(message);
            this.type = type;
        }

        public ErrorUmlType getType() {
            return type;
        }
    }

    public static <T> CompletableFuture<T> validate( String code ) {
        CompletableFuture<T> result = new CompletableFuture<>();

        SourceStringReader reader = new SourceStringReader(code);

        final List<BlockUml> blocks = reader.getBlocks();
        if ( blocks.size() != 1 ) {
            result.completeExceptionally(new IllegalArgumentException("Invalid PlantUML code"));
            return result;
        }

        final Diagram system = blocks.get(0).getDiagram();

        if( system instanceof PSystemError ) {
            PSystemError errors = (PSystemError) system;
            ErrorUml err = errors.getFirstError();

            try(ByteArrayOutputStream png = new ByteArrayOutputStream()) {
                reader.outputImage(png, 0, new FileFormatOption(FileFormat.UTXT));

                result.completeExceptionally( new Error(png.toString(), err.getType() ) );

            } catch (IOException e) {
                result.completeExceptionally(e);
            }
        }
        else {
            result.complete(null);
        }

        return result;
    }
    public static <T> CompletableFuture<T> validateWithSingleLineError( String code ) {
        CompletableFuture<T> result = new CompletableFuture<>();

        SourceStringReader reader = new SourceStringReader(code);

        final List<BlockUml> blocks = reader.getBlocks();
        if ( blocks.size() != 1 ) {
            result.completeExceptionally(new IllegalArgumentException("Invalid PlantUML code"));
            return result;
        }

        final Diagram system = blocks.get(0).getDiagram();

        if( system instanceof PSystemError ) {

            PSystemError errors = (PSystemError) system;
            ErrorUml err = errors.getFirstError();
            String error = format( "error '%s' at line %d : '%s'",
                    err.getType(),
                    err.getPosition(),
                    errors.getSource().getLine( err.getLineLocation() ) );

            result.completeExceptionally(new Error(error, err.getType()));
        }
        else {
            result.complete(null);
        }

        return result;
    }
}
