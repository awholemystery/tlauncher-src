package org.tlauncher.tlauncher.configuration.enums;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/configuration/enums/ConnectionQuality.class */
public enum ConnectionQuality {
    GOOD(2, 5, 3, 30000),
    NORMAL(3, 6, 2, 45000),
    BAD(3, 6, 1, 60000);
    
    private final int minTries;
    private final int maxTries;
    private final int maxThreads;
    private final int timeout;
    private final int[] configuration;

    ConnectionQuality(int minTries, int maxTries, int maxThreads, int timeout) {
        this.minTries = minTries;
        this.maxTries = maxTries;
        this.maxThreads = maxThreads;
        this.timeout = timeout;
        this.configuration = new int[]{minTries, maxTries, maxThreads};
    }

    public static boolean parse(String val) {
        ConnectionQuality[] values;
        if (val == null) {
            return false;
        }
        for (ConnectionQuality cur : values()) {
            if (cur.toString().equalsIgnoreCase(val)) {
                return true;
            }
        }
        return false;
    }

    public static ConnectionQuality get(String val) {
        ConnectionQuality[] values;
        for (ConnectionQuality cur : values()) {
            if (cur.toString().equalsIgnoreCase(val)) {
                return cur;
            }
        }
        return null;
    }

    public int[] getConfiguration() {
        return this.configuration;
    }

    public int getMinTries() {
        return this.minTries;
    }

    public int getMaxTries() {
        return this.maxTries;
    }

    public int getMaxThreads() {
        return this.maxThreads;
    }

    public int getTries(boolean fast) {
        return fast ? this.minTries : this.maxTries;
    }

    public int getTimeout() {
        return this.timeout;
    }

    @Override // java.lang.Enum
    public String toString() {
        return super.toString().toLowerCase();
    }

    public static ConnectionQuality getDefault() {
        return GOOD;
    }
}
