package org.bsc.langgraph4j.multi_agent.springai;


public abstract class AbstractAgent<B extends AbstractAgent.Builder<B>>  {

    public static abstract class Builder<B extends Builder<B>> {

        @SuppressWarnings("unchecked")
        protected B result() {
            return (B) this;
        }


    }

    public AbstractAgent(Builder<B> builder ) {
    }

}
