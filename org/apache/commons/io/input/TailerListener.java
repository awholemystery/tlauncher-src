package org.apache.commons.io.input;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/input/TailerListener.class */
public interface TailerListener {
    void init(Tailer tailer);

    void fileNotFound();

    void fileRotated();

    void handle(String str);

    void handle(Exception exc);
}
