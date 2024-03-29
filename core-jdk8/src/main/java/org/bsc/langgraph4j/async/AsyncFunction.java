package org.bsc.langgraph4j.async;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@FunctionalInterface
public interface AsyncFunction<T,R> extends Function<T, CompletableFuture<R>> {

    static <T> AsyncFunction<T,Void> consumer_async(java.util.function.Consumer<T> syncConsumer  )  {
        return t -> {
            CompletableFuture<Void> result = new CompletableFuture<>();
            try {
                syncConsumer.accept(t);
                result.complete(null);
            } catch (Exception e) {
                result.completeExceptionally(e);
            }
            return result;
        };
    }
    static <T,R> AsyncFunction<T,R> function_async(java.util.function.Function<T,R> syncFunction  )  {
        return t -> {
            CompletableFuture<R> result = new CompletableFuture<>();
            try {
                result.complete(syncFunction.apply(t));
            } catch (Exception e) {
                result.completeExceptionally(e);
            }
            return result;
        };
    }
}
