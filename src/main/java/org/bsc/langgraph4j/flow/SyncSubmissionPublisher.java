package org.bsc.langgraph4j.flow;

import java.util.concurrent.Executor;
import java.util.concurrent.SubmissionPublisher;

public class SyncSubmissionPublisher<T> extends SubmissionPublisher<T> {
    static class DirectExecutor implements Executor {
        public void execute(Runnable r) {
            r.run();
        }
    }
    public SyncSubmissionPublisher() {
        super( new DirectExecutor(), 25 );
    }
}
