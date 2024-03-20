package org.bsc.langgraph4j.flow;


import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.io.Closeable;

public class SyncSubmissionPublisher<T> implements Publisher<T>, AutoCloseable {

    private Subscriber<? super T> subscriber;
    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        this.subscriber = subscriber;
    }

    public void closeExceptionally(Throwable ex) {
        if (subscriber != null) {
            subscriber.onError(ex);
        }
        subscriber = null;
    }
    public void submit(T t) {
        if (subscriber != null) {
            subscriber.onNext(t);
        }
    }

    @Override
    public void close() throws Exception {
        if (subscriber != null) {
            subscriber.onComplete();
        }
        subscriber = null;
    }

}
