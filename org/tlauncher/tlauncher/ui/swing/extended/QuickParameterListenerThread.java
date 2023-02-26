package org.tlauncher.tlauncher.ui.swing.extended;

import org.tlauncher.tlauncher.ui.swing.util.IntegerArrayGetter;
import org.tlauncher.util.U;
import org.tlauncher.util.async.LoopedThread;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/swing/extended/QuickParameterListenerThread.class */
public class QuickParameterListenerThread extends LoopedThread {
    public static final int DEFAULT_TICK = 500;
    private final IntegerArrayGetter paramGetter;
    private final Runnable runnable;
    private final int tick;

    /* JADX INFO: Access modifiers changed from: package-private */
    public QuickParameterListenerThread(IntegerArrayGetter getter, Runnable run, int tick) {
        super("QuickParameterListenerThread");
        if (getter == null) {
            throw new NullPointerException("Getter is NULL!");
        }
        if (run == null) {
            throw new NullPointerException("Runnable is NULL!");
        }
        if (tick < 0) {
            throw new IllegalArgumentException("Tick must be positive!");
        }
        this.paramGetter = getter;
        this.runnable = run;
        this.tick = tick;
        setPriority(1);
        startAndWait();
    }

    QuickParameterListenerThread(IntegerArrayGetter getter, Runnable run) {
        this(getter, run, 500);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startListening() {
        iterate();
    }

    @Override // org.tlauncher.util.async.LoopedThread
    protected void iterateOnce() {
        boolean equal;
        int[] initial = this.paramGetter.getIntegerArray();
        do {
            sleep();
            int[] newvalue = this.paramGetter.getIntegerArray();
            equal = true;
            for (int i = 0; i < initial.length; i++) {
                if (initial[i] != newvalue[i]) {
                    equal = false;
                }
            }
            initial = newvalue;
        } while (!equal);
        this.runnable.run();
    }

    private void sleep() {
        U.sleepFor(this.tick);
    }
}
