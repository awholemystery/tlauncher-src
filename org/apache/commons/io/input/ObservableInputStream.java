package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/input/ObservableInputStream.class */
public class ObservableInputStream extends ProxyInputStream {
    private final List<Observer> observers;

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/io/input/ObservableInputStream$Observer.class */
    public static abstract class Observer {
        public void closed() throws IOException {
        }

        public void data(byte[] buffer, int offset, int length) throws IOException {
        }

        public void data(int value) throws IOException {
        }

        public void error(IOException exception) throws IOException {
            throw exception;
        }

        public void finished() throws IOException {
        }
    }

    public ObservableInputStream(InputStream inputStream) {
        this(inputStream, new ArrayList());
    }

    private ObservableInputStream(InputStream inputStream, List<Observer> observers) {
        super(inputStream);
        this.observers = observers;
    }

    public ObservableInputStream(InputStream inputStream, Observer... observers) {
        this(inputStream, Arrays.asList(observers));
    }

    public void add(Observer observer) {
        this.observers.add(observer);
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        IOException ioe = null;
        try {
            super.close();
        } catch (IOException e) {
            ioe = e;
        }
        if (ioe == null) {
            noteClosed();
        } else {
            noteError(ioe);
        }
    }

    public void consume() throws IOException {
        byte[] buffer = IOUtils.byteArray();
        do {
        } while (read(buffer) != -1);
    }

    public List<Observer> getObservers() {
        return this.observers;
    }

    protected void noteClosed() throws IOException {
        for (Observer observer : getObservers()) {
            observer.closed();
        }
    }

    protected void noteDataByte(int value) throws IOException {
        for (Observer observer : getObservers()) {
            observer.data(value);
        }
    }

    protected void noteDataBytes(byte[] buffer, int offset, int length) throws IOException {
        for (Observer observer : getObservers()) {
            observer.data(buffer, offset, length);
        }
    }

    protected void noteError(IOException exception) throws IOException {
        for (Observer observer : getObservers()) {
            observer.error(exception);
        }
    }

    protected void noteFinished() throws IOException {
        for (Observer observer : getObservers()) {
            observer.finished();
        }
    }

    private void notify(byte[] buffer, int offset, int result, IOException ioe) throws IOException {
        if (ioe != null) {
            noteError(ioe);
            throw ioe;
        } else if (result == -1) {
            noteFinished();
        } else if (result > 0) {
            noteDataBytes(buffer, offset, result);
        }
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int result = 0;
        IOException ioe = null;
        try {
            result = super.read();
        } catch (IOException ex) {
            ioe = ex;
        }
        if (ioe != null) {
            noteError(ioe);
            throw ioe;
        }
        if (result == -1) {
            noteFinished();
        } else {
            noteDataByte(result);
        }
        return result;
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buffer) throws IOException {
        int result = 0;
        IOException ioe = null;
        try {
            result = super.read(buffer);
        } catch (IOException ex) {
            ioe = ex;
        }
        notify(buffer, 0, result, ioe);
        return result;
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buffer, int offset, int length) throws IOException {
        int result = 0;
        IOException ioe = null;
        try {
            result = super.read(buffer, offset, length);
        } catch (IOException ex) {
            ioe = ex;
        }
        notify(buffer, offset, result, ioe);
        return result;
    }

    public void remove(Observer observer) {
        this.observers.remove(observer);
    }

    public void removeAllObservers() {
        this.observers.clear();
    }
}
