package org.apache.commons.logging.impl;

import java.io.ObjectStreamException;
import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/logging/impl/SLF4JLog.class */
public class SLF4JLog implements Log, Serializable {
    private static final long serialVersionUID = 680728617011167209L;
    protected String name;
    private transient Logger logger;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SLF4JLog(Logger logger) {
        this.logger = logger;
        this.name = logger.getName();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isErrorEnabled() {
        return this.logger.isErrorEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isFatalEnabled() {
        return this.logger.isErrorEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isTraceEnabled() {
        return this.logger.isTraceEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isWarnEnabled() {
        return this.logger.isWarnEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public void trace(Object message) {
        this.logger.trace(String.valueOf(message));
    }

    @Override // org.apache.commons.logging.Log
    public void trace(Object message, Throwable t) {
        this.logger.trace(String.valueOf(message), t);
    }

    @Override // org.apache.commons.logging.Log
    public void debug(Object message) {
        this.logger.debug(String.valueOf(message));
    }

    @Override // org.apache.commons.logging.Log
    public void debug(Object message, Throwable t) {
        this.logger.debug(String.valueOf(message), t);
    }

    @Override // org.apache.commons.logging.Log
    public void info(Object message) {
        this.logger.info(String.valueOf(message));
    }

    @Override // org.apache.commons.logging.Log
    public void info(Object message, Throwable t) {
        this.logger.info(String.valueOf(message), t);
    }

    @Override // org.apache.commons.logging.Log
    public void warn(Object message) {
        this.logger.warn(String.valueOf(message));
    }

    @Override // org.apache.commons.logging.Log
    public void warn(Object message, Throwable t) {
        this.logger.warn(String.valueOf(message), t);
    }

    @Override // org.apache.commons.logging.Log
    public void error(Object message) {
        this.logger.error(String.valueOf(message));
    }

    @Override // org.apache.commons.logging.Log
    public void error(Object message, Throwable t) {
        this.logger.error(String.valueOf(message), t);
    }

    @Override // org.apache.commons.logging.Log
    public void fatal(Object message) {
        this.logger.error(String.valueOf(message));
    }

    @Override // org.apache.commons.logging.Log
    public void fatal(Object message, Throwable t) {
        this.logger.error(String.valueOf(message), t);
    }

    protected Object readResolve() throws ObjectStreamException {
        Logger logger = LoggerFactory.getLogger(this.name);
        return new SLF4JLog(logger);
    }
}
