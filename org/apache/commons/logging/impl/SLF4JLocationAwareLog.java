package org.apache.commons.logging.impl;

import java.io.ObjectStreamException;
import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/logging/impl/SLF4JLocationAwareLog.class */
public class SLF4JLocationAwareLog implements Log, Serializable {
    private static final long serialVersionUID = -2379157579039314822L;
    protected String name;
    private transient LocationAwareLogger logger;
    private static final String FQCN = SLF4JLocationAwareLog.class.getName();

    /* JADX INFO: Access modifiers changed from: package-private */
    public SLF4JLocationAwareLog(LocationAwareLogger logger) {
        this.logger = logger;
        this.name = logger.getName();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isTraceEnabled() {
        return this.logger.isTraceEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    @Override // org.apache.commons.logging.Log
    public boolean isWarnEnabled() {
        return this.logger.isWarnEnabled();
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
    public void trace(Object message) {
        this.logger.log(null, FQCN, 0, String.valueOf(message), null, null);
    }

    @Override // org.apache.commons.logging.Log
    public void trace(Object message, Throwable t) {
        this.logger.log(null, FQCN, 0, String.valueOf(message), null, t);
    }

    @Override // org.apache.commons.logging.Log
    public void debug(Object message) {
        this.logger.log(null, FQCN, 10, String.valueOf(message), null, null);
    }

    @Override // org.apache.commons.logging.Log
    public void debug(Object message, Throwable t) {
        this.logger.log(null, FQCN, 10, String.valueOf(message), null, t);
    }

    @Override // org.apache.commons.logging.Log
    public void info(Object message) {
        this.logger.log(null, FQCN, 20, String.valueOf(message), null, null);
    }

    @Override // org.apache.commons.logging.Log
    public void info(Object message, Throwable t) {
        this.logger.log(null, FQCN, 20, String.valueOf(message), null, t);
    }

    @Override // org.apache.commons.logging.Log
    public void warn(Object message) {
        this.logger.log(null, FQCN, 30, String.valueOf(message), null, null);
    }

    @Override // org.apache.commons.logging.Log
    public void warn(Object message, Throwable t) {
        this.logger.log(null, FQCN, 30, String.valueOf(message), null, t);
    }

    @Override // org.apache.commons.logging.Log
    public void error(Object message) {
        this.logger.log(null, FQCN, 40, String.valueOf(message), null, null);
    }

    @Override // org.apache.commons.logging.Log
    public void error(Object message, Throwable t) {
        this.logger.log(null, FQCN, 40, String.valueOf(message), null, t);
    }

    @Override // org.apache.commons.logging.Log
    public void fatal(Object message) {
        this.logger.log(null, FQCN, 40, String.valueOf(message), null, null);
    }

    @Override // org.apache.commons.logging.Log
    public void fatal(Object message, Throwable t) {
        this.logger.log(null, FQCN, 40, String.valueOf(message), null, t);
    }

    protected Object readResolve() throws ObjectStreamException {
        Logger logger = LoggerFactory.getLogger(this.name);
        return new SLF4JLocationAwareLog((LocationAwareLogger) logger);
    }
}
