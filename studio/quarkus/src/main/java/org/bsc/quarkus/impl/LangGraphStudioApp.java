package org.bsc.quarkus.impl;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class LangGraphStudioApp {

    public static void main(String... args) {
        Quarkus.run(  args);
    }
}


