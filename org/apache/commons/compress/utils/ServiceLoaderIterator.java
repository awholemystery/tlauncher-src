package org.apache.commons.compress.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

@Deprecated
/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/utils/ServiceLoaderIterator.class */
public class ServiceLoaderIterator<E> implements Iterator<E> {
    private E nextServiceLoader;
    private final Class<E> service;
    private final Iterator<E> serviceLoaderIterator;

    public ServiceLoaderIterator(Class<E> service) {
        this(service, ClassLoader.getSystemClassLoader());
    }

    public ServiceLoaderIterator(Class<E> service, ClassLoader classLoader) {
        this.service = service;
        this.serviceLoaderIterator = ServiceLoader.load(service, classLoader).iterator();
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        while (this.nextServiceLoader == null) {
            try {
            } catch (ServiceConfigurationError e) {
                if (!(e.getCause() instanceof SecurityException)) {
                    throw e;
                }
            }
            if (!this.serviceLoaderIterator.hasNext()) {
                return false;
            }
            this.nextServiceLoader = this.serviceLoaderIterator.next();
        }
        return true;
    }

    @Override // java.util.Iterator
    public E next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements for service " + this.service.getName());
        }
        E tempNext = this.nextServiceLoader;
        this.nextServiceLoader = null;
        return tempNext;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException("service=" + this.service.getName());
    }
}
