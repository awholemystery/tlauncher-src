package org.apache.http.impl.client;

import java.net.URI;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/client/RedirectLocations.class */
public class RedirectLocations extends AbstractList<Object> {
    private final Set<URI> unique = new HashSet();
    private final List<URI> all = new ArrayList();

    public boolean contains(URI uri) {
        return this.unique.contains(uri);
    }

    public void add(URI uri) {
        this.unique.add(uri);
        this.all.add(uri);
    }

    public boolean remove(URI uri) {
        boolean removed = this.unique.remove(uri);
        if (removed) {
            Iterator<URI> it = this.all.iterator();
            while (it.hasNext()) {
                URI current = it.next();
                if (current.equals(uri)) {
                    it.remove();
                }
            }
        }
        return removed;
    }

    public List<URI> getAll() {
        return new ArrayList(this.all);
    }

    @Override // java.util.AbstractList, java.util.List
    public URI get(int index) {
        return this.all.get(index);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public int size() {
        return this.all.size();
    }

    @Override // java.util.AbstractList, java.util.List
    public Object set(int index, Object element) {
        URI removed = this.all.set(index, (URI) element);
        this.unique.remove(removed);
        this.unique.add((URI) element);
        if (this.all.size() != this.unique.size()) {
            this.unique.addAll(this.all);
        }
        return removed;
    }

    @Override // java.util.AbstractList, java.util.List
    public void add(int index, Object element) {
        this.all.add(index, (URI) element);
        this.unique.add((URI) element);
    }

    @Override // java.util.AbstractList, java.util.List
    public URI remove(int index) {
        URI removed = this.all.remove(index);
        this.unique.remove(removed);
        if (this.all.size() != this.unique.size()) {
            this.unique.addAll(this.all);
        }
        return removed;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean contains(Object o) {
        return this.unique.contains(o);
    }
}
