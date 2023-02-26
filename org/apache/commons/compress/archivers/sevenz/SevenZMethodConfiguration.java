package org.apache.commons.compress.archivers.sevenz;

import java.util.Objects;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/SevenZMethodConfiguration.class */
public class SevenZMethodConfiguration {
    private final SevenZMethod method;
    private final Object options;

    public SevenZMethodConfiguration(SevenZMethod method) {
        this(method, null);
    }

    public SevenZMethodConfiguration(SevenZMethod method, Object options) {
        this.method = method;
        this.options = options;
        if (options != null && !Coders.findByMethod(method).canAcceptOptions(options)) {
            throw new IllegalArgumentException("The " + method + " method doesn't support options of type " + options.getClass());
        }
    }

    public SevenZMethod getMethod() {
        return this.method;
    }

    public Object getOptions() {
        return this.options;
    }

    public int hashCode() {
        if (this.method == null) {
            return 0;
        }
        return this.method.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SevenZMethodConfiguration other = (SevenZMethodConfiguration) obj;
        return Objects.equals(this.method, other.method) && Objects.equals(this.options, other.options);
    }
}
