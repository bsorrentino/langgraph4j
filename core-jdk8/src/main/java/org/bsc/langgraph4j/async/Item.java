package org.bsc.langgraph4j.async;

import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
class Item<E> {
    AsyncIterator.Data<E> data;
    Throwable error;

    boolean isEnd() { return data.done();  }
    boolean isError() {
        return error != null;
    }

    static <E> Item<E> of(AsyncIterator.Data<E> data) {
        return new Item<>(data, null);
    }

    static <E> Item<E> of(Throwable error) {
        return new Item<>(null, error);
    }
}