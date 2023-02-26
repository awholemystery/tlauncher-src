package org.apache.commons.compress.harmony.unpack200;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/SegmentConstantPoolArrayCache.class */
public class SegmentConstantPoolArrayCache {
    protected IdentityHashMap<String[], CachedArray> knownArrays = new IdentityHashMap<>(1000);
    protected List<Integer> lastIndexes;
    protected String[] lastArray;
    protected String lastKey;

    public List<Integer> indexesForArrayKey(String[] array, String key) {
        if (!arrayIsCached(array)) {
            cacheArray(array);
        }
        if (this.lastArray == array && this.lastKey == key) {
            return this.lastIndexes;
        }
        this.lastArray = array;
        this.lastKey = key;
        this.lastIndexes = this.knownArrays.get(array).indexesForKey(key);
        return this.lastIndexes;
    }

    protected boolean arrayIsCached(String[] array) {
        if (!this.knownArrays.containsKey(array)) {
            return false;
        }
        CachedArray cachedArray = this.knownArrays.get(array);
        if (cachedArray.lastKnownSize() != array.length) {
            return false;
        }
        return true;
    }

    protected void cacheArray(String[] array) {
        if (arrayIsCached(array)) {
            throw new IllegalArgumentException("Trying to cache an array that already exists");
        }
        this.knownArrays.put(array, new CachedArray(array));
        this.lastArray = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/SegmentConstantPoolArrayCache$CachedArray.class */
    public class CachedArray {
        String[] primaryArray;
        int lastKnownSize;
        HashMap<String, List<Integer>> primaryTable;

        public CachedArray(String[] array) {
            this.primaryArray = array;
            this.lastKnownSize = array.length;
            this.primaryTable = new HashMap<>(this.lastKnownSize);
            cacheIndexes();
        }

        public int lastKnownSize() {
            return this.lastKnownSize;
        }

        public List<Integer> indexesForKey(String key) {
            if (!this.primaryTable.containsKey(key)) {
                return Collections.EMPTY_LIST;
            }
            return this.primaryTable.get(key);
        }

        protected void cacheIndexes() {
            for (int index = 0; index < this.primaryArray.length; index++) {
                String key = this.primaryArray[index];
                if (!this.primaryTable.containsKey(key)) {
                    this.primaryTable.put(key, new ArrayList());
                }
                this.primaryTable.get(key).add(Integer.valueOf(index));
            }
        }
    }
}
